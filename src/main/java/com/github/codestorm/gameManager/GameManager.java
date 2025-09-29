package com.github.codestorm.gameManager;

import java.util.List;

import com.github.codestorm.brick.Brick;
import com.github.codestorm.paddle.Paddle;

public class GameManager {
    private Paddle paddle;
    private Ball ball;
    private List<Brick> bricks;
    private List<PowerUp> powerups;
    private int score;
    private int lives;
    private int level;

    public void updateScore() {
        for (Brick x : bricks) {
            if (x.isDestroyed()) {
                score += x.getScore();
            }
        }
    }
}
