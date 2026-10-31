package net.atlanticbb.tantlinger.i18n;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.stream.Stream;

/**
 * @author Bob Tantlinger
 *
 */
public final class I18n {
    private static final Map<String, I18n> PACKAGE_INSTANCE_MAP = new HashMap<>();
    private static final Properties BUNDLE_NAMES = new Properties();
    private static final File LANG_PACK_DIR = new File(System.getProperty("user.dir"), "languages");
    private static final String DEFAULT_BUNDLE_NAME = "messages";
    private static final String MNEM_SUFFIX = ".mnemonic";

    public static Locale locale = Locale.getDefault();

    private final String packageName;

    private ResourceBundle bundle;

    private I18n(String packageName) {
        this.packageName = packageName;
    }

    public static I18n getInstance(String packageName) {
        return PACKAGE_INSTANCE_MAP.computeIfAbsent(packageName, I18n::new);
    }

    public static Locale getLocale() {
        return locale;
    }

    public static void setLocale(Locale locale) {
        I18n.locale = Objects.requireNonNull(locale);

        // reset so bundles with new locale get created...
        PACKAGE_INSTANCE_MAP.values().forEach(i18n -> i18n.bundle = null);
    }

    public static void setLocale(String localeString) throws IllegalArgumentException {
        setLocale(localeFromString(localeString));
    }

    public static String getBundleName(String packageName) {
        return BUNDLE_NAMES.getProperty(packageName,
                createBundleName(packageName, DEFAULT_BUNDLE_NAME));
    }

    public static void setBundleName(String packageName, String bundleName) {
        if (bundleName == null)
            BUNDLE_NAMES.remove(packageName);
        else
            BUNDLE_NAMES.setProperty(packageName, bundleName);

        // reset to null so the bundle is recreated for the new name
        I18n i18n = PACKAGE_INSTANCE_MAP.get(packageName);
        if (i18n != null) i18n.bundle = null;
    }

    public static File getLanguagePackDirectory() {
        return LANG_PACK_DIR;
    }

    public static File[] getAvailableLanguagePacks() {
        List<File> languagePacks = new ArrayList<>();

        for (Locale locale : getAvailableLanguagePackLocales()) {
            String name = locale.toString() + ".zip";
            File f = new File(LANG_PACK_DIR, name);
            if (f.isFile() && f.canRead())
                languagePacks.add(f);
        }

        return languagePacks.toArray(File[]::new);
    }

    public static Locale[] getAvailableLanguagePackLocales() {
        List<Locale> locales = new ArrayList<>();
        File dir = getLanguagePackDirectory();

        if (dir.isDirectory() && dir.canRead()) {
            File[] packs = dir.listFiles(f -> f.isFile() && f.canRead() &&
                    f.getName().toLowerCase(Locale.ROOT).endsWith(".zip"));

            if (packs != null) {
                for (File pack : packs) {
                    String packName = pack.getName();
                    int lastDotIndex = packName.lastIndexOf(".");

                    if (lastDotIndex >= 0) {
                        String localeString = packName.substring(0, lastDotIndex);
                        locales.add(localeFromString(localeString));
                    }
                }
            }
        }

        return locales.toArray(Locale[]::new);
    }

    public static Locale localeFromString(String localeString) {
        String[] parts = localeString.split("_");
        String lang = parts[0];
        String country = (parts.length > 1) ? parts[1] : "";
        String variant = (parts.length > 2) ? parts[2] : "";
        return new Locale(lang, country, variant);
    }

    public String str(String key) {
        try {
            if (bundle == null) {
                bundle = createBundle(locale);
            }

            return bundle.getString(key);
        } catch (Exception ex) {
            return '!' + key + '!';
        }
    }

    public String str(String key, Locale locale) {
        if (Objects.equals(I18n.locale, locale)) {
            return str(key);
        }

        try {
            return createBundle(locale).getString(key);
        } catch (Exception ex) {
            return '!' + key + '!';
        }
    }

    public int mnem(String key) {
        String s = str(key + MNEM_SUFFIX);
        return s.isEmpty() ? '!' : s.charAt(0);
    }

    public int mnem(String key, Locale locale) {
        if (Objects.equals(I18n.locale, locale)) {
            return mnem(key);
        }

        String s = str(key + MNEM_SUFFIX, locale);
        return s.isEmpty() ? '!' : s.charAt(0);
    }

    /**
     * Converts slashes to dots in a pathname
     *
     * @param path The string to convert
     * @return a dotted string
     */
    private static String slashesToDots(String path) {
        String dottedString = path.replaceAll("/+", ".");
        return dottedString.startsWith(".")
                ? dottedString.substring(1)
                : dottedString;
    }

    private static String createBundleName(String packageName, String bundleName) {
        StringBuilder sb = new StringBuilder(slashesToDots(packageName));
        if (!sb.toString().endsWith(".")) sb.append('.');
        return sb.append(bundleName).toString();
    }

    private static URL file2URL(File file) {
        try {
            return file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private ResourceBundle createBundle(Locale locale) {
        String bundleName = getBundleName(packageName);
        URL[] urls = Stream.of(getAvailableLanguagePacks())
                .map(I18n::file2URL)
                .toArray(URL[]::new);

        return urls.length > 0
                ? ResourceBundle.getBundle(bundleName, locale, URLClassLoader.newInstance(urls))
                : ResourceBundle.getBundle(bundleName, locale);
    }
}
