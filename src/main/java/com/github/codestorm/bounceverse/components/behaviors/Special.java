package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.typing.annotations.ForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 * Khi brick bị phá thì spawn PowerUp.
 */
@ForEntity({ EntityType.BRICK })
public class Special extends Component {

    @Override
    public void onRemoved() {
        FXGL.spawn("powerUp", getEntity().getCenter());
    }
}
