package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.*;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.*;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.components.properties.powerup.FallingComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.FastBallPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.MultipleBallPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.SlowBallPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.misc.ShieldPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.ExpandPaddlePowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.ShrinkPaddlePowerUp;
import com.github.codestorm.bounceverse.core.systems.PowerUpSpawner;
import com.github.codestorm.bounceverse.typing.enums.*;

import com.github.codestorm.bounceverse.data.types.PowerUpType;

import javafx.geometry.Point2D;

/**
 * Factory để tạo các Entity loại POWER_UP.
 */
public final class PowerUpFactory implements EntityFactory {

    public static final double DEFAULT_RADIUS = 10;
    public static final double DEFAULT_SPEED = 150;

    private Entity newPowerUp(Point2D pos, Texture texture, Component... components) {
        var hitbox = new HitBox(BoundingShape.circle(DEFAULT_RADIUS));

        return FXGL.entityBuilder()
                .type(EntityType.POWER_UP)
                .at(pos)
                .bbox(hitbox)
                .view(texture)
                .collidable()
                .with(new FallingComponent(), new PowerUpContainer(components))
                .buildAndAttach();
    }

    @Spawns("powerUp")
    public Entity newRandomPowerUp(SpawnData data) {

        PowerUpType type = PowerUpSpawner.getRandomPowerUpType();
        Point2D pos = new Point2D(data.getX(), data.getY());
        Texture texture;

        switch (type) {
            case EXPAND_PADDLE -> {
                texture = FXGL.texture("powerups/expand.png");
                return newPowerUp(pos, texture, new ExpandPaddlePowerUp());
            }
            case SHRINK_PADDLE -> {
                texture = FXGL.texture("powerups/shrink.png");
                return newPowerUp(pos, texture, new ShrinkPaddlePowerUp());
            }
            case MULTI_BALL -> {
                texture = FXGL.texture("powerups/multiball.png");
                return newPowerUp(pos, texture, new MultipleBallPowerUp());
            }
            case FAST_BALL -> {
                texture = FXGL.texture("powerups/fast.png");
                return newPowerUp(pos, texture, new FastBallPowerUp());
            }
            case SLOW_BALL -> {
                texture = FXGL.texture("powerups/slow.png");
                return newPowerUp(pos, texture, new SlowBallPowerUp());
            }
            case SHIELD -> {
                texture = FXGL.texture("powerups/shield.png");
                return newPowerUp(pos, texture, new ShieldPowerUp());
            }
            default -> {
                texture = FXGL.texture("powerups/default.png");
                return newPowerUp(pos, texture);
            }
        }
    }

}
