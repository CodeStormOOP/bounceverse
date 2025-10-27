package com.github.codestorm.bounceverse.typing.enums;

import com.almasb.fxgl.core.math.Vec2;

/**
 *
 *
 * <h1>#{@link DirectionUnit}</h1>
 *
 * Vector đơn vị đại diện cho hướng di chuyển.
 *
 * @see Vec2
 */
public enum DirectionUnit {
    LEFT(-1, 0),
    RIGHT(1, 0),
    UP(0, -1),
    DOWN(0, 1),
    STAND(0, 0);

    private final Vec2 vector;

    DirectionUnit(double vx, double vy) {
        this.vector = new Vec2(vx, vy).normalize();
    }

    /**
     * Lấy vector {@link Vec2} tương ứng.
     *
     * @return Vector
     */
    public Vec2 getVector() {
        return vector;
    }
}
