package com.github.codestorm.bounceverse.data.types;

import com.almasb.fxgl.core.math.Vec2;

/**
 *
 *
 * <h1>{@link UnitVelocity}</h1>
 *
 * Vector đơn vị đại diện cho hướng di chuyển.
 *
 * @see Vec2
 */
public enum UnitVelocity {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1),
    STAND(0, 0);

    private final Vec2 vector;

    UnitVelocity(double vx, double vy) {
        this.vector = new Vec2(vx, vy).normalize();
    }

    public Vec2 getVector() {
        return vector;
    }
}
