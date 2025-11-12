package com.github.codestorm.bounceverse.typing.enums;

import com.almasb.fxgl.core.math.Vec2;

import javafx.geometry.Side;

/**
 *
 *
 * <h1>{@link DirectionUnit}</h1>
 *
 * Vector đơn vị đại diện cho <b>hướng di chuyển</b>.
 *
 * @apiNote Tránh nhầm lẫn với {@link javafx.geometry.Side}
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

    /** Lấy vector {@link Vec2} tương ứng. */
    public Vec2 getVector() {
        return vector;
    }

    /**
     * Chuyển hướng logic (DirectionUnit) sang hướng hình học (Side). Dùng trong PhysicSystem để
     * kiểm tra va chạm với Shield.
     */
    public Side toSide() {
        return switch (this) {
            case UP -> Side.TOP;
            case DOWN -> Side.BOTTOM;
            case LEFT -> Side.LEFT;
            case RIGHT -> Side.RIGHT;
            case STAND -> null;
        };
    }
}
