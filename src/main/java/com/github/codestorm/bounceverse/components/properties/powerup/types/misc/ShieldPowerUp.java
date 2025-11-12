package com.github.codestorm.bounceverse.components.properties.powerup.types.misc;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.data.types.PowerUpType;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Tạo một tấm chắn ở dưới đáy ngăn bóng rơi trong vài giây.
 */
public class ShieldPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(8);
    private Entity shieldEntity;

    public ShieldPowerUp() {
        super("Shield");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                this::spawnShield,
                this::removeShield);
    }

    private void spawnShield() {
        double appWidth = FXGL.getAppWidth();
        double appHeight = FXGL.getAppHeight();

        double shieldHeight = 20.0;

        Rectangle view = new Rectangle(appWidth, 8, Color.AQUA);

        shieldEntity = FXGL.entityBuilder()
                .at(0, appHeight - 10)
                .type(PowerUpType.SHIELD)
                .view(view)
                .bbox(new HitBox(BoundingShape.box(appWidth, shieldHeight)))
                .collidable()
                .buildAndAttach();
    }

    private void removeShield() {
        if (shieldEntity != null)
            shieldEntity.removeFromWorld();
    }
}
