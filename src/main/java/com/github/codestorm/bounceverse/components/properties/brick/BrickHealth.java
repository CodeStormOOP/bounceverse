package com.github.codestorm.bounceverse.components.properties.brick;

<<<<<<< HEAD:src/main/java/com/github/codestorm/bounceverse/components/brick/properties/BrickHealth.java
import com.almasb.fxgl.dsl.components.HealthIntComponent;
import com.github.codestorm.bounceverse.components.base.properties.Health;
import com.github.codestorm.bounceverse.tags.ForBrick;
=======
import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.components.properties.Health;
import com.github.codestorm.bounceverse.data.tags.entities.ForBrick;
import com.github.codestorm.bounceverse.data.tags.requirements.RequiredTag;
>>>>>>> 7b79420144f692b2327565779d6d4140aff0f34f:src/main/java/com/github/codestorm/bounceverse/components/properties/brick/BrickHealth.java

/**
 *
 *
 * <h1>{@link BrickHealth}</h1>
 *
 * <p>
 * Lớp này đại diện cho thuộc tính HP của Viên gạch.
 */
@CoreComponent
public final class BrickHealth extends Health implements ForBrick, RequiredTag {
    public BrickHealth(int maxHealth) {
        super(maxHealth);
    }

    @Override
    public void onUpdate(double tpf) {
        super.onUpdate(tpf);
        // updateColor here
    }

    public void damage(int amount) {
        if (amount <= 0)
            return;
        HealthIntComponent h = getHealth();

        if (h.isZero())
            return;

        h.damage(amount);
    }

    public boolean isDead() {
        return getHealth().isZero();
    }
}
