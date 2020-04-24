package mod.reborn.client.proxy;

import mod.reborn.client.gui.*;
import mod.reborn.client.model.RebornTabulaModelHandler;
import mod.reborn.server.block.entity.*;
import net.ilexiconn.llibrary.client.lang.LanguageHandler;
import net.ilexiconn.llibrary.server.util.WebUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.RebornMod;
import mod.reborn.client.event.ClientEventHandler;
import mod.reborn.client.render.RenderingHandler;
import mod.reborn.client.sound.CarSound;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.VenomEntity;
import mod.reborn.server.entity.particle.VenomParticle;
import mod.reborn.server.entity.vehicle.VehicleEntity;
import mod.reborn.server.event.KeyBindingHandler;
import mod.reborn.server.item.JournalItem;
import mod.reborn.server.proxy.ServerProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SideOnly(Side.CLIENT)
public class ClientProxy extends ServerProxy {
    public static final Minecraft MC = Minecraft.getMinecraft();
    private static KeyBindingHandler keyHandler = new KeyBindingHandler();
    public static final List<UUID> MEMBERS = new ArrayList<>();
    public static final List<UUID> TESTERS = new ArrayList<>();

    @Override
    @SuppressWarnings("unchecked")
    public void onPreInit(FMLPreInitializationEvent event) {
        super.onPreInit(event);

        KeyBindingHandler.init();

        try {
            LanguageHandler.INSTANCE.loadRemoteLocalization(RebornMod.MODID);
        } catch (Exception e) {
            RebornMod.getLogger().error("Failed to load remote localizations", e);
        }

        ClientEventHandler eventHandler = new ClientEventHandler();
        MinecraftForge.EVENT_BUS.register(eventHandler);
        MinecraftForge.EVENT_BUS.register(RenderingHandler.INSTANCE);
        RenderingHandler.INSTANCE.preInit();
        ModelLoaderRegistry.registerLoader(RebornTabulaModelHandler.INSTANCE);
        RebornTabulaModelHandler.INSTANCE.addDomain(RebornMod.MODID);
    }

    public static void playHelicopterSound(VehicleEntity entity) {
        MC.getSoundHandler().playSound(new CarSound(entity));
    }

    @Override
    public void onInit(FMLInitializationEvent event) {
        super.onInit(event);
        RenderingHandler.INSTANCE.init();
    }

    @Override
    public void onPostInit(FMLPostInitializationEvent event) {
        super.onPostInit(event);

        RenderingHandler.INSTANCE.postInit();

        new Thread(() -> {
            List<String> modmembers = WebUtils.readPastebinAsList("U5UTKQRk");
            List<String> bugtesters = WebUtils.readPastebinAsList("9H4eiu0S");//fgJQkCMa
            if (modmembers != null) {
                for (String members : modmembers) {
                    MEMBERS.add(UUID.fromString(members));
                }
            } if (bugtesters != null) {
                for (String testers : bugtesters) {
                    TESTERS.add(UUID.fromString(testers));
                }
            }
        }).start();
    }

    @Override
    public EntityPlayer getPlayer() {
        return MC.player;
    }

    @Override
    public EntityPlayer getPlayerEntityFromContext(MessageContext ctx) {
        return (ctx.side.isClient() ? this.getPlayer() : super.getPlayerEntityFromContext(ctx));
    }

