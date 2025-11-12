package com.github.codestorm.bounceverse.components.properties.powerup.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleViewManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public final class DuplicatePaddlePowerUp extends PowerUp {

    private static final Duration DURATION = Duration.seconds(10);
    private static final double PADDLE_GAP = 25.0;

    public static final String CLONES_LIST_KEY = "clonedPaddles";

    public DuplicatePaddlePowerUp() {
        super("DuplicatePaddle");
    }

    @Override
    public void apply(Entity paddle) {
        PowerUpManager.getInstance().clearPowerUp(this.name);
        PowerUpManager.getInstance()
                .activate(name, DURATION, () -> spawnClones(paddle), () -> removeClones(paddle));
    }

    private void spawnClones(Entity mainPaddle) {
        var mainViewManager = mainPaddle.getComponent(PaddleViewManager.class);
        var width = mainPaddle.getWidth();
        var mainPaddleX = mainPaddle.getX();
        var mainPaddleY = mainPaddle.getY();

        // Tạo bản sao bên trái
        var leftClone = createClone(mainPaddle);
        // THAY ĐỔI QUAN TRỌNG: KHÔNG DÙNG .bind(), chỉ đặt vị trí ban đầu
        leftClone.setPosition(mainPaddleX - width - PADDLE_GAP, mainPaddleY);
        FXGL.getGameWorld().addEntity(leftClone);
        mainViewManager.registerClone(leftClone);

        // Tạo bản sao bên phải
        var rightClone = createClone(mainPaddle);
        // THAY ĐỔI QUAN TRỌNG: KHÔNG DÙNG .bind(), chỉ đặt vị trí ban đầu
        rightClone.setPosition(mainPaddleX + width + PADDLE_GAP, mainPaddleY);
        FXGL.getGameWorld().addEntity(rightClone);
        mainViewManager.registerClone(rightClone);

        // Lưu danh sách vào biến chung để InputSystem điều khiển
        List<Entity> clones = new ArrayList<>();
        clones.add(leftClone);
        clones.add(rightClone);
        FXGL.set(CLONES_LIST_KEY, clones);
    }

    private void removeClones(Entity mainPaddle) {
        if (mainPaddle != null && mainPaddle.isActive()) {
            mainPaddle.getComponent(PaddleViewManager.class).clearClones();
        }

        if (FXGL.getWorldProperties().exists(CLONES_LIST_KEY)) {
            List<Entity> clones = FXGL.geto(CLONES_LIST_KEY);
            for (var clone : clones) {
                if (clone.isActive()) {
                    // Không cần unbind vì chúng ta không bind ngay từ đầu
                    clone.removeFromWorld();
                }
            }
            FXGL.getWorldProperties().remove(CLONES_LIST_KEY);
        }
    }

    private Entity createClone(Entity mainPaddle) {
        var color = FXGL.gets("paddleColor");
        var colorCapitalized = color.substring(0, 1).toUpperCase() + color.substring(1);
        var texturePath = "paddle/power/" + color + "/" + colorCapitalized + " Power Paddle.png";
        var texture = FXGL.texture(texturePath, mainPaddle.getWidth(), mainPaddle.getHeight());

        var physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);

        return FXGL.entityBuilder()
                .type(EntityType.PADDLE_CLONE)
                .view(texture)
                .bbox(new HitBox(BoundingShape.box(mainPaddle.getWidth(), mainPaddle.getHeight())))
                .collidable()
                .with(physics)
                .build();
    }
}
