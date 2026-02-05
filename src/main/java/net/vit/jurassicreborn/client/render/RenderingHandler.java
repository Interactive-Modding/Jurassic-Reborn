package net.vit.jurassicreborn.client.render;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.PaintingRenderer;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.block.*;
import net.vit.jurassicreborn.client.render.entity.*;
import net.vit.jurassicreborn.client.render.entity.BenchSeatRenderer;
import net.vit.jurassicreborn.client.render.entity.animation.entity.*;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.client.render.entity.vehicle.*;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Function;
import java.util.function.Supplier;

import static net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler.*;

import net.vit.jurassicreborn.client.render.entity.model.JurassicBoatModelLayers;
import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;


@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value=Dist.CLIENT)
public class RenderingHandler{
    public static final RenderingHandler INSTANCE = new RenderingHandler();
    private float thirdPersonViewDistance = 4.0f;

    public float getThirdPersonViewDistance() {
        return thirdPersonViewDistance;
    }

    public void setThirdPersonViewDistance(float dist) {
        this.thirdPersonViewDistance = dist;
    }

    public void resetThirdPersonViewDistance() {
        this.thirdPersonViewDistance = 4.0f;
    }
    public static ArrayList<EntityAnimator<? extends LivingEntity>> ANIMATORS = new ArrayList<>();

    @SubscribeEvent
    public static void registerEntityRendersEvent(EntityRenderersEvent.@NotNull RegisterRenderers event){

        DinosaurHandler.doDinosInit();

        helper.doEntityRegistration(event);
        event.registerEntityRenderer(ModEntities.TRANQUILIZER_DART.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(ModEntities.PARK_BENCH_SEAT_LEFT.get(),  BenchSeatRenderer::new);
        event.registerEntityRenderer(ModEntities.PARK_BENCH_SEAT_RIGHT.get(), BenchSeatRenderer::new);
        EntityRenderers.register(ModEntities.BLUEPRINT_PAINTING.get(), BlueprintRenderer::new);
        EntityRenderers.register(ModEntities.MURAL_PAINTING.get(), ctx -> new PaintingRenderer(ctx));
        EntityRenderers.register(ModEntities.PADDOCK_SIGN.get(),  PaddockSignRenderer::new);
        EntityRenderers.register(ModEntities.ATTRACTION_SIGN.get(), AttractionSignRenderer::new);
        EntityRenderers.register(ModEntities.FORD_EXPLORER.get(), FordExplorerRenderer::new);
        EntityRenderers.register(ModEntities.FORD_EXPLORER_SNOW.get(), FordExplorerSnowRenderer::new);
        EntityRenderers.register(ModEntities.MONORAIL.get(), MonorailRenderer::new);
        EntityRenderers.register(ModEntities.GYROSPHERE.get(), GyrosphereRenderer::new);
        EntityRenderers.register(ModEntities.JEEP_WRANGLER.get(), JeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.BLACK_JEEP_WRANGLER.get(), BlackJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.BLUE_JEEP_WRANGLER.get(), BlueJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.GREEN_JEEP_WRANGLER.get(), GreenJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.LIME_JEEP_WRANGLER.get(), LimeJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.PINK_JEEP_WRANGLER.get(), PinkJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.PURPLE_JEEP_WRANGLER.get(), PurpleJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.SORNA_JEEP_WRANGLER.get(), SornaJeepWranglerRenderer::new);
        EntityRenderers.register(ModEntities.HELICOPTER.get(), HeliRenderer::new);
        EntityRenderers.register(ModEntities.JURASSIC_BOAT.get(), ctx -> new JurassicBoatRenderer<>(ctx, false));
        EntityRenderers.register(ModEntities.JURASSIC_CHEST_BOAT.get(), ctx -> new JurassicBoatRenderer<>(ctx, true));


        event.registerEntityRenderer(ModEntities.CRAB.get(), CrabEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.GOAT.get(), GoatEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.SHARK.get(), (ctx) ->
                new SharkEntityRenderer(ctx, 1.6f)
                );
        event.registerBlockEntityRenderer(ModBlockEntities.SKULL_DISPLAY_BLOCK_ENTITY.get(), helper.makeProvider(SkullDisplayRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.DISPLAY_BLOCK_ENTITY.get(), helper.makeProvider(DisplayBlockEntityRender::new));
        event.registerBlockEntityRenderer(ModBlockEntities.HOLOGRAM_BLOCK_ENTITY.get(), helper.makeProvider(HologramBlockEntityRender::new));
        event.registerBlockEntityRenderer(ModBlockEntities.CLEANING_STATION.get(), helper.makeProvider(CleaningStationRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.DNA_EXTRACTOR_BLOCK_ENTITY.get(), helper.makeProvider(DNAExtractorRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.BASE_FENCE_BLOCK_ENTITY.get(), helper.makeProvider(FenceBlockRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.INCUBATOR_BLOCK_ENTITY.get(), helper.makeProvider(IncubatorRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.SKELETON_ASSEMBLY_ENTITY.get(), helper.makeProvider(SkeletonAssemblyRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.DNA_SEQUENCER_BLOCK_ENTITY.get(), helper.makeProvider(DNASequencerRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.TOUR_RAIL_BLOCK_ENTITY.get(), helper.makeProvider(TourRailBlockEntityRenderer::new));
        event.registerBlockEntityRenderer(ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE.get(), helper.makeProvider(CultivatorRenderer::new));
        event.registerEntityRenderer(ModEntities.DINOSAUR_EGG.get(), DinosaurEggRenderer::new);




    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (ModBoatType type : ModBoatType.values()) {
            // Plain boat layer (no chest)
            event.registerLayerDefinition(
                    JurassicBoatModelLayers.createBoatModelName(type),
                    () -> BoatModel.createBodyModel()
            );
            // Chest-boat layer (with chest)
            event.registerLayerDefinition(
                    JurassicBoatModelLayers.createChestBoatModelName(type),
                    () -> ChestBoatModel.createBodyModel()
            );
        }
    }


//    static {
//        System.out.println("Loaded!");
//    }




    static class helper{

        public static <T extends BlockEntity> BlockEntityRendererProvider<T> makeProvider(Supplier<BlockEntityRenderer<T>> renderer){
            return new BlockEntityRendererProvider<T>() {
                @Override
                public BlockEntityRenderer<T> create(Context pContext) {
                    return renderer.get();
                }
            };
        }
        public static <T extends BlockEntity> BlockEntityRendererProvider<T> makeProvider(Function<BlockEntityRendererProvider.Context, BlockEntityRenderer<T>> renderer){
            return new BlockEntityRendererProvider<T>() {
                @Override
                public BlockEntityRenderer<T> create(Context pContext) {
                    return renderer.apply(pContext);
                }
            };
        }

        static void doEntityRegistration(final EntityRenderersEvent.RegisterRenderers event){


            //OviraptorEntity registration: auto generated
            DinosaurRenderInfo OviraptorInfo = new DinosaurRenderInfo(OVIRAPTOR, new OviraptorAnimator());
            event.registerEntityRenderer(ModEntities.OVIRAPTOR.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(OviraptorInfo),
                            0.5f,
                            OviraptorInfo));

//DeinotheriumEntity registration: auto generated
            DinosaurRenderInfo DeinotheriumInfo = new DinosaurRenderInfo(DEINOTHERIUM, new DeinotheriumAnimator());
            event.registerEntityRenderer(ModEntities.DEINOTHERIUM.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DeinotheriumInfo),
                            0.5f,
                            DeinotheriumInfo));

//MicroraptorEntity registration: auto generated
            DinosaurRenderInfo MicroraptorInfo = new DinosaurRenderInfo(MICRORAPTOR, new MicroraptorAnimator());
            event.registerEntityRenderer(ModEntities.MICRORAPTOR.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MicroraptorInfo),
                            0.5f,
                            MicroraptorInfo));

//MammothEntity registration: auto generated
            DinosaurRenderInfo MammothInfo = new DinosaurRenderInfo(MAMMOTH, new MammothAnimator());
            event.registerEntityRenderer(ModEntities.MAMMOTH.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MammothInfo),
                            0.5f,
                            MammothInfo));

//DodoEntity registration: auto generated
            DinosaurRenderInfo DodoInfo = new DinosaurRenderInfo(DODO, new DodoAnimator());
            event.registerEntityRenderer(ModEntities.DODO.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DodoInfo),
                            0.5f,
                            DodoInfo));

//ZhenyuanopterusEntity registration: auto generated
            DinosaurRenderInfo ZhenyuanopterusInfo = new DinosaurRenderInfo(ZHENYUANOPTERUS, new ZhenyuanopterusAnimator());
            event.registerEntityRenderer(ModEntities.ZHENYUANOPTERUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ZhenyuanopterusInfo),
                            0.5f,
                            ZhenyuanopterusInfo));

