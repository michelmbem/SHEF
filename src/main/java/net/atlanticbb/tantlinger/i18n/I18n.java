/*
 * Created on Nov 6, 2007
 */
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
public class I18n {

    public static final Properties BUNDLE_PROPS = new Properties();
    public static final Map I18NS = new HashMap();
    private static final File LANG_PACK_DIR = new File(System.getProperty("user.dir"), "languages");
    private static final String DEFAULT_BUNDLE_NAME = "messages";
    private static final String MNEM_POSTFIX = ".mnemonic";
    public static Locale locale = Locale.getDefault();

    private ResourceBundle bundle;
    private String _package;

    private I18n(String _package) {
        this._package = _package;
    }

    public static I18n getInstance(String _package) {
        I18n i18n = (I18n) I18NS.get(_package);
        if (i18n == null) {
            i18n = new I18n(_package);
            I18NS.put(_package, i18n);
        }

        return i18n;
    }

    public static Locale getLocale() {
        if (locale == null)
            Locale.getDefault();
        return locale;
    }

    public static void setLocale(Locale loc) {
        locale = loc;
        for (Object o : I18NS.values()) {
            I18n i18n = (I18n) o;
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
     * @param path
     * @return
     */
    private static String slashesToDots(String path) {
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(path, "/");
        while (st.hasMoreTokens()) {
            sb.append(".");
            sb.append(st.nextToken());
        }

        if (sb.toString().startsWith("."))
            sb.deleteCharAt(0);

        return sb.toString();
    }

    private static String createBundleName(String bundlePackage, String bundleName) {
        StringBuilder sb = new StringBuilder(slashesToDots(bundlePackage));
        if (!sb.toString().endsWith("."))
            sb.append('.');
        sb.append(bundleName);
        return sb.toString();
    }

    public static void setBundleForPackage(String _package, String bundle) {
        if (bundle == null)
            BUNDLE_PROPS.remove(_package);
        else
            BUNDLE_PROPS.setProperty(_package, bundle);

        I18n i18n = (I18n) I18NS.get(_package);
        if (i18n != null)
            i18n.bundle = null; //reset to null so the bundle is recreated for the new name
    }

    public static String getBundleForPackage(String _package) {
        String bun = BUNDLE_PROPS.getProperty(_package);
        if (bun == null)//use default
            bun = createBundleName(_package, DEFAULT_BUNDLE_NAME);

        return bun;
    }

    public static File getLanguagePackDirectory() {
        return LANG_PACK_DIR;
    }

    public static File[] getAvailableLanguagePacks() {
        Locale[] locs = getAvailableLanguagePackLocales();
        List packs = new ArrayList();

        for (int i = 0; i < locs.length; i++) {
            String name = locs[i].toString() + ".zip";
            File f = new File(LANG_PACK_DIR, name);
            if (f.isFile() && f.canRead())
                packs.add(f);
        }

        return (File[]) packs.toArray(new File[packs.size()]);
    }

    public static Locale[] getAvailableLanguagePackLocales() {
        List locs = new ArrayList();
        File dir = getLanguagePackDirectory();
        if (dir.isDirectory() && dir.canRead()) {
            File[] packs = dir.listFiles(new ZipFileFilter());
            if (packs != null) {
                for (int i = 0; i < packs.length; i++) {
                    String name = packs[i].getName();
                    int p = name.lastIndexOf(".");
                    if (p != -1) {
                        String locStr = name.substring(0, p);
                        Locale loc = localeFromString(locStr);
                        if (loc != null)
                            locs.add(loc);
                    }
                }
            }
        }

        return (Locale[]) locs.toArray(new Locale[locs.size()]);
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
        if (s != null && !s.isEmpty())
            return s.charAt(0);
        return '!';
    }

    public int mnem(String key, Locale loc) {
        String s = str(key + MNEM_POSTFIX, loc);
        if (s != null && !s.isEmpty())
            return s.charAt(0);
        return '!';
    }

    private ResourceBundle createBundle(Locale loc) {
        String bun = getBundleForPackage(_package);

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
