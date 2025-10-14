package com.github.codestorm.bounceverse.components.behaviors.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.properties.brick.BrickHealth;
import com.github.codestorm.bounceverse.data.tags.components.BehaviorComponent;
import com.github.codestorm.bounceverse.data.tags.entities.ForBrick;
import com.github.codestorm.bounceverse.data.tags.requirements.OptionalTag;
import com.github.codestorm.bounceverse.data.types.EntityType;
import java.util.List;

/**
 *
 *
 * <h1>{@link BrickExplode}</h1>
 *
 * <p>
 * Lớp này biểu diễn hành vi nổ của viên gạch. Khi viên gạch bị phá hủy, nó sẽ
 * kích hoạt hiệu ứng
 * nổ, có thể gây sát thương đến các viên gạch xung quanh trong bán kính xác
 * định.
 */
public final class BrickExplode extends Component
        implements BehaviorComponent, ForBrick, OptionalTag {
    public static final int DEFAULT_EXPLODE_RADIUS = 1;
    private int explodeRadius;

    /**
     * Triggers the explosion effect of this brick.
     *
     * <p>
     * This method can be extended to apply damage to surrounding bricks
     */
    private void explode() {
        double cx = getEntity().getCenter().getX();
        double cy = getEntity().getCenter().getY();

        List<Entity> entities = FXGL.getGameWorld().getEntities();
        for (var entity : entities) {
            if (entity == getEntity()) continue;
            if (!entity.isType(EntityType.BRICK)) continue;

            double ex = entity.getCenter().getX();
            double ey = entity.getCenter().getY();

            double dx = Math.abs(ex - cx) / getEntity().getWidth();
            double dy = Math.abs(ey - cy) / getEntity().getHeight();

            if (Math.hypot(dx, dy) <= explodeRadius) {
                entity.getComponentOptional(BrickHealth.class)
                        .ifPresent(health -> {
                            // Giảm máu
                            health.damage(1);

                            // Nếu máu <= 0 thì cho nổ tiếp (chain reaction)
                            if (health.isDead() && entity.hasComponent(BrickExplode.class)) {
                                entity.getComponent(BrickExplode.class).explode();
                            }
                        });
            }
        }
    }

    @Override
    public void onRemoved() {
        explode();
    }

    public int getExplodeRadius() {
        return explodeRadius;
    }

    public void setExplodeRadius(int explodeRadius) {
        this.explodeRadius = explodeRadius;
    }

    public BrickExplode(int explodeRadius) {
        assert explodeRadius > 0;
        this.explodeRadius = explodeRadius;
    }
}
