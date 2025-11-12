package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public final class GunPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(10);
    private static final double GUN_WIDTH = 6;
    private static final double GUN_HEIGHT = 10;
    private static final double OFFSET_Y = -10;
    private static final double OFFSET_X_LEFT = 4;
    private static final double OFFSET_X_RIGHT = 4;

    private Entity leftGun;
    private Entity rightGun;
    private TimerAction shootingTask;
    private TimerAction selfDestructTask;

    public GunPowerUp() {
        super("Gun");
    }

    @Override
    public void onAdded() {
        var paddle = getEntity();

        // 1. Thêm hoặc lấy component bắn
        var shooting = paddle.getComponentOptional(PaddleShooting.class)
                .orElseGet(() -> {
                    var newShooting = new PaddleShooting(0.25);
                    paddle.addComponent(newShooting);
                    return newShooting;
                });

        // 2. Tạo và gắn súng
        leftGun = createGunEntity();
        rightGun = createGunEntity();

        var pT = paddle.getTransformComponent();
        var lT = leftGun.getTransformComponent();
        var rT = rightGun.getTransformComponent();

        lT.xProperty().bind(pT.xProperty().add(OFFSET_X_LEFT));
        lT.yProperty().bind(pT.yProperty().add(OFFSET_Y));
        rT.xProperty().bind(pT.xProperty().add(paddle.getBoundingBoxComponent().widthProperty()).subtract(GUN_WIDTH)
                .subtract(OFFSET_X_RIGHT));
        rT.yProperty().bind(pT.yProperty().add(OFFSET_Y));

        // 3. Bắt đầu timer bắn
        shootingTask = FXGL.getGameTimer().runAtInterval(() -> shooting.execute(null), Duration.seconds(0.25));

        // 4. Lên lịch tự hủy component này sau DURATION
        selfDestructTask = FXGL.getGameTimer().runOnceAfter(() -> paddle.removeComponent(GunPowerUp.class), DURATION);
    }

    @Override
    public void onRemoved() {
        var paddle = getEntity();

        // Dọn dẹp mọi thứ
        if (shootingTask != null)
            shootingTask.expire();
        if (selfDestructTask != null)
            selfDestructTask.expire();

        if (leftGun != null && leftGun.isActive()) {
            leftGun.getTransformComponent().xProperty().unbind();
            leftGun.getTransformComponent().yProperty().unbind();
            leftGun.removeFromWorld();
        }
        if (rightGun != null && rightGun.isActive()) {
            rightGun.getTransformComponent().xProperty().unbind();
            rightGun.getTransformComponent().yProperty().unbind();
            rightGun.removeFromWorld();
        }
        if (paddle != null && paddle.hasComponent(PaddleShooting.class)) {
            paddle.removeComponent(PaddleShooting.class);
        }
    }

    private Entity createGunEntity() {
        return FXGL.entityBuilder()
                .view(new Rectangle(GUN_WIDTH, GUN_HEIGHT, Color.DARKRED))
                .zIndex(getEntity().getZIndex() + 1)
                .buildAndAttach();
    }

    @Override
    public void apply(Entity target) {
        // Phương thức này giờ chỉ cần thêm chính component này vào paddle
        if (!target.hasComponent(GunPowerUp.class)) {
            target.addComponent(new GunPowerUp());
        }
    }
}