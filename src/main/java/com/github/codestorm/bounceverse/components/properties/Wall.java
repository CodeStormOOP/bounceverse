package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.PropertyComponent;
import com.github.codestorm.bounceverse.data.types.Side;

public class Wall extends Component implements PropertyComponent {

    private Side side;

    public Wall(Side side) {
        this.side = side;
    }

    public Side getSide() {
        return side;
    }
}
