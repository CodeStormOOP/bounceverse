package com.github.codestorm.bounceverse.components.properties.powerup;

import com.almasb.fxgl.entity.component.Component;

/**
 * Giúp PowerUp rơi xuống mà không cần PhysicsComponent.
 * Dùng update thủ công để tránh lỗi "Physics not initialized yet".
 */
public class FallingComponent extends Component {

    private static final double FALL_SPEED = 150; // pixel/s

    @Override
    public void onUpdate(double tpf) {
        // Dịch chuyển entity xuống dưới theo thời gian
        getEntity().translateY(FALL_SPEED * tpf);
    }
}
