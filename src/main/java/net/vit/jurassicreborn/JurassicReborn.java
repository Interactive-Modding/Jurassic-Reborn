package net.vit.jurassicreborn;

import net.minecraft.world.entity.SpawnPlacementTypes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.level.LevelEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.client.render.entity.MicroraptorShoulderRenderer;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.screens.paleopad.GuiAppRegistry;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.CommonRegistries;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemHandlerSideWrapper;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.command.DoDinoBreedingCommand;
import net.vit.jurassicreborn.common.command.ForceAnimationCommand;
import net.vit.jurassicreborn.common.command.MetabolismCommand;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.datagen.data.ModDataComponent;
import net.vit.jurassicreborn.common.entities.BlueprintPaintings;
import net.vit.jurassicreborn.common.entities.EventListener;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.MuralPaintings;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.items.ModJukeboxSongs;
import net.vit.jurassicreborn.common.paleopad.AppHandler;
import net.vit.jurassicreborn.common.recipes.ModRecipeSerializers;
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
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerPotBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.api.distmarker.Dist;
import net.vit.jurassicreborn.common.worldgen.ModFeatures;
import net.vit.jurassicreborn.common.worldgen.loot.ModLootModifiers;
import net.vit.jurassicreborn.common.worldgen.villager.ModVillagers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

@Mod(JurassicReborn.MODID)
public class JurassicReborn {

    public static final String MODID = "jurassicreborn";

    public static ResourceLocation resource(String resource){
        return ResourceLocation.fromNamespaceAndPath(MODID, resource);
    }
    public static ResourceLocation location(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }

    private static final Logger LOGGER = LogManager.getLogger();

    public JurassicReborn(IEventBus modEventBus, ModContainer modContainer) {

        JurassicConfig.register(modEventBus, modContainer);
        SoundHandler.SOUNDS.register(modEventBus);
        ModDataComponent.register(modEventBus);

        if (FMLEnvironment.dist.isClient()) {
            JurassicClient.init(modEventBus);
            NeoForge.EVENT_BUS.register(new MicroraptorShoulderRenderer());
        }
        WoodBlocks.register();
        ModParticles.init(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITY_TYPES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModItems.register(modEventBus);
        ModJukeboxSongs.JUKEBOX_SONGS.register(modEventBus);
        ModEntities.init(modEventBus);
        ModVillagers.register(modEventBus);
        TabHandler.TABS.register(modEventBus);
        ModRecipeSerializers.SERIALIZERS.register(modEventBus);

        ModFeatures.FEATURES.register(modEventBus);
        CommonRegistries.BIOME_MODIFIER_SERIALIZERS.register(modEventBus);
        CommonRegistries.init();

//        modEventBus.addListener(EventListener::registerStrippables);
        modEventBus.addListener(EventListener::registerAttributes);
        modEventBus.addListener(this::setup);
        modEventBus.addListener(JRDatagen::gather);
        modEventBus.addListener(Network::registerPayloadHandlers);
        modEventBus.addListener(this::registerCapabilities);

        NeoForge.EVENT_BUS.addListener(this::serverTickEvent);
        NeoForge.EVENT_BUS.addListener(this::onLevelLoadEvent);
        NeoForge.EVENT_BUS.addListener(this::registerCommands);
        modEventBus.addListener(this::onLoadComplete);
        modEventBus.addListener(this::registerSpawnPlacements);

        AppHandler.INSTANCE.init();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            GuiAppRegistry.register();
        }
        MuralPaintings.register(modEventBus);
        BlueprintPaintings.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        GameRuleHandler.init();

    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        FoodHelper.init();
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        registerMachineItemHandler(event, ModBlockEntities.CLEANING_STATION);
        registerMachineItemHandler(event, ModBlockEntities.DNA_COMBINATOR_HYBRIDIZER);
        registerMachineItemHandler(event, ModBlockEntities.DNA_EXTRACTOR_BLOCK_ENTITY);
        registerMachineItemHandler(event, ModBlockEntities.DNA_SEQUENCER_BLOCK_ENTITY);
        registerMachineItemHandler(event, ModBlockEntities.DNA_SYNTHESIZER_BLOCK_ENTITY);
        registerMachineItemHandler(event, ModBlockEntities.EMBRYONIC_MACHINE_BLOCK_ENTITY);
        registerMachineItemHandler(event, ModBlockEntities.EMBRYO_CALCIFICATION_MACHINE_BLOCK_ENTITY_TYPE);
        registerMachineItemHandler(event, ModBlockEntities.CULTIVATOR_TOP_BLOCK_ENTITY_TYPE);
        registerMachineItemHandler(event, ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE);
        registerMachineItemHandler(event, ModBlockEntities.INCUBATOR_BLOCK_ENTITY);
        registerMachineItemHandler(event, ModBlockEntities.FOSSIL_GRINDER_BLOCK_ENTITY);
    }

    private <T extends BlockEntity & ItemHandlerBlockEntity> void registerMachineItemHandler(
            RegisterCapabilitiesEvent event,
            DeferredHolder<BlockEntityType<?>, BlockEntityType<T>> blockEntityType) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, blockEntityType.get(), (blockEntity, side) -> {
            if (blockEntity == null) {
                return null;
            }
            var handler = blockEntity.getItemHandler();
            if (handler instanceof MachineItemStackHandler machineHandler && side != null) {
                return new MachineItemHandlerSideWrapper(machineHandler, side);
            }
            return handler;
        });
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


        });
    }
    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {

        if (JurassicConfig.spawnCrabs) {
            event.register(
                    ModEntities.CRAB.get(),
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    CrabEntity::checkCrabSpawnRules,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE
            );
        }

        if (JurassicConfig.spawnSharks) {
            event.register(
                    ModEntities.SHARK.get(),
                    SpawnPlacementTypes.IN_WATER,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    SharkEntity::checkSharkSpawnRules,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE
            );
        }

        if (JurassicConfig.spawnGoats) {
            event.register(
                    ModEntities.GOAT.get(),
                    SpawnPlacementTypes.ON_GROUND,
                    Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    GoatEntity::checkGoatSpawnRules,
                    RegisterSpawnPlacementsEvent.Operation.REPLACE
            );
        }


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

//    public void registerRecipeSerializers(RegisterEvent event) {
//        event.register(Registr.Keys.RECIPE_SERIALIZERS, (helper) ->{
//            helper.register(resource("cleaning_recipe_serializer"), CleaningRecipe.INSTANCE);
//            helper.register(resource("crafting_special_potion_dart"), PotionDartRecipe.SERIALIZER);
//        });
//        event.register(ForgeRegistries.Keys.RECIPE_TYPES, (helper) ->{
//            helper.register(resource("cleaning_recipe_type"), CleaningRecipe.CLEANING);
//        });
//    }

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




    public void serverTickEvent(ServerTickEvent.Post event) {
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
