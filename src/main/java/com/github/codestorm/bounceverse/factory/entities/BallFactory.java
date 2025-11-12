package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.almasb.fxgl.physics.box2d.dynamics.FixtureDef;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Factory tạo bóng trong trò chơi.
 * Hỗ trợ spawn bóng gắn (attached) hoặc tự do (free).
 */
public final class BallFactory extends EntityFactory {

    public static final double DEFAULT_RADIUS = 10;
    public static final Color DEFAULT_COLOR = Color.RED;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        final boolean attached = Utilities.Typing.getOr(data, "attached", false);

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
            physics.getBody().setBullet(true);

            if (attached) {
                physics.setLinearVelocity(Point2D.ZERO);
            } else {
                double initialSpeed = FXGL.getd("ballSpeed");
                Point2D initialVelocity = new Point2D(1, -1).normalize().multiply(initialSpeed);
                physics.setLinearVelocity(initialVelocity);
            }
        });

        var builder = FXGL.entityBuilder(data)
                .type(EntityType.BALL)
                .viewWithBBox(new Circle(DEFAULT_RADIUS, DEFAULT_COLOR))
                .collidable()
                .with(physics, new Attack());

        if (attached) {
            builder.with(new Attachment());
        }

        return builder;
    }

    @Spawns("ball")
    public Entity spawnBall(SpawnData data) {
        final boolean attached = data.hasKey("attached") && Boolean.TRUE.equals(data.get("attached"));

        Point2D pos = null;
        if (data.hasKey("x") && data.hasKey("y")) {
            pos = new Point2D(data.getX(), data.getY());
        } else if (data.hasKey("position")) {
            pos = (Point2D) data.get("position");
        }

        if (pos == null) {
            var paddleOpt = FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE).stream().findFirst();
            if (paddleOpt.isPresent()) {
                var paddle = paddleOpt.get();
                pos = new Point2D(
                        paddle.getCenter().getX() - DEFAULT_RADIUS,
                        paddle.getY() - DEFAULT_RADIUS * 2);
            } else {
                // Fallback an toàn nếu không tìm thấy paddle
                pos = new Point2D(FXGL.getAppWidth() / 2.0, FXGL.getAppHeight() / 2.0);
            }
        }

        data.put("attached", attached);
        if (pos != null) {
            data.put("x", pos.getX());
            data.put("y", pos.getY());
        }
        return getBuilder(data).buildAndAttach();
    }
}