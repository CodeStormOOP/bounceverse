package com.github.codestorm.bounceverse.core;

public final class GameVars {
    private static int lives = 3;
    private static int score = 0;

    private GameVars() {}

    public static void init(int initLives, int initScore) {
        lives = initLives;
        score = initScore;
    }

    public static int getLives() {
        return lives;
    }

    public static int decrementLives() {
        return --lives;
    }

    public static int getScore() {
        return score;
    }

    public static void addScore(int delta) {
        score += delta;
    }

    public static void setLives(int l) {
        lives = l;
    }

    public static void setScore(int s) {
        score = s;
    }
}
