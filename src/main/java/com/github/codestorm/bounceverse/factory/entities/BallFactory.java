package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 *
 * <h1>{@link BallFactory}</h1>
 *
 * <p>This class defines and spawns a new {@link EntityType#BALL} entity in the game world.
 *
 * <p>By default, the spawned ball has:
 *
 * <ul>
 *   <li>Radius = {@link #DEFAULT_RADIUS}
 *   <li>Position: {@link #DEFAULT_POS}
 *   <li>Color: {@link #DEFAULT_COLOR}
 * </ul>
 *
 * @author minngoc1213
 */
public final class BallFactory implements EntityFactory {

    public static final int DEFAULT_RADIUS = 10;
    public static final Color DEFAULT_COLOR = Color.RED;

    private Entity buildBall(SpawnData data, boolean attached) {
        var physics = new PhysicsComponent();

        var fixture = new FixtureDef();
        fixture.setDensity(1.0f);
        fixture.setFriction(0.0f);
        fixture.setRestitution(1.0f);

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.DYNAMIC);

        physics.setOnPhysicsInitialized(
                () -> {
                    physics.getBody().setGravityScale(0f);
                    physics.getBody().setFixedRotation(true);
                    physics.getBody().setLinearDamping(0f);
                    physics.getBody().setAngularDamping(0f);

                    if (attached) {
                        physics.setLinearVelocity(Point2D.ZERO);
                    } else {
                        physics.setLinearVelocity(200, -200);
                    }
                });

        return FXGL.entityBuilder(data)
                .type(EntityType.BALL)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Circle(DEFAULT_RADIUS, DEFAULT_COLOR))
                .collidable()
                .with(physics, new Attack(), new Attachment())
                .buildAndAttach();
    }

    @Spawns("ball")
    public Entity spawnBall(SpawnData data) {
        boolean attached = data.hasKey("attached") && Boolean.TRUE.equals(data.get("attached"));
        return buildBall(data, attached);
    }
}
