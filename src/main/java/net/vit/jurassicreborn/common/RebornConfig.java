package net.vit.jurassicreborn.common;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Simple configuration values for JurassicReborn.
 */
public final class RebornConfig {

    public static final ForgeConfigSpec COMMON_SPEC;
    private static final Common COMMON;

    /** Determines if dinosaurs should become carcasses instead of dying instantly. */
    public static boolean allowCarcass = true;
    public static boolean enableVehicleTilting = true;
    public static boolean attackOnlyWhenHungry = false;

    /** Enable natural spawning for crabs. */
    public static boolean spawnCrabs = true;

    /** Enable natural spawning for sharks. */
    public static boolean spawnSharks = true;

    /** Enable natural spawning for goats. */
    public static boolean spawnGoats = true;

    /** Configurable entity blacklist for capture mechanics. */
    public static final EntityBlacklist ENTITY_BLACKLIST = new EntityBlacklist();

    static {
        Pair<Common, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
        // No sync() here — values are loaded via onLoad/onReload events.
    }

    private RebornConfig() {}

    public static void onLoad(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            sync();
        }
    }

    public static void onReload(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == COMMON_SPEC) {
            sync();
        }
    }

    private static void sync() {
        allowCarcass = COMMON.allowCarcass.get();
        enableVehicleTilting = COMMON.enableVehicleTilting.get();
        attackOnlyWhenHungry = COMMON.attackOnlyWhenHungry.get();
        spawnCrabs = COMMON.spawnCrabs.get();
        spawnSharks = COMMON.spawnSharks.get();
        spawnGoats = COMMON.spawnGoats.get();
        List<? extends String> blacklist = COMMON.entityBlacklist.get();
        ENTITY_BLACKLIST.blacklist = blacklist.toArray(new String[0]);
    }

    /** Holds the list of entity IDs that cages should refuse to capture. */
    public static class EntityBlacklist {
        public String[] blacklist = new String[0];
    }

    private static class Common {
        final ForgeConfigSpec.BooleanValue allowCarcass;
        final ForgeConfigSpec.BooleanValue enableVehicleTilting;
        final ForgeConfigSpec.BooleanValue attackOnlyWhenHungry;
        final ForgeConfigSpec.BooleanValue spawnCrabs;
        final ForgeConfigSpec.BooleanValue spawnSharks;
        final ForgeConfigSpec.BooleanValue spawnGoats;
        final ForgeConfigSpec.ConfigValue<List<? extends String>> entityBlacklist;

        Common(ForgeConfigSpec.Builder builder) {
            builder.comment("General configuration for Jurassic Reborn").push("general");
            allowCarcass = builder.comment("Determines if dinosaurs should become carcasses instead of dying instantly.")
                    .define("allowCarcass", true);
            enableVehicleTilting = builder.comment("Enable tilting animations for vehicles such as cars.")
                    .define("enableVehicleTilting", true);
            attackOnlyWhenHungry = builder.comment("If true, carnivores will only attack when hungry.")
                    .define("attackOnlyWhenHungry", false);
            spawnCrabs = builder.comment("Enable natural spawning for crabs.")
                    .define("spawnCrabs", true);
            spawnSharks = builder.comment("Enable natural spawning for sharks.")
                    .define("spawnSharks", true);
            spawnGoats = builder.comment("Enable natural spawning for goats.")
                    .define("spawnGoats", true);
            entityBlacklist = builder.comment(
                            "Entities that cages should refuse to capture.",
                            "Use fully qualified entity IDs, e.g. minecraft:pig")
                    .defineList("entityBlacklist", List.of(), value -> value instanceof String);
            builder.pop();
        }
    }
}
