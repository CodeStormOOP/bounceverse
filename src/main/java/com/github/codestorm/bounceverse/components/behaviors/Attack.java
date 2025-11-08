package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import java.util.List;

/**
 *
 *
 * <h1>{@link Attack}</h1>
 *
 * Hành động tấn công/gây sát thương của {@link Entity}. <br>
 */
public class Attack extends Behavior {
    public static final int DEFAULT_DAMAGE = 1;
    private int damage = DEFAULT_DAMAGE;

    @Override
    public void execute(List<Object> data) {
        var entities =
                data.stream().filter(obj -> obj instanceof Entity).map(e -> (Entity) e).toList();
        for (var obj : entities) {
            final var theirHealth = obj.getComponentOptional(HealthIntComponent.class);
            if (theirHealth.isEmpty()) {
                continue;
            }
            final var theirAttributes = obj.getComponentOptional(Attributes.class);
            final var theirDefense = theirAttributes.map(Attributes::getDefense).orElse(0);

            final var actualDamage = damage >= 0 ? Math.max(0, damage - theirDefense) : damage;
            theirHealth.get().damage(actualDamage);
        }
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public Attack() {}

    public Attack(int damage) {
        this.damage = damage;
    }
}
