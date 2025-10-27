package com.github.codestorm.bounceverse.typing.enums;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.shape.Rectangle;

/**
 *
 *
 * <h1>#{@link AnchorPoint}</h1>
 *
 * Các cạnh neo phổ biến trên {@link Rectangle2D}.
 */
public enum AnchorPoint {
    TOP_LEFT(0, 0),
    TOP_CENTER(0.5, 0),
    TOP_RIGHT(1, 0),
    BOTTOM_LEFT(0, 1),
    BOTTOM_CENTER(0.5, 1),
    BOTTOM_RIGHT(1, 1),
    CENTER_LEFT(0, 0.5),
    CENTER(0.5, 0.5),
    CENTER_RIGHT(1, 0.5);

    private final Point2D point;

    AnchorPoint(double nx, double ny) {
        this.point = new Point2D(nx, ny);
    }

    /**
     * Lấy {@link AnchorPoint} trên một {@link Rectangle2D}
     *
     * @param rect Hình chữ nhật 2D
     * @return Vị trí tương ứng trên hình
     */
    public Point2D of(Rectangle2D rect) {
        final var movement =
                new Point2D(rect.getWidth() * point.getX(), rect.getHeight() * point.getY());
        return new Point2D(rect.getMinX(), rect.getMinY()).add(movement);
    }

    /**
     * Lấy {@link AnchorPoint} trên một {@link Rectangle}
     *
     * @param rect Hình chữ nhật
     * @return Vị trí tương ứng trên hình
     */
    public Point2D of(Rectangle rect) {
        return of(new Rectangle2D(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight()));
    }
}
