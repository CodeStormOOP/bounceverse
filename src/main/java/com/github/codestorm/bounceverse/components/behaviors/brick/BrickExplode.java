package com.github.codestorm.bounceverse.components.behaviors.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.components.properties.brick.BrickHealth;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;

/**
 *
 *
 * <h1>{@link BrickExplode}</h1>
 *
 * <p>Lớp này biểu diễn hành vi nổ của viên gạch. Khi viên gạch bị phá hủy sẽ bị nổ, có thể gây sát
 * thương đến các viên gạch xung quanh trong bán kính xác định.
 */
@ForEntity(EntityType.BRICK)
public final class BrickExplode extends Component implements Behavior {
    public static final int DEFAULT_RADIUS = 1;
    private int radius;

    /**
     * Triggers the explosion effect of this brick.
     *
     * <p>This method can be extended to apply damage to surrounding bricks
     */
    private void explode() {
        final double cx = getEntity().getCenter().getX();
        final double cy = getEntity().getCenter().getY();
        final double cw = getEntity().getWidth();
        final double ch = getEntity().getHeight();

        final var nearEntities =
                FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).stream()
                        .filter(
                                e -> {
                                    if (e == getEntity()) {
                                        return false;
                                    }

                                    final double ex = e.getCenter().getX();
                                    final double ey = e.getCenter().getY();
                                    final double dx = Math.abs(ex - cx) / cw;
                                    final double dy = Math.abs(ey - cy) / ch;
                                    return Math.hypot(dx, dy) <= radius;
                                })
                        .toList();
        for (var entity : nearEntities) {
            final var health = entity.getComponent(BrickHealth.class);
            health.damage(1);
        }
    }

    @Override
    public void onRemoved() {
        explode();
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public BrickExplode(int radius) {
        assert radius > 0;
        this.radius = radius;
    }

    public BrickExplode() {
        this(DEFAULT_RADIUS);
    }
}
