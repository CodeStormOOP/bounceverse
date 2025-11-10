package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.typing.interfaces.Undoable;

import javafx.util.Duration;

import java.util.List;

/**
 *
 *
 * <h1>{@link UndoableBehavior}</h1>
 *
 * {@link Behavior} có thể thực thi và hoàn tác được.
 *
 * @see Undoable
 */
// TODO: Convert to interface
public abstract class UndoableBehavior extends Behavior implements Undoable {
    protected boolean removeWhenUndo = true;
    protected Duration duration = Duration.INDEFINITE;
    private List<Object> modified = null;
    private TimerAction current;

    /**
     * Có phải hành động đã được thực thi không.
     *
     * @return {@code true} nếu đã thực thi, {@code false} nếu chưa
     */
    public final boolean isActive() {
        return current != null && current.isExpired() && modified != null;
    }

    /**
     * Logic bên trong {@link #execute(List)}.
     *
     * @param data Dữ liệu truyền vào
     * @return {@code null} nếu không thực thi, {@link List} các dữ liệu cần hoàn tác lại sau này
     *     nếu ngược lại
     */
    protected abstract List<Object> executeLogic(List<Object> data);

    /**
     * Logic bên trong {@link Undoable#undo()}.
     *
     * @param data Dữ liệu cần hoàn tác
     * @return {@code true} nếu cho phép hoàn tác, {@code false} nếu không.
     */
    protected abstract boolean undoLogic(List<Object> data);

    @Override
    public final void execute(List<Object> data) {
        if (isActive()) {
            return;
        }

        modified = executeLogic(data);
        if (modified != null) {
            current = FXGL.getGameTimer().runOnceAfter(this::undo, duration);
        }
    }

    @Override
    public final void undo() {
        if (!isActive()) {
            return;
        }

        if (undoLogic(modified)) {
            current = null;
            modified = null;
            if (removeWhenUndo) {
                entity.removeComponent(this.getClass());
            }
        }
    }

    @Override
    public void onRemoved() {
        undo();
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public boolean isRemoveWhenUndo() {
        return removeWhenUndo;
    }

    public void setRemoveWhenUndo(boolean removeWhenUndo) {
        this.removeWhenUndo = removeWhenUndo;
    }

    public UndoableBehavior() {}

    public UndoableBehavior(Duration duration) {
        this.duration = duration;
    }

    public UndoableBehavior(Duration duration, boolean removeWhenUndo) {
        this(duration);
        this.removeWhenUndo = removeWhenUndo;
    }
}
