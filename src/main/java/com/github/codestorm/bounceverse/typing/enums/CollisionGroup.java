package com.github.codestorm.bounceverse.typing.enums;

/**
 * Định nghĩa các nhóm va chạm vật lý cho Collision Filtering. Mỗi giá trị là một lũy thừa của 2 để
 * có thể kết hợp bằng phép toán bitwise OR.
 */
public enum CollisionGroup {
    BALL(1), // 2^0
    BULLET(2), // 2^1
    PADDLE(4), // 2^2
    BRICK(8), // 2^3
    WALL(16); // 2^4

    public final int bits;

    CollisionGroup(int bits) {
        this.bits = bits;
    }
}
