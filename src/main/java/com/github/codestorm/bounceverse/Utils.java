package com.github.codestorm.bounceverse;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/** Utilities. */
public final class Utils {
    /** Input/Output utilities. */
    public static final class IO {
        /**
         * Load .properties file.
         *
         * @param path Relative path
         * @return Parsed properties
         * @throws IOException if an error occurred when reading from the input stream.
         */
        public static Properties loadProperties(String path) throws IOException {
            InputStream fileStream = IO.class.getResourceAsStream(path);
            if (fileStream == null) {
                throw new IOException("Cannot open InputStream in" + path);
            }

            Properties prop = new Properties();
            prop.load(fileStream);
            fileStream.close();
            return prop;
        }

        /**
         * Convert an array of key=value pairs into a hashmap. The string "key=" maps key onto "",
         * while just "key" maps key onto null. The value may contain '=' characters, only the first
         * "=" is a delimiter.
         *
         * <p>Source code from <a href="https://stackoverflow.com/a/52940215/16410937">here</a>.
         *
         * @param args command-line arguments in the key=value format (or just key= or key)
         * @param defaults a map of default values, may be null. Mappings to null are not copied to
         *     the resulting map.
         * @param whiteList if not null, the keys not present in this map cause an exception (and
         *     keys mapped to null are ok)
         * @return a map that maps these keys onto the corresponding values.
         */
        public static HashMap<String, String> parseArgs(
                String[] args,
                HashMap<String, String> defaults,
                HashMap<String, String> whiteList) {
            // HashMap allows null values
            HashMap<String, String> res = new HashMap<>();
            if (defaults != null) {
                for (Map.Entry<String, String> e : defaults.entrySet()) {
                    if (e.getValue() != null) {
                        res.put(e.getKey(), e.getValue());
                    }
                }
            }
            for (String s : args) {
                String[] kv = s.split("=", 2);
                if (whiteList != null && !whiteList.containsKey(kv[0])) {
                    continue;
                }
                res.put(kv[0], kv.length < 2 ? null : kv[1]);
            }
            return res;
        }

        /**
         * Read text file (txt) and put all lines into {@link List}.
         *
         * @param path File path
         * @return All lines in text file
         */
        public static List<String> readTextFile(String path) {
            var res = new ArrayList<String>();
            var scanner = new Scanner(path);
            while (scanner.hasNext()) {
                res.add(scanner.next());
            }
            scanner.close();
            return res;
        }

        private IO() {}
    }

    private Utils() {}
}
