package com.github.codestorm.bounceverse.components.properties.paddle;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.FastBallPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.MultipleBallPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.misc.ExtraLifePowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.misc.ShieldPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.DuplicatePaddlePowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.GunPowerUp;
import com.github.codestorm.bounceverse.typing.structures.Cooldown;
import javafx.util.Duration;

public class PaddlePowerComponent extends Component {

    private final Cooldown powerCooldown = new Cooldown(Duration.seconds(20));
    private static final Duration POWER_VISUAL_DURATION = Duration.seconds(1.0);

    /**
     * Phương thức chính để kích hoạt sức mạnh đặc biệt của paddle.
     */
    public void activatePower() {
        if (!powerCooldown.getCurrent().isExpired()) {
            System.out.println("Power is on cooldown!");
            return;
        }

        var paddle = getEntity();
        String color = FXGL.gets("paddleColor");
        var viewManager = paddle.getComponent(PaddleViewManager.class);

        System.out.println("Activating power for " + color + " paddle!");

        switch (color) {
            case "red":
                if (!paddle.hasComponent(GunPowerUp.class)) {
                    paddle.addComponent(new GunPowerUp());
                }
                break;
            case "green":
                new ExtraLifePowerUp().apply(paddle);
                break;
            case "blue":
                new ShieldPowerUp().apply(paddle);
                break;
            case "orange":
                new MultipleBallPowerUp().apply(paddle);
                break;
            case "pink":
                new FastBallPowerUp().apply(paddle);
                break;
            case "yellow":
                new DuplicatePaddlePowerUp().apply(paddle);
                break;
            default:
                return;
        }

        viewManager.setPowerState();
        FXGL.getGameTimer().runOnceAfter(viewManager::setNormalState, POWER_VISUAL_DURATION);

        powerCooldown.getCurrent().createNew();
    }
}