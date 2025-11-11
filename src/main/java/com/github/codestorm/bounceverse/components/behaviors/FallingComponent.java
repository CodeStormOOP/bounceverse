package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.entity.component.Component;

/**
 * Cho Power-Up rơi thẳng xuống với tốc độ cố định.
 */
public final class FallingComponent extends Component {

    private static final double SPEED = 150;

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(SPEED * tpf);
    }
}
