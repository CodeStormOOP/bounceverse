package com.github.codestorm.bounceverse.components.properties.bullet;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.components.properties.Velocity;
import com.github.codestorm.bounceverse.data.tags.components.Behavior;
import com.github.codestorm.bounceverse.data.tags.entities.ForBullet;
import com.github.codestorm.bounceverse.data.tags.requirements.Required;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.geometry.Point2D;

/**
 *
 *
 * <h1>{@link BulletVelocity}</h1>
 *
 * Đại diện cho vận tốc hiện có của entity {@link EntityType#BULLET}.
 */
@CoreComponent
public final class BulletVelocity extends Velocity implements Behavior, ForBullet, Required {
    public BulletVelocity(Vec2 velocity) {
        super(velocity);
    }

    public BulletVelocity(Point2D velocity) {
        super(velocity);
    }

    public BulletVelocity(double vx, double vy) {
        super(vx, vy);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);

        final var viewport = FXGL.getGameScene().getViewport().getVisibleArea();
        final var entity = getEntity();
        // Remove when out of playground
        if (entity.getRightX() < viewport.getMinX()
                || entity.getX() > viewport.getMaxX()
                || entity.getBottomY() < viewport.getMinY()
                || entity.getY() > viewport.getMaxY()) {
            entity.removeFromWorld();
        }
    }
}
