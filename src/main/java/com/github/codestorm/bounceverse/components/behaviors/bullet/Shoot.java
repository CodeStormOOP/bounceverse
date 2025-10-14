package com.github.codestorm.bounceverse.components.behaviors.bullet;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.entity.component.Component;

import javafx.geometry.Point2D;

/**
 * Component quản lý khả năng bắn đạn của entity.
 */
public class Shoot extends Component {

    private final double cooldown;
    private double cooldownTimer = 0;
    private final double duration;
    private double timer = 0;
    private boolean active = false;

    public Shoot(double cooldown, double duration) {
        this.cooldown = cooldown;
        this.duration = duration;
    }

    public void activate() {
        active = true;
        cooldownTimer = 0;
    }

    @Override
    public void onUpdate(double tpf) {
        if (active) {
            cooldownTimer -= tpf;
            timer += tpf;
        }
    }

    public boolean canShoot() {
        return active && cooldownTimer <= 0;
    }

    public void shoot() {
        if (!canShoot()) {
            return;
        }
        if (timer >= duration) {
            active = false;
            timer = 0;
        }
        var e = getEntity();
        double halfWidth = e.getWidth() / 2;

        double leftX = e.getCenter().getX() - halfWidth + 4;
        double rightX = e.getCenter().getX() + halfWidth - 8;

        double y = e.getY() - 10;

        SpawnData dataLeft = new SpawnData(leftX, y)
                .put("speed", 450.0)
                .put("direction", new Point2D(0, -1));

        SpawnData dataRight = new SpawnData(rightX, y)
                .put("speed", 450.0)
                .put("direction", new Point2D(0, -1));

        FXGL.spawn("bullet", dataLeft);
        FXGL.spawn("bullet", dataRight);

        cooldownTimer = cooldown;
    }
}
