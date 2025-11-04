package com.github.codestorm.bounceverse.components.properties.powerup;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;
import com.almasb.fxgl.entity.component.CoreComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.Property;
import com.github.codestorm.bounceverse.typing.annotations.ForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.google.common.collect.MutableClassToInstanceMap;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 *
 *
 * <h1>{@link PowerUpContainer}</h1>
 *
 * Giống như một cái túi, nơi lưu trữ {@link Component} bên trong {@link Entity} và không kích hoạt
 * logic của các component đó.
 */
@ForEntity({EntityType.POWER_UP})
@CoreComponent
public final class PowerUpContainer extends Property {
    private MutableClassToInstanceMap<Component> container = MutableClassToInstanceMap.create();

    /**
     * Gán trực tiếp các {@link Component} lên trên {@link Entity}.
     *
     * @param entity Entity.
     */
    public void addTo(Entity entity) {
        for (var entry : container.entrySet()) {
            final var component = entry.getValue();
            // TODO: Gán việc kiểm tra trên chính Entity/Component thay vì thủ tục ntn
            Utilities.Compatibility.throwIfNotCompatible((EntityType) entity.getType(), component);
            entity.addComponent(entry.getValue());
        }
    }

    /**
     * Thêm các {@link Component} vào {@link #container}.
     *
     * @param components Các component
     * @see MutableClassToInstanceMap#putAll(Map)
     */
    public void put(@NotNull Component... components) {
        for (var component : components) {
            container.put(component.getClass(), component);
        }
    }

    public PowerUpContainer(Component... components) {
        put(components);
    }

    public MutableClassToInstanceMap<Component> getContainer() {
        return container;
    }

    public void setContainer(MutableClassToInstanceMap<Component> container) {
        this.container = container;
    }
}
