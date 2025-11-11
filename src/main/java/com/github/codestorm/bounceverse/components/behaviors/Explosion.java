package com.github.codestorm.bounceverse.components.behaviors;

import java.util.List;

import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 * Hành vi nổ của Brick – gây damage cho các đối tượng xung quanh.
 */
@Required(Attributes.class)
@OnlyForEntity({EntityType.BRICK})
public final class Explosion extends Attack {
    public static final int DEFAULT_RADIUS = 1;
    private int radius = DEFAULT_RADIUS;

    @Override
    public void execute(List<Object> data) {
        double cx = getEntity().getCenter().getX();
        double cy = getEntity().getCenter().getY();
        var nearEntities = Utilities.Geometric.getEntityInCircle(cx, cy, radius);
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
