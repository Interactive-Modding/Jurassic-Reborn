package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;import net.neoforged.api.distmarker.Dist;import net.neoforged.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.entity.CrabAnimator;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity;
import net.vit.jurassicreborn.common.entities.animal.CrabEntity.Type;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class CrabEntityRenderer extends LivingEntityRenderer<CrabEntity, BasicEntityModel<CrabEntity>> {
    private static final AnimatableModel CRAB_MODEL;
    private static final ResourceLocation CRAB_TEXTURE = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/crab/crab.png");
    private static final ResourceLocation ALT_TEXTURE = ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "textures/entities/crab/male_crab.png");

    static {
        TabulaModelContainer crab = null;
        try {
            crab = TabulaModelHelper.loadTabulaModel("assets/jurassicreborn/models/entities/crab/adult/crab");
        } catch (IOException e) {
            e.printStackTrace();
        }
        CRAB_MODEL = new AnimatableModel(crab, new CrabAnimator());
    }
    @Override
    protected boolean shouldShowName(CrabEntity entity) {
        return entity.hasCustomName();
    }
    public CrabEntityRenderer(EntityRendererProvider.Context context) {
        super(context, CRAB_MODEL, 0.2F);
    }

    @Override
    public void render(CrabEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    @Override
    protected void scale(CrabEntity entity, PoseStack poseStack, float partialTickTime) {
        float base = entity.isBaby() ? 0.3F : 0.5F;
        if (entity.getCrabType() == Type.ALTERNATIVE && !entity.isBaby()) {
            base = 0.55F;
        }
        poseStack.scale(base, base, base);
        super.scale(entity, poseStack, partialTickTime);
    }


    @Override
    public ResourceLocation getTextureLocation(CrabEntity entity) {
        return entity.getCrabType() == Type.ALTERNATIVE ? ALT_TEXTURE : CRAB_TEXTURE;
    }

}
