package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.core.math.Vec2;
import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.Property;
import com.github.codestorm.bounceverse.data.tags.requirements.Optional;
import com.github.codestorm.bounceverse.data.types.UnitVelocity;
import javafx.geometry.Point2D;

/**
 *
 *
 * <h1>{@link Velocity}</h1>
 *
 * Vận tốc hiện có của Entity. Giống như một Wrapper của {@link Vec2} dưới dạng {@link Component}.
 *
 * @see Vec2
 */
public class Velocity extends Component implements Property, Optional {
    private final Vec2 vector;

    public Velocity(Vec2 velocity) {
        this.vector = velocity;
    }

    public Velocity(Point2D velocity) {
        this(new Vec2(velocity));
    }

    public Velocity(double vx, double vy) {
        this(new Vec2(vx, vy));
    }

    /**
     * Dừng lại.
     *
     * @see UnitVelocity#STAND
     */
    public void stop() {
        setVector(UnitVelocity.STAND.getVector());
    }

    @Override
    public void onUpdate(double tpf) {
        if (!vector.equals(UnitVelocity.STAND.getVector())) {
            entity.translate(vector.mul(tpf));
        }
    }

    public Vec2 getVector() {
        return vector;
    }

    public void setVector(Vec2 vector) {
        this.vector.set(vector);
    }
}
