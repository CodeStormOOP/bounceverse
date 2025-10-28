package com.github.codestorm.bounceverse.components.behaviors.paddle;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.github.codestorm.bounceverse.Utilities.Time.Cooldown;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.components.behaviors.Behavior;
import com.github.codestorm.bounceverse.typing.annotations.ForEntity;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import java.util.List;
import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link PaddleShooting}</h1>
 *
 * Khả năng {@link EntityType#PADDLE} có thể bắn ra {@link EntityType#BULLET}.
 */
@ForEntity(EntityType.PADDLE)
public class PaddleShooting extends Behavior {
    private static final double OFFSET_LEFT = 4;
    private static final double OFFSET_RIGHT = -8;
    private static final double OFFSET_HEIGHT = -10;
    private static final Vec2 BULLET_VELOCITY = DirectionUnit.UP.getVector().mul(450);

    private final Cooldown cooldown;

    public PaddleShooting(double cooldown) {
        this(Duration.seconds(cooldown));
    }

    public PaddleShooting(Duration cooldown) {
        this.cooldown = new Cooldown(cooldown);
    }

    @Override
    public void execute(List<Object> data) {
        if (!cooldown.getCurrent().expired()) {
            return;
        }
        double leftX = entity.getX() + OFFSET_LEFT;
        double rightX = entity.getRightX() + OFFSET_RIGHT;
        double y = entity.getY() + OFFSET_HEIGHT;

        final var attack = entity.getComponentOptional(Attack.class);
        if (attack.isEmpty()) {
            return;
        }

        var leftData =
                new SpawnData(leftX, y)
                        .put("velocity", BULLET_VELOCITY)
                        .put("damage", attack.get().getDamage());
        var rightData =
                new SpawnData(rightX, y)
                        .put("velocity", BULLET_VELOCITY)
                        .put("damage", attack.get().getDamage());

        FXGL.spawn("paddleBullet", leftData);
        FXGL.spawn("paddleBullet", rightData);
        cooldown.getCurrent().makeNew();
    }

    public Cooldown getCooldown() {
        return cooldown;
    }
}
