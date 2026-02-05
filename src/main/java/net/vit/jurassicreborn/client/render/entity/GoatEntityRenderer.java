package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.entity.GoatAnimator;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity.Type;
import net.vit.jurassicreborn.common.entities.animal.GoatEntity.Variant;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class GoatEntityRenderer extends LivingEntityRenderer<GoatEntity, BasicEntityModel<GoatEntity>> {
    private static final AnimatableModel BILLY_MODEL;
    private static final AnimatableModel NANNY_MODEL;
    private static final AnimatableModel KID_MODEL;

    private static final ResourceLocation BILLY_JW = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/billy_jurassic_world.png");
    private static final ResourceLocation BILLY_JP = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/billy_jurassic_park.png");
    private static final ResourceLocation BILLY_JPOG = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/billy_jpog.png");
    private static final ResourceLocation NANNY_JW = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/nanny_jurassic_world.png");
    private static final ResourceLocation NANNY_JP = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/nanny_jurassic_park.png");
    private static final ResourceLocation NANNY_JPOG = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/nanny_jpog.png");
    private static final ResourceLocation KID_JW = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/kid_jurassic_world.png");
    private static final ResourceLocation KID_JP = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/kid_jurassic_park.png");
    private static final ResourceLocation KID_JPOG = new ResourceLocation(JurassicReborn.MODID, "textures/entities/goat/kid_jpog.png");

    static {
        TabulaModelContainer billy = null;
        TabulaModelContainer nanny = null;
        TabulaModelContainer kid = null;
        try {
            billy = TabulaModelHelper.loadTabulaModel("assets/jurassicreborn/models/entities/goat_billy/adult/goat_billy");
            nanny = TabulaModelHelper.loadTabulaModel("assets/jurassicreborn/models/entities/goat_nanny/adult/goat_nanny");
            kid = TabulaModelHelper.loadTabulaModel("assets/jurassicreborn/models/entities/goat_kid/adult/goat_kid");
        } catch (IOException e) {
            e.printStackTrace();
        }
        BILLY_MODEL = new AnimatableModel(billy, new GoatAnimator());
        NANNY_MODEL = new AnimatableModel(nanny, new GoatAnimator());
        KID_MODEL = new AnimatableModel(kid, new GoatAnimator());
    }

    public GoatEntityRenderer(EntityRendererProvider.Context context) {
        super(context, NANNY_MODEL, 0.4F);
    }

    private AnimatableModel modelFor(GoatEntity entity) {
        Type type = entity.getGoatType();
        return switch (type) {
            case BILLY -> BILLY_MODEL;
            case KID -> KID_MODEL;
            default -> NANNY_MODEL;
        };
    }

    private ResourceLocation textureFor(GoatEntity entity) {
        Variant variant = entity.getVariant();
        return switch (entity.getGoatType()) {
            case BILLY -> switch (variant) {
                case JURASSIC_WORLD -> BILLY_JW;
                case JURASSIC_PARK -> BILLY_JP;
                case JPOG -> BILLY_JPOG;
            };
            case KID -> switch (variant) {
                case JURASSIC_WORLD -> KID_JW;
                case JURASSIC_PARK -> KID_JP;
                case JPOG -> KID_JPOG;
            };
            case NANNY -> switch (variant) {
                case JURASSIC_WORLD -> NANNY_JW;
                case JURASSIC_PARK -> NANNY_JP;
                case JPOG -> NANNY_JPOG;
            };
        };
    }

    @Override
    protected void scale(GoatEntity entity, PoseStack poseStack, float partialTickTime) {
        float scale = switch (entity.getGoatType()) {
            case BILLY -> 0.5F;
            case KID   -> 0.3F;
            default    -> 0.45F; // NANNY
        };
        poseStack.scale(scale, scale, scale);
        super.scale(entity, poseStack, partialTickTime);
    }

    @Override
    public void render(GoatEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        AnimatableModel model = modelFor(entity);
        if (this.model != model) {
            this.model = model;
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(GoatEntity entity) {
        return textureFor(entity);
    }
}
