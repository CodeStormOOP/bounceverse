package com.github.codestorm.bounceverse.components.behaviors;

import com.github.codestorm.bounceverse.typing.interfaces.CanExecute;
import com.github.codestorm.bounceverse.typing.structures.Cooldown;

import javafx.util.Duration;

import java.util.List;

/**
 *
 *
 * <h1>{@link CooldownBehavior}</h1>
 *
 * {@link Behavior} sau khi thực thi sẽ mất thời gian để có thể {@link #executeLogic(List)} lại.
 */
public abstract class CooldownBehavior extends Behavior {
    private final Cooldown cooldown = new Cooldown();

    public Cooldown getCooldown() {
        return cooldown;
    }

    /**
     * Logic bên trong {@link CanExecute#execute(List)}.
     *
     * @param data Dữ liệu truyền vào
     * @return {@code true} nếu cho phép thực thi, ngược lại {@code false}
     */
    protected abstract boolean executeLogic(List<Object> data);

    @Override
    public final void execute(List<Object> data) {
        if (!cooldown.getCurrent().isExpired()) {
            return;
        }
        if (executeLogic(data)) {
            cooldown.getCurrent().createNew();
        }
    }

    protected CooldownBehavior() {}

    protected CooldownBehavior(Duration duration) {
        cooldown.setDuration(duration);
    }
}
