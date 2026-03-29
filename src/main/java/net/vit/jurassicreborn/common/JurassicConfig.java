package net.vit.jurassicreborn.common;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Simple configuration values for Jurassic Reborn.
 */
public final class JurassicConfig {

    public static final ModConfigSpec COMMON_SPEC;
    private static final Common COMMON;

    /** Determines if dinosaurs should become carcasses instead of dying instantly. */
    public static boolean allowCarcass = true;
    public static boolean enableVehicleTilting = true;
    public static boolean attackOnlyWhenHungry = false;

    /** Enable natural spawning for dinosaurs based on their configured biomes. */

    /** Enable natural spawning for crabs. */
    public static boolean spawnCrabs = true;
    public static boolean naturalspawningaddon = false;
    /** Enable natural spawning for sharks. */
    public static boolean spawnSharks = true;

    /** Enable natural spawning for goats. */
    public static boolean spawnGoats = true;

    /** Configurable entity blacklist for capture mechanics. */
    public static final EntityBlacklist ENTITY_BLACKLIST = new EntityBlacklist();

    static {
        Pair<Common, ModConfigSpec> pair = new ModConfigSpec.Builder().configure(Common::new);
        COMMON = pair.getLeft();
        COMMON_SPEC = pair.getRight();
    }

    private JurassicConfig() {}

    public static void register(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, COMMON_SPEC);
        modEventBus.addListener(JurassicConfig::onLoad);
        modEventBus.addListener(JurassicConfig::onReload);
    }


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
        naturalspawningaddon = COMMON.naturalspawningaddon.get();
        spawnGoats = COMMON.spawnGoats.get();
        List<? extends String> blacklist = COMMON.entityBlacklist.get();
        ENTITY_BLACKLIST.blacklist = blacklist.toArray(new String[0]);
    }

    /** Holds the list of entity IDs that cages should refuse to capture. */
    public static class EntityBlacklist {
        public String[] blacklist = new String[0];
    }

    private static class Common {
        final ModConfigSpec.ConfigValue<Boolean> allowCarcass;
        final ModConfigSpec.ConfigValue<Boolean> enableVehicleTilting;
        final ModConfigSpec.ConfigValue<Boolean> attackOnlyWhenHungry;
        final ModConfigSpec.ConfigValue<Boolean> spawnCrabs;
        final ModConfigSpec.ConfigValue<Boolean> spawnSharks;
        final ModConfigSpec.ConfigValue<Boolean> naturalspawningaddon;
        final ModConfigSpec.ConfigValue<Boolean> spawnGoats;
        final ModConfigSpec.ConfigValue<List<? extends String>> entityBlacklist;

        Common(ModConfigSpec.Builder builder) {
            builder.comment("General configuration for Jurassic Reborn").push("general");
            allowCarcass = builder.comment("Determines if dinosaurs should become carcasses instead of dying instantly.")
                    .define("allowCarcass", true);
            enableVehicleTilting = builder.comment("Enable tilting animations for vehicles such as cars.")
                    .define("enableVehicleTilting", true);
            naturalspawningaddon = builder.comment("ENABLE ONLY IF NATURAL SPWANING ADDON IS INSTALLED!")
                    .define("naturalspawningaddon", false);
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
