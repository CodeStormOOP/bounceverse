package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.factory.entities.BrickFactory;
import com.github.codestorm.bounceverse.factory.entities.BulletFactory;
import com.github.codestorm.bounceverse.factory.entities.PaddleFactory;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

public final class GameSystem extends System {

    private GameSystem() {
    }

    public static GameSystem getInstance() {
        return GameSystem.Holder.INSTANCE;
    }

    private static void addFactory(EntityFactory... factories) {
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

    @Override
    public void apply() {
        addFactory(
                new WallFactory(),
                new BrickFactory(),
                new BulletFactory(),
                new PaddleFactory(),
                new BallFactory());

        // Wall
        spawnWalls();

        // Brick
        int rows = 6;
        int cols = 10;
        double startX = 85;
        double startY = 50;
        double brickWidth = 80;
        double brickHeight = 30;
        double spacingX = 5;
        double spacingY = 5;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                double posX = startX + x * (brickWidth + spacingX);
                double posY = startY + y * (brickHeight + spacingY);

                // Xác định loại brick theo hàng (bạn có thể chỉnh lại tuỳ ý)
                String type;
                switch (y) {
                    case 0 ->
                        type = "shieldBrick";    // Hàng đầu có khiên
                    case 1 ->
                        type = "explodingBrick"; // Hàng thứ 2 nổ
                    case 2 ->
                        type = "specialBrick";   // Hàng thứ 3 rơi power-up
                    case 3 ->
                        type = "strongBrick";    // Hàng thứ 4 trâu
                    default ->
                        type = "normalBrick";   // Còn lại là thường
                }

                FXGL.spawn(type, posX, posY);
            }
        }

        // Paddle
        FXGL.getGameWorld()
                .getEntitiesByType(EntityType.PADDLE)
                .forEach(Entity::removeFromWorld);

        double px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);

        // Ball
        if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
            Entity paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);

            double x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
            double y = paddle.getY() - BallFactory.DEFAULT_RADIUS + 1;

            FXGL.spawn("ball", new com.almasb.fxgl.entity.SpawnData(x, y).put("attached", true));
            FXGL.set("ballAttached", true);
        }

    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow
     * <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {

        static final GameSystem INSTANCE = new GameSystem();
    }
}
