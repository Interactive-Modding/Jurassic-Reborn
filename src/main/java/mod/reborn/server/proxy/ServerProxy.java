package mod.reborn.server.proxy;

import java.util.List;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.event.ServerEventHandler;
import mod.reborn.server.food.FoodHelper;
import mod.reborn.server.food.FoodNutrients;
import mod.reborn.server.item.FossilItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.item.JournalItem;
import mod.reborn.server.maps.VillagerTradeHandler;
import mod.reborn.server.paleopad.AppHandler;
import mod.reborn.server.util.RegistryHandler;
import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.BugCrateBlockEntity;
import mod.reborn.server.block.entity.CleaningStationBlockEntity;
import mod.reborn.server.block.entity.CultivatorBlockEntity;
import mod.reborn.server.block.entity.DNACombinatorHybridizerBlockEntity;
import mod.reborn.server.block.entity.DNAExtractorBlockEntity;
import mod.reborn.server.block.entity.DNASequencerBlockEntity;
import mod.reborn.server.block.entity.DNASynthesizerBlockEntity;
import mod.reborn.server.block.entity.EmbryoCalcificationMachineBlockEntity;
import mod.reborn.server.block.entity.EmbryonicMachineBlockEntity;
import mod.reborn.server.block.entity.FeederBlockEntity;
import mod.reborn.server.block.entity.FossilGrinderBlockEntity;
import mod.reborn.server.block.entity.IncubatorBlockEntity;
import mod.reborn.server.container.CleaningStationContainer;
import mod.reborn.server.container.CultivateContainer;
import mod.reborn.server.container.DNACombinatorHybridizerContainer;
import mod.reborn.server.container.DNAExtractorContainer;
import mod.reborn.server.container.DNASequencerContainer;
import mod.reborn.server.container.DNASynthesizerContainer;
import mod.reborn.server.container.EmbryoCalcificationMachineContainer;
import mod.reborn.server.container.EmbryonicMachineContainer;
import mod.reborn.server.container.FeederContainer;
import mod.reborn.server.container.FossilGrinderContainer;
import mod.reborn.server.container.IncubatorContainer;
import mod.reborn.server.container.SkeletonAssemblyContainer;
import mod.reborn.server.datafixers.RebornModDataFixers;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.entity.animal.GoatEntity;
import mod.reborn.server.entity.villager.VillagerHandler;
import mod.reborn.server.genetics.StorageTypeRegistry;
import mod.reborn.server.plant.PlantHandler;
import mod.reborn.server.recipe.SmeltingRecipeHandler;
import mod.reborn.server.world.WorldGenerator;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.reborn.server.world.structure.StructureGenerationHandler;

public class ServerProxy implements IGuiHandler {
    public static final int GUI_CLEANING_STATION_ID = 0;
    public static final int GUI_FOSSIL_GRINDER_ID = 1;
    public static final int GUI_DNA_SEQUENCER_ID = 2;
    public static final int GUI_EMBRYONIC_MACHINE_ID = 3;
    public static final int GUI_EMBRYO_CALCIFICATION_MACHINE_ID = 4;
    public static final int GUI_DNA_SYNTHESIZER_ID = 5;
    public static final int GUI_INCUBATOR_ID = 6;
    public static final int GUI_DNA_COMBINATOR_HYBRIDIZER_ID = 7;
    public static final int GUI_DNA_EXTRACTOR_ID = 8;
    public static final int GUI_CULTIVATOR_ID = 9;
    public static final int GUI_FEEDER_ID = 10;
    public static final int GUI_BUG_CRATE = 11;
    public static final int GUI_SKELETON_ASSEMBLER = 12;
    public static final int GUI_PALEO_PAD = 13;

    public void onPreInit(FMLPreInitializationEvent event) {
        EntityHandler.init();

        FossilItem.init();

        PlantHandler.init();
        BlockHandler.init();
        ItemHandler.init();
        StorageTypeRegistry.init();
        StructureGenerationHandler.register();
        VillagerHandler.init();
        AppHandler.INSTANCE.init();

        FoodNutrients.register();

        GameRegistry.registerWorldGenerator(WorldGenerator.INSTANCE, 0);

        NetworkRegistry.INSTANCE.registerGuiHandler(RebornMod.INSTANCE, this);

        ServerEventHandler eventHandler = new ServerEventHandler();
        MinecraftForge.EVENT_BUS.register(new RegistryHandler());
        MinecraftForge.EVENT_BUS.register(eventHandler);
    }

