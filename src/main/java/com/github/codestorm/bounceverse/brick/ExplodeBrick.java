package com.github.codestorm.bounceverse.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import java.util.List;
import javafx.scene.paint.Color;

public class ExplodeBrick extends BrickComponent {
    private static final int EXPLODE_RADIUS = 1;

    public ExplodeBrick(int width, int height, int hp, Color baseColor) {
        super(width, height, hp, baseColor);
    }

    public int getRadius() {
        return EXPLODE_RADIUS;
    }

    /**
     * Handles a hit on this brick.
     *
     * <p>
     * Decreases hit points using the parent logic. If the brick is destroyed after
     * the hit, it triggers an explosion.
     */
    @Override
    public void hit() {
        super.hit();
        if (isDestroyed()) {
            explode();
        }
    }

    /**
     * Triggers the explosion effect of this brick.
     *
     * <p>
     * This method can be extended to apply damage to surrounding bricks
     */
    private void explode() {
        double cx = getEntity().getCenter().getX();
        double cy = getEntity().getCenter().getY();

        List<Entity> entites = FXGL.getGameWorld().getEntities();
        for (Entity e : entites) {
            if (e == getEntity())
                continue;
            if (!e.hasComponent(BrickComponent.class))
                continue;

            BrickComponent brick = e.getComponent(BrickComponent.class);
            if (brick.isDestroyed()) {
                continue;
            }

            double ex = e.getCenter().getX();
            double ey = e.getCenter().getY();

            double dx = Math.abs(ex - cx) / getWidth();
            double dy = Math.abs(ey - cy) / getHeight();

            if (dx <= EXPLODE_RADIUS && dy <= EXPLODE_RADIUS) {
                brick.hit();
            }
        }
    }
}
