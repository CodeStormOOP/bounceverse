package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

public class Special extends Component{

    @Override
    public void onRemoved() {
        // Khi bị phá → spawn PowerUp ngẫu nhiên
        FXGL.spawn("powerUp", getEntity().getCenter());
    }
}
