package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBlockEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import org.jetbrains.annotations.NotNull;

public class CultivatorRenderer implements BlockEntityRenderer<CultivatorBlockEntity> {

    @Override
    public void render(@NotNull CultivatorBlockEntity tile, float partialTick, @NotNull PoseStack pose,
                       @NotNull MultiBufferSource buf, int packedLight, int packedOverlay) {

        DinosaurEntity entity = tile.getRenderEntity();
        if (entity == null) return;

        float progress = tile.getProcessTime() / (float) CultivatorBlockEntity.STACK_PROCESS_TIME;
        progress = Math.max(progress, 0.05f); // never zero scale

        pose.pushPose();
        pose.translate(0.5, 1.0, 0.5);
        pose.mulPose(Axis.XN.rotationDegrees(90F));
        org.joml.Vector3f rot = entity.getDinosaurCultivatorRotation();
        pose.mulPose(Axis.YP.rotationDegrees(rot.y()));
        pose.mulPose(Axis.ZP.rotationDegrees(rot.z()));
        pose.scale(progress, progress, progress);
        pose.scale(0.04f, 0.04f, 0.04f);

        Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0, 0, 0, 0, 0, pose, buf, packedLight);
        pose.popPose();
    }
}