//PostosuchusEntity registration: auto generated
            DinosaurRenderInfo PostosuchusInfo = new DinosaurRenderInfo(POSTOSUCHUS, new PostosuchusAnimator());
            event.registerEntityRenderer(ModEntities.POSTOSUCHUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(PostosuchusInfo),
                            0.5f,
                            PostosuchusInfo));

//IndoraptorEntity registration: auto generated
            DinosaurRenderInfo IndoraptorInfo = new DinosaurRenderInfo(INDORAPTOR, new IndoraptorAnimator());
            event.registerEntityRenderer(ModEntities.INDORAPTOR.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(IndoraptorInfo),
                            0.5f,
                            IndoraptorInfo));

//OthnieliaEntity registration: auto generated
            DinosaurRenderInfo OthnieliaInfo = new DinosaurRenderInfo(OTHNIELIA, new OthnieliaAnimator());
            event.registerEntityRenderer(ModEntities.OTHNIELIA.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(OthnieliaInfo),
                            0.5f,
                            OthnieliaInfo));

//PteranodonEntity registration: auto generated
            DinosaurRenderInfo PteranodonInfo = new DinosaurRenderInfo(PTERANODON, new PteranodonAnimator());
            event.registerEntityRenderer(ModEntities.PTERANODON.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(PteranodonInfo),
                            0.5f,
                            PteranodonInfo));

//IndominusEntity registration: auto generated
            DinosaurRenderInfo IndominusInfo = new DinosaurRenderInfo(INDOMINUS, new IndominusAnimator());
            event.registerEntityRenderer(ModEntities.INDOMINUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(IndominusInfo),
                            0.5f,
                            IndominusInfo));

//AnkylosaurusEntity registration: auto generated
            DinosaurRenderInfo AnkylosaurusInfo = new DinosaurRenderInfo(ANKYLOSAURUS, new AnkylosaurusAnimator());
            event.registerEntityRenderer(ModEntities.ANKYLOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AnkylosaurusInfo),
                            0.5f,
                            AnkylosaurusInfo));

