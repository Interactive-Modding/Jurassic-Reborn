package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Axis;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.render.entity.model.JurassicBoatModelLayers;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicBoat;
import net.vit.jurassicreborn.common.entities.vehicle.boat.JurassicChestBoat;
import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;

import java.util.EnumMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JurassicBoatRenderer<T extends net.minecraft.world.entity.vehicle.Boat> extends EntityRenderer<T> {
    private final Map<ModBoatType, Pair<ResourceLocation, BoatModel>> boatResources;
    private final boolean hasChest;

    public JurassicBoatRenderer(EntityRendererProvider.Context context, boolean hasChest) {
        super(context);
        this.hasChest = hasChest;
        this.shadowRadius = 0.8F;

        this.boatResources = Stream.of(ModBoatType.values()).collect(Collectors.toMap(
                type -> type,
                type -> Pair.of(createTexture(type), createModel(context, type, hasChest)),
                (a, b) -> a,
                () -> new EnumMap<>(ModBoatType.class)
        ));
    }

    private ResourceLocation createTexture(ModBoatType type) {
        String folder = hasChest ? "chest_boat" : "boat";
        return new ResourceLocation(JurassicReborn.MODID, "textures/entity/" + folder + "/" + type.getSerializedName() + ".png");
    }

    private BoatModel createModel(EntityRendererProvider.Context context, ModBoatType type, boolean hasChest) {
        ModelLayerLocation layer = hasChest
                ? JurassicBoatModelLayers.createChestBoatModelName(type)
                : JurassicBoatModelLayers.createBoatModelName(type);
        ModelPart part = context.bakeLayer(layer);

        return hasChest ? new ChestBoatModel(part) : new BoatModel(part);
    }

    @Override
    public void render(T boat, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        poseStack.translate(0.0D, 0.375D, 0.0D);
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - yaw));

        float hurtTime = boat.getHurtTime() - partialTicks;
        float damage = boat.getDamage() - partialTicks;
        if (damage < 0.0F) damage = 0.0F;
        if (hurtTime > 0.0F) {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.sin(hurtTime) * hurtTime * damage / 10.0F * boat.getHurtDir()));
        }

        float bubbleAngle = boat.getBubbleAngle(partialTicks);
        if (!Mth.equal(bubbleAngle, 0.0F)) {
            poseStack.mulPose(Axis.XP.rotationDegrees(bubbleAngle));
        }
        if (hasChest || boat instanceof JurassicChestBoat) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
        }
        ModBoatType variant = resolveVariant(boat);
        Pair<ResourceLocation, BoatModel> pair = boatResources.get(variant);
        if (pair == null) pair = boatResources.get(ModBoatType.ARAUCARIA);
        if (pair == null) {
            poseStack.popPose();
            super.render(boat, yaw, partialTicks, poseStack, buffer, packedLight);
            return;
        }

        ResourceLocation texture = pair.getFirst();
        BoatModel model = pair.getSecond();

        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
        poseStack.scale(-1.0F, -1.0F, 1.0F);

        model.setupAnim(boat, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);

        VertexConsumer vc = buffer.getBuffer(model.renderType(texture));
        model.renderToBuffer(poseStack, vc, packedLight, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        if (!boat.isUnderWater()) {
            VertexConsumer waterMask = buffer.getBuffer(RenderType.waterMask());
            model.waterPatch().render(poseStack, waterMask, packedLight, OverlayTexture.NO_OVERLAY);
        }

        poseStack.popPose();
        super.render(boat, yaw, partialTicks, poseStack, buffer, packedLight);
    }

    private ModBoatType resolveVariant(T boat) {
        if (boat instanceof JurassicBoat jb) {
            return jb.getJurassicVariant();
        } else if (boat instanceof JurassicChestBoat jcb) {
            return jcb.getJurassicVariant();
        }
        return ModBoatType.ARAUCARIA;
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return createTexture(resolveVariant(entity));
    }
}
