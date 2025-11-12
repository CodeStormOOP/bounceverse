package com.github.codestorm.bounceverse.ui.elements;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.texture.Texture;
import com.github.codestorm.bounceverse.AssetsPath;

import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.scene.Node;
import javafx.scene.layout.HBox;

/**
 *
 *
 * <h1>{@link HorizontalPositiveInteger}</h1>
 *
 * Hiển thị {@link Integer} dương theo chiều ngang, sử dụng các {@link Texture} chữ số. <br>
 */
public class HorizontalPositiveInteger extends ViewElement {
    private static final int DEFAULT_MIN_DIGITS = 0;
    private static final double DEFAULT_SPACING = 2;
    private static final double DEFAULT_DIGIT_WIDTH = 16;
    private static final double DEFAULT_DIGIT_HEIGHT = 24;

    private final ChangeListener<Number> listener = (obs, oldVal, newVal) -> update();
    private final HBox container = new HBox(DEFAULT_SPACING);
    private int minDigits = DEFAULT_MIN_DIGITS;
    private ReadOnlyIntegerProperty value;
    private String lastDisplayedValue = "";

    public HorizontalPositiveInteger(ReadOnlyIntegerProperty value) {
        setValue(value);
    }

    @Override
    public Node getView() {
        return container;
    }

    @Override
    public void update() {
        final var score = value.get();
        final var positiveScore = Math.max(0, score);
        final var actualDigits = String.valueOf(positiveScore).length();
        final var digitsAmount = Math.max(minDigits, actualDigits);
        final var scoreStr = String.format("%0" + digitsAmount + "d", positiveScore);

        final var oldLength = lastDisplayedValue.length();
        final var newLength = scoreStr.length();

        if (newLength > oldLength) {
            // Thêm
            for (var i = 0; i < newLength - oldLength; i++) {
                var digit = Character.getNumericValue(scoreStr.charAt(i));
                var digitTexture = createDigitTexture(digit);
                container.getChildren().addFirst(digitTexture);
            }
        } else if (newLength < oldLength) {
            // Bớt
            container.getChildren().remove(0, oldLength - newLength);
        }

        // Cập nhật những chữ số thay đổi
        var startIndex = Math.max(0, newLength - oldLength);
        for (var index = startIndex; index < newLength; index++) {
            var oldIndex = index - (newLength - oldLength);
            if (oldIndex < 0
                    || oldIndex >= oldLength
                    || scoreStr.charAt(index) != lastDisplayedValue.charAt(oldIndex)) {
                updateDigitAt(index, Character.getNumericValue(scoreStr.charAt(index)));
            }
        }

        lastDisplayedValue = scoreStr;
    }

    /**
     * Update một chữ số tại vị trí cụ thể.
     *
     * @param index Vị trí chữ số
     * @param digit Chữ số mới (0-9)
     */
    private void updateDigitAt(int index, int digit) {
        if (index >= container.getChildren().size()) {
            return;
        }

        var digitTexture = createDigitTexture(digit);
        container.getChildren().set(index, digitTexture);
    }

    /**
     * Tạo texture cho một chữ số.
     *
     * @param digit Chữ số (0-9)
     * @return Texture của chữ số
     */
    private Texture createDigitTexture(int digit) {
        var texturePath = AssetsPath.Textures.NUMBERS.get(digit);
        return FXGL.getAssetLoader()
                .loadTexture(texturePath, DEFAULT_DIGIT_WIDTH, DEFAULT_DIGIT_HEIGHT);
    }

    public int getMinDigits() {
        return minDigits;
    }

    public void setMinDigits(int minDigits) {
        this.minDigits = minDigits;
        update();
    }

    public ReadOnlyIntegerProperty getValue() {
        return value;
    }

    public void setValue(ReadOnlyIntegerProperty value) {
        if (this.value != null) {
            this.value.removeListener(listener);
        }
        this.value = value;
        this.value.addListener(listener);
        update();
    }
}