//ArsinoitheriumEntity registration: auto generated
            DinosaurRenderInfo ArsinoitheriumInfo = new DinosaurRenderInfo(ARSINOITHERIUM, new ArsinoitheriumAnimator());
            event.registerEntityRenderer(ModEntities.ARSINOITHERIUM.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ArsinoitheriumInfo),
                            0.5f,
                            ArsinoitheriumInfo));

//CrassigyrinusEntity registration: auto generated
            DinosaurRenderInfo CrassigyrinusInfo = new DinosaurRenderInfo(CRASSIGYRINUS, new CrassigyrinusAnimator());
            event.registerEntityRenderer(ModEntities.CRASSIGYRINUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CrassigyrinusInfo),
                            0.5f,
                            CrassigyrinusInfo));

//PerisphinctesEntity registration: auto generated
            DinosaurRenderInfo PerisphinctesInfo = new DinosaurRenderInfo(PERISPHINCTES, new PerisphinctesAnimator());
            event.registerEntityRenderer(ModEntities.PERISPHINCTES.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(PerisphinctesInfo),
                            0.5f,
                            PerisphinctesInfo));

//ProceratosaurusEntity registration: auto generated
            DinosaurRenderInfo ProceratosaurusInfo = new DinosaurRenderInfo(PROCERATOSAURUS, new ProceratosaurusAnimator());
            event.registerEntityRenderer(ModEntities.PROCERATOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ProceratosaurusInfo),
                            0.5f,
                            ProceratosaurusInfo));

//ApatosaurusEntity registration: auto generated
            DinosaurRenderInfo ApatosaurusInfo = new DinosaurRenderInfo(APATOSAURUS, new ApatosaurusAnimator());
            event.registerEntityRenderer(ModEntities.APATOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ApatosaurusInfo),
                            0.5f,
                            ApatosaurusInfo));

//CarnotaurusEntity registration: auto generated
            DinosaurRenderInfo CarnotaurusInfo = new DinosaurRenderInfo(CARNOTAURUS, new CarnotaurusAnimator());
            event.registerEntityRenderer(ModEntities.CARNOTAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CarnotaurusInfo),
                            0.5f,
                            CarnotaurusInfo));

//DunkleosteusEntity registration: auto generated
            DinosaurRenderInfo DunkleosteusInfo = new DinosaurRenderInfo(DUNKLEOSTEUS, new DunkleosteusAnimator());
            event.registerEntityRenderer(ModEntities.DUNKLEOSTEUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DunkleosteusInfo),
                            0.5f,
                            DunkleosteusInfo));

//TyrannosaurusEntity registration: auto generated
            DinosaurRenderInfo TyrannosaurusInfo = new DinosaurRenderInfo(TYRANNOSAURUS, new TyrannosaurusAnimator());
            event.registerEntityRenderer(ModEntities.TYRANNOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TyrannosaurusInfo),
                            0.5f,
                            TyrannosaurusInfo));

//RaphusrexEntity registration: auto generated
            DinosaurRenderInfo RaphusrexInfo = new DinosaurRenderInfo(RAPHUSREX, new RaphusrexAnimator());
            event.registerEntityRenderer(ModEntities.RAPHUSREX.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(RaphusrexInfo),
                            0.5f,
                            RaphusrexInfo));

//ChasmosaurusEntity registration: auto generated
            DinosaurRenderInfo ChasmosaurusInfo = new DinosaurRenderInfo(CHASMOSAURUS, new ChasmosaurusAnimator());
            event.registerEntityRenderer(ModEntities.CHASMOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ChasmosaurusInfo),
                            0.5f,
                            ChasmosaurusInfo));

//MetriacanthosaurusEntity registration: auto generated
            DinosaurRenderInfo MetriacanthosaurusInfo = new DinosaurRenderInfo(METRIACANTHOSAURUS, new MetriacanthosaurusAnimator());
            event.registerEntityRenderer(ModEntities.METRIACANTHOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MetriacanthosaurusInfo),
                            0.5f,
                            MetriacanthosaurusInfo));

//TroodonEntity registration: auto generated
            DinosaurRenderInfo TroodonInfo = new DinosaurRenderInfo(TROODON, new TroodonAnimator());
            event.registerEntityRenderer(ModEntities.TROODON.get(), (ctx) ->
                    new TroodonRenderer(ctx,
                            getDefaultModelFromDinosaur(TroodonInfo),
                            0.5f,
                            TroodonInfo));

//HerrerasaurusEntity registration: auto generated
            DinosaurRenderInfo HerrerasaurusInfo = new DinosaurRenderInfo(HERRERASAURUS, new HerrerasaurusAnimator());
            event.registerEntityRenderer(ModEntities.HERRERASAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(HerrerasaurusInfo),
                            0.5f,
                            HerrerasaurusInfo));

//BaryonyxEntity registration: auto generated
            DinosaurRenderInfo BaryonyxInfo = new DinosaurRenderInfo(BARYONYX, new BaryonyxAnimator());
            event.registerEntityRenderer(ModEntities.BARYONYX.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(BaryonyxInfo),
                            0.5f,
                            BaryonyxInfo));

//BeelzebufoEntity registration: auto generated
            DinosaurRenderInfo BeelzebufoInfo = new DinosaurRenderInfo(BEELZEBUFO, new BeelzebufoAnimator());
            event.registerEntityRenderer(ModEntities.BEELZEBUFO_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(BeelzebufoInfo),
                            0.5f,
                            BeelzebufoInfo));

//VelociraptorBlueEntity registration: auto generated
            DinosaurRenderInfo VelociraptorBlueInfo = new DinosaurRenderInfo(BLUE, new VelociraptorBlueAnimator());
            event.registerEntityRenderer(ModEntities.VELOCIRAPTORBLUE_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VelociraptorBlueInfo),
                            0.5f,
                            VelociraptorBlueInfo));

