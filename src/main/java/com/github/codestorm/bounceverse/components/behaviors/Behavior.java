package com.github.codestorm.bounceverse.components.behaviors;

import com.almasb.fxgl.entity.Entity;
import com.github.codestorm.bounceverse.components.Component;
import com.github.codestorm.bounceverse.components.properties.Property;
import com.github.codestorm.bounceverse.typing.interfaces.Executable;

/**
 *
 *
 * <h1>{@link Behavior}</h1>
 *
 * <br>
 * Một {@link Component} biểu diễn <a
 * href="https://github.com/AlmasB/FXGL/wiki/Entity-Component-%28FXGL-11%29#component-as-behavior">
 * hành vi (behavior)</a> của {@link Entity}.
 *
 * @see Property
 */
public abstract class Behavior extends Component implements Executable {}
