package com.github.codestorm.bounceverse.systems.manager.metrics;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.typing.records.BlitzScore;
import com.github.codestorm.bounceverse.typing.records.EndlessScore;
import java.io.*;
import java.util.LinkedHashSet;

/**
 *
 *
 * <h1>{@link LeaderboardManager}</h1>
 *
 * Quản lý Bảng xếp hạng.
 *
 * @apiNote Đây là một Singleton, cần lấy instance thông qua {@link #getInstance()}.
 */
public final class LeaderboardManager extends MetricsManager {
    public static final int MAX_CAPACITY = 10;
    public static final String FILENAME = "leaderboard.dat";
    private static final String BLITZ = "blitz";
    private static final String ENDLESS = "endless";

    public static LeaderboardManager getInstance() {
        return Holder.INSTANCE;
    }

    /** Tải lại thông tin BXH. */
    public void reload() {
        final var file = new File(FILENAME);
        try {
            final var ois = new ObjectInputStream(new FileInputStream(file));
            final var bundle = (Bundle) ois.readObject();

            final LinkedHashSet<EndlessScore> endlessLB = bundle.get(ENDLESS);
            final LinkedHashSet<BlitzScore> blitzLB = bundle.get(BLITZ);

            endlessLeaderboard.clear();
            endlessLeaderboard.addAll(endlessLB);

            blitzLeaderboard.clear();
            blitzLeaderboard.addAll(blitzLB);

            Logger.get(LeaderboardManager.class)
                    .infof("Loaded leaderboard from: %s", file.getAbsolutePath());

        } catch (Exception e) {
            Logger.get(LeaderboardManager.class)
                    .warning("Leaderboard file not found or corrupted. Using new one.", e);
        }
    }

    /** Lưu lại BXH. */
    public void save() {
        final var leaderboard = new Bundle("leaderboard");
        leaderboard.put(BLITZ, blitzLeaderboard);
        leaderboard.put(ENDLESS, endlessLeaderboard);

        try {
            final var file = new File(FILENAME);
            final var oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(leaderboard);
            Logger.get(LeaderboardManager.class)
                    .infof("Saved leaderboard to: %s", file.getAbsolutePath());
            oos.close();
        } catch (Exception e) {
            Logger.get(LeaderboardManager.class).warning("Cannot save leaderboard ", e);
        }
    }

    private LeaderboardManager() {
        reload();
    }

    private final LinkedHashSet<BlitzScore> blitzLeaderboard = new LinkedHashSet<>(MAX_CAPACITY);
    private final LinkedHashSet<EndlessScore> endlessLeaderboard =
            new LinkedHashSet<>(MAX_CAPACITY);

    public LinkedHashSet<BlitzScore> getBlitzLeaderboard() {
        return blitzLeaderboard;
    }

    public LinkedHashSet<EndlessScore> getEndlessLeaderboard() {
        return endlessLeaderboard;
    }

    /**
     * Lazy-loaded singleton holder. <br>
     * Follow <a href= "https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom">
     * Initialization-on-demand holder idiom</a>.
     */
    private static final class Holder {
        static final LeaderboardManager INSTANCE = new LeaderboardManager();
    }
}
