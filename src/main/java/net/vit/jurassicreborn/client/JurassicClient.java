package net.vit.jurassicreborn.client;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.FoliageColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.input.VehicleKeyHandler;
import net.vit.jurassicreborn.client.input.DinosaurKeyHandler;
import net.vit.jurassicreborn.client.render.block.CultivatorRenderer;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterEngineParticle;
import net.vit.jurassicreborn.client.render.entity.vehicle.HelicopterGroundParticle;
import net.vit.jurassicreborn.client.screens.*;
import net.vit.jurassicreborn.client.screens.paleopad.PaleoPadViewDinosaurScreen;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.entities.bugcrate.BugCrateMenu;
import net.vit.jurassicreborn.common.blocks.entities.feeder.FeederMenu;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerMenu;
import net.vit.jurassicreborn.common.blocks.entities.trashcan.TrashCanMenu;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.client.render.RenderingHandler;
import net.vit.jurassicreborn.client.render.block.DisplayBlockRendererWithoutLevel;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerMenu;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.cleaner.CleanerMenu;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.VenomEntity;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.vit.jurassicreborn.common.entities.vehicle.WashingParticle;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.network.ChangeStationMessage;
import net.vit.jurassicreborn.common.network.MicroraptorDismountMessage;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.paleopad.App;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.network.SwitchSeatMessage;
import net.vit.jurassicreborn.common.network.UpdateVehicleControlMessage;
import net.vit.jurassicreborn.common.util.particles.ModParticles;
import net.vit.jurassicreborn.common.util.particles.VenomParticle;

