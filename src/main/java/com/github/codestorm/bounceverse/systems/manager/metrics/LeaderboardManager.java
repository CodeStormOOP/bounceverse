package com.github.codestorm.bounceverse.systems.manager.metrics;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.logging.Logger;
import com.github.codestorm.bounceverse.typing.records.EndlessScore;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.MinMaxPriorityQueue;

import org.jspecify.annotations.NonNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;

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
    public static final int MAX_SIZE = 10;
    public static final String FILENAME = "leaderboard.dat";
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

            final ArrayList<@NonNull EndlessScore> endlessLB = bundle.get(ENDLESS);

            endlessLeaderboard.clear();
            endlessLeaderboard.addAll(endlessLB);

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
        leaderboard.put(ENDLESS, new ArrayList<>(endlessLeaderboard));

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

    private final MinMaxPriorityQueue<@NonNull EndlessScore> endlessLeaderboard =
            MinMaxPriorityQueue.<EndlessScore>orderedBy(Comparator.reverseOrder())
                    .maximumSize(MAX_SIZE)
                    .create();

    /**
     * Kiểm tra xem điểm số có nằm trong top leaderboard không.
     *
     * @param score Điểm số cần kiểm tra
     * @return true nếu điểm số đủ để vào leaderboard
     */
    public boolean isTopScore(int score) {
        if (endlessLeaderboard.size() < LeaderboardManager.MAX_SIZE) {
            return true;
        }
        final var lowestScore = endlessLeaderboard.peek();
        assert lowestScore != null;
        return score > lowestScore.score();
    }

    /**
     * Thêm một điểm số mới vào leaderboard nếu đủ điều kiện.
     *
     * @param newScore Điểm số mới
     */
    public void addScore(EndlessScore newScore) {
        if (!isTopScore(newScore.score())) {
            return;
        }
        endlessLeaderboard.add(newScore);
        save();
    }

    /**
     * Lấy danh sách leaderboard hiện tại dưới dạng {@link ImmutableList}.
     *
     * @return Danh sách leaderboard
     */
    public ImmutableList<@NonNull EndlessScore> getViewLeaderboard() {
        return ImmutableList.copyOf(endlessLeaderboard);
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
