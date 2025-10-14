package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;

public class Bullet extends Component {

    private final double cooldown;
    private double timerCooldown = 0;
    private final double duration;
    private double activeTime = 0;
    private boolean active = false;

    public Bullet(double cooldown, double duration) {
        this.cooldown = cooldown;
        this.duration = duration;
    }

    public void active() {
        active = true;
        activeTime = 0;
        timerCooldown = 0;
    }

    @Override
    public void onUpdate(double tpf) {
        if (!active) {
            return;
        }

        activeTime += tpf;
        timerCooldown -= tpf;

        if (activeTime >= duration) {
            active = false;
        }
    }

    public boolean canShoot() {
        return active && timerCooldown <= 0;
    }

    public void shoot() {
        timerCooldown = cooldown;
    }
}
