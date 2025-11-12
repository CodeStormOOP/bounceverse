package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.systems.manager.settings.UserSettingsManager;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.scene.input.KeyCode;

/**
 *
 *
 * <h1>{@link InputSystem}</h1>
 *
 * {@link InitialSystem} quản lý Input trong game. <br>
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 */
public final class InputSystem extends InitialSystem {
    public static InputSystem getInstance() {
        return InputSystem.Holder.INSTANCE;
    }

    public static final double NORMAL_SPEED = 10.0;

    @Override
    public void apply() {
        // Load key bindings from settings
        var settings = UserSettingsManager.getInstance().get();
        var controls = settings.getControls();

        KeyCode moveLeftKey;
        KeyCode moveRightKey;
        KeyCode launchBallKey;

        try {
            moveLeftKey = KeyCode.valueOf(controls.getMoveLeft().toUpperCase());
        } catch (Exception e) {
            moveLeftKey = KeyCode.LEFT;
        }

        try {
            moveRightKey = KeyCode.valueOf(controls.getMoveRight().toUpperCase());
        } catch (Exception e) {
            moveRightKey = KeyCode.RIGHT;
        }

        try {
            launchBallKey = KeyCode.valueOf(controls.getLaunchBall().toUpperCase());
        } catch (Exception e) {
            launchBallKey = KeyCode.SPACE;
        }

        // --- Paddle - Move Left ---
        FXGL.getInput()
                .addAction(
                        new UserAction("Move Left") {
                            @Override
                            protected void onAction() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(
                                                paddle -> {
                                                    paddle.translate(
                                                            DirectionUnit.LEFT
                                                                    .getVector()
                                                                    .mul(NORMAL_SPEED));
                                                });
                            }
                        },
                        moveLeftKey);

        // --- Paddle - Move Right ---
        FXGL.getInput()
                .addAction(
                        new UserAction("Move Right") {
                            @Override
                            protected void onAction() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(
                                                paddle -> {
                                                    paddle.translate(
                                                            DirectionUnit.RIGHT
                                                                    .getVector()
                                                                    .mul(NORMAL_SPEED));
                                                });
                            }
                        },
                        moveRightKey);

        // --- Ball - Launch ---
        FXGL.getInput()
                .addAction(
                        new UserAction("Launch Ball") {
                            @Override
                            protected void onActionBegin() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.BALL)
                                        .forEach(
                                                ball -> {
                                                    var attachment =
                                                            ball.getComponentOptional(
                                                                    Attachment.class);
                                                    attachment.ifPresent(
                                                            a -> {
                                                                if (a.isAttached()) {
                                                                    a.releaseBall();
                                                                }
                                                            });
                                                });
                            }
                        },
                        launchBallKey);

        // --- Keep Ball Attached to Paddle ---
        FXGL.getGameTimer()
                .runAtInterval(
                        () -> {
                            if (FXGL.getb("ballAttached")) {
                                var paddleOpt =
                                        FXGL.getGameWorld().getSingletonOptional(EntityType.PADDLE);
                                var ballOpt =
                                        FXGL.getGameWorld().getSingletonOptional(EntityType.BALL);

                                if (paddleOpt.isPresent() && ballOpt.isPresent()) {
                                    var paddle = paddleOpt.get();
                                    var ball = ballOpt.get();

                                    var paddleBBox = paddle.getBoundingBoxComponent();
                                    var ballBBox = ball.getBoundingBoxComponent();

                                    double x =
                                            paddleBBox.getCenterWorld().getX()
                                                    - ballBBox.getWidth() / 2;
                                    double y = paddleBBox.getMinYWorld() - ballBBox.getHeight() - 4;

                                    ball.setPosition(x, y);

                                    var physics = ball.getComponent(PhysicsComponent.class);
                                    physics.setLinearVelocity(0, 0);
                                    physics.getBody().setAwake(false);
                                }
                            }
                        },
                        javafx.util.Duration.millis(16));
    }

    /**
     * Lazy-loaded singleton holder. <a
     * href="https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}
