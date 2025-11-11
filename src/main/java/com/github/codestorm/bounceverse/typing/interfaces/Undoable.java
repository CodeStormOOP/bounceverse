package com.github.codestorm.bounceverse.typing.interfaces;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.time.TimerAction;
import com.github.codestorm.bounceverse.components.Behavior;

import javafx.util.Duration;

import java.util.List;

/**
 *
 *
 * <h1>%{@link Undoable}</h1>
 *
 * Có thể thực thi và hoàn tác được.
 */
public interface Undoable extends Executable {
    /**
     * Lấy duration để tự động hoàn tác.
     *
     * @return Duration
     */
    Duration getDuration();

    /**
     * Lấy trạng thái có xóa behavior khi undo không.
     *
     * @return {@code true} nếu xóa, {@code false} nếu không
     */
    boolean isRemoveWhenUndo();

    /**
     * Lấy dữ liệu đã được modified.
     *
     * @return Dữ liệu modified
     */
    List<Object> getModified();

    /**
     * Set dữ liệu modified.
     *
     * @param modified Dữ liệu modified
     */
    void setModified(List<Object> modified);

    /**
     * Lấy timer action hiện tại.
     *
     * @return TimerAction
     */
    TimerAction getCurrent();

    /**
     * Set timer action hiện tại.
     *
     * @param current TimerAction
     */
    void setCurrent(TimerAction current);

    /**
     * Có phải hành động đã được thực thi không.
     *
     * @return {@code true} nếu đã thực thi, {@code false} nếu chưa
     */
    default boolean isActive() {
        return getCurrent() != null && getCurrent().isExpired() && getModified() != null;
    }

    /**
     * Logic bên trong {@link #execute(List)}.
     *
     * @param data Dữ liệu truyền vào
     * @return {@code null} nếu không thực thi, {@link List} các dữ liệu cần hoàn tác lại sau này
     *     nếu ngược lại
     */
    List<Object> executeLogic(List<Object> data);

    /**
     * Logic bên trong {@link Undoable#undo()}.
     *
     * @param data Dữ liệu cần hoàn tác
     * @return {@code true} nếu cho phép hoàn tác, {@code false} nếu không.
     */
    boolean undoLogic(List<Object> data);

    /**
     * @deprecated Viết logic lên {@link #executeLogic(List)} thay vì method này.
     * @param data Dữ liệu truyền vào
     */
    @Deprecated
    @Override
    default void execute(List<Object> data) {
        if (isActive()) {
            return;
        }

        var modified = executeLogic(data);
        if (modified != null) {
            setModified(modified);
            setCurrent(FXGL.getGameTimer().runOnceAfter(this::undo, getDuration()));
        }
    }

    /** Hoàn tác trạng thái về trước đó. */
    default void undo() {
        if (!isActive()) {
            return;
        }

        if (undoLogic(getModified())) {
            setCurrent(null);
            setModified(null);
            if (isRemoveWhenUndo() && this instanceof Behavior behavior) {
                behavior.getEntity().removeComponent(behavior.getClass());
            }
        }
    }
}
