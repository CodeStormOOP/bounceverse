package com.github.codestorm.bounceverse.core.systems;

import com.github.codestorm.bounceverse.data.types.PowerUpType;
import java.util.*;

/**
 * <h1>PowerUpSpawner</h1>
 * Random loại Power-Up theo trọng số.
 */
public final class PowerUpSpawner {

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
            Map.entry(PowerUpType.SCORE_BOOST, 0.05));

    private static final Random RANDOM = new Random();

    private PowerUpSpawner() {
    }

    /** Random loại Power-Up theo trọng số, công bằng và ổn định. */
    public static PowerUpType getRandomPowerUpType() {
        double totalWeight = WEIGHTS.values().stream().mapToDouble(Double::doubleValue).sum();
        double r = RANDOM.nextDouble() * totalWeight;
        double cumulative = 0.0;

        for (var entry : WEIGHTS.entrySet()) {
            cumulative += entry.getValue();
            if (r <= cumulative)
                return entry.getKey();
        }
        return PowerUpType.EXPAND_PADDLE;
    }
}
