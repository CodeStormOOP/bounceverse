package com.github.codestorm.bounceverse.components.behaviors.brick;

import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import java.util.List;

/** Khi brick bị phá thì spawn PowerUp. */
@OnlyForEntity({EntityType.BRICK})
public final class BrickDropPowerUp extends Behavior {
    // TODO: Thêm Component PowerUpContainer

    @Override
    public void execute(List<Object> data) {
        FXGL.spawn("powerUp", getEntity().getCenter());
    }

    @Override
    public void onRemoved() {
        execute(List.of());
    }
}
