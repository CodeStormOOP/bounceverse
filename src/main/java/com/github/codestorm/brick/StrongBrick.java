package com.github.codestorm.brick;

public class StrongBrick extends Brick {
    private int hp;
    private final int initialHp;

    public StrongBrick(int x, int y, int width, int height, int hp){
        super(x, y, width, height);
        this.hp = hp;
        this.initialHp = hp;
    }

    @Override
    public void hit(){
        if(!destroyed && hp >0){
            hp--;
            if(hp == 0){
                destroyed = true;
            }
            
        }
    }

    @Override
    public int getScore(){
        return initialHp*10;
    }
}
