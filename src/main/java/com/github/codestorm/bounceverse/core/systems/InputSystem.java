package com.github.codestorm.bounceverse.core.systems;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.UserAction;
import com.github.codestorm.bounceverse.components.properties.Velocity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import javafx.scene.input.KeyCode;

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
                            protected void onActionBegin() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(e -> e.getComponent(Velocity.class).left());
                            }

                            @Override
                            protected void onActionEnd() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(e -> e.getComponent(Velocity.class).stop());
                            }
                        },
                        KeyCode.LEFT);

        FXGL.getInput()
                .addAction(
                        new UserAction("Move Right") {
                            @Override
                            protected void onActionBegin() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(e -> e.getComponent(Velocity.class).right());
                            }

                            @Override
                            protected void onActionEnd() {
                                FXGL.getGameWorld()
                                        .getEntitiesByType(EntityType.PADDLE)
                                        .forEach(e -> e.getComponent(Velocity.class).stop());
                            }
                        },
                        KeyCode.RIGHT);
    }

    /**
     * Lazy-loaded singleton holder.
     *
     * <p>Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final InputSystem INSTANCE = new InputSystem();
    }
}
