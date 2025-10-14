package com.github.codestorm.bounceverse.factory;

import com.almasb.fxgl.app.scene.FXGLMenu;
import com.github.codestorm.bounceverse.scenes.MainMenu;
import org.jetbrains.annotations.NotNull;

public class SceneFactory extends com.almasb.fxgl.app.scene.SceneFactory {
    @NotNull @Override
    public FXGLMenu newMainMenu() {
        return new MainMenu();
    }
}
