package com.github.codestorm.brick;

public class PowerBrick extends Brick {
    public PowerBrick (int x, int y, int width, int height, int hp){
        super(x, y, width, height, hp);
    }

    @Override
    public void hit(){
        super.hit();
        if(isDestroyed()){
            activePower();
        }
    }

    public void activePower() {};
}
