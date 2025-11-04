package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.core.BackgroundColorManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import java.util.List;

/**
 *
 *
 * <h1>{@link HealthDeath}</h1>
 *
 * Hành động chết <s>vì yêu :<</s> vì hết máu. <br>
 * <b>Yêu cầu entity có {@link HealthIntComponent} trước.</b>
 */
@Required(HealthIntComponent.class)
public class HealthDeath extends Behavior {
    @Override
    public void execute(List<Object> data) {
        final var health = entity.getComponent(HealthIntComponent.class);
        if (health != null && health.isZero()) {
            if (entity.isType(EntityType.BRICK)) {
                BackgroundColorManager.brickDestroyed();
            }
            entity.removeFromWorld();
        }
    }

    @Override
    public void onUpdate(double tpf) {
        execute(null);
    }
}
