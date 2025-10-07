package com.github.codestorm.bounceverse.components.brick.behaviors;

import com.github.codestorm.bounceverse.components.base.BehaviorComponent;
import com.github.codestorm.bounceverse.components.base.EntityComponent;
import com.github.codestorm.bounceverse.tags.ForBrick;
import com.github.codestorm.bounceverse.tags.Optional;
import java.util.List;

/**
 *
 *
 * <h1><b>BrickDrop</b></h1>
 *
 * Lớp này biểu diễn hành vi rơi ra vật phẩm của Viên gạch sau khi bị phá hủy.
 */
public final class BrickDrop extends BehaviorComponent implements ForBrick, Optional {
    private List<EntityComponent> items;

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

    public BrickDrop(List<EntityComponent> items) {
        this.items = items;
    }
}
