package net.vit.jurassicreborn;

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
import net.vit.jurassicreborn.common.command.DoDinoBreedingCommand;
import net.vit.jurassicreborn.common.command.ForceAnimationCommand;
import net.vit.jurassicreborn.common.command.MetabolismCommand;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.BlueprintPaintings;
import net.vit.jurassicreborn.common.entities.EventListener;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.MuralPaintings;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.util.GameRuleHandler;
import net.vit.jurassicreborn.common.util.particles.ModParticles;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.datagen.JRDatagen;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.TabHandler;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;
import net.vit.jurassicreborn.common.recipes.PotionDartRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import net.vit.jurassicreborn.common.worldgen.ModFeatures;
import net.vit.jurassicreborn.common.worldgen.loot.ModLootModifiers;
import net.vit.jurassicreborn.common.worldgen.villager.ModVillagers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

@Mod(JurassicReborn.MODID)
public class JurassicReborn {

    public static final String MODID = "jurassicreborn";

    public static ResourceLocation resource(String resource){
        return new ResourceLocation(MODID, resource);
    }
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public JurassicReborn() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, RebornConfig.COMMON_SPEC);
        modEventBus.addListener(RebornConfig::onLoad);
        modEventBus.addListener(RebornConfig::onReload);

        if (FMLEnvironment.dist.isClient()) {
            JurassicClient.init(modEventBus);
        }
        SoundHandler.registrer.register(modEventBus);

        ModParticles.init(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        WoodBlocks.register();
        ModItems.register(modEventBus);
        ModEntities.init(modEventBus);
        ModVillagers.register(modEventBus);
        modEventBus.addListener(TabHandler::registerCreativeModeTabs);
        modEventBus.addListener(TabHandler::fillTabContents);

        ModFeatures.FEATURES.register(modEventBus);
        CommonRegistries.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        CommonRegistries.init();

        modEventBus.addListener(EventListener::finalizeSetup);
        modEventBus.addListener(EventListener::registerAttributes);
        modEventBus.addListener(this::registerRecipeSerializers);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(JRDatagen::gather);

        MinecraftForge.EVENT_BUS.addListener(this::serverTickEvent);
        MinecraftForge.EVENT_BUS.addListener(this::onLevelLoadEvent);
        MinecraftForge.EVENT_BUS.addListener(this::registerCommands);
        modEventBus.addListener(this::onLoadComplete);

        AppHandler.INSTANCE.init();
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> GuiAppRegistry::register);
        MuralPaintings.register(modEventBus);
        BlueprintPaintings.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        Network.init();
        GameRuleHandler.init();

        System.out.println(toString(EntityAnimation.values()));
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        FoodHelper.init();
    }

    public void setup(final FMLCommonSetupEvent event) {
        CultivatorBlockEntity.FoodNutrients.register();

        event.enqueueWork(() -> {
            FlowerPotBlock flowerPot = (FlowerPotBlock) Blocks.FLOWER_POT;
            flowerPot.addPlant(ModBlocks.ARAUCARIA_SAPLING.getId(), ModBlocks.POTTED_ARAUCARIA_SAPLING);
            flowerPot.addPlant(ModBlocks.GINKGO_SAPLING.getId(), ModBlocks.POTTED_GINKGO_SAPLING);
            flowerPot.addPlant(ModBlocks.CALAMITES_SAPLING.getId(), ModBlocks.POTTED_CALAMITES_SAPLING);
            flowerPot.addPlant(ModBlocks.PHOENIX_SAPLING.getId(), ModBlocks.POTTED_PHOENIX_SAPLING);
            flowerPot.addPlant(ModBlocks.PSARONIUS_SAPLING.getId(), ModBlocks.POTTED_PSARONIUS_SAPLING);
            flowerPot.addPlant(ModBlocks.MAGNOLIA_SAPLING.getId(), ModBlocks.POTTED_MAGNOLIA_SAPLING);
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

    public void onLevelLoadEvent(LevelEvent.Load evt){
    }

    public void registerCommands(RegisterCommandsEvent event) {
        MetabolismCommand.register(event.getDispatcher());
        DoDinoBreedingCommand.register(event.getDispatcher());
        // AnimationCommand.register(event.getDispatcher());
        ForceAnimationCommand.register(event.getDispatcher());
    }

    public static Logger getLogger(){
        return LOGGER;
    }

    public void registerRecipeSerializers(RegisterEvent event) {
        event.register(ForgeRegistries.Keys.RECIPE_SERIALIZERS, (helper) ->{
            helper.register(resource("cleaning_recipe_serializer"), CleaningRecipe.INSTANCE);
            helper.register(resource("crafting_special_potion_dart"), PotionDartRecipe.SERIALIZER);
        });
        event.register(ForgeRegistries.Keys.RECIPE_TYPES, (helper) ->{
            helper.register(resource("cleaning_recipe_type"), CleaningRecipe.CLEANING);
        });
    }

    public static Boolean never(BlockState p_50779_, BlockGetter p_50780_, BlockPos p_50781_, EntityType<?> p_50782_) {
        return false;
    }
    public static boolean never(BlockState p_50806_, BlockGetter p_50807_, BlockPos p_50808_) {
        return false;
    }

    public static void debugHook(){
        System.out.println("Debugging!");
    }

    public static ArrayList<String> erroredIdentifiers = new ArrayList<>();

    public static void checkCubeId(String id) {
    }

    public void serverTickEvent(TickEvent.ServerTickEvent evt){
        Network.removeRemovedEntities();
    }

    public static String toString(Object[] a) {
        if (a == null)
            return "null";

        int iMax = a.length - 1;
        if (iMax == -1)
            return "[]";

        StringBuilder b = new StringBuilder();
        b.append('[');
        for (int i = 0; ; i++) {
            b.append("\"");
            b.append(a[i]);
            b.append("\"");

            if (i == iMax)
                return b.append(']').toString();
            b.append(", ");
        }
    }

    private static final String things_to_bring_up = """

            
            """;
}
