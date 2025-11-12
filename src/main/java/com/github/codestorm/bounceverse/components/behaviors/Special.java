package com.github.codestorm.bounceverse.components.behaviors;

import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import com.github.codestorm.bounceverse.systems.init.PowerUpSpawner;
import javafx.util.Duration;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;

/**
 * Khi brick bị phá thì spawn PowerUp.
 */
@ForEntity({ EntityType.BRICK })
public class Special extends Component {

    @Override
    public void onRemoved() {
        var pos = getEntity().getCenter();

        FXGL.runOnce(() -> PowerUpSpawner.spawnRandom(pos.add(0, 10)), Duration.seconds(0.017));
    }
}
