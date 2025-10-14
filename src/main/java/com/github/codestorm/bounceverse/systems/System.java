package com.github.codestorm.bounceverse.systems;

/**
 *
 *
 * <h1>{@link System}</h1>
 *
 * Hệ thống logic trong game. <b>Các lớp kế thừa nên thiết kế dựa trên (lazy-loaded) Singleton.</b>
 *
 * <p>Tất cả logic của hệ thống được áp dụng thông qua {@link #apply()}.
 */
public abstract class System {
    /**
     * Áp dụng logic của hệ thống vào game.
     *
     * <p>Sử dụng trên {@link com.github.codestorm.bounceverse.Bounceverse}
     */
    public abstract void apply();

    protected System() {}
}
