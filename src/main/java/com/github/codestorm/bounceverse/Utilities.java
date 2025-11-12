package com.github.codestorm.bounceverse;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.github.codestorm.bounceverse.components.Component;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Rectangle2D;

import java.io.*;
import java.util.*;

/** Utilities. */
public final class Utilities {
    private Utilities() {
    }

    /** Input/Output utilities. */
    public static final class IO {
        // ... (Nội dung của lớp IO giữ nguyên, không cần thay đổi)
        private IO() {
        }

        public static Properties loadProperties(String path) throws IOException {
            var fileStream = IO.class.getResourceAsStream(path);
            if (fileStream == null) {
                throw new IOException("Cannot open InputStream on " + path);
            }

            var prop = new Properties();
            prop.load(fileStream);
            fileStream.close();
            return prop;
        }

        public static HashMap<String, String> parseArgs(
                String[] args,
                HashMap<String, String> defaults,
                HashMap<String, String> whiteList) {
            var res = new HashMap<String, String>();
            if (defaults != null) {
                for (var e : defaults.entrySet()) {
                    if (e.getValue() != null) {
                        res.put(e.getKey(), e.getValue());
                    }
                }
            }
            for (var s : args) {
                var kv = s.split("=", 2);
                if (whiteList != null && !whiteList.containsKey(kv[0])) {
                    continue;
                }
                res.put(kv[0], kv.length < 2 ? null : kv[1]);
            }
            return res;
        }

        public static List<String> readTextFile(String path) throws IOException {
            final var res = new ArrayList<String>();
            final var stream = IO.class.getResourceAsStream(path);
            if (stream == null) {
                throw new FileNotFoundException(path);
            }
            final var scanner = new Scanner(stream);
            while (scanner.hasNextLine()) {
                res.add(scanner.nextLine());
            }
            scanner.close();
            stream.close();
            return res;
        }
    }

    public static final class Geometric {
        private Geometric() {
        }

        /**
         * Lọc các Entity trong phạm vi Hình tròn.
         *
         * @param cx     Tâm X
         * @param cy     Tâm Y
         * @param radius Bán kính
         * @return Các entity
         */
        public static List<Entity> getEntityInCircle(double cx, double cy, double radius) {
            final var outRect = new Rectangle2D(cx - radius, cy - radius, 2 * radius, 2 * radius);
            return FXGL.getGameWorld().getEntitiesInRange(outRect).stream()
                    .filter(
                            e -> {
                                var nearestX = Math.max(e.getX(), Math.min(cx, e.getX() + e.getWidth()));
                                var nearestY = Math.max(e.getY(), Math.min(cy, e.getY() + e.getHeight()));
                                var dx = cx - nearestX;
                                var dy = cy - nearestY;
                                return (dx * dx + dy * dy) <= radius * radius;
                            })
                    .toList();
        }

        /**
         * Lọc các Entity trong phạm vi Hình chữ nhật.
         *
         * @param centerX Tâm X của hình chữ nhật
         * @param centerY Tâm Y của hình chữ nhật
         * @param width   Chiều rộng của hình chữ nhật
         * @param height  Chiều cao của hình chữ nhật
         * @return Danh sách các entity nằm trong khu vực đó
         */
        public static List<Entity> getEntitiesInRectangle(double centerX, double centerY, double width, double height) {
            double topLeftX = centerX - width / 2;
            double topLeftY = centerY - height / 2;
            Rectangle2D explosionArea = new Rectangle2D(topLeftX, topLeftY, width, height);
            return FXGL.getGameWorld().getEntitiesInRange(explosionArea);
        }
    }

    // ... (Các lớp Collision, Compatibility, Typing giữ nguyên, không cần thay đổi)
    public static final class Collision {
        private Collision() {
        }

        public static DirectionUnit getCollisionDirection(Entity source, Entity target) {
            var fromBox = source.getBoundingBoxComponent();
            var toBox = target.getBoundingBoxComponent();
            var fCenter = fromBox.getCenterWorld();
            var tCenter = toBox.getCenterWorld();
            var direction = tCenter.subtract(fCenter);
            return Math.abs(direction.getX()) > Math.abs(direction.getY())
                    ? direction.getX() > 0 ? DirectionUnit.RIGHT : DirectionUnit.LEFT
                    : direction.getY() > 0 ? DirectionUnit.DOWN : DirectionUnit.UP;
        }
    }

    public static final class Compatibility {
        private Compatibility() {
        }

        public static void throwIfNotCompatible(EntityType entityType,
                com.almasb.fxgl.entity.component.Component... components) {
            for (var param : components) {
                final var annotation = param.getClass().getAnnotation(OnlyForEntity.class);
                if (annotation == null) {
                    continue;
                }
                final var paramEntityTypeSet = EnumSet.copyOf(Arrays.asList(annotation.value()));
                if (!paramEntityTypeSet.contains(entityType)) {
                    throw new IllegalArgumentException(
                            String.format(
                                    "Class '%s' does not compatible for entity has '%s'"
                                            + " entityType.",
                                    param.getClass().getSimpleName(), entityType.name()));
                }
            }
        }

        public static boolean isCompatible(EntityType entityType, Component... params) {
            try {
                throwIfNotCompatible(entityType, params);
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }

    public static final class Typing {
        private Typing() {
        }

        public static <T> T getOr(SpawnData data, String key, T ifNot) {
            if (data.hasKey(key)) {
                return data.get(key);
            }
            return ifNot;
        }

        @SafeVarargs
        public static <T> T[] toArray(T... varargs) {
            return varargs;
        }
    }

}