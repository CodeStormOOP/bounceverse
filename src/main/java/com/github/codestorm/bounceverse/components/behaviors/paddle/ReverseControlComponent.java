package com.github.codestorm.bounceverse.components.behaviors.paddle;

import com.almasb.fxgl.entity.component.Component;

/**
 * Component đảo ngược điều khiển của paddle trong thời gian ngắn.
 */
public final class ReverseControlComponent extends Component {

    /** Nếu true thì đảo ngược hướng điều khiển. */
    private boolean reversed = true;

    public boolean isReversed() {
        return reversed;
    }

    public void setReversed(boolean reversed) {
        this.reversed = reversed;
    }
}
