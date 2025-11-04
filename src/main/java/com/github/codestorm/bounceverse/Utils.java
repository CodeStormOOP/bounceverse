package com.github.codestorm.bounceverse;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.data.types.DirectionUnit;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Circle;
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
         * "=" is a delimiter. <br>
         * Source code from <a href="https://stackoverflow.com/a/52940215/16410937">here</a>.
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

            private final ActiveCooldown current = new ActiveCooldown();
            private Duration duration = Duration.INDEFINITE;

            public Duration getDuration() {
                return duration;
            }

            /**
             * Đặt thời lượng cooldown mới. <br>
             * <b>Lưu ý: Chỉ áp dụng cho cooldown mới.</b>
             *
             * @param duration Thời lượng mới
             */
            public void setDuration(Duration duration) {
                this.duration = duration;
            }

            public ActiveCooldown getCurrent() {
                return current;
            }

            public Cooldown() {}

            public Cooldown(Duration duration) {
                this.duration = duration;
            }

            /** Cooldown thời điểm hiện tại. Giống như một wrapper của {@link TimerAction}. */
            public final class ActiveCooldown {

                private TimerAction waiter = null;
                private double timestamp = Double.NaN;
                private Runnable onExpiredCallback = null;

                /** Hành động khi cooldown hết. */
                private void onExpired() {
                    timestamp = Double.NaN;
                    if (onExpiredCallback != null) {
                        onExpiredCallback.run();
                    }
                }

                /**
                 * Callback thực thi khi cooldown hết hạn.
                 *
                 * @param callback Callback sẽ thực thi
                 */
                public void setOnExpired(Runnable callback) {
                    this.onExpiredCallback = callback;
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

                /** Tạm dừng cooldown. */
                public void pause() {
                    if (!expired()) {
                        waiter.pause();
                    }
                }

                /** Tiếp tục cooldown. */
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

                private ActiveCooldown() {}
            }
        }
    }

    public static final class Geometric {

        /**
         * Lọc các Entity trong phạm vi Hình tròn.
         *
         * @param circle Hình tròn
         * @return Các entity
         */
        public static List<Entity> getEntityInCircle(Circle circle) {
            final var cx = circle.getCenterX();
            final var cy = circle.getCenterY();
            final var radius = circle.getRadius();

            return getEntityInCircle(cx, cy, radius);
        }

        /**
         * Lọc các Entity trong phạm vi Hình tròn.
         *
         * @param cx Tâm X
         * @param cy Tâm Y
         * @param radius Bán kính
         * @return Các entity
         */
        public static List<Entity> getEntityInCircle(double cx, double cy, double radius) {
            final Rectangle2D outRect =
                    new Rectangle2D(cx - radius, cy - radius, 2 * radius, 2 * radius);
            return FXGL.getGameWorld().getEntitiesInRange(outRect).stream()
                    .filter(
                            e -> {
                                double nearestX =
                                        Math.max(e.getX(), Math.min(cx, e.getX() + e.getWidth()));
                                double nearestY =
                                        Math.max(e.getY(), Math.min(cy, e.getY() + e.getHeight()));
                                double dx = cx - nearestX;
                                double dy = cy - nearestY;
                                return (dx * dx + dy * dy) <= radius * radius;
                            })
                    .toList();
        }
    }

    public static final class Collision {

        /**
         * Xác định hướng va chạm giữa hai Entity bằng cách tính độ chồng lấn (overlap). Trả về
         * hướng mà source "tiếp xúc" với target: UP, DOWN, LEFT, RIGHT. Nếu không chồng lấn rõ
         * ràng, trả về null.
         */
        public static DirectionUnit getCollisionDirection(Entity source, Entity target) {
            var fromBox = source.getBoundingBoxComponent();
            var toBox = target.getBoundingBoxComponent();

            double fx = fromBox.getMinXWorld();
            double fy = fromBox.getMinYWorld();
            double fw = fromBox.getWidth();
            double fh = fromBox.getHeight();

            double tx = toBox.getMinXWorld();
            double ty = toBox.getMinYWorld();
            double tw = toBox.getWidth();
            double th = toBox.getHeight();

            double overlapX = Math.min(fx + fw, tx + tw) - Math.max(fx, tx);
            double overlapY = Math.min(fy + fh, ty + th) - Math.max(fy, ty);
            final double EPS = 0.0001;
            if (Math.abs(overlapX - overlapY) <= EPS) {
                var phys = source.getComponentOptional(PhysicsComponent.class);
                if (phys.isPresent()) {
                    Point2D vel = (Point2D) phys.get().getLinearVelocity();
                    if (Math.abs(vel.getX()) > Math.abs(vel.getY())) {
                        return (vel.getX() > 0) ? DirectionUnit.LEFT : DirectionUnit.RIGHT;
                    } else {
                        return (vel.getY() > 0) ? DirectionUnit.UP : DirectionUnit.DOWN;
                    }
                }
            }

            if (overlapX <= 0 || overlapY <= 0) {
                return null;
            }

            double fcx = fx + fw / 2.0;
            double fcy = fy + fh / 2.0;
            double tcx = tx + tw / 2.0;
            double tcy = ty + th / 2.0;

            if (overlapX < overlapY) {

                return (fcx < tcx) ? DirectionUnit.LEFT : DirectionUnit.RIGHT;
            } else {

                return (fcy < tcy) ? DirectionUnit.UP : DirectionUnit.DOWN;
            }
        }
    }
}
