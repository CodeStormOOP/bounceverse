package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.typing.interfaces.Undoable;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * <h1>{@link ScaleChange}</h1>
 *
 * Hành vi ScaleChange. Có thể {@link #executeLogic(List)} nhiều lần nhưng sẽ không thể stack lên
 * nhau (và không làm mới). <br>
 * ScaleChange sẽ áp dụng thông qua {@link Entity#getTransformComponent()} (tức cả view và
 * bounding).
 *
 * @see TransformComponent
 */
public class ScaleChange extends Behavior implements Undoable {
    public static final double DONT_CHANGE = 1;
    private double scaleWidth = DONT_CHANGE;
    private double scaleHeight = DONT_CHANGE;

    private final boolean removeWhenUndo;
    private final Duration duration;
    private List<Object> modified = null;
    private TimerAction current;

    @Override
    public Duration getDuration() {
        return duration;
    }

    @Override
    public boolean isRemoveWhenUndo() {
        return removeWhenUndo;
    }

    @Override
    public List<Object> getModified() {
        return modified;
    }

    @Override
    public void setModified(List<Object> modified) {
        this.modified = modified;
    }

    @Override
    public TimerAction getCurrent() {
        return current;
    }

    @Override
    public void setCurrent(TimerAction current) {
        this.current = current;
    }

    @Override
    public List<Object> executeLogic(List<Object> data) {
        if (scaleWidth <= 0 || scaleHeight <= 0) {
            return null;
        }
        final var transform = entity.getTransformComponent();
        transform.setScaleX(transform.getScaleX() * scaleWidth);
        transform.setScaleY(transform.getScaleY() * scaleHeight);
        return new ArrayList<>();
    }

    @Override
    public boolean undoLogic(List<Object> data) {
        if (scaleWidth <= 0 || scaleHeight <= 0) {
            return false;
        }
        final var transform = entity.getTransformComponent();
        transform.setScaleX(transform.getScaleX() / scaleWidth);
        transform.setScaleY(transform.getScaleY() / scaleHeight);
        return true;
    }

    @Override
    public void onRemoved() {
        undo();
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

    public ScaleChange(Duration duration) {
        this.duration = duration;
        this.removeWhenUndo = true;
    }
}
