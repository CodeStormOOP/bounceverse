package com.github.codestorm.bounceverse.typing.interfaces;

import java.util.List;

/**
 *
 *
 * <h1>%{@link CanExecute}</h1>
 *
 * Có thể thực thi hành động nào đó.
 */
public interface CanExecute {
    /**
     * Thực thi hành động.
     *
     * @param data Dữ liệu truyền vào
     */
    void execute(List<Object> data);
}
