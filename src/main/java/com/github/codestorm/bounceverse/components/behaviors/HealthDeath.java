// C:\Users\Admin\Documents\bounceverse\src\main\java\com\github\codestorm\bounceverse\components\behaviors\HealthDeath.java
package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.components.properties.brick.BrickTextureManager;
import com.github.codestorm.bounceverse.systems.init.GameSystem;
import com.github.codestorm.bounceverse.systems.init.UISystem;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.util.Duration;

import java.util.List;

@Required(HealthIntComponent.class)
public class HealthDeath extends Behavior {

    @Override
    public void execute(List<Object> data) {
        var health = entity.getComponent(HealthIntComponent.class);
        if (health != null && health.isZero()) {
            if (entity.isType(EntityType.BRICK)) {
                entity.getComponentOptional(BrickTextureManager.class)
                        .ifPresent(
                                textureManager -> {
                                    var brickColor = textureManager.getColor();

                                    UISystem.getInstance().addColorToWave(brickColor);

                                    var scoreToAdd = 0;
                                    switch (textureManager.brickType) {
                                        case NORMAL, KEY:
                                            scoreToAdd = 10;
                                            break;
                                        case SHIELD:
                                            scoreToAdd = 20;
                                            break;
                                        case STRONG:
                                            scoreToAdd = 30;
                                            break;
                                        case EXPLODING:
                                            break;
                                    }

                                    if (scoreToAdd > 0) {
                                        FXGL.inc("score", scoreToAdd);
                                    }
                                });
            }
            entity.removeFromWorld();

            FXGL.runOnce(
                    () -> {
                        if (FXGL.getGameWorld().getEntitiesByType(EntityType.BRICK).isEmpty()) {
                            GameSystem.nextLevel();
                        }
                    },
                    Duration.seconds(0.1));
        }
    }

    @Override
    public void onUpdate(double tpf) {
        execute(null);
    }
}
