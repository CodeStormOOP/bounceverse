package com.github.codestorm.bounceverse.components.properties.brick;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.components.properties.Health;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;

/**
 *
 *
 * <h1>{@link BrickHealth}</h1>
 *
 * <p>Lớp này đại diện cho thuộc tính HP của Viên gạch.
 */
@CoreComponent
@ForEntity(EntityType.BRICK)
public final class BrickHealth extends Health {
    public BrickHealth(int maxHealth) {
        super(maxHealth);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        // updateColor here
    }

    public void damage(int amount) {
        if (amount <= 0) {
            return;
        }
        HealthIntComponent h = getHealth();

        if (h.isZero()) {
            return;
        }

        h.damage(amount);
    }

    public boolean isDead() {
        return getHealth().isZero();
    }
}
