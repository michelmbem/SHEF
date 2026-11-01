package net.atlanticbb.tantlinger.ui;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Objects;

public final class ImageHandlers {
    private static ImageHandler current = new Default();

    private ImageHandlers() {}

    public static ImageHandler getCurrent() {
        return current;
    }

    public static void setCurrent(ImageHandler handler) {
        current = Objects.requireNonNull(handler);
    }

    private static final class Default implements ImageHandler {
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
}
