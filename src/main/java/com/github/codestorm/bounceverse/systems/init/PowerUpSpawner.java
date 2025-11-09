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
            Map.entry(PowerUpType.LASER, 0.10),
            Map.entry(PowerUpType.MAGNET, 0.10),
            Map.entry(PowerUpType.EXTRA_LIFE, 0.10),
            Map.entry(PowerUpType.SCORE_BOOST, 0.05)
    );

    private static final Random RANDOM = new Random();

    private PowerUpSpawner() {}

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

    public static PowerUp getPowerUpInstance(PowerUpType type) {
        return switch (type) {
            case EXPAND_PADDLE -> new ExpandPaddlePowerUp();
            case SHRINK_PADDLE -> new ShrinkPaddlePowerUp();
            case MULTI_BALL -> new MultipleBallPowerUp();
            case SLOW_BALL -> new SlowBallPowerUp();
            case FAST_BALL -> new FastBallPowerUp();
            case SHIELD -> new ShieldPowerUp();
            default -> throw new IllegalArgumentException("Unexpected value: " + type);
        };
    }

    public static Texture getPowerUpTexture(PowerUpType type) {
        String path = "powerups/" + type.name().toLowerCase() + ".png";
        return FXGL.texture(path);
    }

    public static Map.Entry<PowerUp, Texture> nextPowerUp() {
        var type = getRandomPowerUpType();
        return Map.entry(getPowerUpInstance(type), getPowerUpTexture(type));
    }

    public static void spawnRandom(Point2D position) {
        var pair = nextPowerUp();
        var powerUp = pair.getKey();
        var texture = pair.getValue();

        FXGL.spawn("powerUp",
                new com.almasb.fxgl.entity.SpawnData(position)
                        .put("texture", texture)
                        .put("contains", powerUp));
    }
}