import java.lang.reflect.Constructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JurassicClient {


    public static DisplayBlockRendererWithoutLevel displayBlockRendererWithoutLevel;

    public static void init(IEventBus bus) {
        bus.addListener(JurassicClient::clientSetup);
        bus.addListener(net.vit.jurassicreborn.client.input.VehicleKeyHandler::register);
        bus.addListener(net.vit.jurassicreborn.client.input.DinosaurKeyHandler::register);
        NeoForge.EVENT_BUS.addListener(JurassicClient::clientTickEvent);

        //        Minecraft.getInstance().getSearchTreeManager().register(SearchRegistry.CREATIVE_NAMES, (itemStacks) -> {
//            return new FullTextSearchTree<>((itemStack) -> {
//                return itemStack.getTooltipLines((Player)null, TooltipFlag.Default.NORMAL).stream().map((name) -> {
//                    return ChatFormatting.stripFormatting(name.getString()).trim();
//                }).filter((nameString) -> {
//                    return !nameString.isEmpty();
//                });
//            }, (p_91317_) -> {
//                return Stream.of(Registry.ITEM.getKey(p_91317_.getItem()));
//            }, itemStacks);
//        });

    }




    public static void clientTickEvent(ClientTickEvent.Post evt){
        Level level = Minecraft.getInstance().level;
        Player player = Minecraft.getInstance().player;

        if (DinosaurKeyHandler.MICRORAPTOR_DISMOUNT.consumeClick()) {
            boolean hasLeftMicroraptor = player.getShoulderEntityLeft().getString("id").contains("microraptor");
            boolean hasRightMicroraptor = player.getShoulderEntityRight().getString("id").contains("microraptor");

            if (hasLeftMicroraptor || hasRightMicroraptor) {
                Network.sendToServer(new MicroraptorDismountMessage(-1));
            }
        }

        if (player != null && player.getVehicle() instanceof VehicleEntity) {
            VehicleEntity car = (VehicleEntity) player.getVehicle();
            int seat = car.getSeatForEntity(player);
            if (seat == 0) {
                car.left(Minecraft.getInstance().options.keyLeft.isDown());
                car.right(Minecraft.getInstance().options.keyRight.isDown());
                car.forward(Minecraft.getInstance().options.keyUp.isDown());
                car.backward(Minecraft.getInstance().options.keyDown.isDown());
                byte state = car.getControlState();
                if(state != car.getPreviousState()){
                    Network.sendToServer(new UpdateVehicleControlMessage(car.getId(), state));
                }
                car.setPreviousState(state);
            }
            if (VehicleKeyHandler.SWITCH_SEAT.consumeClick()) {
                Network.sendToServer(new SwitchSeatMessage(car.getId()));
            }
            if (VehicleKeyHandler.NEXT_STATION.consumeClick()) {
                Network.sendToServer(new ChangeStationMessage(car.getId()));
            }
        }


        HashMap<BlockPos, Int2ObjectArrayMap<ItemStack>> newSlotMap = new HashMap<>();

        Network.slotMap.forEach(((blockPos, map) -> {
            if(level != null) {
                if (level.getBlockEntity(blockPos) != null) {
                    newSlotMap.put(blockPos, map);
                }
            }
        }));

        Network.slotMap = newSlotMap;

                       if (level != null && Minecraft.getInstance().isWindowActive()) {
                   }
    }

    // CLIENT-ONLY helper; call during client tick of the projectile or on impact
    public static void spawnVenomParticles(VenomEntity entity) {
        if (!entity.level().isClientSide) return;

        var level = entity.level();
        var rand  = level.getRandom();
        double size = 0.35D;

        for (int i = 0; i < 16; ++i) {
            double ox = (rand.nextDouble() - 0.5D) * size;
            double oy = (rand.nextDouble() - 0.5D) * size;
            double oz = (rand.nextDouble() - 0.5D) * size;

            level.addParticle(
                    ModParticles.VENOM.value(),
                    entity.getX() + ox,
                    entity.getEyeY() + oy,
                    entity.getZ() + oz,
                    0.0D, 0.0D, 0.0D
            );
        }
    }

    public static void openPaleoPad() {
        Minecraft.getInstance().setScreen(new PaleoPadScreen());
    }

    public static void openPaleoPad(App app) {
        Minecraft.getInstance().setScreen(new PaleoPadScreen(app));
    }

    public static void openFeederNameScreen(BlockPos pos) {
        Minecraft.getInstance().setScreen(new FeederNameScreen(pos));
    }

    public static void openOrderMenu(DinosaurEntity entity) {
        Minecraft.getInstance().setScreen(new OrderMenuScreen(entity));
    }
    public static void openPaleoDinosaurPad(DinosaurEntity entity, DinosaurEntity.FieldGuideInfo guideInfo) {
        Minecraft.getInstance().setScreen(new PaleoPadViewDinosaurScreen(entity, guideInfo));
    }
    @SuppressWarnings("removal")
    public static void clientSetup(final FMLClientSetupEvent evt){
        evt.enqueueWork(() -> {

            // BlockEntityRenderer for the **bottom** block entity type
            BlockEntityRenderers.register(
                    ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE.get(),
                    ctx -> new CultivatorRenderer()
            );
        });
//            Minecraft.getInstance().particleEngine.register(
//                    ModParticles.WASHING_DROPLET.get(), WashingParticle.Provider::new);
//            Minecraft.getInstance().particleEngine.register(
//                    ModParticles.HELICOPTER_ENGINE.get(), HelicopterEngineParticle.Provider::new);
//            Minecraft.getInstance().particleEngine.register(
//                    ModParticles.HELICOPTER_GROUND.get(), HelicopterGroundParticle.Provider::new);


//        profilerFiller = Minecraft.getInstance().getProfiler();

        SoundHandler.init();
        //wood type rendering
        evt.enqueueWork(() -> {
            List<? extends Block> ancientLeavesBlocks =
                    ModBlocks.MOD_BLOCKS.getEntries()
                            .stream()
                            .map(DeferredHolder::value)
                            .filter(AncientLeavesBlock.class::isInstance)
                            .toList();

            Minecraft mc = Minecraft.getInstance();
            BlockColors blockColors = mc.getBlockColors();
            ItemColors itemColors = mc.getItemColors();

            Block magnoliaLeaves = WoodBlocks.MAGNOLIA_LEAVES.isBound()
                    ? WoodBlocks.MAGNOLIA_LEAVES.value()
                    : null;

            for (Block block : ancientLeavesBlocks) {
                if (block == magnoliaLeaves) {
                    blockColors.register((state, access, pos, tintIndex) -> 0xFFFFFF, block);
                    itemColors.register((stack, tintIndex) -> 0xFFC0CB, block);
                } else {
                    blockColors.register(
                            (state, access, pos, tintIndex) ->
                                    pos == null
                                            ? FoliageColor.getDefaultColor()
                                            : BiomeColors.getAverageFoliageColor(access, pos),
                            block
                    );
                    itemColors.register(
                            (stack, tintIndex) -> FoliageColor.getDefaultColor(),
                            block
                    );
                }
            }

                // TOUR_RAIL color handlers (if config enabled)

            Sheets.addWoodType(ModWoodTypes.araucaria);
            Sheets.addWoodType(ModWoodTypes.calamites);
            Sheets.addWoodType(ModWoodTypes.ginkgo);
            Sheets.addWoodType(ModWoodTypes.phoenix);
            Sheets.addWoodType(ModWoodTypes.psaronius);
            Sheets.addWoodType(ModWoodTypes.magnolia);

        });


        Minecraft.getInstance().particleEngine.register(
                ModParticles.VENOM.value(),
                VenomParticle.Provider::new
        );
        Minecraft.getInstance().particleEngine.register(
                ModParticles.WASHING_DROPLET.value(),
                WashingParticle.Provider::new
        );
        Minecraft.getInstance().particleEngine.register(
                ModParticles.HELICOPTER_ENGINE.value(),
                HelicopterEngineParticle.Provider::new
        );
        Minecraft.getInstance().particleEngine.register(
                ModParticles.HELICOPTER_GROUND.value(),
                HelicopterGroundParticle.Provider::new
        );
        //Binding screens to types

//        ModScreens.<FossilGrinderBlockEntity, FossilGrinderMenu, FossilGrinderScreen>register(ModBlockEntities.FOSSIL_GRINDER_BLOCK_ENTITY.get(), FossilGrinderScreen::new);


        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DICKSONIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DICROIDIUM_ZUBERI.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AJUGINUCULA_SMITHII.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_ONION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRACILARIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DICTYOPHYLLUM.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WEST_INDIAN_LILAC.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SERENNA_VERIFORMANS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LADINIA_SIMPLEX.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ORONTIUM_MACKII.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.UMALTOLEPIS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LIRIODENDRITES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RAPHAELIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENCEPHALARTOS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WILD_POTATO_PLANT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RHAMNUS_SALICIFOLIUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CINNAMON_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.BRISTLE_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.TEMPSKYA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.WOOLLY_STALKED_BEGONIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LARGESTIPULE_LEATHER_ROOT.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.RHACOPHYTON.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GRAMINIDITES_BAMBUSOIDES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.HELICONIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMALL_ROYAL_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMALL_CHAIN_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SMALL_CYCAD.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.KRILL_SWARM.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PLANKTON_SWARM.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CYCADEOIDEA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CRY_PANSY.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SCALY_TREE_FERN.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ZAMITES.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ENALLHELIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AULOPORA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLADOCHONUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LITHOSTROTION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.STYLOPHYLLOPSIS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.HIPPURITES_RADIOSUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_AULOPORA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_CLADOCHONUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_ENALLHELIA.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_LITHOSTROTION.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_HIPPURITES_RADIOSUS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.DEAD_STYLOPHYLLOPSIS.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AMBER_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AMBER_MOSQUITO.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.AMBER_APHID.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SEA_LAMPREY.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.FROZEN_LEECH.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLEAR_GLASS.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CLEAR_GLASS_PANE.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.ARAUCARIA_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.CALAMITES_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.GINKGO_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.MAGNOLIA_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PHOENIX_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.PSARONIUS_SAPLING.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.REINFORCED_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.SECURITY_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.ARAUCARIA_DOOR.get(),RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.CALAMITES_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.GINKGO_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.MAGNOLIA_DOOR.get(),RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PHOENIX_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PSARONIUS_DOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.ARAUCARIA_TRAPDOOR.get(),RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.CALAMITES_TRAPDOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.GINKGO_TRAPDOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.MAGNOLIA_TRAPDOOR.get(),RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PHOENIX_TRAPDOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PSARONIUS_TRAPDOOR.get(),RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.ARAUCARIA_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.CALAMITES_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.GINKGO_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.MAGNOLIA_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PHOENIX_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(WoodBlocks.PSARONIUS_LEAVES.get(),RenderType.cutoutMipped());
        ItemBlockRenderTypes.setRenderLayer(ModBlocks.LOW_SECURITY_FENCE_POLE.get(), RenderType.cutout());



        //        NonNullList<Supplier<ItemStack>> FOSSILS = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> ENCASED_FOSSILS = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> SOFT_TISSUES = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> DNA = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> HATCHED_EGGS = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> EGGS = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> STEAKS = NonNullList.create();
//        NonNullList<Supplier<ItemStack>> MEATS = NonNullList.create();

//        NonNullList<ItemStack> BONES = NonNullList.withSize(ModItems.ALL_BONES.size(), ItemStack.EMPTY);
//        NonNullList<ItemStack> FOSSIL_BONES = NonNullList.withSize(ModItems.BONES.)

//        Dinosaur.DINOSAURS.forEach((id, dino) -> {
//            FOSSILS.add(() -> FaunaFossilBlockItem.setDino(ModItems.FAUNA_FOSSIL_BLOCK.get().getDefaultInstance(), dino) );
//            ENCASED_FOSSILS.add(() -> EncasedFaunaFossilBlockItem.setDino(ModItems.ENCASED_FAUNA_FOSSIL.get().getDefaultInstance(), dino) );
//            SOFT_TISSUES.add(() -> DinosaurItem.setDino(ModItems.SOFT_TISSUE.get(dino).get().getDefaultInstance(), dino) );
//
//            DNA.add(() -> {
//                ItemStack defaultDNAItem = ModItems.DINOSAUR_DNA.get(dino).get().getDefaultInstance();
//
//                defaultDNAItem.getOrCreateTag().putBoolean("isCreative", true);
//
//                return defaultDNAItem;
//            });
//
//            HATCHED_EGGS.add(() -> {
//                ItemStack defaultDNAItem = ModItems.hatchedDinoEggs.get(dino).get().getDefaultInstance();
//
//                defaultDNAItem.getOrCreateTag().putBoolean("isCreative", true);
//
//                return defaultDNAItem;
//            });
//            var eggItem = ModItems.dinoEggs.get(dino);
//            if(eggItem != null) {
//
//                EGGS.add(() -> {
//
//
//                    ItemStack defaultDNAItem = eggItem.get().getDefaultInstance();
//
//                    defaultDNAItem.getOrCreateTag().putBoolean("isCreative", true);
//
//                    return defaultDNAItem;
//                });
//            }
//
//            STEAKS.add(() -> ModItems.STEAKS.get(dino).get().getDefaultInstance());
//            MEATS.add(() -> ModItems.MEATS.get(dino).get().getDefaultInstance());
//        });
//        for (int i = 0; i < ModItems.ALL_BONES.size(); i++) {
//            BONES.set(i, ModItems.ALL_BONES.get(i).get().getDefaultInstance());
//        }

//        var filteredDNA = DNA.stream().filter((dnaSup) -> DNA.stream().filter(dnaSup::equals).count() == 1).collect(Collectors.toCollection(NonNullList::create));

//        ItemStackCreativeModeTabSystem.addItemStacksToTab(TabHandler.FOSSILS, FOSSILS, ENCASED_FOSSILS)
//        ItemStackCreativeModeTabSystem.addItemStacksToTab(TabHandler.DNA, SOFT_TISSUES, filteredDNA, HATCHED_EGGS, EGGS);
//        ItemStackCreativeModeTabSystem.addItemStacksToTab(TabHandler.FOODS, STEAKS, MEATS);
//        ItemStackCreativeModeTabSystem.addItemStacksToTab(TabHandler.FOSSILS, ENCASED_FOSSILS);



    }
}
