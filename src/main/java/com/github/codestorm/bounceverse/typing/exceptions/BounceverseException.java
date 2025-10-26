package com.github.codestorm.bounceverse.typing.exceptions;

import com.github.codestorm.bounceverse.Bounceverse;

/**
 *
 *
 * <h1>!{@link BounceverseException}</h1>
 *
 * Những lỗi xảy ra trong game {@link Bounceverse}.
 */
public abstract class BounceverseException extends RuntimeException {
    public BounceverseException(String message) {
        super(message);
    }
}
