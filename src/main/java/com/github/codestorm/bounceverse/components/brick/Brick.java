package com.github.codestorm.bounceverse.components.brick;

import com.github.codestorm.bounceverse.components.base.EntityComponent;
import com.github.codestorm.bounceverse.components.brick.properties.BrickHealth;

/**
 *
 *
 * <h1><b>Brick</b></h1>
 *
 * <p>Lớp này đại diện cho Viên gạch trong trò chơi.
 */
public final class Brick extends EntityComponent {
    private final BrickHealth health;

    @Override
    public void onAdded() {
        super.onAdded();
        entity.addComponent(health);
    }

    public Brick(BrickHealth health) {
        this.health = health;
    }
}
