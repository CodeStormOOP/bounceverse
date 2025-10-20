package com.github.codestorm.bounceverse.data.contracts;

/**
 *
 *
 * <h1>{@link CanUndo}</h1>
 *
 * Có thể quay lại trạng thái lúc trước khi thực thi.
 */
public interface CanUndo {
    void apply();

    void undo();
}
