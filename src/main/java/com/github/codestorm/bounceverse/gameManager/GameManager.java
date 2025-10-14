package com.github.codestorm.bounceverse.gameManager;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.ball.Ball;
import com.github.codestorm.bounceverse.components._old.brick.BrickComponent;
import com.github.codestorm.bounceverse.components._old.brick.BrickFactory;
import com.github.codestorm.bounceverse.paddle.Paddle;
import com.github.codestorm.bounceverse.powerup.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the main game logic of BounceVerse, including the paddle, ball, bricks, power-ups, score,
 * and level state.
 */
public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Entity> bricks = new ArrayList<>();
    private List<PowerUp> powerups;
    private int score;
    private int lives;
    private int level;

    /** Spawns a predefined pattern of brick rows into the game world. */
    public void spawnBricks() {
        clearBricks();

        double startX = 100;
        double startY = 100;
        double offsetX = 85;
        double offsetY = 40;

        // Row 1: Normal Bricks
        for (int i = 0; i < 8; i++) {
            Entity brick = BrickFactory.newBrick(startX + i * offsetX, startY);
            FXGL.getGameWorld().addEntity(brick);
            bricks.add(brick);
        }

        // Row 2: Strong Brick
        for (int i = 0; i < 8; i++) {
            Entity brick = BrickFactory.newStrongBrick(startX + i * offsetX, startY + offsetY);
            FXGL.getGameWorld().addEntity(brick);
            bricks.add(brick);
        }

        // Row 3: Explode Brick
        for (int i = 0; i < 8; i++) {
            Entity brick = BrickFactory.newExplodeBrick(startX + i * offsetX, startY + offsetY * 2);
            FXGL.getGameWorld().addEntity(brick);
            bricks.add(brick);
        }

        // Row 4: Protected Bricks (with directional shields)
        String[] shields = {"top", "left", "right", "bottom"};
        for (int i = 0; i < 8; i++) {
            String shieldSide = shields[i % shields.length];
            Entity brick =
                    BrickFactory.newProtectedBrick(
                            startX + i * offsetX, startY + offsetY * 3, shieldSide);
            FXGL.getGameWorld().addEntity(brick);
            bricks.add(brick);
        }
    }

    /** Removes all existing bricks from the game world and clears the internal brick list. */
    public void clearBricks() {
        bricks.forEach(Entity::removeFromWorld);
        bricks.clear();
    }

    public List<Entity> getBricks() {
        return bricks;
    }

    /** Update the player's score based on destroyed bricks. */
    public void updateScore() {
        for (Entity brickEntity : bricks) {
            if (!brickEntity.hasComponent(BrickComponent.class)) continue;

            BrickComponent brick = brickEntity.getComponent(BrickComponent.class);
            if (brick.isDestroyed()) {
                score += brick.getScore();
                brickEntity.removeFromWorld();
            }
        }
    }
}
