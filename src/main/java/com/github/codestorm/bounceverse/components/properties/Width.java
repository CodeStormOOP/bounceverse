package com.github.codestorm.bounceverse.components.properties;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.data.tags.components.Property;

public class Width extends Component implements Property {

    private double originalWidth;

    @Override
    public void onAdded() {
        originalWidth = entity.getWidth();
    }

    public double getOriginalWidth() {
        return originalWidth;
    }

    public void setWidth(double width) {
        var transform = entity.getTransformComponent();
        transform.setAnchoredPosition(entity.getCenter());
        entity.setScaleX(width / originalWidth);
    }

    public double getWidth() {
        return entity.getWidth();
    }

    public void resetWidth() {
        entity.setScaleX(1.0);
    }
}
