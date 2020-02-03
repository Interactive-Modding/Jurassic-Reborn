package mod.reborn.server.util;

public final class Easings {

    private Easings() {
        throw new UnsupportedOperationException("Ya've been naughty, no instanz for yu!");
    }

    public static float easeInCubic(float time, float startValue, float change, float duration) {
        time /= duration;
        return change * time * time * time + startValue;
    }
}
