package com.github.codestorm.bounceverse.components.behaviors.paddle;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.properties.Width;
import com.github.codestorm.bounceverse.data.tags.components.BehaviorComponent;
import com.github.codestorm.bounceverse.data.tags.entities.ForPaddle;
import com.github.codestorm.bounceverse.data.tags.requirements.OptionalTag;

public class ExpandPaddle extends Component implements BehaviorComponent, ForPaddle, OptionalTag {

    private final double expandedWidth = 200;
    private final double duration = 5.0;
    private double timer = 0;
    private boolean expanded = false;

    public void active() {
        if (!expanded) {
            entity.getComponent(Width.class).setWidth(expandedWidth);
            expanded = true;
            timer = 0;
        }
    }

    @Override
    public void onUpdate(double tpf) {
        if (expanded) {
            timer += tpf;
            if (timer >= duration) {
                entity.getComponent(Width.class).resetWidth();
                expanded = false;
            }
        }
    }
}
