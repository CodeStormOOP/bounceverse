package com.github.codestorm.bounceverse.typing.records;

import com.github.codestorm.bounceverse.typing.interfaces.ScoreLike;

import java.time.Instant;

/**
 *
 *
 * <h1>${@link BlitzScore}</h1>
 *
 * Biểu diễn điểm số Blitz mode.
 *
 * @param name Tên người chơi
 * @param score Số điểm
 * @param timestamp Thời điểm ghi điểm
 */
public record BlitzScore(String name, long score, Instant timestamp)
        implements ScoreLike<BlitzScore> {}
