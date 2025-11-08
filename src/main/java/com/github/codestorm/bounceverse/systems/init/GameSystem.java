package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.EntityFactory;
import com.github.codestorm.bounceverse.factory.entities.*;

import org.jetbrains.annotations.NotNull;

public final class GameSystem extends InitialSystem {
    private GameSystem() {}

    public static GameSystem getInstance() {
        return GameSystem.Holder.INSTANCE;
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

    private static void spawnBrick() {
        for (var y = 1; y <= 6; y++) {
            for (var x = 1; x <= 10; x++) {
                FXGL.spawn("normalBrick", 85 * x, 35 * y);
            }
        }
    }

    private static void spawnPaddle() {
        var px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);
    }

    private static void spawnBall() {
        FXGL.spawn("ball");
    }

    @Override
    public void apply() {
        addFactory(
                new WallFactory(),
                new BrickFactory(),
                new BulletFactory(),
                new PaddleFactory(),
                new BallFactory());

        spawnWalls();
        spawnBrick();
        spawnPaddle();
        spawnBall();
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
