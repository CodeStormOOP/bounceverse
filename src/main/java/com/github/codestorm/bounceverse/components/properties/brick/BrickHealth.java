package com.github.codestorm.bounceverse.components.properties.brick;

import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.components.properties.Health;
import com.github.codestorm.bounceverse.data.tags.entities.ForBrick;
import com.github.codestorm.bounceverse.data.tags.requirements.RequiredTag;

/**
 *
 *
 * <h1>{@link BrickHealth}</h1>
 *
 * <p>Lớp này đại diện cho thuộc tính HP của Viên gạch.
 */
@CoreComponent
public final class BrickHealth extends Health implements ForBrick, RequiredTag {
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
