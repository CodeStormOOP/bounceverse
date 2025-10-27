package com.github.codestorm.bounceverse.components.behaviors;

import com.github.codestorm.bounceverse.Utils.Time.Cooldown;
import com.github.codestorm.bounceverse.data.contracts.CanExecute;
import java.util.List;
import javafx.util.Duration;

/**
 *
 *
 * <h1>{@link CooldownBehavior}</h1>
 *
 * {@link Behavior} sau khi thực thi sẽ mất thời gian để có thể {@link #executeLogic(List)} lại.
 */
public abstract class CooldownBehavior extends Behavior implements CanExecute {
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
        if (!cooldown.getCurrent().expired()) {
            return;
        }
        if (executeLogic(data)) {
            cooldown.getCurrent().makeNew();
        }
    }

    protected CooldownBehavior() {}

    protected CooldownBehavior(Duration duration) {
        cooldown.setDuration(duration);
    }
}
