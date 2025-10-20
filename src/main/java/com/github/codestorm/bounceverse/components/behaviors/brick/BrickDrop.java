package com.github.codestorm.bounceverse.components.behaviors.brick;

import com.almasb.fxgl.entity.component.Component;
import com.github.codestorm.bounceverse.components.Behavior;
import com.github.codestorm.bounceverse.components._old.base.EntityComponent;
import com.github.codestorm.bounceverse.data.meta.entities.ForEntity;
import com.github.codestorm.bounceverse.data.types.EntityType;
import java.util.List;

/**
 *
 *
 * <h1>{@link BrickDrop}</h1>
 *
 * Lớp này biểu diễn hành vi rơi ra vật phẩm của Viên gạch sau khi bị phá hủy.
 *
 * @deprecated <b>TODO: Lớp này chưa hoàn thiện!</b>
 */
@ForEntity(EntityType.BRICK)
public final class BrickDrop extends Component implements Behavior {
    private List<EntityComponent> items;

    public BrickDrop(List<EntityComponent> items) {
        this.items = items;
    }

    /** Hành động rơi ra vật phẩm. */
    private void drop() {
        // TODO: How to drop?
    }

    @Override
    public void onRemoved() {
        drop();
    }

    public List<EntityComponent> getItems() {
        return items;
    }

    public void setItems(List<EntityComponent> items) {
        this.items = items;
    }
}
