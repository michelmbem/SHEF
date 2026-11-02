package net.atlanticbb.tantlinger.ui.text;

import javax.imageio.ImageIO;
import javax.swing.text.Element;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.ImageView;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;

/**
 *
 * An ImageView which caches images to a local directory after they're
 * initially loaded. After the images are cached, they're subsequently
 * loaded from the local directory, thereby reducing the overhead of
 * refetching them from their remote location.
 *
 * @author Bob Tantlinger
 *
 */
public class CachedImageView extends ImageView {
    public static final String USER_HOME = System.getProperty("user.home");
    /**
     * Directory where app properties are stored
     */
    public static final String PROP_DIR = USER_HOME + File.separator + ".thinga";
    public static final String IMG_CACHE_DIR = PROP_DIR + File.separator + "img_cache";

    // flag which keeps track of if the image has been writen to disk
    private boolean hasCached;

    public CachedImageView(Element elem) {
        super(elem);
    }

    @Override
    public void paint(Graphics g, Shape a) {
        super.paint(g, a);

        Image img = getImage();

        //test if the image is completely loaded.
        int flags = Toolkit.getDefaultToolkit().checkImage(
                img, -1, -1, getContainer());
        boolean done = ((flags & (ImageObserver.ALLBITS)) != 0);
        if (done && !hasCached) //If it is, write it out to the cache
            cacheImage(img);
    }

    /**
     * Writes an image out to the cache
     *
     * @param image the {@link Image} to cache
     */
    private void cacheImage(final Image image) {
        final File cImg = getCachedImg();
        if (image == null || cImg == null || cImg.exists())
            return;

        hasCached = true; // set the hasCached flag so we don't write it twice

        Runnable runner = () -> {
            String type = "png"; // assume png for non-jpegs
            if (cImg.getAbsolutePath()
                    .toLowerCase(Locale.ROOT)
                    .endsWith(".jpg"))
                type = "jpg";

            BufferedImage bufImg = makeBufferedImage(image);
            try {
                ImageIO.write(bufImg, type, cImg);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        };

        Thread th = new Thread(runner, "Image Cacher");
        th.setPriority(Thread.NORM_PRIORITY);
        th.start();
    }

    /**
     * Gets the real image URL as specified in the src attribute of the image tag
     *
     * @return the <em>src</em> attribute of the associated <em>img</em> tag
     */
    private String srcAttribute() {
        return (String) getElement().getAttributes().getAttribute(HTML.Attribute.SRC);
    }

    /**
     * Return a URL for the image source,
     * or null if it could not be determined.
     * <p>
     * If the image is cached, the local file URL is returned.
     * The superclass treats this the same as the actual url, but
     * the image is loaded from the local cache rather than redownloading it
     */
    @Override
    public URL getImageURL() {
        String src = srcAttribute();
        if (src == null) return null;

        URL imgURL = null;
        File diskCachedImg = getCachedImg();

        try {
            if (diskCachedImg.exists()) {
                imgURL = diskCachedImg.toURI().toURL();
            } else if (src.regionMatches(true, 0, "data:", 0, 5)) {
                imgURL = cacheDataUrlImage(src, diskCachedImg);
            } else {
                URL reference = ((HTMLDocument) getDocument()).getBase();
                imgURL = new URL(reference, src);
            }
        } catch (MalformedURLException ignored) {
        }

        return imgURL;
    }

    /**
     * Decodes a data URL to the image cache and returns its file URL. Swing's
     * standard ImageView loads images through java.net.URL, which does not
     * provide a data: protocol handler by default.
     */
    private URL cacheDataUrlImage(String dataUrl, File cacheFile) {
        try {
            int comma = dataUrl.indexOf(',');
            if (comma < 0) return null;

            String metadata = dataUrl.substring(5, comma);
            String payload = dataUrl.substring(comma + 1);
            byte[] imageBytes;
            if (metadata.toLowerCase(Locale.ROOT).endsWith(";base64")) {
                imageBytes = Base64.getDecoder().decode(payload);
            } else {
                imageBytes = decodeDataUrlPayload(payload);
            }

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageBytes));
            if (image == null) return null;

            File parent = cacheFile.getParentFile();
            if (parent != null && !parent.isDirectory() && !parent.mkdirs()) return null;
            if (!ImageIO.write(image, "png", cacheFile)) return null;
            return cacheFile.toURI().toURL();
        } catch (IOException | IllegalArgumentException ignored) {
            return null;
        }
    }

    private byte[] decodeDataUrlPayload(String payload) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(payload.length());

        for (int i = 0; i < payload.length();) {
            char c = payload.charAt(i);
            if (c == '%') {
                if (i + 2 >= payload.length()) throw new IOException("Invalid data URL escape");
                int high = Character.digit(payload.charAt(i + 1), 16);
                int low = Character.digit(payload.charAt(i + 2), 16);
                if (high < 0 || low < 0) throw new IOException("Invalid data URL escape");
                output.write((high << 4) + low);
                i += 3;
            } else {
                int codePoint = payload.codePointAt(i);
                output.write(new String(Character.toChars(codePoint))
                        .getBytes(StandardCharsets.UTF_8));
                i += Character.charCount(codePoint);
            }
        }

        return output.toByteArray();
    }

    private BufferedImage makeBufferedImage(Image image) {
        BufferedImage buffered = new BufferedImage(
                image.getWidth(null),
                image.getHeight(null),
                BufferedImage.TYPE_INT_RGB);

        Graphics2D g = buffered.createGraphics();
        g.drawImage(image, null, null);
        g.dispose();

        return buffered;
    }

    /**
     * Returns the local file of the cached version of the image.
     * The local file is named with the absolute value of the hashcode for the
     * actual image url and a "png" or "jpg" file extension
     *
     * @return A {@link File} instance representing the cached image file on the local drive
     */
    private File getCachedImg() {
        String iUrl = srcAttribute();
        if (iUrl == null) return null;

        String imgType = ".png";
        if (iUrl.toLowerCase().endsWith(".jpg") ||
                iUrl.toLowerCase().endsWith(".jpeg"))
            imgType = ".jpg";

        int hc = Math.abs(iUrl.hashCode());
        return new File(IMG_CACHE_DIR, hc + imgType);
    }
}
