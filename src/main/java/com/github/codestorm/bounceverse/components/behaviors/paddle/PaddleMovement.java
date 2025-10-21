package com.github.codestorm.bounceverse.components.behaviors.paddle;

import com.almasb.fxgl.entity.component.Required;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.components.behaviors.Behavior;
import com.github.codestorm.bounceverse.data.types.DirectionUnit;
import java.util.List;

@Required(PhysicsComponent.class)
public class PaddleMovement extends Behavior {
    public static final double NORMAL_SPEED = 100;
    public static final double DASH_SPEED = NORMAL_SPEED * 2;
    private double speed = NORMAL_SPEED;

    @Override
    public void execute(List<Object> data) {
        final var physics = entity.getComponent(PhysicsComponent.class);
        final var standVector = DirectionUnit.STAND.getVector();
        for (var obj : data) {
            final var direction = (DirectionUnit) obj;
            switch (direction) {
                case LEFT ->
                        physics.setLinearVelocity(
                                physics.getVelocityX() - speed, physics.getVelocityY());
                case RIGHT ->
                        physics.setLinearVelocity(
                                physics.getVelocityX() + speed, physics.getVelocityY());
                case STAND -> physics.setLinearVelocity(standVector.x, standVector.y);
            }
        }
    }
}
