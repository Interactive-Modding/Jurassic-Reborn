package net.vit.jurassicreborn;

import net.minecraft.core.Registry;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.screens.paleopad.GuiAppRegistry;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.CommonRegistries;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.command.DoDinoBreedingCommand;
import net.vit.jurassicreborn.common.command.ForceAnimationCommand;
import net.vit.jurassicreborn.common.command.MetabolismCommand;
import net.vit.jurassicreborn.common.datagen.JRDatagen;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.EventListener;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.recipes.ModRecipeSerializers;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;
import net.vit.jurassicreborn.common.util.GameRuleHandler;
import net.vit.jurassicreborn.common.util.particles.ModParticles;
import net.vit.jurassicreborn.common.worldgen.*;
import net.vit.jurassicreborn.common.worldgen.loot.ModLootModifiers;
import net.vit.jurassicreborn.common.worldgen.villager.ModVillagers;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Mod(JurassicReborn.MODID)
public class JurassicReborn {

    public static final String MODID = "jurassicreborn";
    private static final Logger LOGGER = LogManager.getLogger();

    public static ResourceLocation resource(String resource) {
        return new ResourceLocation(MODID, resource);
    }

    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }

    public JurassicReborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RebornConfig.COMMON_SPEC);
        modEventBus.addListener(RebornConfig::onLoad);
        modEventBus.addListener(RebornConfig::onReload);

        // Client-side only
        if (FMLEnvironment.dist.isClient()) {
            JurassicClient.init(modEventBus);
        }
        SoundHandler.registrer.register(modEventBus);

        // Core registry objects
        WoodBlocks.register();
        ModParticles.init(modEventBus);
        ModBlocks.register(modEventBus);
        DinosaurHandler.doDinosInit();
        PlantHandler.init();
        ModItems.register(modEventBus);
        ModVillagers.register(modEventBus);
        ModEntities.init(modEventBus);
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);

        ModFeatures.FEATURES.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        CommonRegistries.init();
        modEventBus.addListener(EventListener::finalizeSetup);
        modEventBus.addListener(EventListener::registerAttributes);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(JRDatagen::gather);
        modEventBus.addListener(this::onLoadComplete);

        // MinecraftForge event bus
        MinecraftForge.EVENT_BUS.addListener(this::serverTickEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelLoadEvent);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        MinecraftForge.EVENT_BUS.register(BiomeModification.class);

        // Misc
        AppHandler.INSTANCE.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> GuiAppRegistry::register);

        Network.init();
        GameRuleHandler.init();

        System.out.println(toString(EntityAnimation.values()));
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        FoodHelper.init();
    }

    public void setup(final FMLCommonSetupEvent event) {
        ConfiguredFeatureRegistries.init();
        CultivatorBlockEntity.FoodNutrients.register();

        event.enqueueWork(() -> {
            FlowerPotBlock flowerPot = (FlowerPotBlock) Blocks.FLOWER_POT;
            flowerPot.addPlant(ModBlocks.ARAUCARIA_SAPLING.getId(), ModBlocks.POTTED_ARAUCARIA_SAPLING);
            flowerPot.addPlant(ModBlocks.GINKGO_SAPLING.getId(), ModBlocks.POTTED_GINKGO_SAPLING);
            flowerPot.addPlant(ModBlocks.CALAMITES_SAPLING.getId(), ModBlocks.POTTED_CALAMITES_SAPLING);
            flowerPot.addPlant(ModBlocks.PHOENIX_SAPLING.getId(), ModBlocks.POTTED_PHOENIX_SAPLING);
            flowerPot.addPlant(ModBlocks.PSARONIUS_SAPLING.getId(), ModBlocks.POTTED_PSARONIUS_SAPLING);
            flowerPot.addPlant(ModBlocks.MAGNOLIA_SAPLING.getId(), ModBlocks.POTTED_MAGNOLIA_SAPLING);
            Registry.register(Registry.RECIPE_TYPE, JurassicReborn.resource("cleaning"), CleaningRecipe.CLEANING);
            if (RebornConfig.spawnCrabs) {
                SpawnPlacements.register(ModEntities.CRAB.get(),
                        SpawnPlacements.Type.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        CrabEntity::checkCrabSpawnRules);
            }
            if (RebornConfig.spawnSharks) {
                SpawnPlacements.register(ModEntities.SHARK.get(),
                        SpawnPlacements.Type.IN_WATER,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        SharkEntity::checkSharkSpawnRules);
            }
            if (RebornConfig.spawnGoats) {
                SpawnPlacements.register(ModEntities.GOAT.get(),
                        SpawnPlacements.Type.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                        GoatEntity::checkGoatSpawnRules);
            }
        });
    }

    public void onLevelLoadEvent(WorldEvent.Load evt) {}

    public void registerCommands(RegisterCommandsEvent event) {
        MetabolismCommand.register(event.getDispatcher());
        DoDinoBreedingCommand.register(event.getDispatcher());
        ForceAnimationCommand.register(event.getDispatcher());
    }

    public void serverTickEvent(TickEvent.ServerTickEvent evt) {
        Network.removeRemovedEntities();
    }

    public static Logger getLogger() {
        return LOGGER;
    }

    public static Boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entityType) {
        return false;
    }

    public static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    public static void debugHook() {
        System.out.println("Debugging!");
    }

    public static ArrayList<String> erroredIdentifiers = new ArrayList<>();

    public static void checkCubeId(String id) {}

    public static String toString(Object[] a) {
        if (a == null) return "null";
        int iMax = a.length - 1;
        if (iMax == -1) return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0;; i++) {
            b.append("\"").append(a[i]).append("\"");
            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }
}