//VelociraptorEchoEntity registration: auto generated
            DinosaurRenderInfo VelociraptorEchoInfo = new DinosaurRenderInfo(ECHO, new VelociraptorEchoAnimator());
            event.registerEntityRenderer(ModEntities.VELOCIRAPTORECHO_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VelociraptorEchoInfo),
                            0.5f,
                            VelociraptorEchoInfo));

//SinoceratopsEntity registration: auto generated
            DinosaurRenderInfo SinoceratopsInfo = new DinosaurRenderInfo(SINOCERATOPS, new SinoceratopsAnimator());
            event.registerEntityRenderer(ModEntities.SINOCERATOPS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SinoceratopsInfo),
                            0.5f,
                            SinoceratopsInfo));

//ParasaurolophusEntity registration: auto generated
            DinosaurRenderInfo ParasaurolophusInfo = new DinosaurRenderInfo(PARASAUROLOPHUS, new ParasaurolophusAnimator());
            event.registerEntityRenderer(ModEntities.PARASAUROLOPHUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ParasaurolophusInfo),
                            0.5f,
                            ParasaurolophusInfo));

//MamenchisaurusEntity registration: auto generated
            DinosaurRenderInfo MamenchisaurusInfo = new DinosaurRenderInfo(MAMENCHISAURUS, new MamenchisaurusAnimator());
            event.registerEntityRenderer(ModEntities.MAMENCHISAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MamenchisaurusInfo),
                            0.5f,
                            MamenchisaurusInfo));

//DimorphodonEntity registration: auto generated
            DinosaurRenderInfo DimorphodonInfo = new DinosaurRenderInfo(DIMORPHODON, new DimorphodonAnimator());
            event.registerEntityRenderer(ModEntities.DIMORPHODON_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DimorphodonInfo),
                            0.5f,
                            DimorphodonInfo));

//AllosaurusEntity registration: auto generated
            DinosaurRenderInfo AllosaurusInfo = new DinosaurRenderInfo(ALLOSAURUS, new AllosaurusAnimator());
            event.registerEntityRenderer(ModEntities.ALLOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AllosaurusInfo),
                            0.5f,
                            AllosaurusInfo));

//MosasaurusEntity registration: auto generated
            DinosaurRenderInfo MosasaurusInfo = new DinosaurRenderInfo(MOSASAURUS, new MosasaurusAnimator());
            event.registerEntityRenderer(ModEntities.MOSASAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MosasaurusInfo),
                            0.5f,
                            MosasaurusInfo));

//MawsoniaEntity registration: auto generated
            DinosaurRenderInfo MawsoniaInfo = new DinosaurRenderInfo(MAWSONIA, new MawsoniaAnimator());
            event.registerEntityRenderer(ModEntities.MAWSONIA_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MawsoniaInfo),
                            0.5f,
                            MawsoniaInfo));

//VelociraptorDeltaEntity registration: auto generated
            DinosaurRenderInfo VelociraptorDeltaInfo = new DinosaurRenderInfo(DELTA, new VelociraptorDeltaAnimator());
            event.registerEntityRenderer(ModEntities.VELOCIRAPTORDELTA.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VelociraptorDeltaInfo),
                            0.5f,
                            VelociraptorDeltaInfo));

//AlvarezsaurusEntity registration: auto generated
            DinosaurRenderInfo AlvarezsaurusInfo = new DinosaurRenderInfo(ALVAREZSAURUS, new AlvarezsaurusAnimator());
            event.registerEntityRenderer(ModEntities.ALVAREZSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AlvarezsaurusInfo),
                            0.5f,
                            AlvarezsaurusInfo));

//RugopsEntity registration: auto generated
            DinosaurRenderInfo RugopsInfo = new DinosaurRenderInfo(RUGOPS, new RugopsAnimator());
            event.registerEntityRenderer(ModEntities.RUGOPS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(RugopsInfo),
                            0.5f,
                            RugopsInfo));

//CearadactylusEntity registration: auto generated
            DinosaurRenderInfo CearadactylusInfo = new DinosaurRenderInfo(CEARADACTYLUS, new CearadactylusAnimator());
            event.registerEntityRenderer(ModEntities.CEARADACTYLUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CearadactylusInfo),
                            0.5f,
                            CearadactylusInfo));

//CorythosaurusEntity registration: auto generated
            DinosaurRenderInfo CorythosaurusInfo = new DinosaurRenderInfo(CORYTHOSAURUS, new CorythosaurusAnimator());
            event.registerEntityRenderer(ModEntities.CORYTHOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CorythosaurusInfo),
                            0.5f,
                            CorythosaurusInfo));

//CompsognathusEntity registration: auto generated
            DinosaurRenderInfo CompsognathusInfo = new DinosaurRenderInfo(COMPSOGNATHUS, new CompsognathusAnimator());
            event.registerEntityRenderer(ModEntities.COMPSOGNATHUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CompsognathusInfo),
                            0.5f,
                            CompsognathusInfo));

//LudodactylusEntity registration: auto generated
            DinosaurRenderInfo LudodactylusInfo = new DinosaurRenderInfo(LUDODACTYLUS, new LudodactylusAnimator());
            event.registerEntityRenderer(ModEntities.LUDODACTYLUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(LudodactylusInfo),
                            0.5f,
                            LudodactylusInfo));

