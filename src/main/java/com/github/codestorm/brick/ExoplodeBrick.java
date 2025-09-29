package com.github.codestorm.brick;

public class ExoplodeBrick extends Brick {
    private int radius = 1;

    public ExoplodeBrick(int x, int y, int width, int height, int radius){
        super(x, y, width, height);
        this.radius = radius;
    }

    @Override
    public void hit(){
        this.destroyed = true;
        explode();
    }

    private void explode{};
}
