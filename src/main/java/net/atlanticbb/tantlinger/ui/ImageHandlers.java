package net.atlanticbb.tantlinger.ui;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Objects;

/**
 * Holds the {@link ImageHandler} used by SHEF when an image is inserted into an
 * HTML document, either by pasting it or selecting a file.
 * <p>
 * The default handler returns a local {@code file:} URL. Applications can
 * replace it with a handler that embeds image data or uploads images elsewhere.
 */
public final class ImageHandlers {
    /** The handler currently used for image insertions. */
    private static ImageHandler current = new Default();

    /** Prevents instantiation of this utility class. */
    private ImageHandlers() {}

    /**
     * Returns the handler currently used for image insertions.
     *
     * @return the current image handler
     */
    public static ImageHandler getCurrent() {
        return current;
    }

    /**
     * Sets the handler used for subsequent image insertions.
     *
     * @param handler the handler to use
     * @throws NullPointerException if {@code handler} is {@code null}
     */
    public static void setCurrent(ImageHandler handler) {
        current = Objects.requireNonNull(handler);
    }

    /**
     * Default handler that returns a {@code file:} URL for the image on disk.
     */
    public static final class Default implements ImageHandler {
        /**
         * Returns a {@code file:} URL for the specified local image file.
         *
         * @param imageFile the image file to reference
         * @return the file URL as an external-form string
         * @throws NullPointerException if {@code imageFile} is {@code null}
         * @throws IllegalArgumentException if a URL cannot be created for the file
         */
        @Override
        public String handleImage(File imageFile) {
            Objects.requireNonNull(imageFile, "imageFile must be non-null");

            try {
                return imageFile.toURI().toURL().toExternalForm();
            } catch (MalformedURLException e) {
                throw new IllegalArgumentException(
                        "Could not generate a URL from the given file", e);
            }
        }
    }

    /**
     * Creates a Base64-encoded Data URL for an image file.
     */
    public static final class DataURL implements ImageHandler {
        /**
         * Reads the image file and returns its contents as a Base64 data URL.
         * The MIME type is detected from the file and falls back to
         * {@code application/octet-stream} if it cannot be determined.
         *
         * @param imageFile the image file to embed
         * @return a Base64 data URL containing the file contents
         * @throws NullPointerException if {@code imageFile} is {@code null}
         * @throws IllegalArgumentException if the file cannot be read
         */
        @Override
        public String handleImage(File imageFile) {
            Objects.requireNonNull(imageFile, "imageFile must be non-null");

            try {
                String contentType = Files.probeContentType(imageFile.toPath());
                if (contentType == null) {
                    contentType = URLConnection.guessContentTypeFromName(imageFile.getName());
                }
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                byte[] imageBytes = Files.readAllBytes(imageFile.toPath());
                return "data:" + contentType + ";base64," + Base64.getEncoder().encodeToString(imageBytes);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not read the given image file", e);
            }
        }
    }
}