//LeaellynasauraEntity registration: auto generated
            DinosaurRenderInfo LeaellynasauraInfo = new DinosaurRenderInfo(LEAELLYNASAURA, new LeaellynasauraAnimator());
            event.registerEntityRenderer(ModEntities.LEAELLYNASAURA_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(LeaellynasauraInfo),
                            0.5f,
                            LeaellynasauraInfo));

//MoganopterusEntity registration: auto generated
            DinosaurRenderInfo MoganopterusInfo = new DinosaurRenderInfo(MOGANOPTERUS, new MoganopterusAnimator());
            event.registerEntityRenderer(ModEntities.MOGANOPTERUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MoganopterusInfo),
                            0.5f,
                            MoganopterusInfo));

//SuchomimusEntity registration: auto generated
            DinosaurRenderInfo SuchomimusInfo = new DinosaurRenderInfo(SUCHOMIMUS, new SuchomimusAnimator());
            event.registerEntityRenderer(ModEntities.SUCHOMIMUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SuchomimusInfo),
                            0.5f,
                            SuchomimusInfo));

//MajungasaurusEntity registration: auto generated
            DinosaurRenderInfo MajungasaurusInfo = new DinosaurRenderInfo(MAJUNGASAURUS, new MajungasaurusAnimator());
            event.registerEntityRenderer(ModEntities.MAJUNGASAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MajungasaurusInfo),
                            0.5f,
                            MajungasaurusInfo));

//ProtoceratopsEntity registration: auto generated
            DinosaurRenderInfo ProtoceratopsInfo = new DinosaurRenderInfo(PROTOCERATOPS, new ProtoceratopsAnimator());
            event.registerEntityRenderer(ModEntities.PROTOCERATOPS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ProtoceratopsInfo),
                            0.5f,
                            ProtoceratopsInfo));

//TitanisEntity registration: auto generated
            DinosaurRenderInfo TitanisInfo = new DinosaurRenderInfo(TITANIS, new TitanisAnimator());
            event.registerEntityRenderer(ModEntities.TITANIS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TitanisInfo),
                            0.5f,
                            TitanisInfo));

//CoelacanthEntity registration: auto generated
            DinosaurRenderInfo CoelacanthInfo = new DinosaurRenderInfo(COELACANTH, new CoelacanthAnimator());
            event.registerEntityRenderer(ModEntities.COELACANTH_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CoelacanthInfo),
                            0.5f,
                            CoelacanthInfo));

//GallimimusEntity registration: auto generated
            DinosaurRenderInfo GallimimusInfo = new DinosaurRenderInfo(GALLIMIMUS, new GallimimusAnimator());
            event.registerEntityRenderer(ModEntities.GALLIMIMUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(GallimimusInfo),
                            0.5f,
                            GallimimusInfo));

//CeratosaurusEntity registration: auto generated
            DinosaurRenderInfo CeratosaurusInfo = new DinosaurRenderInfo(CERATOSAURUS, new CeratosaurusAnimator());
            event.registerEntityRenderer(ModEntities.CERATOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CeratosaurusInfo),
                            0.5f,
                            CeratosaurusInfo));

//VelociraptorCharlieEntity registration: auto generated
            DinosaurRenderInfo VelociraptorCharlieInfo = new DinosaurRenderInfo(CHARLIE, new VelociraptorCharlieAnimator());
            event.registerEntityRenderer(ModEntities.VELOCIRAPTORCHARLIE_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VelociraptorCharlieInfo),
                            0.5f,
                            VelociraptorCharlieInfo));

//SpinosaurusEntity registration: auto generated
            DinosaurRenderInfo SpinosaurusInfo = new DinosaurRenderInfo(SPINOSAURUS, new SpinosaurusAnimator());
            event.registerEntityRenderer(ModEntities.SPINOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SpinosaurusInfo),
                            0.5f,
                            SpinosaurusInfo));

//PachycephalosaurusEntity registration: auto generated
            DinosaurRenderInfo PachycephalosaurusInfo = new DinosaurRenderInfo(PACHYCEPHALOSAURUS, new PachycephalosaurusAnimator());
            event.registerEntityRenderer(ModEntities.PACHYCEPHALOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(PachycephalosaurusInfo),
                            0.5f,
                            PachycephalosaurusInfo));

//QuetzalEntity registration: auto generated
            DinosaurRenderInfo QuetzalInfo = new DinosaurRenderInfo(QUETZAL, new QuetzalAnimator());
            event.registerEntityRenderer(ModEntities.QUETZAL.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(QuetzalInfo),
                            0.5f,
                            QuetzalInfo));

//CarcharodontosaurusEntity registration: auto generated
            DinosaurRenderInfo CarcharodontosaurusInfo = new DinosaurRenderInfo(CARCHARODONTOSAURUS, new CarcharodontosaurusAnimator());
            event.registerEntityRenderer(ModEntities.CARCHARODONTOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CarcharodontosaurusInfo),
                            0.5f,
                            CarcharodontosaurusInfo));

//TylosaurusEntity registration: auto generated
            DinosaurRenderInfo TylosaurusInfo = new DinosaurRenderInfo(TYLOSAURUS, new TylosaurusAnimator());
            event.registerEntityRenderer(ModEntities.TYLOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TylosaurusInfo),
                            0.5f,
                            TylosaurusInfo));

//OrnithomimusEntity registration: auto generated
            DinosaurRenderInfo OrnithomimusInfo = new DinosaurRenderInfo(ORNITHOMIMUS, new OrnithomimusAnimator());
            event.registerEntityRenderer(ModEntities.ORNITHOMIMUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(OrnithomimusInfo),
                            0.5f,
                            OrnithomimusInfo));

