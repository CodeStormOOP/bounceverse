package com.github.codestorm.bounceverse.paddle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.TransformComponent;
import com.github.codestorm.bounceverse.gameManager.BounceverseType;

/**
 * A special type of {@link Paddle} that can shoot bullets upward to destroy
 * bricks. This is usually
 * activated by a power-up.
 */
public class LaserPaddle extends PaddleComponent {
    /** List of bullets currently fired by the paddle. */
    private final List<Entity> bullets = new ArrayList<>();

    // constructor.
    public LaserPaddle(double speed) {
        super(speed);
    }

    // Shoots bullet from paddle.
    public void shoot() {
        TransformComponent t = entity.getTransformComponent();
        double paddleX = t.getX();
        double paddleY = t.getY();
        double width = entity.getWidth();

        // Create bullet gun on the left paddle.
        double bulletLeftX = paddleX + 10;
        double bulletLeftY = paddleY - 10;

        Entity bulletLeft = FXGL.entityBuilder()
                .at(bulletLeftX, bulletLeftY)
                .type(BounceverseType.BULLET)
                .viewWithBBox("")
                .with(new BulletComponent(500))
                .buildAndAttach();

        // Create bullet gun on the right paddle.
        double bulletRightX = paddleX + width - 18;
        double bulletRightY = paddleY - 10;

        Entity bulletRight = FXGL.entityBuilder()
                .at(bulletRightX, bulletRightY)
                .type(BounceverseType.BULLET)
                .viewWithBBox("")
                .with(new BulletComponent(500))
                .buildAndAttach();

        bullets.add(bulletLeft);
        bullets.add(bulletRight);
    }

    /** Updates all bullets when them get off-screen or hit the brick. */
    public void updateBullets() {
        Iterator<Entity> iterator = bullets.iterator();
        while (iterator.hasNext()) {
            Entity bullet = iterator.next();
            if (bullet.getY() < -10) {
                bullet.removeFromWorld();
                iterator.remove();
            }
        }
    }

    public List<Entity> getBullets() {
        return bullets;
    }
}
