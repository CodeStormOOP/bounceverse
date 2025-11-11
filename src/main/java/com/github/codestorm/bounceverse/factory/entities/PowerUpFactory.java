package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
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
public final class PowerUpFactory extends EntityFactory {
    public static final double DEFAULT_RADIUS = 10;
    public static final double DEFAULT_SPEED = 10;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        final var velocity = DirectionUnit.DOWN.getVector().mul(DEFAULT_SPEED);

        final var physics = new PhysicsComponent();
        physics.setOnPhysicsInitialized(
                () -> {
                    physics.setLinearVelocity(velocity.toPoint2D());
                    physics.setAngularVelocity(0);
                    physics.getBody().setFixedRotation(true);
                    physics.getBody().setLinearDamping(0f);
                    physics.getBody().setAngularDamping(0f);
                });

        return FXGL.entityBuilder().type(EntityType.POWER_UP).collidable().with(physics);
    }

    /**
     * Tạo mới một PowerUp. Đây là một "abstract" method.
     *
     * @param data Dữ liệu EntitySpawn
     * @return Entity PowerUp
     */
    private Entity newPowerUp(SpawnData data) {
        final double radius = Utilities.Typing.getOr(data, "radius", DEFAULT_RADIUS);
        final var contains = Utilities.Typing.getOr(data, "contains", new Component[0]);
        final Point2D pos = data.get("pos");
        final Texture texture = data.get("texture");

        final var hitbox = new HitBox(BoundingShape.circle(radius));

        return getBuilder(data)
                .bbox(hitbox)
                .at(pos)
                .view(texture)
                .with(new PowerUpContainer(contains))
                .buildAndAttach();
    }
}
