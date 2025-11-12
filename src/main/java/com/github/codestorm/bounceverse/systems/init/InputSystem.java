package com.github.codestorm.bounceverse.systems.init;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.components.behaviors.paddle.ReverseControlComponent;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddlePowerComponent;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.DuplicatePaddlePowerUp;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.scene.input.KeyCode;

/**
 * Quản lý Input cho game.
 */
public final class InputSystem extends InitialSystem {

    private static final double MOVE_SPEED = 400.0;
    private static final double WALL_THICKNESS = WallFactory.DEFAULT_THICKNESS;

    public static InputSystem getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * Hàm di chuyển paddle tập trung, xử lý mọi logic va chạm và đảo ngược.
     * 
     * @param inputDirection Hướng nhận từ bàn phím (-1 cho trái, 1 cho phải).
     */
    private void movePaddle(double inputDirection) {
        Entity mainPaddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        Entity leftmostPaddle = mainPaddle;
        Entity rightmostPaddle = mainPaddle;

        if (FXGL.getWorldProperties().exists(DuplicatePaddlePowerUp.CLONES_LIST_KEY)) {
            List<Entity> clones = FXGL.geto(DuplicatePaddlePowerUp.CLONES_LIST_KEY);
            if (!clones.isEmpty() && clones.get(0).isActive()) { // Thêm kiểm tra isActive
                leftmostPaddle = clones.get(0);
                rightmostPaddle = clones.get(1);
            }
        }

        boolean isReversed = mainPaddle.hasComponent(ReverseControlComponent.class);
        double actualMoveDirection = isReversed ? -inputDirection : inputDirection;

        if (actualMoveDirection < 0) {
            if (leftmostPaddle.getX() <= WALL_THICKNESS) {
                setAllPaddlesVelocity(0);
                return;
            }
        } else {
            // SỬA ĐỔI Ở ĐÂY: Dùng getRightX() cho paddle ngoài cùng bên phải
            if (rightmostPaddle.getRightX() >= FXGL.getAppWidth() - WALL_THICKNESS) {
                setAllPaddlesVelocity(0);
                return;
            }
        }

        setAllPaddlesVelocity(actualMoveDirection * MOVE_SPEED);
    }

    /**
     * Đặt vận tốc X cho paddle chính và tất cả các paddle bản sao (nếu có).
     * 
     * @param velocity Vận tốc theo trục X
     */
    private void setAllPaddlesVelocity(double velocity) {
        // 1. Điều khiển paddle chính
        Entity mainPaddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        mainPaddle.getComponent(PhysicsComponent.class).setVelocityX(velocity);

        // 2. Điều khiển các paddle bản sao
        if (FXGL.getWorldProperties().exists(DuplicatePaddlePowerUp.CLONES_LIST_KEY)) {
            @SuppressWarnings("unchecked")
            List<Entity> clones = (List<Entity>) FXGL.getWorldProperties()
                    .getObject(DuplicatePaddlePowerUp.CLONES_LIST_KEY);
            if (clones != null) {
                for (Entity clone : clones) {
                    clone.getComponent(PhysicsComponent.class).setVelocityX(velocity);
                }
            }
        }
    }

    @Override
    public void apply() {

        // --- Các hành động giờ đây chỉ gọi hàm di chuyển tập trung ---
        FXGL.getInput().addAction(new UserAction("Move Left") {
            @Override
            protected void onAction() {
                movePaddle(-1.0); // -1 đại diện cho phím trái
            }

            @Override
            protected void onActionEnd() {
                setAllPaddlesVelocity(0);
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                movePaddle(1.0); // 1 đại diện cho phím phải
            }

            @Override
            protected void onActionEnd() {
                setAllPaddlesVelocity(0);
            }
        }, KeyCode.RIGHT);

        // --- Logic phóng bóng (giữ nguyên) ---
        FXGL.getInput().addAction(new UserAction("Launch Ball") {
            @Override
            protected void onActionBegin() {
                FXGL.getGameWorld().getEntitiesByType(EntityType.BALL)
                        .forEach(ball -> ball.getComponentOptional(Attachment.class).ifPresent(a -> {
                            if (a.isAttached()) {
                                a.releaseBall();
                                FXGL.set("ballAttached", false);
                            }
                        }));
            }
        }, KeyCode.SPACE);

        // Thêm đoạn code này vào cuối phương thức apply()

        FXGL.getInput().addAction(new UserAction("Activate Power") {
            @Override
            protected void onActionBegin() {
                var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
                // Tìm và kích hoạt component sức mạnh trên paddle
                paddle.getComponentOptional(PaddlePowerComponent.class)
                        .ifPresent(PaddlePowerComponent::activatePower);
            }
        }, KeyCode.S);
    }

    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}