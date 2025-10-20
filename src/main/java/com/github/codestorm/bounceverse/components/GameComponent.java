package com.github.codestorm.bounceverse.components;

import com.almasb.fxgl.entity.component.Component;

/**
 *
 *
 * <h1>{@link GameComponent}</h1>
 *
 * <p>Minh họa cho một {@link Component} trong game.
 *
 * <p>Đọc thêm tài liệu về Component ở <a
 * href="https://github.com/AlmasB/FXGL/wiki/Entity-Component-%28FXGL-11%29">đây</a>.
 */
sealed interface GameComponent permits Property, Behavior {}
