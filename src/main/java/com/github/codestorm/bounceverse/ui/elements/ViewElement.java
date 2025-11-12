package com.github.codestorm.bounceverse.ui.elements;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.components.ViewComponent;

import javafx.scene.Node;

/**
 *
 *
 * <h1>%{@link ViewElement}</h1>
 *
 * Phần tử UI chứa logic riêng, <b>độc lập</b> và có thể hiển thị trên màn hình. <br>
 * <b>Đừng nhầm lẫn với {@link ViewComponent} - nó là một thành phần của {@link Entity}, không độc
 * lập.</b>
 */
public abstract class ViewElement {
    public abstract Node getView();

    /** Cập nhật Element. */
    public void update() {}
}
