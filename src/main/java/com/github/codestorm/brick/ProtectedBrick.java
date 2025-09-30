package com.github.codestorm.brick;

public class ProtectedBrick extends Brick {
    private String shieldSide;

    public ProtectedBrick(int x, int y, int width, int height, int hp, String shieldSide) {
        super(x, y, width, height, hp);
        this.shieldSide = shieldSide;
    }

    public String getShieldSide(){
        return shieldSide;
    }

    public void setShieldSide(String shieldSide){
        this.shieldSide = shieldSide;
    }

    public void hit(String direction) {
        if (!isDestroyed()) {
            if (!direction.equalsIgnoreCase(shieldSide)) {
                super.hit();
            }
        }
    }

}
