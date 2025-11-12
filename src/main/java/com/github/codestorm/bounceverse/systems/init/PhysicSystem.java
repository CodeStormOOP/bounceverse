package com.github.codestorm.bounceverse.systems.init;

import java.util.List;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;
import com.almasb.fxgl.entity.SpawnData;
import com.almasb.fxgl.physics.CollisionHandler;
import com.almasb.fxgl.physics.PhysicsComponent;
import com.github.codestorm.bounceverse.Utilities;
import com.github.codestorm.bounceverse.components.behaviors.Attack;
import com.github.codestorm.bounceverse.components.properties.Shield;
import com.github.codestorm.bounceverse.components.properties.paddle.PaddleViewManager;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpContainer;
import com.github.codestorm.bounceverse.components.properties.powerup.PowerUpManager;
import com.github.codestorm.bounceverse.components.properties.powerup.types.PowerUp;
import com.github.codestorm.bounceverse.data.types.PowerUpType;
import com.github.codestorm.bounceverse.factory.entities.BallFactory;
import com.github.codestorm.bounceverse.typing.enums.DirectionUnit;
import com.github.codestorm.bounceverse.typing.enums.EntityType;
import com.github.codestorm.bounceverse.typing.structures.HealthIntValue;
import javafx.geometry.Point2D;
import javafx.geometry.Side;

/**
 * <h1>PhysicSystem</h1>
 * Quản lý toàn bộ logic va chạm vật lý trong game (ball, paddle, wall,
 * power-up...).
 */
public final class PhysicSystem extends InitialSystem {

    private PhysicSystem() {
    }

