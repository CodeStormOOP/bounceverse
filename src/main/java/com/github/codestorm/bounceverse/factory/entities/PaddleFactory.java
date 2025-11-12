package com.github.codestorm.bounceverse.factory.entities;

import com.almasb.fxgl.dsl.EntityBuilder;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.Spawns;
import com.almasb.fxgl.physics.BoundingShape;
import com.almasb.fxgl.physics.HitBox;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.almasb.fxgl.physics.box2d.dynamics.BodyType;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.components.behaviors.paddle.PaddleShooting;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddlePowerComponent;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleViewManager;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 * Factory để tạo các entity loại PADDLE trong trò chơi.
 */
public final class PaddleFactory extends EntityFactory {

    private static final double DEFAULT_WIDTH = 150;
    private static final double DEFAULT_HEIGHT = 40;
    private static final double DEFAULT_ARC_WIDTH = 14;
    private static final double DEFAULT_ARC_HEIGHT = 14;
    public static final Color DEFAULT_COLOR = Color.LIGHTBLUE;
    private static final Duration DEFAULT_SHOOT_COOLDOWN = Duration.ZERO;

    @Override
    protected EntityBuilder getBuilder(SpawnData data) {
        return FXGL.entityBuilder(data)
                .type(EntityType.PADDLE)
                .collidable();
    }

    @Spawns("paddle")
    public Entity newPaddle(SpawnData data) {
        final double width = Utilities.Typing.getOr(data, "width", DEFAULT_WIDTH);
        final double height = Utilities.Typing.getOr(data, "height", DEFAULT_HEIGHT);

        var physics = new PhysicsComponent();
        physics.setBodyType(BodyType.KINEMATIC);
        double px = FXGL.getAppWidth() / 2.0 - width / 2.0;
        double py = FXGL.getAppHeight() - height - 40;

        return getBuilder(data)
                .at(px, py)
                .bbox(new HitBox(BoundingShape.box(width, height)))
                .with(physics)
                .with(new PaddleShooting(DEFAULT_SHOOT_COOLDOWN))
                .with(new Attack())
                .with(new PaddleViewManager())
                .with(new PaddlePowerComponent())
                .build();
    }
}