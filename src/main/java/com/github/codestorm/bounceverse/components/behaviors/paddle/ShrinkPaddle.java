package com.github.codestorm.bounceverse.components.behaviors.paddle;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.properties.Width;
import com.github.codestorm.bounceverse.data.tags.components.BehaviorComponent;
import com.github.codestorm.bounceverse.data.tags.entities.ForPaddle;
import com.github.codestorm.bounceverse.data.tags.requirements.OptionalTag;

public class ShrinkPaddle extends Component implements BehaviorComponent, ForPaddle, OptionalTag {

    private final double shrinkedWidth = 60;
    private final double duration = 5.0;
    private double timer = 0;
    private boolean shrinked = false;

    public void active() {
        var width = entity.getComponent(Width.class);
        width.resetWidth();
        width.setWidth(shrinkedWidth);
        shrinked = true;
        timer = 0;
    }

    @Override
    public void onUpdate(double tpf) {
        if (shrinked) {
            timer += tpf;
            if (timer >= duration) {
                entity.getComponent(Width.class).resetWidth();
                shrinked = false;
            }
        }
    }
}
