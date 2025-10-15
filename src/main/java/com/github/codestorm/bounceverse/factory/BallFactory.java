package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.EntityFactory;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.components._old.ball.BallComponent;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 *
 *
 * <h1><b>Ball Factory</b></h1>
 *
 * <p>This class defines and spawns a new {@code Ball} entity in the game world.
 *
 * <p>By default, the spawned ball has:
 *
 * <ul>
 *   <li>Radius = 25
 *   <li>Position: (x = 50, y = 50)
 *   <li>Color: Red
 * </ul>
 *
 * @author minngoc1213
 */
public class BallFactory implements EntityFactory {
    public static final int RADIUS = 10;

    @Spawns("ball")
    public Entity spawnBall(SpawnData data) {
        PhysicsComponent physics = new PhysicsComponent();

        var fixture = new FixtureDef();
        fixture.setDensity(1.0f);
        fixture.setFriction(0f);
        fixture.setRestitution(1f);

        physics.setFixtureDef(fixture);
        physics.setBodyType(BodyType.DYNAMIC);

        // set ball doesn't rotate
        physics.setOnPhysicsInitialized(
                () -> {
                    physics.setAngularVelocity(0);
                    physics.getBody().setFixedRotation(true);
                    physics.getBody().setLinearDamping(0f);
                    physics.getBody().setAngularDamping(0f);
                });

        return FXGL.entityBuilder(data)
                .type(EntityType.BALL)
                .at(400, 500)
                .viewWithBBox(new Circle(RADIUS, Color.RED))
                .collidable()
                .with(physics)
                .with(new BallComponent())
                .anchorFromCenter()
                .buildAndAttach();
    }
}
