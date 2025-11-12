package com.github.codestorm.bounceverse.components.properties.powerup.types.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

// Power up tạo ra gun.
public final class GunPowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(10);
    private static final double GUN_WIDTH = 6;
    private static final double GUN_HEIGHT = 10;
    private static final double OFFSET_Y = -10;
    private static final double OFFSET_X_LEFT = 4;
    private static final double OFFSET_X_RIGHT = 4;

    public GunPowerUp() {
        super("Gun");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().clearPowerUp(this.name);

        PowerUpManager.getInstance().activate(
                name,
                DURATION,
                () -> {
                    var shooting = paddle.getComponentOptional(PaddleShooting.class)
                            .orElseGet(() -> {
                                var newShooting = new PaddleShooting(0.25);
                                paddle.addComponent(newShooting);
                                return newShooting;
                            });

                    var leftGun = createGunEntity(paddle);
                    var rightGun = createGunEntity(paddle);

                    var pT = paddle.getTransformComponent();
                    var lT = leftGun.getTransformComponent();
                    var rT = rightGun.getTransformComponent();

                    lT.xProperty().bind(pT.xProperty().add(OFFSET_X_LEFT));
                    lT.yProperty().bind(pT.yProperty().add(OFFSET_Y));
                    rT.xProperty().bind(pT.xProperty().add(paddle.getBoundingBoxComponent().widthProperty()).subtract(GUN_WIDTH).subtract(OFFSET_X_RIGHT));
                    rT.yProperty().bind(pT.yProperty().add(OFFSET_Y));

                    var shootingTask = FXGL.getGameTimer().runAtInterval(() -> shooting.execute(null), Duration.seconds(0.25));

                    // Lưu lại các đối tượng cần dọn dẹp vào paddle
                    paddle.setProperty("gun.left", leftGun);
                    paddle.setProperty("gun.right", rightGun);
                    paddle.setProperty("gun.task", shootingTask);
                },
                () -> {
                    Entity leftGun = paddle.getObject("gun.left");
                    Entity rightGun = paddle.getObject("gun.right");
                    TimerAction shootingTask = paddle.getObject("gun.task");

                    if (shootingTask != null) {
                        shootingTask.expire();
                    }

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
                    if (paddle.hasComponent(PaddleShooting.class)) {
                        paddle.removeComponent(PaddleShooting.class);
                    }
                }
        );
    }

    private Entity createGunEntity(Entity owner) {
        return FXGL.entityBuilder()
                .view(new Rectangle(GUN_WIDTH, GUN_HEIGHT, Color.DARKRED))
                .zIndex(owner.getZIndex() + 1)
                .buildAndAttach();
    }
}
