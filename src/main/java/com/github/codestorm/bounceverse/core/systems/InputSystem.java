package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.scene.input.KeyCode;

/**
 * <h1>{@link InputSystem}</h1>
 *
 * {@link System} quản lý Input trong game. <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua
 * {@link #getInstance()}</i>.
 */
public class InputSystem extends System {

    public static InputSystem getInstance() {
        return InputSystem.Holder.INSTANCE;
    }

    @Override
    public void apply() {

        final double MOVE_SPEED = 10.0;
        final double WALL_THICKNESS = WallFactory.DEFAULT_THICKNESS;

        // --- Paddle Move Left ---
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> {
                            // Di chuyển sang trái
                            paddle.translate(DirectionUnit.LEFT.getVector().mul(MOVE_SPEED));

                            // Giới hạn biên trái
                            double minX = WALL_THICKNESS;
                            if (paddle.getX() < minX) {
                                paddle.setX(minX);
                            }
                        });
            }
        }, KeyCode.LEFT);

        // --- Paddle Move Right ---
        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.PADDLE)
                        .forEach(paddle -> {
                            // Di chuyển sang phải
                            paddle.translate(DirectionUnit.RIGHT.getVector().mul(MOVE_SPEED));

                            // Giới hạn biên phải
                            double maxX = FXGL.getAppWidth() - paddle.getWidth() - WALL_THICKNESS;
                            if (paddle.getX() > maxX) {
                                paddle.setX(maxX);
                            }
                        });
            }
        }, KeyCode.RIGHT);

        // --- Launch Ball ---
        FXGL.getInput().addAction(new UserAction("Launch Ball") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld()
                        .getEntitiesByType(EntityType.BALL)
                        .forEach(ball -> {
                            var attachment = ball.getComponentOptional(Attachment.class);
                            attachment.ifPresent(a -> {
                                if (a.isAttached()) {
                                    a.releaseBall();
                                }
                            });
                        });
            }
        }, KeyCode.SPACE);

        // --- Keep Ball Attached to Paddle ---
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

                    var physics = ball.getComponent(PhysicsComponent.class);
                    physics.setLinearVelocity(0, 0);
                    physics.getBody().setAwake(false);
                }
            }
        }, javafx.util.Duration.millis(16));
    }

    /**
     * Lazy-loaded singleton holder.
     * <a href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}
