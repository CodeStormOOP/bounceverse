package com.github.codestorm.brick;

public class ExoplodeBrick extends Brick {
    private static final int radius = 1;

    public ExoplodeBrick(int x, int y, int width, int height, int hp, int radius) {
        super(x, y, width, height, hp);
    }

    public int getRadius(){
        return radius;
    }

    @Override
    public void hit() {
        super.hit();
        if(isDestroyed()){
            explode();
        }
    }

    private void explode() {}
}
