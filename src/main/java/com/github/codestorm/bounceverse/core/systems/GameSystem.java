package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.EntityFactory;
import com.github.codestorm.bounceverse.factory.entities.*;

public final class GameSystem extends System {
    private GameSystem() {}

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
        for (int y = 1; y <= 6; y++) {
            for (int x = 1; x <= 10; x++) {
                FXGL.spawn("normalBrick", 85 * x, 35 * y);
            }
        }

        // Paddle
        double px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);

        // Ball
        FXGL.spawn("ball");
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
