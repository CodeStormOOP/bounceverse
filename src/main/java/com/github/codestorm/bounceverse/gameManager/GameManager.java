package com.github.codestorm.bounceverse.gameManager;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.github.codestorm.bounceverse.ball.Ball;
import com.github.codestorm.bounceverse.brick.BrickComponent;
import com.github.codestorm.bounceverse.paddle.PaddleComponent;
import com.github.codestorm.bounceverse.powerup.PowerUp;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the main game logic of BounceVerse, including the paddle, ball,
 * bricks, power-ups, score,
 * and level state.
 */
public class GameManager {
    private PaddleComponent paddle;
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
            Entity brick = FXGL.spawn("normalBrick", startX + i * offsetX, startY);
            bricks.add(brick);
        }

        // Row 2: Strong Bricks
        for (int i = 0; i < 8; i++) {
            Entity brick = FXGL.spawn("strongBrick", startX + i * offsetX, startY + offsetY);
            bricks.add(brick);
        }

        // Row 3: Explode Bricks
        for (int i = 0; i < 8; i++) {
            Entity brick = FXGL.spawn("explodeBrick", startX + i * offsetX, startY + offsetY * 2);
            bricks.add(brick);
        }

        // Row 4: Protected Bricks
        String[] shields = { "top", "left", "right", "bottom" };
        for (int i = 0; i < 8; i++) {
            String shieldSide = shields[i % shields.length];
            Entity brick = FXGL.spawn("protectedBrick",
                    new SpawnData(startX + i * offsetX, startY + offsetY * 3)
                            .put("shieldSide", shieldSide));
            bricks.add(brick);
        }
    }

    /**
     * Removes all existing bricks from the game world and clears the internal brick
     * list.
     */
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
            if (!brickEntity.hasComponent(BrickComponent.class))
                continue;

            BrickComponent brick = brickEntity.getComponent(BrickComponent.class);
            if (brick.isDestroyed()) {
                score += brick.getScore();
                brickEntity.removeFromWorld();
            }
        }
    }
}
