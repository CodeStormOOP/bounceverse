package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

/**
 * Quản lý Input cho game.
 */
public final class InputSystem extends InitialSystem {

    private static final double MOVE_SPEED = 10.0;
    private static final double WALL_THICKNESS = WallFactory.DEFAULT_THICKNESS;

    public static InputSystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        // --- Paddle Move Left ---
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> paddle.getComponentOptional(PhysicsComponent.class)
                        .ifPresent(phys -> phys.setLinearVelocity(-MOVE_SPEED * 20, 0)));
            }

            @Override
            protected void onActionEnd() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> paddle.getComponentOptional(PhysicsComponent.class)
                        .ifPresent(phys -> phys.setLinearVelocity(0, 0)));
            }
        }, KeyCode.LEFT);

// --- Paddle Move Right ---
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> paddle.getComponentOptional(PhysicsComponent.class)
                        .ifPresent(phys -> phys.setLinearVelocity(MOVE_SPEED * 20, 0)));
            }

            @Override
            protected void onActionEnd() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> paddle.getComponentOptional(PhysicsComponent.class)
                        .ifPresent(phys -> phys.setLinearVelocity(0, 0)));
            }
        }, KeyCode.RIGHT);

        // Launch Ball
        FXGL.getInput().addAction(new UserAction("Launch Ball") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball
                        -> ball.getComponentOptional(Attachment.class).ifPresent(a -> {
                            if (a.isAttached()) {
                                a.releaseBall();
                            }
                        })
                );
            }
        }, KeyCode.SPACE);

        // Keep ball attached to paddle
        FXGL.getGameTimer().runAtInterval(() -> {
            if (FXGL.getb("ballAttached")) {
                var paddleOpt = FXGL.getGameWorld().getSingletonOptional(EntityType.PADDLE);
                var ballOpt = FXGL.getGameWorld().getSingletonOptional(EntityType.BALL);

                if (paddleOpt.isPresent() && ballOpt.isPresent()) {
                    var paddle = paddleOpt.get();
                    var ball = ballOpt.get();

                    var paddleBBox = paddle.getBoundingBoxComponent();
                    var ballBBox = ball.getBoundingBoxComponent();

                    double x = paddleBBox.getCenterWorld().getX() - ballBBox.getWidth() / 2;
                    double y = paddleBBox.getMinYWorld() - ballBBox.getHeight() - 4;

                    ball.setPosition(x, y);
                    ball.getComponentOptional(PhysicsComponent.class).ifPresent(phys -> {
                        phys.setLinearVelocity(0, 0);
                        phys.getBody().setAwake(false);
                    });
                }
            }
        }, javafx.util.Duration.millis(16));
    }

    private static final class Holder {

        static final InputSystem INSTANCE = new InputSystem();
    }
}
