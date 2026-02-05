package net.vit.jurassicreborn.client.render.entity;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.DinosaurEggEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.legacy.tabula.TabulaModelHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public class DinosaurEggRenderer extends EntityRenderer<DinosaurEggEntity> {
    private static TabulaModel DEFAULT_MODEL;
    private static ResourceLocation DEFAULT_TEXTURE;

    private final java.util.Map<Dinosaur, TabulaModel> modelCache = new java.util.HashMap<>();

    static {
        try {
            DEFAULT_MODEL = new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/tyrannosaurus"));
            DEFAULT_TEXTURE = new ResourceLocation(JurassicReborn.MODID, "textures/entities/egg/tyrannosaurus.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DinosaurEggRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(DinosaurEggEntity egg, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();
        poseStack.mulPose(com.mojang.math.Vector3f.YP.rotationDegrees(180.0F - yaw));
        // Tabula egg models are centered around y=24 which causes them to float
        // above the ground when rendered at the entity position. Offset the
        // model downwards to align it with the hitbox.
        poseStack.translate(0, -1.0F, 0);
        TabulaModel model = getModel(egg);
        ResourceLocation texture = getTextureLocation(egg);
        var vertexConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(texture));
        model.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        poseStack.popPose();
        super.render(egg, yaw, partialTicks, poseStack, buffer, light);
    }

    @Override
    public ResourceLocation getTextureLocation(DinosaurEggEntity egg) {
        Dinosaur dino = egg.getDinosaur();
        if (dino != null) {
            return new ResourceLocation(JurassicReborn.MODID, "textures/entities/egg/" + dino.getName().toLowerCase(Locale.ENGLISH) + ".png");
        }
        return DEFAULT_TEXTURE;
    }

    private TabulaModel getModel(DinosaurEggEntity egg) {
        Dinosaur dino = egg.getDinosaur();
        if (dino != null) {
            return modelCache.computeIfAbsent(dino, d -> {
                try {
                    return new TabulaModel(TabulaModelHelper.loadTabulaModel("/assets/jurassicreborn/models/entities/egg/" + d.getName().toLowerCase(Locale.ENGLISH)));
                } catch (Exception e) {
                    return DEFAULT_MODEL;
                }
            });
        }
        return DEFAULT_MODEL;
    }
}