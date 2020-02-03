package mod.reborn.server.util;

import net.minecraft.world.GameRules;
import net.minecraft.world.World;

public enum GameRuleHandler {
    DINO_METABOLISM("dinoMetabolism", true),
    DINO_GROWTH("dinoGrowth", true),
    PLANT_SPREADING("plantSpreading", true),
    KILL_HERD_OUTCAST("killHerdOutcast", true);

    String name;
    Object defaultValue;
    GameRules.ValueType valueType;

    GameRuleHandler(String name, boolean defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.valueType = GameRules.ValueType.BOOLEAN_VALUE;
    }

    GameRuleHandler(String name, int defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.valueType = GameRules.ValueType.NUMERICAL_VALUE;
    }

    public boolean getBoolean(World world) {
        return world.getGameRules().getBoolean(this.name);
    }

    public int getInteger(World world) {
        return world.getGameRules().getInt(this.name);
    }

    public static void register(World world) {
        GameRules gameRules = world.getGameRules();
        for (GameRuleHandler rule : GameRuleHandler.values()) {
            if (!gameRules.hasRule(rule.name)) {
                gameRules.addGameRule(rule.name, String.valueOf(rule.defaultValue), rule.valueType);
            }
        }
    }
}