//MegapiranhaEntity registration: auto generated
            DinosaurRenderInfo MegapiranhaInfo = new DinosaurRenderInfo(MEGAPIRANHA, new MegapiranhaAnimator());
            event.registerEntityRenderer(ModEntities.MEGAPIRANHA_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MegapiranhaInfo),
                            0.5f,
                            MegapiranhaInfo));

//DiplodocusEntity registration: auto generated
            DinosaurRenderInfo DiplodocusInfo = new DinosaurRenderInfo(DIPLODOCUS, new DiplodocusAnimator());
            event.registerEntityRenderer(ModEntities.DIPLODOCUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DiplodocusInfo),
                            0.5f,
                            DiplodocusInfo));

//DilophosaurusEntity registration: auto generated
            DinosaurRenderInfo DilophosaurusInfo = new DinosaurRenderInfo(DILOPHOSAURUS, new DilophosaurusAnimator());
            event.registerEntityRenderer(ModEntities.DILOPHOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DilophosaurusInfo),
                            0.5f,
                            DilophosaurusInfo));

//StyracosaurusEntity registration: auto generated
            DinosaurRenderInfo StyracosaurusInfo = new DinosaurRenderInfo(STYRACOSAURUS, new StyracosaurusAnimator());
            event.registerEntityRenderer(ModEntities.STYRACOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(StyracosaurusInfo),
                            0.5f,
                            StyracosaurusInfo));

//GuanlongEntity registration: auto generated
            DinosaurRenderInfo GuanlongInfo = new DinosaurRenderInfo(GUANLONG, new GuanlongAnimator());
            event.registerEntityRenderer(ModEntities.GUANLONG_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(GuanlongInfo),
                            0.5f,
                            GuanlongInfo));

//CamarasaurusEntity registration: auto generated
            DinosaurRenderInfo CamarasaurusInfo = new DinosaurRenderInfo(CAMARASAURUS, new CamarasaurusAnimator());
            event.registerEntityRenderer(ModEntities.CAMARASAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CamarasaurusInfo),
                            0.5f,
                            CamarasaurusInfo));
            //NigersaurusEntity registration: auto generated
            DinosaurRenderInfo NigersaurusInfo = new DinosaurRenderInfo(NIGERSAURUS, new NigersaurusAnimator());
            event.registerEntityRenderer(ModEntities.NIGERSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(NigersaurusInfo),
                            0.5f,
                            NigersaurusInfo));
            DinosaurRenderInfo LivyatanInfo = new DinosaurRenderInfo(LIVYATAN, new LivyatanAnimator());
            event.registerEntityRenderer(ModEntities.LIVYATAN_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(LivyatanInfo),
                            0.5f,
                            LivyatanInfo));
            DinosaurRenderInfo MegalodonInfo = new DinosaurRenderInfo(MEGALODON, new MegalodonAnimator());
            event.registerEntityRenderer(ModEntities.MEGALODON_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MegalodonInfo),
                            0.5f,
                            MegalodonInfo));
            DinosaurRenderInfo CalymeneInfo = new DinosaurRenderInfo(CALYMENE, new CalymeneAnimator());
            event.registerEntityRenderer(ModEntities.CALYMENE_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CalymeneInfo),
                            0.5f,
                            CalymeneInfo));

//HyaenodonEntity registration: auto generated
            DinosaurRenderInfo HyaenodonInfo = new DinosaurRenderInfo(HYAENODON, new HyaenodonAnimator());
            event.registerEntityRenderer(ModEntities.HYAENODON_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(HyaenodonInfo),
                            0.5f,
                            HyaenodonInfo));

//CoelurusEntity registration: auto generated
            DinosaurRenderInfo CoelurusInfo = new DinosaurRenderInfo(COELURUS, new CoelurusAnimator());
            event.registerEntityRenderer(ModEntities.COELURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CoelurusInfo),
                            0.5f,
                            CoelurusInfo));

//DiplocaulusEntity registration: auto generated
            DinosaurRenderInfo DiplocaulusInfo = new DinosaurRenderInfo(DIPLOCAULUS, new DiplocaulusAnimator());
            event.registerEntityRenderer(ModEntities.DIPLOCAULUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DiplocaulusInfo),
                            0.5f,
                            DiplocaulusInfo));

//DreadnoughtusEntity registration: auto generated
            DinosaurRenderInfo DreadnoughtusInfo = new DinosaurRenderInfo(DREADNOUGHTUS, new DreadnoughtusAnimator());
            event.registerEntityRenderer(ModEntities.DREADNOUGHTUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DreadnoughtusInfo),
                            0.5f,
                            DreadnoughtusInfo));
            DinosaurRenderInfo PatagotitanInfo = new DinosaurRenderInfo(PATAGOTITAN, new PatagotitanAnimator());
            event.registerEntityRenderer(ModEntities.PATAGOTITAN.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(PatagotitanInfo),
                            0.5f,
                            PatagotitanInfo));
            DinosaurRenderInfo MaiasauraInfo = new DinosaurRenderInfo(MAIASAURA, new MaiasauraAnimator());
            event.registerEntityRenderer(ModEntities.MAIASAURA.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MaiasauraInfo),
                            0.5f,
                            MaiasauraInfo));
//EdmontosaurusEntity registration: auto generated
            DinosaurRenderInfo EdmontosaurusInfo = new DinosaurRenderInfo(EDMONTOSAURUS, new EdmontosaurusAnimator());
            event.registerEntityRenderer(ModEntities.EDMONTOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(EdmontosaurusInfo),
                            0.5f,
                            EdmontosaurusInfo));

