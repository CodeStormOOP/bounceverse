package com.github.codestorm.bounceverse.components.base.properties;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.github.codestorm.bounceverse.components.base.PropertyComponent;

/**
 *
 *
 * <h1><b>Health</b></h1>
 *
 * <p>Lớp này đại diện cho thuộc tính HP của Entity. Khi HP về 0, Entity sẽ bị xóa khỏi thế giới.
 */
public abstract class Health extends PropertyComponent {
    private final HealthIntComponent health;

    public Health(int maxHealth) {
        health = new HealthIntComponent(maxHealth);
    }

    public Health(HealthIntComponent health) {
        this.health = health;
    }

    public HealthIntComponent getHealth() {
        return health;
    }

    @Override
    public void onAdded() {
        super.onAdded();
        entity.addComponent(health);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        if (health.isZero()) {
            entity.removeFromWorld();
        }
    }
}
