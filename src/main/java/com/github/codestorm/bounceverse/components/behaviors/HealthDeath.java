package com.github.codestorm.bounceverse.components.behaviors;

import java.util.List;

import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.components.properties.brick.BrickTextureManager;
import com.github.codestorm.bounceverse.systems.init.UISystem;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.scene.paint.Color;

@Required(HealthIntComponent.class)
public class HealthDeath extends Behavior {

    @Override
    public void execute(List<Object> data) {
        var health = entity.getComponent(HealthIntComponent.class);
        if (health != null && health.isZero()) {
            if (entity.isType(EntityType.BRICK)) {
                entity.getComponentOptional(BrickTextureManager.class).ifPresent(textureManager -> {
                    Color brickColor = textureManager.getColor();

                    UISystem.getInstance().addColorToWave(brickColor);
                });
            }
            entity.removeFromWorld();
        }
    }

    @Override
    public void onUpdate(double tpf) {
        execute(null);
    }
}