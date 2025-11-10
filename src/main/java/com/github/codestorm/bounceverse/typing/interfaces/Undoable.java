package com.github.codestorm.bounceverse.typing.interfaces;

import com.github.codestorm.bounceverse.components.behaviors.UndoableBehavior;

/**
 *
 *
 * <h1>%{@link Undoable}</h1>
 *
 * Có thể hoàn tác trạng thái về lúc trước khi thực thi.
 *
 * @see UndoableBehavior
 */
public interface Undoable extends Executable {
    /** Hoàn tác trạng thái về trước đó. */
    void undo();
}
