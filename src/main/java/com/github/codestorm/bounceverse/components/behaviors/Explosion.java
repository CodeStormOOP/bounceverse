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
@OnlyForEntity({ EntityType.BRICK })
public final class Explosion extends Attack {

    private double explosionWidth;
    private double explosionHeight;

    @Override
    public void execute(List<Object> data) {
        double cx = getEntity().getCenter().getX();
        double cy = getEntity().getCenter().getY();

        var nearEntities = Utilities.Geometric.getEntitiesInRectangle(cx, cy, explosionWidth, explosionHeight);

        var filteredEntities = nearEntities.stream()
                .filter(e -> !e.equals(getEntity()))
                .map(e -> (Object) e)
                .toList();

        super.execute(filteredEntities);
    }

    @Override
    public void onRemoved() {
        execute(List.of());
    }

    public Explosion(double width, double height) {
        this.explosionWidth = width;
        this.explosionHeight = height;
    }

    public double getExplosionWidth() {
        return explosionWidth;
    }

    public void setExplosionWidth(double explosionWidth) {
        this.explosionWidth = explosionWidth;
    }

    public double getExplosionHeight() {
        return explosionHeight;
    }

    public void setExplosionHeight(double explosionHeight) {
        this.explosionHeight = explosionHeight;
    }
}