    @Override
    public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        TileEntity tile = world.getTileEntity(pos);
        if (tile != null) {
            if (tile instanceof CleaningStationBlockEntity && id == GUI_CLEANING_STATION_ID) {
                return new CleaningStationGui(player.inventory, (CleaningStationBlockEntity) tile);
            } else if (tile instanceof FossilGrinderBlockEntity && id == GUI_FOSSIL_GRINDER_ID) {
                return new FossilGrinderGui(player.inventory, (FossilGrinderBlockEntity) tile);
            } else if (tile instanceof DNASequencerBlockEntity && id == GUI_DNA_SEQUENCER_ID) {
                return new DNASequencerGui(player.inventory, (DNASequencerBlockEntity) tile);
            } else if (tile instanceof EmbryonicMachineBlockEntity && id == GUI_EMBRYONIC_MACHINE_ID) {
                return new EmbryonicMachineGui(player.inventory, (EmbryonicMachineBlockEntity) tile);
            } else if (tile instanceof EmbryoCalcificationMachineBlockEntity && id == GUI_EMBRYO_CALCIFICATION_MACHINE_ID) {
                return new EmbryoCalcificationMachineGui(player.inventory, (EmbryoCalcificationMachineBlockEntity) tile);
            } else if (tile instanceof DNASynthesizerBlockEntity && id == GUI_DNA_SYNTHESIZER_ID) {
                return new DNASynthesizerGui(player.inventory, (DNASynthesizerBlockEntity) tile);
            } else if (tile instanceof IncubatorBlockEntity && id == GUI_INCUBATOR_ID) {
                return new IncubatorGui(player.inventory, (IncubatorBlockEntity) tile);
            } else if (tile instanceof DNACombinatorHybridizerBlockEntity && id == GUI_DNA_COMBINATOR_HYBRIDIZER_ID) {
                return new DNACombinatorHybridizerGui(player.inventory, (DNACombinatorHybridizerBlockEntity) tile);
            } else if (tile instanceof DNAExtractorBlockEntity && id == GUI_DNA_EXTRACTOR_ID) {
                return new DNAExtractorGui(player.inventory, (DNAExtractorBlockEntity) tile);
            } else if (tile instanceof CultivatorBlockEntity && id == GUI_CULTIVATOR_ID) {
                CultivatorBlockEntity cultivator = (CultivatorBlockEntity) tile;
                if (cultivator.isProcessing(0)) {
                    return new CultivateProcessGui(player.inventory, cultivator);
                } else {
                    return new CultivateGui(player.inventory, cultivator);
                }
            } else if (tile instanceof FeederBlockEntity && id == GUI_FEEDER_ID) {
                return new FeederGui(player.inventory, (FeederBlockEntity) tile);
            } else if (tile instanceof BugCrateBlockEntity && id == GUI_BUG_CRATE) {
                return new BugCrateGui(player.inventory, (BugCrateBlockEntity) tile);
            }
        }
        if(id == GUI_SKELETON_ASSEMBLER){
            return new SkeletonAssemblyGui(SkeletonAssemblyGui.createContainer(player.inventory, world, pos));
        }
        return null;
    }
    
    public static KeyBindingHandler getKeyHandler() {
		return keyHandler;
	}

    @Override
    public void openSelectDino(BlockPos pos, EnumFacing facing, EnumHand hand) {
        MC.displayGuiScreen(new SelectDinoGui(pos, facing, hand));
    }

    @Override
    public void openOrder(DinosaurEntity entity) {
        MC.displayGuiScreen(new OrderDinosaurGui(entity));
    }

    @Override
    public void openFieldGuide(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo fieldGuideInfo) {
        MC.displayGuiScreen(new FieldGuideGui(entity, fieldGuideInfo));
    }

    @Override
    public void openPaleoDinosaurPad(DinosaurEntity entity) {
        MC.displayGuiScreen(new PaleoPadViewDinosaurGui(entity));
    }

    @Override
    public void openPaleoPad() {
        MC.displayGuiScreen(new PaleoPadGui());
    }

    @Override
    public void openJournal(JournalItem.JournalType type) {
        MC.displayGuiScreen(new JournalGui(type));
    }

    public static void playCarSound(VehicleEntity entity) {
        MC.getSoundHandler().playSound(new CarSound(entity));
    }

    public static void stopSound(ISound sound) {
        MC.getSoundHandler().stopSound(sound);
    }

    public static void spawnVenomParticles(VenomEntity entity) {
        ParticleManager particleManager = Minecraft.getMinecraft().effectRenderer;
        float size = 0.35F;
        for (int i = 0; i < 16; ++i) {
            particleManager.addEffect(new VenomParticle(entity.world, size * Math.random() - size / 2, size * Math.random() - size / 2, size * Math.random() - size / 2, 0.0F, 0.0F, 0.0F, 1.0F, entity));
        }
    }

}
