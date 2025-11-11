package com.github.codestorm.bounceverse.components.behaviors;

import java.util.List;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.core.BackgroundColorManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 * Khi máu của entity về 0 thì xóa entity khỏi thế giới.
 */
@Required(HealthIntComponent.class)
public class HealthDeath extends Behavior {

    @Override
    public void execute(List<Object> data) {
        var health = entity.getComponent(HealthIntComponent.class);
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
