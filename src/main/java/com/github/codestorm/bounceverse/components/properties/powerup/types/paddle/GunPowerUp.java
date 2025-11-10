package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Power-Up: tạo 2 nòng súng trên paddle và tự động bắn trong 10s.
 */
public final class GunPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(10);
    private static final double GUN_WIDTH = 6;
    private static final double GUN_HEIGHT = 10;
    private static final double OFFSET_Y = -10;

    public GunPowerUp() {
        super("Gun");
    }

    @Override
    public void apply(Entity paddle) {
        // Thêm khả năng bắn nếu chưa có
        paddle.getComponentOptional(PaddleShooting.class)
                .orElseGet(() -> {
                    var shooting = new PaddleShooting(0.25);
                    paddle.addComponent(shooting);
                    return shooting;
                });

        // Tạo 2 nòng súng
        var leftGun = FXGL.entityBuilder()
                .view(new Rectangle(GUN_WIDTH, GUN_HEIGHT, Color.DARKRED))
                .zIndex(paddle.getZIndex() + 1)
                .build();

        var rightGun = FXGL.entityBuilder()
                .view(new Rectangle(GUN_WIDTH, GUN_HEIGHT, Color.DARKRED))
                .zIndex(paddle.getZIndex() + 1)
                .build();

        FXGL.getGameWorld().addEntities(leftGun, rightGun);

        // Gắn theo paddle
        var pT = paddle.getTransformComponent();
        var lT = leftGun.getTransformComponent();
        var rT = rightGun.getTransformComponent();
        double rightOffsetX = paddle.getWidth() - GUN_WIDTH - 4;

        lT.xProperty().bind(pT.xProperty().add(4));
        lT.yProperty().bind(pT.yProperty().add(OFFSET_Y));

        rT.xProperty().bind(pT.xProperty().add(rightOffsetX));
        rT.yProperty().bind(pT.yProperty().add(OFFSET_Y));

        // Bắn liên tục mỗi 0.25s
        var shooting = paddle.getComponent(PaddleShooting.class);
        var shootingTask = FXGL.getGameTimer().runAtInterval(() -> shooting.execute(null), Duration.seconds(0.25));

        // Sau DURATION: dừng bắn và gỡ súng
        FXGL.runOnce(() -> {
            shootingTask.expire();
            lT.xProperty().unbind();
            lT.yProperty().unbind();
            rT.xProperty().unbind();
            rT.yProperty().unbind();
            leftGun.removeFromWorld();
            rightGun.removeFromWorld();
            paddle.removeComponent(PaddleShooting.class);
        }, DURATION);
    }
}
