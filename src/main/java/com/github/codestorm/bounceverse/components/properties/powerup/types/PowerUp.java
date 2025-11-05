package com.github.codestorm.bounceverse.components.properties.powerup.types;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

/**
 * Base class cho mọi Power-Up có thể áp dụng hiệu ứng.
 */
public abstract class PowerUp extends Component {

    protected final String name;

    protected PowerUp(String name) {
        this.name = name;
    }

    /** Kích hoạt hiệu ứng lên entity nhận (paddle, ball, v.v.) */
    public abstract void apply(Entity target);
}
