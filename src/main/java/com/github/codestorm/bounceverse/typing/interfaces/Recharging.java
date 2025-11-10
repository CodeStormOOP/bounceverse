package com.github.codestorm.bounceverse.typing.interfaces;

import com.github.codestorm.bounceverse.typing.structures.Cooldown;

import java.util.List;

/**
 *
 *
 * <h1>%{@link Recharging}</h1>
 *
 * {@link Executable} nhưng bị giới hạn hồi chiêu ({@link Cooldown})
 *
 * @see Cooldown
 */
public interface Recharging extends Executable {
    Cooldown getCooldown();

    /**
     * Thực thi Logic bên trong wrapper {@link Executable#execute(List)}.
     *
     * @param data Dữ liệu truyền vào
     * @return {@code true} nếu cho phép thực thi, ngược lại {@code false}
     */
    boolean executeLogic(List<Object> data);

    /**
     * @deprecated Viết logic lên {@link #executeLogic(List)} thay vì method này.
     * @param data Dữ liệu truyền vào
     */
    @Deprecated
    @Override
    default void execute(List<Object> data) {
        if (!getCooldown().getCurrent().isExpired()) {
            return;
        }
        if (executeLogic(data)) {
            getCooldown().getCurrent().createNew();
        }
    }
}
