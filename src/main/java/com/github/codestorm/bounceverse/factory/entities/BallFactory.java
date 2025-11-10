package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
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

public final class BallFactory extends EntityFactory {

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

        physics.setOnPhysicsInitialized(() -> {
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

        // ✅ Tùy theo "attached", quyết định có thêm Attachment hay không
        var builder = FXGL.entityBuilder(data)
                .type(EntityType.BALL)
                .at(data.getX(), data.getY())
                .viewWithBBox(new Circle(DEFAULT_RADIUS, DEFAULT_COLOR))
                .collidable()
                .with(physics, new Attack());

        if (attached) {
            builder.with(new Attachment());
        }

        return builder.buildAndAttach();
    }

    @Spawns("ball")
    public Entity spawnBall(SpawnData data) {
        boolean attached = data.hasKey("attached") && Boolean.TRUE.equals(data.get("attached"));

        double spawnX = data.hasKey("x") ? data.get("x") : Double.NaN;
        double spawnY = data.hasKey("y") ? data.get("y") : Double.NaN;

        if (Double.isNaN(spawnX) || Double.isNaN(spawnY)) {
            var paddle = FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE).stream().findFirst();
            if (paddle.isPresent()) {
                var pos = paddle.get().getCenter();
                spawnX = pos.getX();
                spawnY = pos.getY();
            } else {
                spawnX = 0;
                spawnY = 0;
            }
        }

        System.out.println("[BallFactory] Spawn ball at (" + spawnX + ", " + spawnY + "), attached=" + attached);
        return buildBall(new SpawnData(spawnX, spawnY).put("attached", attached), attached);
    }

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        throw new UnsupportedOperationException("Unimplemented method 'getBuilder'");
    }
}
