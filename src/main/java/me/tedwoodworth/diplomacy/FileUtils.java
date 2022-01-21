package me.tedwoodworth.diplomacy;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Utility class used for dealing with files.
 */
public class FileUtils {

    /**
     * Copies a file from one location to another
     *
     * @param toCopy:   File to copy
     * @param destFile: destination file to paste at
     * @return
     */
    public static boolean copyFile(final File toCopy, final File destFile) {
        try {
            return FileUtils.copyStream(new FileInputStream(toCopy),
                    new FileOutputStream(destFile));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Copies a file from one location to another, and supports the copying of
     * directories via recursion.
     *
     * @param toCopy: File to copy
     * @param destDir: Destination file to paste at
     * @return true if successful, otherwise false
     */
    private static boolean copyFilesRecusively(final File toCopy,
                                               final File destDir) {
        assert destDir.isDirectory();

        if (!toCopy.isDirectory()) {
            return FileUtils.copyFile(toCopy, new File(destDir, toCopy.getName()));
        } else {
            final File newDestDir = new File(destDir, toCopy.getName());
            if (!newDestDir.exists() && !newDestDir.mkdir()) {
                return false;
            }
            for (final File child : toCopy.listFiles()) {
                if (!FileUtils.copyFilesRecusively(child, newDestDir)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Used for copying the resources of a JAR file
     * @param destDir: Destination directory to paste to
     * @param jarConnection: Connection to JAR file
     * @return true if successful, otherwise false
     * @throws IOException if an error related to directory creation takes place
     */
    public static boolean copyJarResourcesRecursively(final File destDir,
                                                      final JarURLConnection jarConnection) throws IOException {

        final JarFile jarFile = jarConnection.getJarFile();

        for (final Enumeration<JarEntry> e = jarFile.entries(); e.hasMoreElements();) {
            final JarEntry entry = e.nextElement();
            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                final String filename = StringUtils.removeStart(entry.getName(), //
                        jarConnection.getEntryName());

                final File f = new File(destDir, filename);
                if (!entry.isDirectory()) {
                    final InputStream entryInputStream = jarFile.getInputStream(entry);
                    if(!FileUtils.copyStream(entryInputStream, f)){
                        return false;
                    }
                    entryInputStream.close();
                } else {
                    if (!FileUtils.ensureDirectoryExists(f)) {
                        throw new IOException("Could not create directory: "
                                + f.getAbsolutePath());
                    }
                }
            }
        }
        return true;
    }

    /**
     * Used for copying an input stream into a file
     * @param is: Stream to copy
     * @param f: File to copy into
     * @return true if successful, otherwise false
     */
    private static boolean copyStream(final InputStream is, final File f) {
        try {
            return FileUtils.copyStream(is, new FileOutputStream(f));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Used for copying an input stream into an output stream
     * @param is: input stream to copy
     * @param os: output stream to paste into
     * @return true if successful, otherwise false
     */
    private static boolean copyStream(final InputStream is, final OutputStream os) {
        try {
            final byte[] buf = new byte[1024];

            int len = 0;
            while ((len = is.read(buf)) > 0) {
                os.write(buf, 0, len);
            }
            is.close();
            os.close();
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Used to validate that a file/directory was successfully created/exists
     * @param f: File to check
     * @return true if exists, otherwise false
     */
    private static boolean ensureDirectoryExists(final File f) {
        return f.exists() || f.mkdir();
    }
}
