package com.github.codestorm.bounceverse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/** Utilities. */
public final class Utils {
    /** File utilities. */
    public static final class File {
        /**
         * Load .properties file.
         *
         * @param path Relative path
         * @return Parsed properties
         * @throws IOException if an error occurred when reading from the input stream.
         */
        public static Properties loadProperties(String path) throws IOException {
            InputStream fileStream = File.class.getResourceAsStream(path);
            assert fileStream != null;

            Properties prop = new Properties();
            prop.load(fileStream);
            fileStream.close();
            return prop;
        }

        private File() {}
    }

    private Utils() {}
}
