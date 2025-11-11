package com.github.codestorm.bounceverse.data.contracts;

/**
 *
 *
 * <h1>{@link CanUndo}</h1>
 *
 * Có thể hoàn tác trạng thái về lúc trước khi thực thi. Chỉ hỗ trợ thực thi 1 lần.
 *
 * @see UndoableBehavior
 */
public interface CanUndo extends CanExecute {
    /** Hoàn tác trạng thái về trước đó. */
    void undo();
}
