package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.Utils.Time.Cooldown;
import javafx.geometry.Point2D;
import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link CanShoot}</h1>
 *
 * Cung cấp khả năng bắn đạn cho Entity.
 */
public class CanShoot extends Component implements RequirementTag {
    private final Cooldown cooldown;

    public void shoot() {
        if (!cooldown.getCurrent().expired()) {
            return;
        }

        var entity = getEntity();
        double halfWidth = entity.getWidth() / 2;
        double leftX = entity.getCenter().getX() - halfWidth + 4;
        double rightX = entity.getCenter().getX() + halfWidth - 8;
        double y = entity.getY() - 10;

        SpawnData dataLeft =
                new SpawnData(leftX, y).put("speed", 450.0).put("direction", new Point2D(0, -1));

        SpawnData dataRight =
                new SpawnData(rightX, y).put("speed", 450.0).put("direction", new Point2D(0, -1));

        FXGL.spawn("bullet", dataLeft);
        FXGL.spawn("bullet", dataRight);

        cooldown.getCurrent().makeNew();
    }

    public Cooldown getCooldown() {
        return cooldown;
    }

    public CanShoot(double cooldown) {
        this(Duration.seconds(cooldown));
    }

    public CanShoot(Duration cooldown) {
        this.cooldown = new Cooldown(cooldown);
    }
}
