package com.github.codestorm.bounceverse.components.properties.powerup.types.misc; // Sửa package cho đúng

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 * Một "lưới an toàn" để bắt lại những quả bóng đi xuyên qua khiên do lỗi vật lý.
 */
public class ShieldSafetyNetComponent extends Component {
    @Override
    public void onUpdate(double tpf) {
        // Lấy vị trí Y của tấm khiên
        double shieldY = entity.getY();

        // Kiểm tra tất cả các quả bóng trong game
        FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).forEach(ball -> {
            // Nếu quả bóng đã ở dưới tấm khiên
            if (ball.getY() > shieldY) {
                // 1. Đẩy nó trở lại ngay phía trên tấm khiên
                ball.setY(shieldY - ball.getHeight());

                // 2. Đảo ngược vận tốc Y của nó để nó nảy lên
                var physics = ball.getComponent(PhysicsComponent.class);
                physics.setLinearVelocity(physics.getVelocityX(), -Math.abs(physics.getVelocityY()));
            }
        });
    }
}