//StegosaurusEntity registration: auto generated
            DinosaurRenderInfo StegosaurusInfo = new DinosaurRenderInfo(STEGOSAURUS, new StegosaurusAnimator());
            event.registerEntityRenderer(ModEntities.STEGOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(StegosaurusInfo),
                            0.5f,
                            StegosaurusInfo));

//SpinoraptorEntity registration: auto generated
            DinosaurRenderInfo SpinoraptorInfo = new DinosaurRenderInfo(SPINORAPTOR, new SpinoraptorAnimator());
            event.registerEntityRenderer(ModEntities.SPINORAPTOR_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SpinoraptorInfo),
                            0.5f,
                            SpinoraptorInfo));

//AchillobatorEntity registration: auto generated
            DinosaurRenderInfo AchillobatorInfo = new DinosaurRenderInfo(ACHILLOBATOR, new AchillobatorAnimator());
            event.registerEntityRenderer(ModEntities.ACHILLOBATOR_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AchillobatorInfo),
                            0.5f,
                            AchillobatorInfo));

//ChilesaurusEntity registration: auto generated
            DinosaurRenderInfo ChilesaurusInfo = new DinosaurRenderInfo(CHILESAURUS, new ChilesaurusAnimator());
            event.registerEntityRenderer(ModEntities.CHILESAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ChilesaurusInfo),
                            0.5f,
                            ChilesaurusInfo));

//MegatheriumEntity registration: auto generated
            DinosaurRenderInfo MegatheriumInfo = new DinosaurRenderInfo(MEGATHERIUM, new MegatheriumAnimator());
            event.registerEntityRenderer(ModEntities.MEGATHERIUM_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MegatheriumInfo),
                            0.5f,
                            MegatheriumInfo));

//SegisaurusEntity registration: auto generated
            DinosaurRenderInfo SegisaurusInfo = new DinosaurRenderInfo(SEGISAURUS, new SegisaurusAnimator());
            event.registerEntityRenderer(ModEntities.SEGISAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SegisaurusInfo),
                            0.5f,
                            SegisaurusInfo));

//AnkylodocusEntity registration: auto generated
            DinosaurRenderInfo AnkylodocusInfo = new DinosaurRenderInfo(ANKYLODOCUS, new AnkylodocusAnimator());
            event.registerEntityRenderer(ModEntities.ANKYLODOCUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AnkylodocusInfo),
                            0.5f,
                            AnkylodocusInfo));

//BrachiosaurusEntity registration: auto generated
            DinosaurRenderInfo BrachiosaurusInfo = new DinosaurRenderInfo(BRACHIOSAURUS, new BrachiosaurusAnimator());
            event.registerEntityRenderer(ModEntities.BRACHIOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(BrachiosaurusInfo),
                            0.5f,
                            BrachiosaurusInfo));

//SmilodonEntity registration: auto generated
            DinosaurRenderInfo SmilodonInfo = new DinosaurRenderInfo(SMILODON, new SmilodonAnimator());
            event.registerEntityRenderer(ModEntities.SMILODON_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(SmilodonInfo),
                            0.5f,
                            SmilodonInfo));

//MicroceratusEntity registration: auto generated
            DinosaurRenderInfo MicroceratusInfo = new DinosaurRenderInfo(MICROCERATUS, new MicroceratusAnimator());
            event.registerEntityRenderer(ModEntities.MICROCERATUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MicroceratusInfo),
                            0.5f,
                            MicroceratusInfo));

//LeptictidiumEntity registration: auto generated
            DinosaurRenderInfo LeptictidiumInfo = new DinosaurRenderInfo(LEPTICTIDIUM, new LeptictidiumAnimator());
            event.registerEntityRenderer(ModEntities.LEPTICTIDIUM_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(LeptictidiumInfo),
                            0.5f,
                            LeptictidiumInfo));

//HypsilophodonEntity registration: auto generated
            DinosaurRenderInfo HypsilophodonInfo = new DinosaurRenderInfo(HYPSILOPHODON, new HypsilophodonAnimator());
            event.registerEntityRenderer(ModEntities.HYPSILOPHODON.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(HypsilophodonInfo),
                            0.5f,
                            HypsilophodonInfo));

//TherizinosaurusEntity registration: auto generated
            DinosaurRenderInfo TherizinosaurusInfo = new DinosaurRenderInfo(THERIZINOSAURUS, new TherizinosaurusAnimator());
            event.registerEntityRenderer(ModEntities.THERIZINOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TherizinosaurusInfo),
                            0.5f,
                            TherizinosaurusInfo));

//VelociraptorEntity registration: auto generated
            DinosaurRenderInfo VelociraptorInfo = new DinosaurRenderInfo(VELOCIRAPTOR, new VelociraptorAnimator());
            event.registerEntityRenderer(ModEntities.VELOCIRAPTOR_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VelociraptorInfo),
                            0.5f,
                            VelociraptorInfo));

//MussaurusEntity registration: auto generated
            DinosaurRenderInfo MussaurusInfo = new DinosaurRenderInfo(MUSSAURUS, new MussaurusAnimator());
            event.registerEntityRenderer(ModEntities.MUSSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(MussaurusInfo),
                            0.5f,
                            MussaurusInfo));

//TriceratopsEntity registration: auto generated
            DinosaurRenderInfo TriceratopsInfo = new DinosaurRenderInfo(TRICERATOPS, new TriceratopsAnimator());
            event.registerEntityRenderer(ModEntities.TRICERATOPS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TriceratopsInfo),
                            0.5f,
                            TriceratopsInfo));

