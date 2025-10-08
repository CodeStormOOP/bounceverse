package com.github.codestorm.bounceverse.components.brick.properties;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.github.codestorm.bounceverse.components.base.properties.Health;
import com.github.codestorm.bounceverse.tags.ForBrick;

/**
 *
 *
 * <h1><b>BrickHealth</b></h1>
 *
 * <p>
 * Lớp này đại diện cho thuộc tính HP của Viên gạch.
 */
public final class BrickHealth extends Health implements ForBrick {
    public BrickHealth(int maxHealth) {
        super(maxHealth);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        // updateColor here
    }

    public void damage(int amount) {
        if (amount <= 0)
            return;
        HealthIntComponent h = getHealth();

        if (h.isZero())
            return;

        h.damage(amount);
    }

    public boolean isDead() {
        return getHealth().isZero();
    }
}
