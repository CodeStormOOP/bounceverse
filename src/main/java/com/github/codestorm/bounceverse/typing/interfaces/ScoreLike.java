package com.github.codestorm.bounceverse.typing.interfaces;

import java.io.Serializable;
import java.time.Instant;

/**
 *
 *
 * <h1>%{@link ScoreLike}</h1>
 *
 * Giống Điểm số trong trò chơi.
 *
 * @param <T> Loại Điểm số
 */
public interface ScoreLike<T extends ScoreLike<T>> extends Serializable, Comparable<T> {
    int score();

    Instant timestamp();

    @Override
    default int compareTo(T o) {
        if (score() != o.score()) {
            return Integer.compare(score(), o.score());
        }
        return -timestamp().compareTo(o.timestamp());
    }
}
