package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.systems.init.PowerUpSpawner;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

import javafx.util.Duration;

/** Khi brick bị phá thì spawn PowerUp. */
@OnlyForEntity({EntityType.BRICK})
public class Special extends Component {

    @Override
    public void onRemoved() {
        var pos = getEntity().getCenter();

        FXGL.runOnce(() -> PowerUpSpawner.spawnRandom(pos.add(0, 10)), Duration.seconds(0.017));
    }
}
