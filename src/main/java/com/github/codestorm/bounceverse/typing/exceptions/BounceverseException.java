package com.github.codestorm.bounceverse.typing.exceptions;

import com.github.codestorm.bounceverse.Bounceverse;

/**
 *
 *
 * <h1>!{@link BounceverseException}</h1>
 *
 * Những lỗi xảy ra trong game {@link Bounceverse}.
 */
public class BounceverseException extends RuntimeException {
    public BounceverseException(String message, String... extras) {
        super(
                message
                        + ((extras.length > 0)
                                ? String.format(" (%s)", String.join(" ", extras))
                                : ""));
    }

    public BounceverseException(Exception e) {
        super(e);
    }
}
