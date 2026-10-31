package net.atlanticbb.tantlinger.io;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Various IO utility methods
 *
 * @author Bob Tantlinger
 */
public final class IOUtils {
    private static final int BUFFER_SIZE = 1024 * 8;
    private static final FileCopyMonitor NO_OP_MONITOR = new NullCopyMonitor();

    private IOUtils() {
        /* This utility class should not be instantiated */
    }

    public static File[] getDirectoryContents(File rootdir) {
        List<File> files = new ArrayList<>();
        getDirectoryContents(rootdir, files);
        return files.toArray(File[]::new);
    }

    private static void getDirectoryContents(File directory, List<File> files) {
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isDirectory()) {
                getDirectoryContents(file, files);
            } else if (file.isFile()) {
                files.add(file);
            }
        }
    }

    public static String sanitize(String value) {
        /*
            Characters that are OK in a file are described
            by regular expressions as:

            \w - alphanumeric (A-Za-z0-9)
            \. - dot
            \- - dash
            \: - colon
            \; - semicolon
            \# - number sign
            \_ - underscore

           Each \ above must be escaped to allow javac to parse
           it correctly. That's why it looks so bad below.

           Since we want to replace things that are not the above,
           set negation ([^ and ]) is used.
        */

        return value.replaceAll("[^\\w.-:;#]", "_");
    }

    /**
     * Gets the extension of a file e.g. anything after the '.'
     *
     * @param f The file from which to get the extension
     * @return The extension, or an empty string if the file has no extension
     */
    public static String getExtension(File f) {
        String name = f.getName();
        int i = name.lastIndexOf(".");
        return i < 0 || i == name.length() - 1 ? "" : name.substring(i + 1);
    }

    /**
     * Gets the name of the file without the extension
     *
     * @param f The file
     * @return The name, sans any extension
     */
    public static String getName(File f) {
        String name = f.getName();
        int i = name.lastIndexOf(".");
        return i < 0 ? name : name.substring(0, i);

    }

    /**
     * If the specified file already exists, a new, uniquely named file is
     * returned. Its name is incremented. For example if a file named "file.txt"
     * already exists, a file named "file-1.txt" is returned.
     *
     * @param f The file fo which to create a name
     * @return A file with a unique name
     */
    public static File createUniqueFile(File f) {
        while (f.exists()) {
            String ext = getExtension(f);
            String name = getName(f);
            int dashPos = name.lastIndexOf('-');

            if (dashPos < 0) {
                name += "-1";
            } else {
                String num = name.substring(dashPos + 1);
                String temp = name.substring(0, dashPos);

                try {
                    int cur = Integer.parseInt(num) + 1;
                    name = temp + "-" + cur;
                } catch (NumberFormatException nfe) {
                    name += "-1";
                }
            }

            String uniqueName = name;
            if (!ext.isEmpty()) uniqueName += "." + ext;

            String parent = Objects.requireNonNullElse(f.getParent(), "");
            f = new File(parent, uniqueName);
        }

        return f;
    }

    /**
     * Copies a Reader to a Writer.
     *
     * @param src The src {@link Reader}
     * @param dest The target {@link Writer}
     * @throws IOException Something went wrong
     */
    public static void copy(Reader src, Writer dest) throws IOException {
        char[] buffer = new char[BUFFER_SIZE];
        int n;

        while ((n = src.read(buffer)) > 0) {
            dest.write(buffer, 0, n);
        }
    }

    /**
     * Copies an InputStream to an OutputStream
     *
     * @param src The source {@link InputStream}
     * @param dest The target {@link OutputStream}
     * @throws IOException Something went wrong
     */
    public static void copy(InputStream src, OutputStream dest) throws IOException {
        copy(src, dest, NO_OP_MONITOR);
    }

    /**
     * Copies an InputStream to an OutputStream
     *
     * @param src The source {@link InputStream}
     * @param dest The target {@link OutputStream}
     * @param monitor Monitors the bytes copied and if the copy was aborted
     * @throws IOException Something went wrong
     */
    public static void copy(InputStream src, OutputStream dest, CopyMonitor monitor) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int n;

        while ((n = src.read(buffer)) > 0 && !monitor.isCopyAborted()) {
            dest.write(buffer, 0, n);
            monitor.bytesCopied(n);
        }
    }

    /**
     * Copies a file. Overwrites the destination file if it already exists
     *
     * @param src The source {@link File}
     * @param dest The target {@link File}
     * @throws IOException Something went wrong
     */
    public static void copy(File src, File dest) throws IOException {
        copy(src, dest, NO_OP_MONITOR);
    }

    /**
     * Copies a file.
     *
     * @param src The source {@link File}
     * @param dest The target {@link File}
     * @throws IOException Something went wrong
     */
    public static void copy(File src, File dest, boolean overwrite) throws IOException {
        copy(src, dest, NO_OP_MONITOR, overwrite);
    }

    /**
     * Copies a file. Overwrites the destination file if it already exists
     *
     * @param src The source {@link File}
     * @param dest The target {@link File}
     * @param monitor monitors the copy
     * @throws IOException Something went wrong
     */
    public static void copy(File src, File dest, FileCopyMonitor monitor) throws IOException {
        copy(src, dest, monitor, true);
    }

    /**
     * Copies a file
     *
     * @param src The source {@link File}
     * @param dest The target {@link File}
     * @param monitor Monitors the copy
     * @param overwrite if true, overwrites a destination file with the same name
     *                  otherwise, it creates a new destination file with a unique name before the copy
     * @throws IOException Something went wrong
     */
    public static void copy(File src, File dest, FileCopyMonitor monitor, boolean overwrite)
            throws IOException {
        monitor.copyingFile(src);
        try (InputStream in = new FileInputStream(src)) {
            try (OutputStream out = new FileOutputStream(
                    overwrite ? dest : createUniqueFile(dest))) {
                copy(in, out, monitor);
            }
        }
    }

    /**
     * Copies the file at the specified source path to the destination path.
     * Overwrites the destination file if it already exists.
     *
     * @param srcPath The source path
     * @param destPath The target path
     * @throws IOException Something went wrong
     */
    public static void copy(String srcPath, String destPath) throws IOException {
        copy(new File(srcPath), new File(destPath));
    }

    /**
     * Copies the file at the specified source path to the destination path.
     * Overwrites the destination file if it already exists.
     *
     * @param srcPath The source path
     * @param destPath The target path
     * @param monitor Monitors the copy
     * @throws IOException Something went wrong
     */
    public static void copy(String srcPath, String destPath, FileCopyMonitor monitor)
            throws IOException {
        copy(new File(srcPath), new File(destPath), monitor);
    }

    /**
     * Copies the file at the specified source path to the destination path.
     *
     * @param srcPath The source path
     * @param destPath The target path
     * @param overwrite If true overwrites the file at the dest path if it exists
     * @throws IOException Something went wrong
     */
    public static void copy(String srcPath, String destPath, boolean overwrite)
            throws IOException {
        copy(new File(srcPath), new File(destPath), overwrite);
    }

    /**
     * Copies the file at the specified source path to the destination path.
     *
     * @param srcPath The source path
     * @param destPath The target path
     * @param monitor Monitors the copy
     * @param overwrite If true overwrites the file at the dest path if it exists
     * @throws IOException Something went wrong
     */
    public static void copy(String srcPath, String destPath, FileCopyMonitor monitor, boolean overwrite)
            throws IOException {
        copy(new File(srcPath), new File(destPath), monitor, overwrite);
    }

    /**
     * Recursively copy all files from one directory to another.
     *
     * @param src  Directory to copy from.
     * @param dest Directory to copy to.
     * @throws IOException Something went wrong
     */
    public static void copyFiles(File src, File dest) throws IOException {
        copyFiles(src, dest, NO_OP_MONITOR);
    }

    /**
     * Recursively copy all files from one directory to another.
     *
     * @param src  Directory to copy from.
     * @param dest Directory to copy to.
     * @param monitor Monitors the copy.
     * @throws IOException Something went wrong
     */
    public static void copyFiles(File src, File dest, FileCopyMonitor monitor) throws IOException {
        copyFilesRecursively(src, dest, monitor);
    }

    private static void copyFilesRecursively(File src, File dest, FileCopyMonitor monitor)
            throws IOException {

        if (monitor.isCopyAborted())
            return;

        if (!src.exists())
            throw new FileNotFoundException("File not found:" + src);

        if (src.isDirectory()) {
            // Create destination directory
            if (!dest.exists()) {
                dest.mkdirs();
            }

            // Go through the contents of the directory
            String[] list = Objects.requireNonNull(src.list());
            Arrays.sort(list);

            for (String entry : list) {
                copyFilesRecursively(new File(src, entry), new File(dest, entry), monitor);
            }
        } else {
            copy(src, dest, monitor, true);
        }
    }

    /**
     * Gets the total bytes in the file or directory
     *
     * @param file A file or directory.
     * @return The total bytes of the File. If the File is a directory
     * the total bytes of all files in all subfolders is returned
     */
    public static long getTotalBytes(File file) {
        long bytes;

        if (file.isDirectory()) {
            bytes = Stream.of(Objects.requireNonNull(file.listFiles()))
                    .map(IOUtils::getTotalBytes)
                    .reduce(0L, Long::sum);
        } else {
            bytes = file.length();
        }

        return bytes;
    }

    /**
     * Reads an input stream and returns the contents as a String
     *
     * @param input The {@link InputStream} to read
     * @return The contents of the stream
     * @throws IOException Something went wrong
     */
    public static String read(InputStream input) throws IOException {
        return read(new InputStreamReader(input));
    }

    /**
     * Reads a File and returns the contents as a String
     *
     * @param file The File to read
     * @return The contents of the file
     * @throws IOException Something went wrong
     */
    public static String read(File file) throws IOException {
        return read(new FileReader(file));
    }

    /**
     * Reads a String from a Reader
     *
     * @param input The {@link Reader} to read from
     * @return The contents of the reader
     * @throws IOException Something went wrong
     */
    public static String read(Reader input) throws IOException {
        try (BufferedReader reader = new BufferedReader(input)) {
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    /**
     * Writes a String to a File using a PrintWriter
     *
     * @param str The string to write
     * @param file The target file
     * @throws IOException Something went wrong
     */
    public static void write(String str, File file) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(file))) {
            out.write(str);
            close(out);
        }
    }

    /**
     * Writes the raw data from an InputStream to a File
     *
     * @param input The input stream to read from
     * @param file The target file
     * @throws IOException Something went wrong
     */
    public static void write(InputStream input, File file) throws IOException {
        try (FileOutputStream output = new FileOutputStream(file)) {
            copy(input, output);
        }
    }

    /**
     * Recursively deletes a directory, thereby removing all its contents
     *
     * @param file the file or file to delete
     * @return true if the file(s) were successfully deleted
     */
    public static boolean deleteRecursively(File file) {
        if (file.isDirectory()) {
            for (File child : Objects.requireNonNull(file.listFiles())) {
                if (!deleteRecursively(child))
                    return false;
            }
        }

        return file.delete();
    }

    /**
     * Quietly closes a {@link Closeable}, swallowing any exceptions
     *
     * @param closeable The Closeable to close
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception ignored) {
            }
        }
    }

    private static class NullCopyMonitor implements FileCopyMonitor {
        public void bytesCopied(int numBytes) {
            // Does nothing
        }

        public void copyingFile(File f) {
            // Does nothing
        }

        public boolean isCopyAborted() {
            return false;
        }
    }
}