package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.factory.entities.BrickFactory;
import com.github.codestorm.bounceverse.factory.entities.BulletFactory;
import com.github.codestorm.bounceverse.factory.entities.PaddleFactory;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import com.github.codestorm.bounceverse.ui.HorizontalPositiveInteger;
import com.github.codestorm.bounceverse.ui.ingame.Hearts;

import javafx.geometry.Point2D;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

public final class GameSystem extends InitialSystem {
    private GameSystem() {}

    public static GameSystem getInstance() {
        return GameSystem.Holder.INSTANCE;
    }

    /** Xử lý lưu trữ biến toàn cục trong Game. */
    public static final class Variables {
        private Variables() {}

        public static final int MAX_LIVES = 5;
        public static final int DEFAULT_LIVES = 3;
        public static final int DEFAULT_SCORE = 0;
        public static final SaveLoadHandler SAVE_LOAD_HANDLER =
                new SaveLoadHandler() {
                    @Override
                    public void onSave(@NotNull DataFile dataFile) {
                        final var properties = FXGL.getWorldProperties();
                        final HealthIntValue lives = properties.getObject("lives");
                        final int score = properties.getValue("score");

                        // TODO: Save map

                        final var bundle = new Bundle("game");
                        bundle.put("lives", lives.getValue());
                        bundle.put("score", score);
                        dataFile.putBundle(bundle);
                    }

                    @Override
                    public void onLoad(@NotNull DataFile dataFile) {
                        final var bundle = dataFile.getBundle("game");
                        final int intLives = bundle.get("lives");
                        final var lives =
                                new HealthIntValue(GameSystem.Variables.MAX_LIVES, intLives);
                        final int score = bundle.get("score");

                        final var vars = FXGL.getWorldProperties();
                        vars.setValue("lives", lives);
                        vars.setValue("score", score);
                    }
                };

        /**
         * Tải biến lưu trữ mặc định vào Game. <br>
         *
         * @apiNote Sử dụng {@link FXGL#getWorldProperties()}
         * @param vars Map biến
         */
        public static void loadDefault(Map<String, Object> vars) {
            final var lives = new HealthIntValue(MAX_LIVES, DEFAULT_LIVES);

            vars.clear();
            vars.put("lives", lives);
            vars.put("score", DEFAULT_SCORE);
        }
    }

    /** Phương thức spawn entities trong Game. */
    private static final class EntitySpawn {
        private EntitySpawn() {}

        public static void addFactory() {
            FXGL.getGameWorld().addEntityFactory(new WallFactory());
            FXGL.getGameWorld().addEntityFactory(new BrickFactory());
            FXGL.getGameWorld().addEntityFactory(new BulletFactory());
            FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
            FXGL.getGameWorld().addEntityFactory(new BallFactory());
        }

        public static void walls() {
            FXGL.spawn("wallTop");
            FXGL.spawn("wallBottom");
            FXGL.spawn("wallLeft");
            FXGL.spawn("wallRight");
        }

        public static void brick() {
            var rows = 6;
            var cols = 10;
            double startX = 85;
            double startY = 50;
            double brickWidth = 80;
            double brickHeight = 30;
            double spacingX = 5;
            double spacingY = 5;

            for (var y = 0; y < rows; y++) {
                for (var x = 0; x < cols; x++) {
                    var posX = startX + x * (brickWidth + spacingX);
                    var posY = startY + y * (brickHeight + spacingY);

                    // Xác định loại brick theo hàng (bạn có thể chỉnh lại tuỳ ý)
                    String type;
                    switch (y) {
                        case 0 -> type = "shieldBrick"; // Hàng đầu có khiên
                            //                    case 1 -> type = "explodingBrick"; // Hàng thứ 2
                            // nổ
                            //                    case 2 -> type = "specialBrick"; // Hàng thứ 3 rơi
                            // power-up
                        case 3 -> type = "strongBrick"; // Hàng thứ 4 trâu
                        default -> type = "normalBrick"; // Còn lại là thường
                    }

                    var data = new SpawnData();
                    data.put("pos", new Point2D(posX, posY));
                    FXGL.spawn(type, data);
                }
            }
        }

        public static void paddle() {
            FXGL.getGameWorld()
                    .getEntitiesByType(EntityType.PADDLE)
                    .forEach(Entity::removeFromWorld);

            var px = FXGL.getAppWidth() / 2.0 - 60;
            double py = FXGL.getAppHeight() - 40;
            FXGL.spawn("paddle", px, py);
        }

        public static void ball() {
            if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
                var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);

                var x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
                var y = paddle.getY() - BallFactory.DEFAULT_RADIUS + 1;

                FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
                FXGL.set("ballAttached", true);
            }
        }
    }

    /**
     * Hiển thị UI/HUD trong Game.
     *
     * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
     */
    private static final class UI {
        private UI() {}

        private boolean isInitialized = false;
        private Hearts heartsDisplay;
        private HorizontalPositiveInteger scoreDisplay;

        public static UI getInstance() {
            return Holder.INSTANCE;
        }

        /** Thêm Score display (hiển thị điểm số) vào game scene. */
        public void addScoreDisplay() {
            if (scoreDisplay == null) {
                final var scoreProperty = FXGL.getWorldProperties().intProperty("score");
                scoreDisplay = new HorizontalPositiveInteger(scoreProperty);
                scoreDisplay.setMinDigits(6);
            }

            FXGL.getGameScene().addUINode(scoreDisplay.getView());
            scoreDisplay.getView().setTranslateX(20);
            scoreDisplay.getView().setTranslateY(20); // Ở trên cùng
        }

        /** Thêm Hearts display (hiển thị số mạng) vào game scene. */
        public void addHeartsDisplay() {
            if (heartsDisplay == null) {
                heartsDisplay = new Hearts();
            }

            FXGL.getGameScene().addUINode(heartsDisplay.getView());
            heartsDisplay.getView().setTranslateX(20);
            heartsDisplay.getView().setTranslateY(FXGL.getAppHeight() - 52); // Dưới cùng màn hình
        }

        /** Thêm tất cả UI elements vào game scene. */
        public void addAll() {
            if (isInitialized) {
                return;
            }

            addScoreDisplay();
            addHeartsDisplay();
            isInitialized = true;
        }

        /** Xóa tất cả UI elements khỏi game scene và giải phóng tài nguyên. */
        public void removeAll() {
            if (!isInitialized) {
                return;
            }

            if (scoreDisplay != null) {
                FXGL.getGameScene().removeUINode(scoreDisplay.getView());
            }

            if (heartsDisplay != null) {
                FXGL.getGameScene().removeUINode(heartsDisplay.getView());
            }

            isInitialized = false;
        }

        /** Giải phóng hoàn toàn tài nguyên (xóa references). */
        public void dispose() {
            removeAll();
            scoreDisplay = null;
            heartsDisplay = null;
        }

        /** Kiểm tra UI đã được khởi tạo chưa. */
        public boolean isInitialized() {
            return isInitialized;
        }

        /** Lazy-loaded singleton holder. */
        private static final class Holder {
            static final UI INSTANCE = new UI();
        }
    }

    @Override
    public void apply() {
        EntitySpawn.addFactory();
        EntitySpawn.walls();
        EntitySpawn.brick();
        EntitySpawn.paddle();
        EntitySpawn.ball();

        UI.getInstance().addAll();
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {

        static final GameSystem INSTANCE = new GameSystem();
    }
}
