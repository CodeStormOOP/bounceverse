package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
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
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;

/**
 * <h1>{@link PowerUpFactory}</h1>
 * Factory tạo các entity loại {@link EntityType#POWER_UP}.
 */
public final class PowerUpFactory extends EntityFactory {

    public static final double DEFAULT_RADIUS = 10;
    public static final double DEFAULT_SPEED = 10;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        // Vector vận tốc rơi xuống
        final var velocity = DirectionUnit.DOWN.getVector().mul(DEFAULT_SPEED);

        final var physics = new PhysicsComponent();

        // Thiết lập body vật lý dạng động để có thể di chuyển
        physics.setBodyType(BodyType.DYNAMIC);

        // Loại bỏ mọi ma sát, nảy, trọng lượng dư thừa
        physics.setFixtureDef(new FixtureDef()
                .density(0.0f)
                .friction(0.0f)
                .restitution(0.0f)
                .sensor(true));

        physics.setOnPhysicsInitialized(() -> {
            // Cố định hướng rơi thẳng
            physics.setLinearVelocity(velocity.toPoint2D());
            physics.getBody().setFixedRotation(true);
            physics.getBody().setLinearDamping(0f);
            physics.getBody().setAngularDamping(1f);
        });

        // Component giữ PowerUp rơi thẳng, không trượt ngang
        var corrector = new Component() {
            @Override
            public void onUpdate(double tpf) {
                var vel = physics.getLinearVelocity();
                if (Math.abs(vel.getX()) > 0.01) {
                    physics.setLinearVelocity(new Point2D(0, vel.getY()));
                }
            }
        };

        return FXGL.entityBuilder()
                .type(EntityType.POWER_UP)
                .collidable()
                .with(physics, corrector);
    }

    /**
     * Tạo mới một Power-Up.
     *
     * @param data Dữ liệu spawn
     * @return Entity Power-Up
     */
    @Spawns("powerUp")
    public Entity newPowerUp(SpawnData data) {
        final double radius = Utilities.Typing.getOr(data, "radius", DEFAULT_RADIUS);
        final var contains = Utilities.Typing.getOr(data, "contains", new Component[0]);
        final Point2D pos = data.hasKey("pos")
                ? data.get("pos")
                : new Point2D(data.getX(), data.getY());
        final Texture texture = data.hasKey("texture")
                ? data.get("texture")
                : FXGL.texture("powerups/default.png");

        final var hitbox = new HitBox(BoundingShape.circle(radius));

        return getBuilder(data)
                .bbox(hitbox)
                .at(pos)
                .view(texture)
                .with(new PowerUpContainer(contains))
                .buildAndAttach();
    }
}
