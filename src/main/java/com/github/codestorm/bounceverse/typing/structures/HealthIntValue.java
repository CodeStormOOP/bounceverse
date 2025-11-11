package com.github.codestorm.bounceverse.typing.structures;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 * <h1>{@link HealthIntValue}</h1>
 *
 * Giá trị máu (health) kiểu số nguyên với giới hạn tối đa. <br>
 * Cung cấp các phương thức để quản lý máu như damage, restore, và kiểm tra trạng thái.
 *
 * @see com.almasb.fxgl.dsl.components.HealthIntComponent
 */
public class HealthIntValue {
    private int value;
    private int maxValue;

    /** Danh sách callbacks được gọi khi giá trị máu thay đổi. */
    public final List<Runnable> onChangedListeners = new ArrayList<>();

    /** Danh sách callbacks được gọi khi nhận sát thương. */
    public final List<Runnable> onDamageListeners = new ArrayList<>();

    /** Danh sách callbacks được gọi khi hồi máu. */
    public final List<Runnable> onHealListeners = new ArrayList<>();

    /** Danh sách callbacks được gọi khi máu về 0. */
    public final List<Runnable> onZeroListeners = new ArrayList<>();

    /** Danh sách callbacks được gọi khi máu lên full. */
    public final List<Runnable> onFullListeners = new ArrayList<>();

    /**
     * Khởi tạo HealthIntValue với giá trị tối đa.
     *
     * @param maxValue Giá trị máu tối đa
     */
    public HealthIntValue(int maxValue) {
        this.maxValue = maxValue;
        this.value = maxValue;
    }

    public HealthIntValue(int maxValue, int value) {
        this(maxValue);
        this.value = value;
    }

    /**
     * Lấy giá trị máu hiện tại.
     *
     * @return Giá trị máu hiện tại
     */
    public int getValue() {
        return value;
    }

    /**
     * Set giá trị máu hiện tại. Giá trị sẽ được giới hạn trong khoảng [0, maxValue].
     *
     * @param value Giá trị máu mới
     */
    public void setValue(int value) {
        var oldValue = this.value;
        this.value = Math.max(0, Math.min(value, maxValue));

        if (oldValue != this.value) {
            triggerEvents(onChangedListeners);

            if (isZero()) {
                triggerEvents(onZeroListeners);
            }
            if (isFull()) {
                triggerEvents(onFullListeners);
            }
        }
    }

    /**
     * Lấy giá trị máu tối đa.
     *
     * @return Giá trị máu tối đa
     */
    public int getMaxValue() {
        return maxValue;
    }

    /**
     * Set giá trị máu tối đa. Nếu giá trị hiện tại vượt quá maxValue mới, nó sẽ được điều chỉnh.
     *
     * @param maxValue Giá trị máu tối đa mới
     */
    public void setMaxValue(int maxValue) {
        this.maxValue = Math.max(0, maxValue);
        if (value > maxValue) {
            value = maxValue;
        }
    }

    /**
     * Gây sát thương (giảm máu).
     *
     * @param damage Lượng sát thương (giá trị dương)
     */
    public void damage(int damage) {
        var oldValue = getValue();
        setValue(getValue() - damage);
        if (getValue() < oldValue) {
            triggerEvents(onDamageListeners);
        }
    }

    /**
     * Hồi máu.
     *
     * @param amount Lượng máu hồi (giá trị dương)
     */
    public void restore(int amount) {
        var oldValue = getValue();
        setValue(getValue() + amount);
        if (getValue() > oldValue) {
            triggerEvents(onHealListeners);
        }
    }

    /** Hồi máu về tối đa. */
    public void restoreToMax() {
        setValue(maxValue);
    }

    /**
     * Kiểm tra máu có bằng 0 không.
     *
     * @return {@code true} nếu máu bằng 0, ngược lại {@code false}
     */
    public boolean isZero() {
        return value == 0;
    }

    /**
     * Kiểm tra máu có đầy không.
     *
     * @return {@code true} nếu máu bằng maxValue, ngược lại {@code false}
     */
    public boolean isFull() {
        return value == maxValue;
    }

    /**
     * Lấy phần trăm máu hiện tại (0-100).
     *
     * @return Phần trăm máu (0-100)
     */
    public double getValuePercent() {
        if (maxValue == 0) {
            return 0;
        }
        return (double) value / maxValue * 100.0;
    }

    /**
     * Kích hoạt tất cả event callbacks trong danh sách.
     *
     * @param listeners Danh sách callbacks cần kích hoạt
     */
    private void triggerEvents(List<Runnable> listeners) {
        for (var listener : listeners) {
            if (listener != null) {
                listener.run();
            }
        }
    }

    @Override
    public String toString() {
        return "HealthIntValue{" + "value=" + value + ", maxValue=" + maxValue + '}';
    }
}
