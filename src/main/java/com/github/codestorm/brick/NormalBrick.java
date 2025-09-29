package com.github.codestorm.brick;

public class NormalBrick extends Brick {

    public NormalBrick(int x, int y, int width, int height){
        super(x, y, width, height);
    }
    
    @Override
    public void hit(){
        this.destroyed = true;
    }

    @Override
    public int getScore(){
        return 10;
    }


}
