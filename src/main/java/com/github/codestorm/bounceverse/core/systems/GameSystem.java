package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.factory.BrickFactory;
import com.github.codestorm.bounceverse.factory.BulletFactory;
import com.github.codestorm.bounceverse.factory.PaddleFactory;
import com.github.codestorm.bounceverse.factory.WallFactory;

public final class GameSystem extends System {
    /**
     * Lazy-loaded singleton holder.
     *
     * <p>Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final GameSystem INSTANCE = new GameSystem();
    }

    public static GameSystem getInstance() {
        return GameSystem.Holder.INSTANCE;
    }

    @Override
    public void apply() {
        FXGL.getGameWorld().addEntityFactory(new BrickFactory());
        FXGL.getGameWorld().addEntityFactory(new BulletFactory());
        FXGL.getGameWorld().addEntityFactory(new PaddleFactory());
        FXGL.getGameWorld().addEntityFactory(new WallFactory());

        // Spawn walls.
        FXGL.spawn("wallLeft");
        FXGL.spawn("wallRight");
        FXGL.spawn("wallTop");

        // Spawn paddle
        double px = FXGL.getAppWidth() / 2.0 - 60;
        double py = FXGL.getAppHeight() - 40;
        FXGL.spawn("paddle", px, py);

        FXGL.spawn("normalBrick", 100, 100);
        FXGL.spawn("normalBrick", 200, 200);
    }

    private GameSystem() {}
}
