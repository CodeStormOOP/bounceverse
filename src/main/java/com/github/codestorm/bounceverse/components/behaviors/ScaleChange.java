package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * <h1>{@link ScaleChange}</h1>
 *
 * Hành vi ScaleChange. Có thể {@link #execute(List)} nhiều lần nhưng sẽ không thể stack lên nhau
 * (và không làm mới). <br>
 * ScaleChange sẽ áp dụng thông qua {@link Entity#getTransformComponent()} (tức cả view và
 * bounding).
 *
 * @see TransformComponent
 */
@ForEntity({})
public class ScaleChange extends UndoableBehavior {
    public static final double DONT_CHANGE = 1;
    private double scaleWidth = DONT_CHANGE;
    private double scaleHeight = DONT_CHANGE;

    @Override
    protected List<Object> executeLogic(List<Object> data) {
        if (scaleWidth <= 0 || scaleHeight <= 0) {
            return null;
        }
        final var transform = entity.getTransformComponent();
        transform.setScaleX(transform.getScaleX() * scaleWidth);
        transform.setScaleY(transform.getScaleY() * scaleHeight);
        return new ArrayList<>();
    }

    @Override
    protected boolean undoLogic(List<Object> data) {
        if (scaleWidth <= 0 || scaleHeight <= 0) {
            return false;
        }
        final var transform = entity.getTransformComponent();
        transform.setScaleX(transform.getScaleX() / scaleWidth);
        transform.setScaleY(transform.getScaleY() / scaleHeight);
        return true;
    }

    public double getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleWidth(double scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public double getScaleHeight() {
        return scaleHeight;
    }

    public void setScaleHeight(double scaleHeight) {
        this.scaleHeight = scaleHeight;
    }
}
