package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import java.util.List;

/**
 *
 *
 * <h1>{@link Explosion}</h1>
 *
 * <br>
 * Hành vi nổ của {@link Entity}, có thể gây sát thương hoặc hồi máu cho những đối tượng xung quanh.
 * <br>
 * <b>Yêu cầu entity có {@link Attributes} trước.</b>
 */
@Required(Attributes.class)
public final class Explosion extends Attack {
    public static final int DEFAULT_RADIUS = 1;
    private int radius = DEFAULT_RADIUS;

    @Override
    public void execute(List<Object> data) {
        final var attributes = entity.getComponent(Attributes.class);
        final double cx = getEntity().getCenter().getX();
        final double cy = getEntity().getCenter().getY();

        final var nearEntities = Utilities.Geometric.getEntityInCircle(cx, cy, radius);
        super.execute(nearEntities.stream().map(e -> (Object) e).toList());
    }

    @Override
    public void onRemoved() {
        execute(null);
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = Math.abs(radius);
    }

    public Explosion() {}

    public Explosion(int radius) {
        setRadius(radius);
    }
}
