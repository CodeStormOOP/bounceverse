package com.github.codestorm.bounceverse;

import java.io.IOException;
import java.util.Map;

import com.almasb.fxgl.app.GameApplication;
import com.github.codestorm.bounceverse.core.LaunchOptions;
import com.github.codestorm.bounceverse.core.SettingsManager;
import com.github.codestorm.bounceverse.core.systems.GameSystem;
import com.github.codestorm.bounceverse.core.systems.InputSystem;
import com.github.codestorm.bounceverse.core.systems.PhysicSystem;
import com.github.codestorm.bounceverse.core.systems.UISystem;

/**
 *
 *
 * <h1>{@link Bounceverse}</h1>
 *
 * Phần Hệ thống Chương trình chính của game, nơi mà mọi thứ bắt đầu từ
 * {@link #main(String[])}...
 * <br>
 * <i>Game {@link Bounceverse} được lấy cảm hứng từ game Arkanoid nổi tiếng, nơi
 * người chơi điều khiển một thanh để đỡ bóng và phá vỡ các viên gạch. Mục tiêu
 * của game là phá vỡ tất cả các viên gạch và dành được điểm số cao nhất. Nhưng
 * liệu mọi thứ chỉ đơn giản như vậy?</i>
 */
public final class Bounceverse extends GameApplication {

    public static void main(String[] args) {
        LaunchOptions.load(args);
        launch(args);
    }

    @Override
    protected void initSettings(com.almasb.fxgl.app.GameSettings settings) {
        try {
            SettingsManager.load(settings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void initGame() {
        GameSystem.getInstance().apply();
    }

    @Override
    protected void initGameVars(Map<String, Object> m) {
        m.put("lives", 3);
        m.put("score", 0);
    }

    @Override
    protected void initInput() {
        InputSystem.getInstance().apply();
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
