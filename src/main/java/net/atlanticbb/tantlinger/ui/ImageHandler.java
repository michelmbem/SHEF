package net.atlanticbb.tantlinger.ui;

import java.io.File;

/**
 * Converts an image file selected from disk or pasted from the clipboard into
 * a source string that can be used by an HTML {@code img} element.
 */
public interface ImageHandler {

    /**
     * Handles an image file and returns its URL or other supported source value.
     *
     * @param imageFile the image file to handle
     * @return the source string to use for the image in the HTML document
     * @throws NullPointerException if {@code imageFile} is {@code null}
     * @throws IllegalArgumentException if the image cannot be converted to a source
     */
    String handleImage(File imageFile);
}
