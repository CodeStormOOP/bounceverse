package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.profile.DataFile;
import com.almasb.fxgl.profile.SaveLoadHandler;
import com.github.codestorm.bounceverse.core.BackgroundColorManager;
import com.github.codestorm.bounceverse.factory.entities.*;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import com.github.codestorm.bounceverse.ui.HorizontalPositiveInteger;
import com.github.codestorm.bounceverse.ui.ingame.Hearts;

import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * <h1>GameSystem</h1>
 * Quản lý việc spawn các hệ thống chính của game (Entity, UI, Save/Load, v.v.).
 */
public final class GameSystem extends InitialSystem {

    private GameSystem() {
    }

    public static GameSystem getInstance() {
        return Holder.INSTANCE;
    }

    /** Biến toàn cục của game (điểm, mạng, save/load). */
    public static final class Variables {

        private Variables() {
        }

        public static final int MAX_LIVES = 5;
        public static final int DEFAULT_LIVES = 3;
        public static final int DEFAULT_SCORE = 0;

        /** Handler cho hệ thống save/load của FXGL. */
        public static final SaveLoadHandler SAVE_LOAD_HANDLER = new SaveLoadHandler() {
            @Override
            public void onSave(@NotNull DataFile dataFile) {
                final var properties = FXGL.getWorldProperties();
                final HealthIntValue lives = properties.getObject("lives");
                final int score = properties.getValue("score");

                final var bundle = new Bundle("game");
                bundle.put("lives", lives.getValue());
                bundle.put("score", score);
                dataFile.putBundle(bundle);
            }

            @Override
            public void onLoad(@NotNull DataFile dataFile) {
                final var bundle = dataFile.getBundle("game");
                final int intLives = bundle.get("lives");
                final var lives = new HealthIntValue(MAX_LIVES, intLives);
                final int score = bundle.get("score");

                final var vars = FXGL.getWorldProperties();
                vars.setValue("lives", lives);
                vars.setValue("score", score);
            }
        };

        public static void loadDefault(Map<String, Object> vars) {
            final var lives = new HealthIntValue(MAX_LIVES, DEFAULT_LIVES);
            vars.clear();
            vars.put("lives", lives);
            vars.put("score", DEFAULT_SCORE);
        }
    }

    /** Lớp con phụ trách spawn các thực thể chính (entity). */
    private static final class EntitySpawn {
        private EntitySpawn() {
        }

        public static void addFactory() {
            FXGL.getGameWorld().addEntityFactory(new WallFactory());
            FXGL.getGameWorld().addEntityFactory(new BrickFactory());
            FXGL.getGameWorld().addEntityFactory(new BulletFactory());
            FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
            FXGL.getGameWorld().addEntityFactory(new BallFactory());
            FXGL.getGameWorld().addEntityFactory(new PowerUpFactory());
        }

        public static void walls() {
            FXGL.spawn("wallTop");
            FXGL.spawn("wallBottom");
            FXGL.spawn("wallLeft");
            FXGL.spawn("wallRight");
        }

        /** Spawn các bricks theo layout mẫu. */
        public static void bricks() {
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

                    String type;

                    // 🛡️ Hàng đầu tiên là shieldBrick
                    if (y == 0) {
                        type = "shieldBrick";
                    }
                    // 🔑 Hàng thứ 4 (index = 3) là keyBrick để test PowerUp
                    else if (y == 3) {
                        type = "keyBrick";
                    }
                    // 💣 Hàng cuối (index = 5) là explodingBrick để test nổ lan
                    else if (y == 5) {
                        type = "explodingBrick";
                    }
                    // 💎 Các hàng còn lại là gạch thường
                    else {
                        type = "normalBrick";
                    }

                    FXGL.spawn(type, new SpawnData(posX, posY));
                }
            }

            // Giữ lại logic nền động
            BackgroundColorManager.init(FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).size());
        }

        public static void paddle() {
            FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
            double px = FXGL.getAppWidth() / 2.0 - 60;
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

    /** Hiển thị giao diện UI trong game. */
    private static final class UI {
        private UI() {
        }

        private boolean isInitialized = false;
        private Hearts heartsDisplay;
        private HorizontalPositiveInteger scoreDisplay;

        public static UI getInstance() {
            return Holder.INSTANCE;
        }

        public void addScoreDisplay() {
            if (scoreDisplay == null) {
                final var scoreProperty = FXGL.getWorldProperties().intProperty("score");
                scoreDisplay = new HorizontalPositiveInteger(scoreProperty);
                scoreDisplay.setMinDigits(6);
            }
            FXGL.getGameScene().addUINode(scoreDisplay.getView());
            scoreDisplay.getView().setTranslateX(20);
            scoreDisplay.getView().setTranslateY(20);
        }

        public void addHeartsDisplay() {
            if (heartsDisplay == null) {
                heartsDisplay = new Hearts();
            }
            FXGL.getGameScene().addUINode(heartsDisplay.getView());
            heartsDisplay.getView().setTranslateX(20);
            heartsDisplay.getView().setTranslateY(FXGL.getAppHeight() - 52);
        }

        public void addAll() {
            if (isInitialized)
                return;
            addScoreDisplay();
            addHeartsDisplay();
            isInitialized = true;
        }

        public void removeAll() {
            if (!isInitialized)
                return;
            if (scoreDisplay != null)
                FXGL.getGameScene().removeUINode(scoreDisplay.getView());
            if (heartsDisplay != null)
                FXGL.getGameScene().removeUINode(heartsDisplay.getView());
            isInitialized = false;
        }

        public void dispose() {
            removeAll();
            scoreDisplay = null;
            heartsDisplay = null;
        }

        private static final class Holder {
            static final UI INSTANCE = new UI();
        }
    }

    @Override
    public void apply() {
        EntitySpawn.addFactory();
        EntitySpawn.walls();
        EntitySpawn.bricks();
        EntitySpawn.paddle();
        EntitySpawn.ball();
        UI.getInstance().addAll();
    }

    private static final class Holder {
        static final GameSystem INSTANCE = new GameSystem();
    }
}
