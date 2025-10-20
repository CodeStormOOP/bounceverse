package com.github.codestorm.bounceverse.components._old.base;

import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.component.Component;

/**
 *
 *
 * <h1>{@link EntityComponent}</h1>
 *
 * <p>Lớp này đại diện cho các {@link Component} chung của {@link Entity} như là một Component. Sau
 * khi kế thừa, bên trong nên có các Component buộc cần phải có của Entity, đồng thời thêm Component
 * vào Entity thông qua override phương thức {@link Component#onAdded()} bằng cách gọi phương thức
 * {@link Entity#addComponent(Component)}.
 *
 * <p><b>Tại sao lại cần có?</b> Nguyên nhân của nhu cầu sinh ra lớp này là vì engine FXGL mà game
 * sử dụng tuân theo mô hình <a
 * href="https://www.wikipedia.org/wiki/Entity_component_system">Entity-Component-System (ECS)</a>,
 * khi mà Entity không nên được kế thừa để phát triển thêm mà được xây dựng theo qua nguyên tắc <a
 * href="https://www.wikipedia.org/wiki/Composition_over_inheritance">composition over
 * inheritance</a>. Ngoài ra ta lại cần nhu cầu xác định kiểu của Entity một cách chặt chẽ nữa. Vậy
 * nên, một thuộc tính kiểu trong Entity như này là cần thiết.
 *
 * @see Optional
 */
public abstract class EntityComponent extends Component {}
