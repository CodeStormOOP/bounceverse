package com.github.codestorm.bounceverse.components.behaviors.bullet;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import javafx.geometry.Point2D;

public class BulletBehavior extends Component {

    private final double speed;
    private final Point2D direction;

    public BulletBehavior(double speed, Point2D direction) {
        this.speed = speed;
        this.direction = direction.normalize();
    }

    @Override
    public void onUpdate(double tpf) {
        var e = entity;

        e.translate(direction.multiply(speed * tpf));

        var viewport = FXGL.getGameScene().getViewport().getVisibleArea();
        if (e.getRightX() < viewport.getMinX()
                || e.getX() > viewport.getMaxX()
                || e.getBottomY() < viewport.getMinY()
                || e.getY() > viewport.getMaxY()) {
            e.removeFromWorld();
        }
    }
}
