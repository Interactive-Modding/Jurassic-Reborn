package net.vit.jurassicreborn.common.util;

import net.minecraft.world.level.GameRules;

public final class GameRuleHandler {
    public static final GameRules.Key<GameRules.BooleanValue> DINO_METABOLISM;
    public static final GameRules.Key<GameRules.BooleanValue> KILL_HERD_OUTCAST;
    public static final GameRules.Key<GameRules.BooleanValue> DINO_BREEDING;
    public static final GameRules.Key<GameRules.BooleanValue> ANCIENT_PLANT_SPREAD;

    static {
        DINO_METABOLISM = GameRules.register(
                "dinosaurMetabolism",
                GameRules.Category.MOBS,
                findCreateMethod()
        );
    }
    static {
        KILL_HERD_OUTCAST = GameRules.register(
                "killHerdOutcast",
                GameRules.Category.MOBS,
                findCreateMethod()
        );
    }
    static {
        DINO_BREEDING = GameRules.register(
                "doDinoBreeding",
                GameRules.Category.MOBS,
                findCreateMethod()
        );
    }
    static {
        ANCIENT_PLANT_SPREAD = GameRules.register(
                "doAncientPlantSpread",
                GameRules.Category.UPDATES,
                findCreateMethod()
        );
    }

    @SuppressWarnings("unchecked")
    private static GameRules.Type<GameRules.BooleanValue> findCreateMethod() {
        try {
            try {
                var method = GameRules.BooleanValue.class.getDeclaredMethod("create", boolean.class);
                method.setAccessible(true);
                return (GameRules.Type<GameRules.BooleanValue>) method.invoke(null, true);
            } catch (NoSuchMethodException ignored) {
                for (var m : GameRules.BooleanValue.class.getDeclaredMethods()) {
                    if (java.lang.reflect.Modifier.isStatic(m.getModifiers()) &&
                            m.getParameterCount() == 1 &&
                            m.getParameterTypes()[0] == boolean.class &&
                            GameRules.Type.class.isAssignableFrom(m.getReturnType())) {
                        m.setAccessible(true);
                        return (GameRules.Type<GameRules.BooleanValue>) m.invoke(null, true);
                    }
                }
                throw new RuntimeException("No suitable create method found");
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException("Unable to register gamerule", e);
        }
    }

    public static void init() {}
}