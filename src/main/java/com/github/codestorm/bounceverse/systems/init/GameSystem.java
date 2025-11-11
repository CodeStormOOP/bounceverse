package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.github.codestorm.bounceverse.core.BackgroundColorManager;
import com.github.codestorm.bounceverse.factory.entities.*;
import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * Quản lý việc spawn các hệ thống chính của game.
 */
public final class GameSystem extends InitialSystem {

    private GameSystem() {
    }

    public static GameSystem getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Biến toàn cục của game.
     */
    public static final class Variables {

        private Variables() {
        }

        public static final int MAX_LIVES = 5;
        public static final int DEFAULT_LIVES = 3;
        public static final long DEFAULT_SCORE = 0L;

        public static void loadDefault(Map<String, Object> vars) {
            var health = new HealthIntComponent(MAX_LIVES);
            health.setValue(DEFAULT_LIVES);
            vars.clear();
            vars.put("lives", health);
            vars.put("score", DEFAULT_SCORE);
        }
    }

    private static void addFactory(@NotNull EntityFactory... factories) {
        for (var factory : factories) {
            FXGL.getGameWorld().addEntityFactory(factory);
        }
    }

    private static void spawnWalls() {
        FXGL.spawn("wallTop");
        FXGL.spawn("wallBottom");
        FXGL.spawn("wallLeft");
        FXGL.spawn("wallRight");
    }

    /**
     * Spawn ngẫu nhiên các loại Brick với màu khác nhau.
     */
    private void spawnBricks() {
        final int rows = 6;
        final int cols = 10;
        final double startX = 50;
        final double startY = 50;
        final double gap = 4;
        final double brickWidth = 80;
        final double brickHeight = 30;

        // 🎨 Danh sách màu khả dụng
        String[] colorKeys = {"blue", "green", "orange", "pink", "red", "yellow"};

        // 🔢 Danh sách loại Brick
        var types = BrickType.values();

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                double x = startX + col * (brickWidth + gap);
                double y = startY + row * (brickHeight + gap);

                // 🎲 Random loại Brick và màu
                BrickType randomType = types[FXGL.random(0, types.length - 1)];
                String randomColorKey = colorKeys[FXGL.random(0, colorKeys.length - 1)];

                // 🔹 Spawn Brick tương ứng
                FXGL.spawn(
                        switch (randomType) {
                    case STRONG ->
                        "strongBrick";
                    case SHIELD ->
                        "shieldBrick";
                    case EXPLODING ->
                        "explodingBrick";
                    case SPECIAL ->
                        "specialBrick";
                    default ->
                        "normalBrick";
                },
                        new SpawnData(x, y)
                                .put("color", randomColorKey)
                                .put("width", brickWidth)
                                .put("height", brickHeight)
                );
            }
        }
    }

    private static void spawnPaddle() {
        FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE).forEach(Entity::removeFromWorld);
        double px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);
    }

    private static void spawnBall() {
        if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
            var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
            double x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
            double y = paddle.getY() - BallFactory.DEFAULT_RADIUS * 2;
            FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
            FXGL.set("ballAttached", true);
        }
    }

    @Override
    public void apply() {
        addFactory(
                new WallFactory(),
                new BrickFactory(),
                new BulletFactory(),
                new PaddleFactory(),
                new BallFactory(),
                new PowerUpFactory()
        );

        spawnWalls();
        spawnBricks();
        BackgroundColorManager.init(
                FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).size());
        spawnPaddle();
        spawnBall();
    }

    private static final class Holder {

        static final GameSystem INSTANCE = new GameSystem();
    }
}
