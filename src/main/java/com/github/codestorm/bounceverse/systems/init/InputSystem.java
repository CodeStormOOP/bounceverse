package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.components.behaviors.paddle.ReverseControlComponent;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddlePowerComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.paddle.DuplicatePaddlePowerUp;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.scene.input.KeyCode;

import java.util.List;

/** Quản lý Input cho game. */
public final class InputSystem extends InitialSystem {

    private static final double MOVE_SPEED = 400.0;
    private static final double WALL_THICKNESS = WallFactory.DEFAULT_THICKNESS;

    private InputSystem() {}

    public static InputSystem getInstance() {
        return Holder.INSTANCE;
    }

    private void movePaddle(double inputDirection) {
        // TODO: Sửa lỗi chọn reset game khi đang giữ di chuyển di chuyển
        var mainPaddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        var leftmostPaddle = mainPaddle;
        var rightmostPaddle = mainPaddle;

        if (FXGL.getWorldProperties().exists(DuplicatePaddlePowerUp.CLONES_LIST_KEY)) {
            List<Entity> clones = FXGL.geto(DuplicatePaddlePowerUp.CLONES_LIST_KEY);
            if (!clones.isEmpty() && clones.get(0).isActive()) {
                leftmostPaddle = clones.get(0);
                rightmostPaddle = clones.get(1);
            }
        }

        var isReversed = mainPaddle.hasComponent(ReverseControlComponent.class);
        var actualMoveDirection = isReversed ? -inputDirection : inputDirection;

        if (actualMoveDirection < 0) {
            if (leftmostPaddle.getX() <= WALL_THICKNESS) {
                setAllPaddlesVelocity(0);
                return;
            }
        } else {
            if (rightmostPaddle.getRightX() >= FXGL.getAppWidth() - WALL_THICKNESS) {
                setAllPaddlesVelocity(0);
                return;
            }
        }
        setAllPaddlesVelocity(actualMoveDirection * MOVE_SPEED);
    }

    private void setAllPaddlesVelocity(double velocity) {
        var mainPaddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        mainPaddle.getComponent(PhysicsComponent.class).setVelocityX(velocity);

        if (FXGL.getWorldProperties().exists(DuplicatePaddlePowerUp.CLONES_LIST_KEY)) {
            List<Entity> clones = FXGL.geto(DuplicatePaddlePowerUp.CLONES_LIST_KEY);
            for (var clone : clones) {
                clone.getComponent(PhysicsComponent.class).setVelocityX(velocity);
            }
        }
    }

    @Override
    public void apply() {
        FXGL.getInput()
                .addAction(
                        new UserAction("Move Left") {
                            @Override
                            protected void onAction() {
                                movePaddle(-1.0);
                            }

                            @Override
                            protected void onActionEnd() {
                                setAllPaddlesVelocity(0);
                            }
                        },
                        KeyCode.LEFT);

        FXGL.getInput()
                .addAction(
                        new UserAction("Move Right") {
                            @Override
                            protected void onAction() {
                                movePaddle(1.0);
                            }

                            @Override
                            protected void onActionEnd() {
                                setAllPaddlesVelocity(0);
                            }
                        },
                        KeyCode.RIGHT);

        FXGL.getInput()
                .addAction(
                        new UserAction("Launch Ball") {
                            @Override
                            protected void onActionBegin() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.BALL)
                                        .forEach(
                                                ball ->
                                                        ball.getComponentOptional(Attachment.class)
                                                                .ifPresent(
                                                                        a -> {
                                                                            if (a.isAttached()) {
                                                                                a.releaseBall();
                                                                                FXGL.set(
                                                                                        "ballAttached",
                                                                                        false);
                                                                            }
                                                                        }));
                            }
                        },
                        KeyCode.SPACE);

        FXGL.getInput()
                .addAction(
                        new UserAction("Activate Power") {
                            @Override
                            protected void onActionBegin() {
                                FXGL.getGameWorld()
                                        .getSingletonOptional(EntityType.PADDLE)
                                        .flatMap(
                                                paddle ->
                                                        paddle.getComponentOptional(
                                                                PaddlePowerComponent.class))
                                        .ifPresent(PaddlePowerComponent::activatePower);
                            }
                        },
                        KeyCode.S);

        FXGL.getInput()
                .addAction(
                        new UserAction("Reset Game") {
                            @Override
                            protected void onActionBegin() {
                                GameSystem.resetGame();
                            }
                        },
                        KeyCode.R);
    }

    private static final class Holder {

        static final InputSystem INSTANCE = new InputSystem();
    }
}
