package com.github.codestorm.bounceverse;

import com.github.codestorm.bounceverse.typing.enums.BrickType;
import com.google.common.collect.ImmutableList;

import javafx.scene.paint.Color;

import org.jspecify.annotations.NonNull;

import java.util.*;

/**
 *
 *
 * <h1>{@link AssetsPath}</h1>
 *
 * Nơi lưu trữ các đường dẫn tới assets.
 */
public final class AssetsPath {

    private AssetsPath() {
    }

    private static final String ROOT = "/assets";

    public static final class Video {

        private Video() {
        }

        private static final String ROOT = AssetsPath.ROOT + "/videos";

        public static final String INTRO = "intro.mp4";
    }

    public static final class Sounds {

        private Sounds() {
        }

        private static final String ROOT = AssetsPath.ROOT + "/sounds";

        public static final class Music {

            private Music() {
            }

            private static final String ROOT = Sounds.ROOT + "/music";

            public static final String IN_GAME = ROOT + "/in_game.wav";
            public static final String MENU = ROOT + "/menu.ogg";
        }

        public static final class SFX {

            private SFX() {
            }

            private static final String ROOT = Sounds.ROOT + "/sfx";

            public static final String BLIP = ROOT + "/blip.wav";
            public static final String BRICK_HIT_1 = ROOT + "/brick-hit-1.wav";
            public static final String BRICK_HIT_2 = ROOT + "/brick-hit-2.wav";
            public static final String BUZZ = ROOT + "/buzz.wav";
            public static final String CLICK_TINY = ROOT + "/click_tiny.wav";
            public static final String CONFIRM = ROOT + "/confirm.wav";
            public static final String ELECTRIC = ROOT + "/electric.wav";
            public static final String HIGH_SCORE = ROOT + "/high_score.wav";
            public static final String HURT = ROOT + "/hurt.wav";
            public static final String KEY_OPEN = ROOT + "/key_open.wav";
            public static final String LOSE = ROOT + "/lose.wav";
            public static final String NO_SELECT = ROOT + "/no-select.wav";
            public static final String PADDLE_HIT = ROOT + "/paddle_hit.wav";
            public static final String PAUSE = ROOT + "/pause.wav";
            public static final String POWER_UP = ROOT + "/power_up.wav";
            public static final String RECOVER = ROOT + "/recover.wav";
            public static final String SCORE = ROOT + "/score.wav";
            public static final String SELECT = ROOT + "/select.wav";
            public static final String SHRINK = ROOT + "/shrink.wav";
            public static final String SWITCH2 = ROOT + "/switch2.wav";
            public static final String VICTORY = ROOT + "/victory.wav";
            public static final String WALL_HIT = ROOT + "/wall_hit.wav";
            public static final String WIN3 = ROOT + "/win3.wav";

            public static final ImmutableList<@NonNull String> BRICK_HITS
                    = ImmutableList.of(BRICK_HIT_1, BRICK_HIT_2);
        }
    }

    public static final class Textures {

        private Textures() {
        }

        public static final String ROOT = AssetsPath.ROOT + "/textures";

        public static final class Bricks {

            private Bricks() {
            }

            private static final String ROOT = "bricks";

            public static final Map<String, ColorAssets> COLORS;

            static {
                COLORS = Map.of(
                        "blue", new ColorAssets(Color.BLUE),
                        "green", new ColorAssets(Color.GREEN),
                        "orange", new ColorAssets(Color.ORANGE),
                        "pink", new ColorAssets(Color.PINK),
                        "red", new ColorAssets(Color.RED),
                        "yellow", new ColorAssets(Color.YELLOW)
                );
            }

            public static final class ColorAssets {

                private static final NavigableMap<Double, String> NORMAL = new TreeMap<>();
                private static final NavigableMap<Double, String> SHIELD = new TreeMap<>();
                private static final NavigableMap<Double, String> STRONG = new TreeMap<>();

                private final Color color;

                public Color getColor() {
                    return color;
                }

                private ColorAssets(Color color) {
                    this.color = color;
                }

                static {
                    // Normal
                    NORMAL.put(0.0, "/normal.png");

                    // Shield
                    SHIELD.put(0.0, "/shield.png");

                    // Strong
                    STRONG.put(2.0 / 3, "/strong.png");
                    STRONG.put(1.0 / 3, "/strongFirstHit.png");
                    STRONG.put(0.0, "/strongSecondHit.png");
                }

                public String getColorName() {
                    if (color.equals(Color.BLUE)) {
                        return "blue";
                    }
                    if (color.equals(Color.GREEN)) {
                        return "green";
                    }
                    if (color.equals(Color.ORANGE)) {
                        return "orange";
                    }
                    if (color.equals(Color.PINK)) {
                        return "pink";
                    }
                    if (color.equals(Color.RED)) {
                        return "red";
                    }
                    if (color.equals(Color.YELLOW)) {
                        return "yellow";
                    }
                    throw new IllegalArgumentException("Unsupported color: " + color);
                }

                public String getRoot() {
                    return ROOT + "/" + getColorName();
                }

                /**
                 * Lấy texture path dựa trên BrickType và HP percent.
                 *
                 * @param brickType Loại brick
                 * @param hpPercent HP phần trăm (0.0 - 1.0)
                 * @return Đường dẫn đến texture
                 */
                public String getTexture(BrickType brickType, double hpPercent) {
                    var map = switch (brickType) {
                        case NORMAL ->
                            NORMAL;
                        case SHIELD ->
                            SHIELD;
                        case STRONG ->
                            STRONG;
                        case EXPLODING, SPECIAL ->
                            NORMAL; // dùng lại texture mặc định
                    };
                    return getRoot() + map.floorEntry(hpPercent).getValue();
                }

                public Color color() {
                    return color;
                }

                @Override
                public boolean equals(Object obj) {
                    if (obj == this) {
                        return true;
                    }
                    if (obj == null || obj.getClass() != this.getClass()) {
                        return false;
                    }
                    var that = (ColorAssets) obj;
                    return Objects.equals(this.color, that.color);
                }

                @Override
                public int hashCode() {
                    return Objects.hash(color);
                }

                @Override
                public String toString() {
                    return "ColorAssets[" + "color=" + color + ']';
                }
            }
        }
    }

    public static final class Other {

        private Other() {
        }

        public static final String CREDITS = AssetsPath.ROOT + "/credits.txt";
    }
}
