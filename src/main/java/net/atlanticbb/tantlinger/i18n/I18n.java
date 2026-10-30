package net.atlanticbb.tantlinger.i18n;

import java.io.File;
import java.io.FileFilter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;

/**
 * @author Bob Tantlinger
 *
 */
public final class I18n {
    private static final Map<String, I18n> MAP = new HashMap<>();
    private static final Properties PROPERTIES = new Properties();
    private static final File LANG_PACK_DIR = new File(System.getProperty("user.dir"), "languages");
    private static final String DEFAULT_BUNDLE_NAME = "messages";
    private static final String MNEM_POSTFIX = ".mnemonic";

    public static Locale locale = Locale.getDefault();

    private final String packageName;

    private ResourceBundle bundle;

    private I18n(String packageName) {
        this.packageName = packageName;
    }

    public static I18n getInstance(String packageName) {
        return MAP.computeIfAbsent(packageName, I18n::new);
    }

    public static Locale getLocale() {
        if (locale == null)
            locale = Locale.getDefault();

        return locale;
    }

    public static void setLocale(Locale loc) {
        locale = loc;
        for (I18n i18n : MAP.values()) {
            i18n.bundle = null; //reset so bundle with new locale gets created...
        }
    }

    public static void setLocale(String locStr) throws IllegalArgumentException {
        Locale loc = localeFromString(locStr);
        setLocale(loc);
    }

    /**
     * Converts slashes to dots in a pathname
     *
     * @param path The string to convert
     * @return a dotted string
     */
    private static String slashesToDots(String path) {
        String s = path.replace('/', '.');
        return s.startsWith(".") ? s.substring(1) : s;
    }

    private static String createBundleName(String bundlePackage, String bundleName) {
        StringBuilder sb = new StringBuilder(slashesToDots(bundlePackage));
        if (!sb.toString().endsWith(".")) sb.append('.');
        sb.append(bundleName);
        return sb.toString();
    }

    public static void setBundleForPackage(String packageName, String bundle) {
        if (bundle == null)
            PROPERTIES.remove(packageName);
        else
            PROPERTIES.setProperty(packageName, bundle);

        I18n i18n = MAP.get(packageName);
        if (i18n != null)
            i18n.bundle = null; //reset to null so the bundle is recreated for the new name
    }

    public static String getBundleForPackage(String packageName) {
        String bun = PROPERTIES.getProperty(packageName);
        if (bun == null)//use default
            bun = createBundleName(packageName, DEFAULT_BUNDLE_NAME);

        return bun;
    }

    public static File getLanguagePackDirectory() {
        return LANG_PACK_DIR;
    }

    public static File[] getAvailableLanguagePacks() {
        Locale[] locs = getAvailableLanguagePackLocales();
        List<File> packs = new ArrayList<>();

        for (Locale loc : locs) {
            String name = loc.toString() + ".zip";
            File f = new File(LANG_PACK_DIR, name);
            if (f.isFile() && f.canRead())
                packs.add(f);
        }

        return packs.toArray(File[]::new);
    }

    public static Locale[] getAvailableLanguagePackLocales() {
        List<Locale> locs = new ArrayList<>();
        File dir = getLanguagePackDirectory();

        if (dir.isDirectory() && dir.canRead()) {
            File[] packs = dir.listFiles(new ZipFileFilter());

            if (packs != null) {
                for (File pack : packs) {
                    String name = pack.getName();
                    int p = name.lastIndexOf(".");

                    if (p != -1) {
                        String locStr = name.substring(0, p);
                        Locale loc = localeFromString(locStr);
                        locs.add(loc);
                    }
                }
            }
        }

        return locs.toArray(Locale[]::new);
    }

    public static Locale localeFromString(String locStr) {
        String[] parts = locStr.split("_");
        String lang = parts[0];
        String country = (parts.length > 1) ? parts[1] : "";
        String variant = (parts.length > 2) ? parts[2] : "";
        return new Locale(lang, country, variant);

    }

    public String str(String key) {
        try {
            if (bundle == null) {
                bundle = createBundle(getLocale());
            }

            return bundle.getString(key);
        } catch (Exception ex) {
            return '!' + key + '!';
        }
    }

    public String str(String key, Locale locale) {
        try {
            return createBundle(locale).getString(key);
        } catch (Exception ex) {
            return '!' + key + '!';
        }
    }

    public int mnem(String key) {
        String s = str(key + MNEM_POSTFIX);
        return s.isEmpty() ? '!' : s.charAt(0);
    }

    public int mnem(String key, Locale loc) {
        String s = str(key + MNEM_POSTFIX, loc);
        return s.isEmpty() ? '!' : s.charAt(0);
    }

    private ResourceBundle createBundle(Locale loc) {
        String bun = getBundleForPackage(packageName);

        File[] packs = getAvailableLanguagePacks();
        if (packs.length > 0) {
            URL[] urls = new URL[packs.length];
            try {
                for (int i = 0; i < urls.length; i++)
                    urls[i] = packs[i].toURL();
                return ResourceBundle.getBundle(bun, loc, URLClassLoader.newInstance(urls));
            } catch (MalformedURLException | MissingResourceException ignored) {
            }
        }

        return ResourceBundle.getBundle(bun, loc); //just return default
    }

    private static class ZipFileFilter implements FileFilter {
        public boolean accept(File f) {
            if (f.isFile() && f.canRead()) {
                String name = f.getName().toLowerCase();
                return name.endsWith(".zip");
            }
            return false;
        }
    }
}
