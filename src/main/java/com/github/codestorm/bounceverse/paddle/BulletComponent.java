package com.github.codestorm.bounceverse.paddle;

import com.almasb.fxgl.entity.component.Component;

public class BulletComponent extends Component {
    private double speed;

    public BulletComponent(double speed) {
        this.speed = speed;
    }

    @Override
    public void onUpdate(double tpf) {
        entity.translateY(-speed * tpf);

        if (entity.getY() < -10) {
            entity.removeFromWorld();
        }
    }
}
