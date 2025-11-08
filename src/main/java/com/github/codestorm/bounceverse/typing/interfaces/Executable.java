package com.github.codestorm.bounceverse.typing.interfaces;

import java.util.List;

/**
 *
 *
 * <h1>%{@link Executable}</h1>
 *
 * Có thể thực thi hành động nào đó.
 */
public interface Executable {
    /**
     * Thực thi hành động.
     *
     * @param data Dữ liệu truyền vào
     */
    void execute(List<Object> data);
}
