package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.PropertyComponent;

public class Move extends Component implements PropertyComponent {

    private double speed = 400;
    private double direction = 0;

    public Move(double speed) {
        this.speed = speed;
    }

    public void setDirection(double direction) {
        this.direction = direction;
    }

    public double getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }
}
