package com.github.codestorm.bounceverse.components._old.brick;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class BrickComponent extends Component {
    private final int width;
    private final int height;
    private final int initialHp;
    private final Color baseColor;
    private int hp;
    private boolean destroyed;
    private Rectangle view;

    /** Represents the core behavior and appearance of a brick in the game. */
    public BrickComponent(int width, int height, int hp, Color baseColor) {
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.initialHp = hp;
        this.baseColor = baseColor;
        this.destroyed = false;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public boolean isDestroyed() {
        return hp <= 0;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }

    /** Create a rectangular visual for brick. */
    public void onAdded() {
        // Lấy view của Entity
        view = new Rectangle(width, height);
        view.setArcWidth(8);
        view.setArcHeight(8);
        updateColor();
        getEntity().getViewComponent().addChild(view);
    }

    /**
     * Reduces the brick's hit points by one when hit.
     *
     * <p>If hit points reach zero, the brick is marked as destroyed.
     */
    public void hit() {
        if (!destroyed && hp > 0) {
            hp--;
            if (hp == 0) {
                destroyed = true;
                onDestroyed();
            } else {
                updateColor();
            }
        }
    }

    /**
     * Updates the brick’s color based on remaining HP.
     *
     * <p>The color gradually darkens as HP decreases, while keeping the base hue consistent across
     * brick types.
     */
    private void updateColor() {
        float ratio = (float) hp / initialHp;
        Color dimmed = baseColor.deriveColor(0, 1, 0.6 + 0.4 * ratio, 1);
        view.setFill(dimmed);
        view.setStroke(Color.BLACK);
    }

    /**
     * Handles destruction behavior when the brick is fully broken.
     *
     * <p>By default, removes the brick entity from the game world. Subclasses may override this for
     * custom effects (e.g. explosions).
     */
    protected void onDestroyed() {
        getEntity().removeFromWorld();
    }

    // Return the score gained by destroying the brick.
    public int getScore() {
        return initialHp * 10;
    }
}