    public static PhysicSystem getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void apply() {
        var world = FXGL.getPhysicsWorld();
        world.setGravity(0, 0);

        // Bullet vs Brick
        world.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity brick) {
                PhysicsComponent bulletPhysics = bullet.getComponent(PhysicsComponent.class);
                Point2D velocity = bulletPhysics.getLinearVelocity();

                Side hitSide;

                if (Math.abs(velocity.getY()) > Math.abs(velocity.getX())) {
                    if (velocity.getY() < 0) {
                        hitSide = Side.BOTTOM;
                    } else {
                        hitSide = Side.TOP;
                    }
                } else {
                    if (velocity.getX() < 0) {
                        hitSide = Side.RIGHT;
                    } else {
                        hitSide = Side.LEFT;
                    }
                }

                var shieldOpt = brick.getComponentOptional(Shield.class);

                // Kiểm tra xem mặt vừa bị va chạm có được bảo vệ không
                if (shieldOpt.isPresent() && shieldOpt.get().hasSide(hitSide)) {
                    // Có khiên bảo vệ, không gây sát thương
                } else {
                    // Không có khiên, gây sát thương
                    bullet.getComponentOptional(Attack.class)
                            .ifPresent(a -> a.execute(List.of(brick)));
                }

                // Viên đạn luôn tự hủy sau va chạm
                bullet.removeFromWorld();
            }
        });

        // Bullet vs Wall
        world.addCollisionHandler(new CollisionHandler(EntityType.BULLET, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity bullet, Entity wall) {
                // Đơn giản là xóa viên đạn khi nó chạm vào bất kỳ bức tường nào
                bullet.removeFromWorld();
            }
        });

        // Ball vs Brick
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.BRICK) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity brick) {
                var physics = ball.getComponent(PhysicsComponent.class);
                var dir = Utilities.Collision.getCollisionDirection(ball, brick);

                var shieldOpt = brick.getComponentOptional(Shield.class);
                if (shieldOpt.isPresent() && shieldOpt.get().hasSide(dir.toSide())) {
                    bounce(physics, dir);
                    return;
                }

                bounce(physics, dir);
                ball.getComponentOptional(com.github.codestorm.bounceverse.components.behaviors.Attack.class)
                        .ifPresent(a -> a.execute(java.util.List.of(brick)));
            }
        });

        // Ball vs Paddle
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.PADDLE) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity paddle) {
                var physics = ball.getComponent(PhysicsComponent.class);
                double offset = (ball.getCenter().getX() - paddle.getCenter().getX()) / (paddle.getWidth() / 2.0);
                offset = Math.max(-1.0, Math.min(1.0, offset));
                double angle = Math.toRadians(90 - 45 * offset);
                double speed = FXGL.getd("ballSpeed");
                physics.setLinearVelocity(speed * Math.cos(angle), -speed * Math.sin(angle));
            }
        });

        // Ball vs Shield (bottom shield power-up)
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, PowerUpType.SHIELD) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity shield) {
                ball.getComponentOptional(PhysicsComponent.class).ifPresent(phys -> {
                    Point2D velocity = phys.getLinearVelocity();
                    phys.setLinearVelocity(new Point2D(velocity.getX(), -Math.abs(velocity.getY())));
                });
            }
        });

        // Ball vs Wall
        world.addCollisionHandler(new CollisionHandler(EntityType.BALL, EntityType.WALL) {
            @Override
            protected void onCollisionBegin(Entity ball, Entity wall) {
                var phys = ball.getComponent(PhysicsComponent.class);
                var v = phys.getLinearVelocity();
                Side side = wall.getObject("side");
                double eps = 0.5; // khoảng đệm nhỏ

                switch (side) {
                    case LEFT: {
                        ball.setX(wall.getRightX() + eps);
                        phys.setLinearVelocity(Math.abs(v.getX()), v.getY());
                        break;
                    }
                    case RIGHT: {
                        ball.setX(wall.getX() - ball.getWidth() - eps);
                        phys.setLinearVelocity(-Math.abs(v.getX()), v.getY());
                        break;
                    }
                    case TOP: {
                        ball.setY(wall.getBottomY() + eps);
                        phys.setLinearVelocity(v.getX(), Math.abs(v.getY()));
                        break;
                    }
                    case BOTTOM: {
                        ball.removeFromWorld();

                        if (FXGL.getGameWorld().getEntitiesByType(EntityType.BALL).isEmpty()) {
                            HealthIntValue lives = FXGL.getWorldProperties().getObject("lives");
                            lives.damage(1);

                            FXGL.getGameTimer().runOnceAfter(() -> {
                                if (lives.getValue() > 0) {
                                    var paddle = FXGL.getGameWorld().getSingleton(EntityType.PADDLE);
                                    paddle.getComponent(PaddleViewManager.class).reset();

                                    PowerUpManager.getInstance().clearAll();

                                    double x = paddle.getCenter().getX() - BallFactory.DEFAULT_RADIUS;
                                    double y = paddle.getY() - BallFactory.DEFAULT_RADIUS * 2;
                                    FXGL.spawn("ball", new SpawnData(x, y).put("attached", true));
                                    FXGL.set("ballAttached", true);
                                }
                            }, javafx.util.Duration.millis(100));
                        }
                        break;
                    }
                }

                var currentVelocity = phys.getLinearVelocity();
                double currentSpeed = currentVelocity.magnitude();
                double targetSpeed = FXGL.getd("ballSpeed");

                // Đặt một khoảng an toàn để tránh điều chỉnh liên tục do sai số vật lý
                double minAllowedSpeed = targetSpeed * 0.95;
                double maxAllowedSpeed = targetSpeed * 1.05;

                double newSpeed = currentSpeed;

                if (currentSpeed < minAllowedSpeed) {
                    newSpeed = minAllowedSpeed;
                } else if (currentSpeed > maxAllowedSpeed) {
                    newSpeed = maxAllowedSpeed;
                }

                // Chỉ cập nhật lại vận tốc nếu tốc độ thực sự bị thay đổi
                if (Math.abs(newSpeed - currentSpeed) > 1e-3) {
                    phys.setLinearVelocity(currentVelocity.normalize().multiply(newSpeed));
                }
            }
        });

        // Va chạm giữa Paddle và Tường đã được GỠ BỎ để tránh xung đột.
        // Logic này giờ được xử lý hoàn toàn trong InputSystem.

        // Paddle vs PowerUp
        world.addCollisionHandler(new CollisionHandler(EntityType.PADDLE, EntityType.POWER_UP) {
            @Override
            protected void onCollisionBegin(Entity paddle, Entity powerUp) {
                if (paddle.isActive() && powerUp.isActive()) {
                    powerUp.getComponentOptional(PowerUpContainer.class).ifPresent(container -> {
                        container.addTo(paddle);
                        container.getContainer().values().forEach(c -> {
                            if (c instanceof PowerUp p)
                                p.apply(paddle);
                        });
                    });
                    powerUp.removeFromWorld();
                }
            }
        });
    }

    /**
     * Đảo hướng vận tốc theo hướng va chạm.
     *
     * @param phys a {@link PhysicsComponent}
     * @param dir  a {@link DirectionUnit}
     */
    private static void bounce(PhysicsComponent phys, DirectionUnit dir) {
        if (dir == null)
            return;

        switch (dir) {
            case UP, DOWN:
                phys.setLinearVelocity(phys.getVelocityX(), -phys.getVelocityY());
                break;
            case LEFT, RIGHT:
                phys.setLinearVelocity(-phys.getVelocityX(), phys.getVelocityY());
                break;
            default:
                break;
        }
    }

    private static final class Holder {
        static final PhysicSystem INSTANCE = new PhysicSystem();
    }
}