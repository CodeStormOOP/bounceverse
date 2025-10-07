package com.github.codestorm.bounceverse.brick;

import com.almasb.fxgl.entity.component.Component;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the core behavior and appearance of a brick in the game.
 * <p>
 * This component handles HP, color updates, and destruction logic.
 */
public class BrickComponent extends Component {
    private final int width;
    private final int height;
    private final int initialHp;
    private final Color baseColor;
    private int hp;
    private boolean destroyed;
    private Rectangle view;

    public BrickComponent(int width, int height, int hp, Color baseColor) {
        this.width = width;
        this.height = height;
        this.hp = hp;
        this.initialHp = hp;
        this.baseColor = baseColor;
        this.destroyed = false;
    }

    @Override
    public void onAdded() {
        // Create visual representation when added to the entity
        view = new Rectangle(width, height);
        view.setArcWidth(8);
        view.setArcHeight(8);
        // view.setStrokeWidth(1.5);
        updateColor();
        getEntity().getViewComponent().addChild(view);
    }

    /**
     * Reduces the brick's HP by one when hit.
     * If HP reaches zero, the brick is removed from the world.
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
     * Updates the brick color based on remaining HP.
     * The color becomes darker as HP decreases.
     */
    private void updateColor() {
        if (view == null)
            return;
        float ratio = (float) hp / initialHp;
        Color dimmed = baseColor.deriveColor(0, 1, 0.6 + 0.4 * ratio, 1);
        view.setFill(dimmed);
        view.setStroke(Color.BLACK);
    }

    /**
     * Handles destruction when the brick is fully broken.
     * Removes the brick entity from the game world.
     */
    protected void onDestroyed() {
        getEntity().removeFromWorld();
    }

    /** Returns score gained by destroying the brick. */
    public int getScore() {
        return initialHp * 10;
    }

    public int getHp() {
        return hp;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
