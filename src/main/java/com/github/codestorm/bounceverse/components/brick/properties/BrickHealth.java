package com.github.codestorm.bounceverse.components.brick.properties;

import com.github.codestorm.bounceverse.components.base.properties.Health;
import com.github.codestorm.bounceverse.tags.ForBrick;

/**
 *
 *
 * <h1><b>BrickHealth</b></h1>
 *
 * <p>Lớp này đại diện cho thuộc tính HP của Viên gạch.
 */
public final class BrickHealth extends Health implements ForBrick {
    public BrickHealth(int maxHealth) {
        super(maxHealth);
    }

    //    private void updateColor() {
    //        var ratio = getHealth().getValuePercent() / 100;
    //        Color dimmed = baseColor.deriveColor(0, 1, 0.6 + 0.4 * ratio, 1);
    //        view.setFill(dimmed);
    //        view.setStroke(Color.BLACK);
    //    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        // updateColor here
    }
}
