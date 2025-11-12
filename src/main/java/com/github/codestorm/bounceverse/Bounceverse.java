package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.github.codestorm.bounceverse.systems.init.AppEventSystem;
import com.github.codestorm.bounceverse.systems.init.GameSystem;
import com.github.codestorm.bounceverse.systems.init.InputSystem;
import com.github.codestorm.bounceverse.systems.init.PhysicSystem;
import com.github.codestorm.bounceverse.systems.init.UISystem;
import com.github.codestorm.bounceverse.systems.manager.settings.GameSettingsManager;
import com.github.codestorm.bounceverse.systems.manager.settings.LaunchOptionsManager;
import com.github.codestorm.bounceverse.systems.manager.settings.UserSettingsManager;
import com.github.codestorm.bounceverse.typing.exceptions.BounceverseException;

import java.io.IOException;
import java.util.Map;

/**
 *
 *
 * <h1>{@link Bounceverse}</h1>
 *
 * Phần Hệ thống Chương trình chính của game, nơi mà mọi thứ bắt đầu từ {@link #main(String[])}...
 * <br>
 * <i>Game {@link Bounceverse} được lấy cảm hứng từ game Arkanoid nổi tiếng, nơi người chơi điều
 * khiển một thanh để đỡ bóng và phá vỡ các viên gạch. Mục tiêu của game là phá vỡ tất cả các viên
 * gạch và dành được điểm số cao nhất. Nhưng liệu mọi thứ chỉ đơn giản như vậy?</i>
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
        UISystem.getInstance().apply();
    }
}
