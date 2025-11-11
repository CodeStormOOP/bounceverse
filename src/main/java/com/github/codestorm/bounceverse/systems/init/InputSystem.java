package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Attachment;
import com.github.codestorm.bounceverse.components.behaviors.paddle.ReverseControlComponent;
import com.github.codestorm.bounceverse.factory.entities.WallFactory;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.geometry.Point2D;
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
        Entity paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
        PhysicsComponent physics = paddle.getComponent(PhysicsComponent.class);

        // 1. Xác định hướng di chuyển thực sự dựa trên power-up
        boolean isReversed = paddle.hasComponent(ReverseControlComponent.class);
        double actualMoveDirection = isReversed ? -inputDirection : inputDirection;

        // 2. Lấy các giá trị biên
        double paddleWidth = paddle.getWidth();
        double minX = WALL_THICKNESS;
        double maxX = FXGL.getAppWidth() - WALL_THICKNESS - paddleWidth;

        // 3. Kiểm tra va chạm dựa trên hướng di chuyển THỰC SỰ
        if (actualMoveDirection < 0) { // Đang cố di chuyển sang TRÁI
            if (paddle.getX() <= minX) {
                physics.overwritePosition(new Point2D(minX, paddle.getY()));
                physics.setVelocityX(0);
                return;
            }
        } else { // Đang cố di chuyển sang PHẢI (actualMoveDirection > 0)
            if (paddle.getX() >= maxX) {
                physics.overwritePosition(new Point2D(maxX, paddle.getY()));
                physics.setVelocityX(0);
                return;
            }
        }

        // 4. Nếu không có va chạm, đặt vận tốc
        physics.setVelocityX(actualMoveDirection * MOVE_SPEED);
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
                FXGL.getGameWorld().getSingleton(EntityType.PADDLE).getComponent(PhysicsComponent.class)
                        .setVelocityX(0);
            }
        }, KeyCode.LEFT);

        FXGL.getInput().addAction(new UserAction("Move Right") {
            @Override
            protected void onAction() {
                movePaddle(1.0); // 1 đại diện cho phím phải
            }

            @Override
            protected void onActionEnd() {
                FXGL.getGameWorld().getSingleton(EntityType.PADDLE).getComponent(PhysicsComponent.class)
                        .setVelocityX(0);
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
    }

    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}