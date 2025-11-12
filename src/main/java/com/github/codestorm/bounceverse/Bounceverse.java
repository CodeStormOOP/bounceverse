package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.systems.init.*;
import com.github.codestorm.bounceverse.systems.manager.settings.GameSettingsManager;
import com.github.codestorm.bounceverse.systems.manager.settings.LaunchOptionsManager;
import com.github.codestorm.bounceverse.systems.manager.settings.UserSettingsManager;
import com.github.codestorm.bounceverse.typing.exceptions.BounceverseException;
import java.io.IOException;
import java.util.Map;

/**
 * <h1>{@link Bounceverse}</h1>
 * Game chính — quản lý vòng đời khởi tạo và vòng lặp của trò chơi.
 */
public final class Bounceverse extends GameApplication {

    public static void main(String[] args) {
        LaunchOptionsManager.getInstance().load(args);
        launch(args);
    }

    @Override
    protected void initSettings(GameSettings settings) {
        UserSettingsManager.getInstance().load();
        try {
            GameSettingsManager.load(settings);
        } catch (IOException e) {
            throw new BounceverseException(e);
        }
    }

    @Override
    protected void initInput() {
        InputSystem.getInstance().apply();
    }

    @Override
    protected void onPreInit() {
        AppEventSystem.getInstance().apply();
    }

    @Override
    protected void initGameVars(Map<String, Object> vars) {
        GameSystem.Variables.loadDefault(vars);
    }

    @Override
    protected void initGame() {
        GameSystem.getInstance().apply();
    }

    @Override
    protected void initPhysics() {
        PhysicSystem.getInstance().apply();
    }

    @Override
    protected void initUI() {
        FXGL.getGameScene().setBackgroundColor(javafx.scene.paint.Color.web("#0d0b1a"));
        UISystem.getInstance().apply();

        FXGL.getGameScene().getRoot().getStylesheets().add("assets/ui/powerup.css");
    }

    @Override
    protected void onUpdate(double tpf) {
        PowerUpManager.getInstance().onUpdate(tpf);
    }
}
