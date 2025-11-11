package com.github.codestorm.bounceverse.components.behaviors;

import java.util.List;

import com.almasb.fxgl.entity.component.Required;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.properties.Attributes;
import com.github.codestorm.bounceverse.typing.annotations.OnlyForEntity;
import com.github.codestorm.bounceverse.typing.enums.EntityType;

/**
 * Hành vi nổ của Brick – gây damage cho các đối tượng xung quanh.
 */
@Required(Attributes.class)
@OnlyForEntity({ EntityType.BRICK })
public final class Explosion extends Attack {

    // Thay thế 'radius' bằng 'explosionWidth' và 'explosionHeight'
    private double explosionWidth;
    private double explosionHeight;

    @Override
    public void execute(List<Object> data) {
        double cx = getEntity().getCenter().getX();
        double cy = getEntity().getCenter().getY();

        // Sử dụng hàm mới để lấy các entity trong hình chữ nhật
        var nearEntities = Utilities.Geometric.getEntitiesInRectangle(cx, cy, explosionWidth, explosionHeight);

        // Lọc ra để vụ nổ không tự phá hủy chính nó (nếu logic game thay đổi)
        var filteredEntities = nearEntities.stream()
                .filter(e -> !e.equals(getEntity()))
                .map(e -> (Object) e)
                .toList();

        super.execute(filteredEntities);
    }

    @Override
    public void onRemoved() {
        // execute(null) sẽ không hoạt động đúng nếu data là null, truyền vào một danh
        // sách trống
        execute(List.of());
    }

    // Constructor mới nhận chiều rộng và cao
    public Explosion(double width, double height) {
        this.explosionWidth = width;
        this.explosionHeight = height;
    }

    public double getExplosionWidth() {
        return explosionWidth;
    }

    public void setExplosionWidth(double explosionWidth) {
        this.explosionWidth = explosionWidth;
    }

    public double getExplosionHeight() {
        return explosionHeight;
    }

    public void setExplosionHeight(double explosionHeight) {
        this.explosionHeight = explosionHeight;
    }
}