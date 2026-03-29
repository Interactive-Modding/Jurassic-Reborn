package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimator;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.util.EntityColorTint;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;

import java.awt.*;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class DinosaurRenderer extends LivingEntityRenderer<DinosaurEntity, BasicEntityModel<DinosaurEntity>> {
    public Dinosaur dinosaur;
//    public DinosaurRenderInfo.Context renderInfo;

    public DinosaurRenderInfo renderInfo;

    private static final float BASE_SHADOW_SIZE = 0.5F;

    public Random random;

    Color tint = new Color(0, 0, 0, 255);

    @Override
    protected boolean shouldShowName(DinosaurEntity entity) {
        return entity.hasCustomName();
    }


    public DinosaurRenderer(EntityRendererProvider.Context renderInfo, BasicEntityModel<DinosaurEntity> model, float shadowSize) {
        super(renderInfo, model, shadowSize);
//        this.dinosaur = renderInfo.getDinosaur();

        this.random = new Random();
//        this.renderInfo = renderInfo;
        this.addLayer(new LayerEyelid(this));
    }

    public DinosaurRenderer(EntityRendererProvider.Context renderInfo, BasicEntityModel<DinosaurEntity> model, float shadowSize, DinosaurRenderInfo info) {
        super(renderInfo, model, shadowSize);
//        this.dinosaur = renderInfo.getDinosaur();

        this.renderInfo = info;
        this.dinosaur = info.getDinosaur();

        this.random = new Random();
//        this.renderInfo = renderInfo;
        this.addLayer(new LayerEyelid(this));
    }

    /**
     * This sets the renderInfo of the render to the dino and animation.
     * !!!CALL THIS RIGHT AFTER REGISTERING IT!!!
     * @param dino The Dinosaur object this class should render
     * @param anim The animator that should be used for the dino
     */
    public void setDinosaur(Dinosaur dino, EntityAnimator<?> anim){
        this.renderInfo = new DinosaurRenderInfo(dino, anim);

    }



    @Override
    public void scale(DinosaurEntity entity, PoseStack stack, float partialTick) {
        boolean skeleton = entity.isSkeleton();
        float scaleModifier = entity.getLegacyAttributes().getScaleModifier();
        double infantScale = this.dinosaur.getScaleInfant(skeleton);
        double adultScale = this.dinosaur.getScaleAdult(skeleton);
        float scale = (float) entity.interpolate(infantScale, adultScale) * scaleModifier;
//        this.shadowRadius = scale * this.shadowRadius;
        this.shadowRadius = scale * BASE_SHADOW_SIZE;
        float offsetX = skeleton ? this.dinosaur.getSkeletonOffsetX() : this.dinosaur.getOffsetX();
        float offsetY = skeleton ? this.dinosaur.getSkeletonOffsetY() : this.dinosaur.getOffsetY();
        float offsetZ = skeleton ? this.dinosaur.getSkeletonOffsetZ() : this.dinosaur.getOffsetZ();


        stack.translate(offsetX * scale, offsetY * scale, offsetZ * scale);
        if(entity.getCustomName() == null){
            stack.scale(scale, scale, scale);
            return;
        }
        String name = entity.getCustomName().getString();
        switch (name) {
            case "Panic":
                stack.scale(0.1F, scale, scale);
                break;
            case "Kal":
                stack.scale(scale, 0.01F, scale);
                break;
            case "Des":
                stack.scale(scale * 1.2f, scale * 1.2f, scale * 1.2f);
                break;
            case "Wyn":
                stack.scale(scale * 1.3f, scale * 1.3f, scale * 1.3f);
                break;
            case "Notch":
                stack.scale(scale * 2, scale * 2, scale * 2);
                break;
            case "jglrxavpok":
                stack.scale(scale, scale, scale * -1);
                break;
            case "Vitiate":
                int color = Color.HSBtoRGB((entity.level().getGameTime() % 1000) / 100f, 1f, 1f);
                this.tint = new Color((color & 0xFF) / 255f, ((color >> 8) & 0xFF) / 255f, ((color >> 16) & 0xFF) / 255f, 1f/*can tweak this later*/);
                break;
            case "Zth":
                stack.scale(scale * random.nextInt(69), scale * random.nextInt(69), scale * random.nextInt(69));
                break;
            case "WIDE":
                stack.scale(scale * 5, scale * 0.5f, scale * 0.5f);
                break;
            default:
                stack.scale(scale, scale, scale);
                this.tint = new Color(0, 0, 0, 0);
                break;
        }
    }

//    @Override
//    public void doRender(final DinosaurEntity entity, final double x, final double y, final double z, final float entityYaw, final float partialTicks) {
//        this.mainModel = this.renderInfo.getModel(entity.getGrowthStage(), (byte) entity.getSkeletonVariant());
//        super.doRender(entity, x, y, z, entityYaw, partialTicks);
//    }

    @Override
    public void render(DinosaurEntity entity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        this.model = this.renderInfo.getModel(entity.getGrowthStage(), (byte) entity.getSkeletonVariant());
        if(this.tint.getRGB() != new Color(0, 0, 0, 255).getRGB()) {
            EntityColorTint.setColor(this.tint);
            EntityColorTint.addEntityClassToList(entity.getClass());
        }
        super.render(entity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(DinosaurEntity entity) {
        // Determine the proper growth stage; default to adult if unsupported
        GrowthStage growthStage = entity.getGrowthStage();
        if (!this.dinosaur.doesSupportGrowthStage(growthStage)) {
            growthStage = GrowthStage.ADULT;
        }

        // Handle skeletons separately to account for fossil state
        if (entity.isSkeleton()) {
            String dinoName = this.dinosaur.getFormattedName();
            StringBuilder texPath = new StringBuilder();
            texPath.append("textures/entities/").append(dinoName);
            texPath.append("/").append(dinoName);
            if (entity.getIsFossile()) {
                texPath.append("_male_skeleton");
            } else {
                texPath.append("_female_skeleton");
            }
            texPath.append(".png");
            return ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, texPath.toString());
        }

        // Special-case textures for certain living dinosaurs
        if (entity instanceof MammothEntity) {
            return ((MammothEntity) entity).getTexture();
        }
        if (entity instanceof CorythosaurusEntity) {
            return ((CorythosaurusEntity) entity).getTexture();
        }
        if (entity instanceof GiganotosaurusEntity) {
            return ((GiganotosaurusEntity) entity).getTexture();
        }
        if (entity instanceof MaiasauraEntity) {
            return ((MaiasauraEntity) entity).getTexture();
        }
        if (entity instanceof PatagotitanEntity) {
            return ((PatagotitanEntity) entity).getTexture();
        }
        if (entity instanceof CalymeneEntity) {
            return ((CalymeneEntity) entity).getTexture();
        }
        if (entity instanceof NigersaurusEntity) {
            return ((NigersaurusEntity) entity).getTexture();
        }
        if (entity instanceof CompsognathusEntity) {
            return ((CompsognathusEntity) entity).getTexture();
        }
        if (entity instanceof AllosaurusEntity) {
            return ((AllosaurusEntity) entity).getTexture();
        }
        if (entity instanceof ParasaurolophusEntity) {
            return ((ParasaurolophusEntity) entity).getTexture();
        }
        if (entity instanceof DeinotheriumEntity) {
            return ((DeinotheriumEntity) entity).getTexture();
        }
        if (entity instanceof SmilodonEntity) {
            return ((SmilodonEntity) entity).getTexture();
        }
        if (entity instanceof TitanisEntity) {
            return ((TitanisEntity) entity).getTexture();
        }
        if (entity instanceof CeratosaurusEntity) {
            return ((CeratosaurusEntity) entity).getTexture();
        }
        if (entity instanceof SpinoraptorEntity) {
            return ((SpinoraptorEntity) entity).getTexture();
        }
        if (entity instanceof IndoraptorEntity) {
            return ((IndoraptorEntity) entity).getTexture();
        }
        if (entity instanceof BaryonyxEntity) {
            return ((BaryonyxEntity) entity).getTexture();
        }
        if (entity instanceof DiplodocusEntity) {
            return ((DiplodocusEntity) entity).getTexture();
        }
        if (entity instanceof AnkylodocusEntity) {
            return ((AnkylodocusEntity) entity).getTexture();
        }
        if (entity instanceof CamarasaurusEntity) {
            return ((CamarasaurusEntity) entity).getTexture();
        }
        if (entity instanceof BrachiosaurusEntity) {
            return ((BrachiosaurusEntity) entity).getTexture();
        }
        if (entity instanceof AnkylosaurusEntity) {
            return ((AnkylosaurusEntity) entity).getTexture();
        }
        if (entity instanceof ApatosaurusEntity) {
            return ((ApatosaurusEntity) entity).getTexture();
        }
        if (entity instanceof ParaceratheriumEntity) {
            return ((ParaceratheriumEntity) entity).getTexture();
        }
        if (entity instanceof VectipeltaEntity) {
            return ((VectipeltaEntity) entity).getTexture();
        }
        if (entity instanceof TriceratopsEntity) {
            return ((TriceratopsEntity) entity).getTexture();
        }
        if (entity instanceof StyracosaurusEntity) {
            return ((StyracosaurusEntity) entity).getTexture();
        }
        if (entity instanceof ChasmosaurusEntity) {
            return ((ChasmosaurusEntity) entity).getTexture();
        }
        if (entity instanceof SinoceratopsEntity) {
            return ((SinoceratopsEntity) entity).getTexture();
        }
        if (entity instanceof OviraptorEntity) {
            return ((OviraptorEntity) entity).getTexture();
        }
        if (entity instanceof ProtoceratopsEntity) {
            return ((ProtoceratopsEntity) entity).getTexture();
        }
        if (entity instanceof MicroceratusEntity) {
            return ((MicroceratusEntity) entity).getTexture();
        }

        // Default case: use the dinosaur's male/female texture for the current growth stage
        return entity.isMale() ? this.dinosaur.getMaleTexture(growthStage) : this.dinosaur.getFemaleTexture(growthStage);
    }
//    @Override
//    protected void applyRotations(DinosaurEntity entity, float p_77043_2_, float p_77043_3_, float partialTicks) {
//        GlStateManager.rotate(180.0F - p_77043_3_, 0.0F, 1.0F, 0.0F);
//    }

//    @Override
//    protected void setupRotations(DinosaurEntity pEntityLiving, PoseStack pMatrixStack, float pAgeInTicks, float pRotationYaw, float pPartialTicks) {
//        pMatrixStack.
//    }

    @OnlyIn(Dist.CLIENT)
    public class LayerEyelid extends RenderLayer<DinosaurEntity, BasicEntityModel<DinosaurEntity>> {
        private final RenderLayerParent<DinosaurEntity, BasicEntityModel<DinosaurEntity>> renderer;

        public LayerEyelid(RenderLayerParent<DinosaurEntity, BasicEntityModel<DinosaurEntity>> pRenderer) {
            super(pRenderer);
            this.renderer = pRenderer;
        }


//        @OverrOverrideide
//        public void render(DinosaurEntity entity, float limbSwing, float limbSwingAmount, float partialTicks, float age, float yaw, float pitch, float scale) {
//            if (!entity.isInvisible()) {
//                if (entity.areEyelidsClosed()) {

//                    if (texture != null) {
//                        ITextureObject textureObject = Minecraft.getMinecraft().getTextureManager().getTexture(texture);
//                        if (textureObject != TextureUtil.MISSING_TEXTURE) {
//                            this.renderer.bindTexture(texture);// - what does this do and what's the modern equivalent? - gamma
                                /*
                                what this does is weird and stupid:
                                it gets the eyelid texture and then renders the entity *again*...
                                with only the eyelid texture.
                                this only works because the eyelid textures are blank except for the eyelids
                                for example:
                                 */
                                /**
                                 * <a href="https://raw.githubusercontent.com/Interactive-Modding/jurassicjurassicreborn/master/src/main/resources/assets/jurassicreborn/textures/entities/ankylodocus/ankylodocus_female_adult_eyelid.png">eyelids.png</a>
                                 */
                                /*
                                   needless to say, this is dumb. I'm going to leave a to-do here for the future to fix this.
                                 */
//
//                            this.renderer.getMainModel().render(entity, limbSwing, limbSwingAmount, age, yaw, pitch, scale);
//                            this.renderer.setLightmap(entity); //TODO: Make sure this works this.renderer.setLightmap(entity, partialTicks); | legacy todo - gamma
//                        }
//                    }
//                }
//            }
//        }



//        @Override
//        public boolean shouldCombineTextures() {
//            return true;
//        }

                                @Override
                                public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                                   DinosaurEntity entity, float limbSwing, float limbSwingAmount,
                                                   float partialTicks, float ageInTicks,
                                                   float netHeadYaw, float headPitch) {

                                    /* 1 ▸ quick outs */
                                    if (entity.isInvisible() || !entity.areEyelidsClosed()) return;

                                    /* 2 ▸ lookup eyelid texture for this dinosaur / stage */
                                    ResourceLocation tex = entity.getDinosaur().getEyelidTexture(entity);
                                    if (tex == null) return;                                  // nothing defined

                                    /* 3 ▸ grab a buffer with that texture and re-draw the base model */
                                    VertexConsumer vc = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));

                                    this.getParentModel().renderToBuffer(
                                            poseStack,
                                            vc,
                                            packedLight,
                                            LivingEntityRenderer.getOverlayCoords(entity, 0.0F),
                                            0xFFFFFFFF);
                                }
    }
}
