package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.basic.BasicEntityModel;
import com.github.alexthe666.citadel.client.model.container.TabulaModelContainer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.model.AnimatableModel;
import net.vit.jurassicreborn.client.render.entity.animation.entity.SharkAnimator;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.io.IOException;

@OnlyIn(Dist.CLIENT)
public class SharkEntityRenderer extends LivingEntityRenderer<SharkEntity, BasicEntityModel<SharkEntity>> {
    private static ResourceLocation SHARK_TEXTURE;

    private static final AnimatableModel SHARK;
    static {
        TabulaModelContainer shark = null;
        try {

            shark = TabulaModelHelper.loadTabulaModel("assets/jurassicreborn/models/entities/shark/adult/shark");
        } catch (IOException e) {
            e.printStackTrace();
        }
        SHARK = new AnimatableModel(shark, new SharkAnimator());
        SHARK_TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/entities/shark/shark.png");
    }

    public SharkEntityRenderer(EntityRendererProvider.Context pContext, float pShadowRadius) {
        super(pContext, SHARK, pShadowRadius);
        if(SHARK != null)
            this.model = (AnimatableModel)SHARK;
    }


//    @Override
//    public void preRenderCallback(SharkEntity entity, float partialTick) {
//        float scale = entity.isChild() ? 0.2F : 1.7F;
//        GlStateManager.scale(scale, scale, scale);
//    }


    @Override
    protected void scale(SharkEntity entity, PoseStack pMatrixStack, float pPartialTickTime) {

        float scale = entity.isBaby() ? 0.2F : 1.7F;
        pMatrixStack.scale(scale, scale, scale);
        super.scale(entity, pMatrixStack, pPartialTickTime);
    }



    @Override
    public void render(SharkEntity pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        super.render(pEntity, pEntityYaw, pPartialTicks, pMatrixStack, pBuffer, pPackedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(SharkEntity pEntity) {
        return SHARK_TEXTURE;
    }
}