//GiganotosaurusEntity registration: auto generated
            DinosaurRenderInfo GiganotosaurusInfo = new DinosaurRenderInfo(GIGANOTOSAURUS, new GiganotosaurusAnimator());
            event.registerEntityRenderer(ModEntities.GIGANOTOSAURUS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(GiganotosaurusInfo),
                            0.5f,
                            GiganotosaurusInfo));

//TropeognathusEntity registration: auto generated
            DinosaurRenderInfo TropeognathusInfo = new DinosaurRenderInfo(TROPEOGNATHUS, new TropeognathusAnimator());
            event.registerEntityRenderer(ModEntities.TROPEOGNATHUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TropeognathusInfo),
                            0.5f,
                            TropeognathusInfo));

//LambeosaurusEntity registration: auto generated
            DinosaurRenderInfo LambeosaurusInfo = new DinosaurRenderInfo(LAMBEOSAURUS, new LambeosaurusAnimator());
            event.registerEntityRenderer(ModEntities.LAMBEOSAURUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(LambeosaurusInfo),
                            0.5f,
                            LambeosaurusInfo));

//AlligatorGarEntity registration: auto generated
            DinosaurRenderInfo AlligatorGarInfo = new DinosaurRenderInfo(ALLIGATOR_GAR, new AlligatorGarAnimator());
            event.registerEntityRenderer(ModEntities.ALLIGATOR_GAR.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AlligatorGarInfo),
                            0.5f,
                            AlligatorGarInfo));

//ElasmotheriumEntity registration: auto generated
            DinosaurRenderInfo ElasmotheriumInfo = new DinosaurRenderInfo(ELASMOTHERIUM, new ElasmotheriumAnimator());
            event.registerEntityRenderer(ModEntities.ELASMOTHERIUM.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ElasmotheriumInfo),
                            0.5f,
                            ElasmotheriumInfo));
            DinosaurRenderInfo AsterocerasInfo = new DinosaurRenderInfo(ASTEROCERAS, new AsterocerasAnimator());
            event.registerEntityRenderer(ModEntities.ASTEROCERAS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(AsterocerasInfo),
                            0.5f,
                            AsterocerasInfo));
            DinosaurRenderInfo DimetrodonInfo = new DinosaurRenderInfo(DIMETRODON, new DimetrodonAnimator());
            event.registerEntityRenderer(ModEntities.DIMETRODON_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DimetrodonInfo),
                            0.5f,
                            DimetrodonInfo));
            DinosaurRenderInfo TitanitesInfo = new DinosaurRenderInfo(TITANITES, new TitanitesAnimator());
            event.registerEntityRenderer(ModEntities.TITANITES_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(TitanitesInfo),
                            0.5f,
                            TitanitesInfo));
            DinosaurRenderInfo DeinosuchusInfo = new DinosaurRenderInfo(DEINOSUCHUS, new DeinosuchusAnimator());
            event.registerEntityRenderer(ModEntities.DEINOSUCHUS.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(DeinosuchusInfo),
                            0.5f,
                            DeinosuchusInfo));
            DinosaurRenderInfo KairukuInfo = new DinosaurRenderInfo(KAIRUKU, new KairukuAnimator());
            event.registerEntityRenderer(ModEntities.KAIRUKU_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(KairukuInfo),
                            0.5f,
                            KairukuInfo));
            DinosaurRenderInfo ParapuzosiaInfo = new DinosaurRenderInfo(PARAPUZOSIA, new ParapuzosiaAnimator());
            event.registerEntityRenderer(ModEntities.PARAPUZOSIA_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ParapuzosiaInfo),
                            0.5f,
                            ParapuzosiaInfo));
            DinosaurRenderInfo CamerocerasInfo = new DinosaurRenderInfo(CAMEROCERAS, new CamerocerasAnimator());
            event.registerEntityRenderer(ModEntities.CAMEROCERAS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(CamerocerasInfo),
                            0.5f,
                            CamerocerasInfo));
            DinosaurRenderInfo EndocerasInfo = new DinosaurRenderInfo(ENDOCERAS, new EndocerasAnimator());
            event.registerEntityRenderer(ModEntities.ENDOCERAS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(EndocerasInfo),
                            0.5f,
                            EndocerasInfo));
            DinosaurRenderInfo OrthocerasInfo = new DinosaurRenderInfo(ORTHOCERAS, new OrthocerasAnimator());
            event.registerEntityRenderer(ModEntities.ORTHOCERAS_ENTITY_TYPE.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(OrthocerasInfo),
                            0.5f,
                            OrthocerasInfo));
            DinosaurRenderInfo VectipeltaInfo = new DinosaurRenderInfo(VECTIPELTA, new VectipeltaAnimator());
            event.registerEntityRenderer(ModEntities.VECTIPELTA.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(VectipeltaInfo),
                            0.5f,
                            VectipeltaInfo));
            DinosaurRenderInfo ParaceratheriumInfo = new DinosaurRenderInfo(PARACERATHERIUM, new ParaceratheriumAnimator());
            event.registerEntityRenderer(ModEntities.PARACERATHERIUM.get(), (ctx) ->
                    new DinosaurRenderer(ctx,
                            getDefaultModelFromDinosaur(ParaceratheriumInfo),
                            0.5f,
                            ParaceratheriumInfo));
        }
        static AnimatableModel getDefaultModelFromDinosaur(DinosaurRenderInfo info){
            return new AnimatableModel(info.getDinosaur().getModelContainer(GrowthStage.ADULT), info.getModelAnimator(GrowthStage.ADULT));
        }
    }


}