    public void onPostInit(FMLPostInitializationEvent event) {
        FoodHelper.init();
        BlockHandler.registerOres();
        ItemHandler.registerOres();
        RebornModDataFixers.init();
        SmeltingRecipeHandler.init();
        VillagerTradeHandler.init();
    }

    public void onInit(FMLInitializationEvent event) {
        for(Biome biome : ForgeRegistries.BIOMES.getValuesCollection()) { //Adds the goat spawning to biomes that spawn pigs. TODO: maybe add a config for biomes ?
            List<Biome.SpawnListEntry> list = biome.getSpawnableList(EnumCreatureType.CREATURE);
            boolean shouldAddGoat = false;
            for(Biome.SpawnListEntry entry : list) {
                if(entry.entityClass == EntityPig.class) {
                    shouldAddGoat = true;
                }
            }
            if(shouldAddGoat) {
                list.add(new Biome.SpawnListEntry(GoatEntity.class,  10, 2, 4));
            }
        }
    }

    public EntityPlayer getPlayer() {
        return null;
    }

    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {	
        return ctx.getServerHandler().player;
    }

    @Override
    public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            switch(id) {
                case GUI_CLEANING_STATION_ID:
                    if (tile instanceof CleaningStationBlockEntity)
                        return new CleaningStationContainer(player.inventory, (CleaningStationBlockEntity) tile);
                    break;
                case GUI_FOSSIL_GRINDER_ID:
                    if (tile instanceof FossilGrinderBlockEntity)
                        return new FossilGrinderContainer(player.inventory, tile);
                    break;
                case GUI_DNA_SEQUENCER_ID:
                    if (tile instanceof DNASequencerBlockEntity)
                        return new DNASequencerContainer(player.inventory, tile);
                    break;
                case GUI_EMBRYONIC_MACHINE_ID:
                    if (tile instanceof EmbryonicMachineBlockEntity)
                        return new EmbryonicMachineContainer(player.inventory, tile);
                    break;
                case GUI_EMBRYO_CALCIFICATION_MACHINE_ID:
                    if (tile instanceof EmbryoCalcificationMachineBlockEntity)
                        return new EmbryoCalcificationMachineContainer(player.inventory, tile);
                    break;
                case GUI_DNA_SYNTHESIZER_ID:
                    if (tile instanceof DNASynthesizerBlockEntity)
                        return new DNASynthesizerContainer(player.inventory, tile);
                    break;
                case GUI_INCUBATOR_ID:
                    if (tile instanceof IncubatorBlockEntity)
                        return new IncubatorContainer(player.inventory, tile);
                    break;
                case GUI_DNA_COMBINATOR_HYBRIDIZER_ID:
                    if (tile instanceof DNACombinatorHybridizerBlockEntity)
                        return new DNACombinatorHybridizerContainer(player.inventory, tile);
                    break;
                case GUI_DNA_EXTRACTOR_ID:
                    if (tile instanceof DNAExtractorBlockEntity)
                        return new DNAExtractorContainer(player.inventory, tile);
                    break;
                case GUI_CULTIVATOR_ID:
                    if (tile instanceof CultivatorBlockEntity)
                        return new CultivateContainer(player.inventory, tile);
                    break;
                case GUI_FEEDER_ID:
                    if (tile instanceof FeederBlockEntity)
                        return new FeederContainer(player.inventory, (FeederBlockEntity) tile);
                    break;
                case GUI_BUG_CRATE:
                    if (tile instanceof BugCrateBlockEntity)
                        return ((BugCrateBlockEntity) tile).createContainer(player.inventory, player);
                    break;
                default:
                    return null;
                }
        }
        switch(id){
            case GUI_SKELETON_ASSEMBLER:
                return new SkeletonAssemblyContainer(player.inventory, world, pos);
        }
        return null;
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        return null;
    }

    public void openSelectDino(BlockPos pos, EnumFacing facing, EnumHand hand) {
    }

    public void openOrder(DinosaurEntity entity) {
    }

    public void openFieldGuide(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo fieldGuideInfo) {
    }

    public void openJournal(JournalItem.JournalType type) {
    }

    public void openPaleoPad() {
    }

    public void openPaleoDinosaurPad(DinosaurEntity entity) {
    }
}
