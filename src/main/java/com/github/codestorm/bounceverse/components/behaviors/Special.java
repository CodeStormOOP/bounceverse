package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.util.Duration;

/**
 * Khi brick bị phá thì spawn PowerUp.
 */
@ForEntity({ EntityType.BRICK })
public class Special extends Component {

    @Override
    public void onRemoved() {
        var pos = getEntity().getCenter();

        // ✅ Delay spawn 1 frame + offset xuống dưới 10px để tránh overlap brick
        FXGL.runOnce(() -> FXGL.spawn("powerUp", pos.add(0, 10)), Duration.seconds(0.017));
    }
}
