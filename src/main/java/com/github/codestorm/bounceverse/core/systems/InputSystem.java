package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.github.codestorm.bounceverse.data.types.DirectionUnit;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.input.KeyCode;

/**
 *
 *
 * <h1>{@link InputSystem}</h1>
 *
 * {@link System} quản lý Input trong game. <br>
 * <i>Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}</i>.
 */
public class InputSystem extends System {
    public static InputSystem getInstance() {
        return InputSystem.Holder.INSTANCE;
    }

    @Override
    public void apply() {
        FXGL.getInput()
                .addAction(
                        new UserAction("Move Left") {
                            @Override
                            protected void onAction() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(
                                                e ->
                                                        e.translate(
                                                                DirectionUnit.LEFT
                                                                        .getVector()
                                                                        .mul(10)));
                            }
                        },
                        KeyCode.LEFT);

        FXGL.getInput()
                .addAction(
                        new UserAction("Move Right") {
                            @Override
                            protected void onAction() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(
                                                e ->
                                                        e.translate(
                                                                DirectionUnit.RIGHT
                                                                        .getVector()
                                                                        .mul(10)));
                            }
                        },
                        KeyCode.RIGHT);
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}
