package com.github.codestorm.bounceverse;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import javafx.util.Duration;

/** Utilities. */
public final class Utils {
    private Utils() {}

    /** Input/Output utilities. */
    public static final class IO {
        private IO() {}

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
                throw new IOException("Cannot open InputStream on " + path);
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
    }

    public static final class Time {
        /**
         * Thời gian hồi để thực hiện lại gì đó. Thực hiện thông qua {@link #current}
         *
         * @see ActiveCooldown
         */
        public static final class Cooldown {
            private Duration duration;
            private ActiveCooldown current;

            public Cooldown(Duration duration) {
                this.duration = duration;
            }

            /**
             * Đặt callback khi cooldown hết hạn.
             *
             * @param callback Callback
             */
            public void setOnExpired(Runnable callback) {
                current.customOnExpired = callback;
            }

            public Duration getDuration() {
                return duration;
            }

            /**
             * Đặt thời lượng cooldown mới.
             *
             * <p><b>Lưu ý: Chỉ áp dụng cho cooldown mới.</b>
             *
             * @param duration Thời lượng mới
             */
            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public ActiveCooldown getCurrent() {
                return current;
            }

            /** Đại diện cooldown hiện tại. Giống như một wrapper của {@link TimerAction}. */
            public final class ActiveCooldown {
                private TimerAction waiter = null;
                private double timestamp = Double.NaN;
                private Runnable customOnExpired = null;

                private ActiveCooldown() {}

                /** Hành động khi cooldown hết. */
                private void onExpired() {
                    timestamp = Double.NaN;
                    if (customOnExpired != null) {
                        customOnExpired.run();
                    }
                }

                /**
                 * Kiểm tra Cooldown hiện tại hết hạn chưa.
                 *
                 * @return {@code true} nếu hết hạn, ngược lại {@code false}.
                 */
                public boolean expired() {
                    return (waiter == null) || waiter.isExpired();
                }

                /** Khiến cooldown hết hạn ngay (nếu có). */
                public void expire() {
                    if (!expired()) {
                        waiter.expire();
                    }
                }

                /** Set một cooldown mới. */
                public void makeNew() {
                    expire();

                    final var gameTimer = FXGL.getGameTimer();
                    waiter = gameTimer.runOnceAfter(this::onExpired, duration);
                    timestamp = gameTimer.getNow();
                }

                public void pause() {
                    if (!expired()) {
                        waiter.pause();
                    }
                }

                public void resume() {
                    if (!expired()) {
                        waiter.resume();
                    }
                }

                public boolean isPaused() {
                    return !expired() && waiter.isPaused();
                }

                /**
                 * Lấy thời gian còn lại của cooldown.
                 *
                 * @return Thời gian còn lại
                 */
                public Duration getTimeLeft() {
                    if (expired()) {
                        return Duration.ZERO;
                    }
                    final var elapsed = Duration.millis(FXGL.getGameTimer().getNow() - timestamp);
                    return duration.subtract(elapsed);
                }

                /**
                 * Giảm thời gian hồi đi một lượng thời gian.
                 *
                 * @param duration Thời lượng giảm.
                 */
                public void reduce(Duration duration) {
                    if (!expired()) {
                        waiter.update(duration.toMillis());
                    }
                }
            }
        }
    }
}
