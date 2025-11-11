package com.github.codestorm.bounceverse.typing.records;

import com.github.codestorm.bounceverse.typing.interfaces.ScoreLike;

import java.time.Instant;

/**
 *
 *
 * <h1>${@link EndlessScore}</h1>
 *
 * Biểu diễn điểm số Endless mode.
 *
 * @param name Tên người chơi
 * @param score Điểm số
 * @param level Cấp độ đạt được
 * @param timestamp Thời điểm ghi điểm
 */
public record EndlessScore(String name, long score, int level, Instant timestamp)
        implements ScoreLike<EndlessScore> {}
