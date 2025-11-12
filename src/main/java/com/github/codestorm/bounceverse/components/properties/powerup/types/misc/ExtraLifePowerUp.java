package com.github.codestorm.bounceverse.components.properties.powerup.types.misc;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;

/**
 * Power-up cộng thêm một mạng cho người chơi.
 * Hiệu ứng xảy ra ngay lập tức.
 */
public final class ExtraLifePowerUp extends PowerUp {

    public ExtraLifePowerUp() {
        super("ExtraLife");
    }

    @Override
    public void apply(Entity target) {
        HealthIntValue lives = FXGL.getWorldProperties().getObject("lives");

        lives.restore(1);
    }
}