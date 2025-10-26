package com.github.codestorm.bounceverse;

import com.almasb.fxgl.app.GameApplication;
import com.github.codestorm.bounceverse.core.*;
import com.github.codestorm.bounceverse.core.systems.*;
import com.github.codestorm.bounceverse.typing.exceptions.BounceverseException;
import java.io.IOException;

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
        LaunchOptions.load(args);
        launch(args);
    }

    @Override
    protected void initSettings(com.almasb.fxgl.app.GameSettings settings) {
        try {
            SettingsManager.load(settings);
        } catch (IOException e) {
            throw new BounceverseException(e);
        }
    }

    @Override
    protected void initGame() {
        GameSystem.getInstance().apply();
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
