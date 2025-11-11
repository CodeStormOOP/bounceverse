package com.github.codestorm.bounceverse.systems.init;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.ball.*;
import com.github.codestorm.bounceverse.components.properties.powerup.types.misc.ShieldPowerUp;
import com.github.codestorm.bounceverse.components.properties.powerup.types.paddle.*;
import com.github.codestorm.bounceverse.data.types.PowerUpType;
import javafx.geometry.Point2D;

import java.util.*;

/**
 * <h1>PowerUpSpawner</h1>
 * Random loại Power-Up theo trọng số.
 */
public final class PowerUpSpawner {

    private static final Logger LOGGER = Logger.get(PowerUpSpawner.class);

    private static final Map<PowerUpType, Double> WEIGHTS = Map.ofEntries(
            Map.entry(PowerUpType.EXPAND_PADDLE, 0.15),
            Map.entry(PowerUpType.SHRINK_PADDLE, 0.10),
            Map.entry(PowerUpType.MULTI_BALL, 0.10),
            Map.entry(PowerUpType.SLOW_BALL, 0.10),
            Map.entry(PowerUpType.FAST_BALL, 0.10),
            Map.entry(PowerUpType.SHIELD, 0.10),
            Map.entry(PowerUpType.GUN, 0.10),
            Map.entry(PowerUpType.REVERSE_PADDLE, 0.15)
    );

    private static final Random RANDOM = new Random();

    private PowerUpSpawner() {
    }

    /** Random theo trọng số */
    public static PowerUpType getRandomPowerUpType() {
        double totalWeight = WEIGHTS.values().stream().mapToDouble(Double::doubleValue).sum();
        double r = RANDOM.nextDouble() * totalWeight;
        double cumulative = 0.0;
        for (var entry : WEIGHTS.entrySet()) {
            cumulative += entry.getValue();
            if (r <= cumulative)
                return entry.getKey();
        }
        LOGGER.warning("PowerUp random fallback triggered.");
        return PowerUpType.EXPAND_PADDLE;
    }

    /** Tạo instance Power-Up */
    public static PowerUp getPowerUpInstance(PowerUpType type) {
        return switch (type) {
            // Paddle
            case EXPAND_PADDLE -> new ExpandPaddlePowerUp();
            case SHRINK_PADDLE -> new ShrinkPaddlePowerUp();
            case GUN -> new GunPowerUp();
            case REVERSE_PADDLE -> new ReversePaddlePowerUp();

            // Ball
            case MULTI_BALL -> new MultipleBallPowerUp();
            case SLOW_BALL -> new SlowBallPowerUp();
            case FAST_BALL -> new FastBallPowerUp();

            // Misc
            case SHIELD -> new ShieldPowerUp();

            // Loại chưa có class — fallback
            default -> {
                LOGGER.warning("⚠️ PowerUp chưa được triển khai: " + type);
                yield new ExpandPaddlePowerUp();
            }
        };
    }

    /** Lấy texture theo loại Power-Up */
    public static Texture getPowerUpTexture(PowerUpType type) {
        String path = switch (type) {
            // 🧱 Paddle
            case EXPAND_PADDLE -> "power/paddle/Expand Paddle.png";
            case SHRINK_PADDLE -> "power/paddle/Shrink Paddle.png";
            case GUN -> "power/paddle/Gun.png";
            case REVERSE_PADDLE -> "power/paddle/Reverse Paddle.png";

            // 🟣 Ball
            case MULTI_BALL -> "power/ball/x2 Ball.png";
            case SLOW_BALL -> "power/ball/Slow Ball.png";
            case FAST_BALL -> "power/ball/Fast Ball.png";

            // 🟢 Misc
            case SHIELD -> "power/misc/shield.png";

            // ⚙️ fallback
            default -> "power/paddle/Expand Paddle.png";
        };
        return FXGL.texture(path);
    }

    /** Random một Power-Up (cả instance + texture) */
    public static Map.Entry<PowerUp, Texture> nextPowerUp() {
        var type = getRandomPowerUpType();
        return Map.entry(getPowerUpInstance(type), getPowerUpTexture(type));
    }

    /** Spawn random Power-Up tại vị trí */
    public static void spawnRandom(Point2D position) {
        var pair = nextPowerUp();
        FXGL.spawn("powerUp",
                new com.almasb.fxgl.entity.SpawnData(position)
                        .put("texture", pair.getValue())
                        .put("contains", pair.getKey()));
    }
}
