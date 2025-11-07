package com.github.codestorm.bounceverse.typing.interfaces;

import java.io.Serializable;
import java.time.Instant;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1>%{@link Score}</h1>
 *
 * Điểm số trong trò chơi.
 *
 * @param <T> Loại Điểm số
 */
public interface Score<T extends Score<T>> extends Serializable, Comparable<T> {
    long score();

    Instant timestamp();

    @Override
    default int compareTo(@NotNull T o) {
        if (score() != o.score()) {
            return Long.compare(score(), o.score());
        }
        return timestamp().compareTo(o.timestamp());
    }
}
