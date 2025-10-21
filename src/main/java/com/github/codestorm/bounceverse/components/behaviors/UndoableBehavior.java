package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.data.contracts.CanUndo;
import java.util.List;
import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link UndoableBehavior}</h1>
 *
 * {@link Behavior} có thể thực thi và hoàn tác được.
 *
 * @see CanUndo
 */
public abstract class UndoableBehavior extends Behavior implements CanUndo {
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
     * Logic bên trong {@link CanUndo#undo()}.
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
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
