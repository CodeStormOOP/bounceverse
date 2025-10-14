package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.PropertyComponent;

public class Move extends Component implements PropertyComponent {

    private double speed;
    private double direction = 0;

    public Move(double speed) {
        this.speed = speed;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public void left() {
        this.direction = -1;
    }

    public void right() {
        this.direction = 1;
    }

    public void stop() {
        this.direction = 0;
    }

    @Override
    public void onUpdate(double tpf) {
        if (direction != 0) {
            entity.translateX(direction * speed * tpf);
        }
    }

}
