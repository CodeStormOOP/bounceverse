package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.components.properties.powerup.FallingComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.ExpandPaddlePowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.ShrinkPaddlePowerUp;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.geometry.Point2D;

/**
 *
 *
 * <h1>{@link PowerUpFactory}</h1>
 *
 * Factory để tạo các entity loại {@link EntityType#POWER_UP} trong trò chơi.
 *
 * @see EntityFactory
 */
public final class PowerUpFactory implements EntityFactory {

    public static final double DEFAULT_RADIUS = 10;
    public static final double DEFAULT_SPEED = 150;

    /**
     * Tạo mới một PowerUp. Đây là một "abstract" method.
     *
     * @param pos Vị trí
     * @param has Những gì PowerUp này sẽ cung cấp
     * @return Entity PowerUp
     */
    private Entity newPowerUp(Point2D pos, Texture texture, Component... has) {
        final var hitbox = new HitBox(BoundingShape.circle(DEFAULT_RADIUS));

        return FXGL.entityBuilder()
                .type(EntityType.POWER_UP)
                .at(pos)
                .bbox(hitbox)
                .view(texture)
                .collidable()
                .with(new FallingComponent(), new PowerUpContainer(has))
                .buildAndAttach();
    }

    @Spawns("powerUpExpand")
    public Entity newPowerUpExpand(SpawnData data) {
        var texture = FXGL.texture("powerups/expand.png");
        return newPowerUp(new Point2D(data.getX(), data.getY()), texture, new ExpandPaddlePowerUp());
    }

    @Spawns("powerUpShrink")
    public Entity newPowerUpShrink(SpawnData data) {
        var texture = FXGL.texture("powerups/shrink.png");
        return newPowerUp(new Point2D(data.getX(), data.getY()), texture, new ShrinkPaddlePowerUp());
    }

    @Spawns("powerUp")
    public Entity newRandomPowerUp(SpawnData data) {
        if (Math.random() < 0.5) {
            return newPowerUpExpand(data);
        } else {
            return newPowerUpShrink(data);
        }
    }
}
