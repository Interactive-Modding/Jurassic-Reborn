package net.vit.jurassicreborn.client.render.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderHighlightEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.SkullDisplayBlock;
import net.vit.jurassicreborn.common.blocks.entities.SkullDisplayBlockEntity;

@Mod.EventBusSubscriber(modid = JurassicReborn.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class SkullDisplayHighlight {
    @SubscribeEvent
    public static void onBlockHighlight(RenderHighlightEvent.Block event) {
        HitResult target = event.getTarget();
        if (target.getType() != HitResult.Type.BLOCK) return;

        BlockHitResult bhr = (BlockHitResult) target;
        BlockPos pos = bhr.getBlockPos();
        BlockState state = event.getCamera().getEntity().level.getBlockState(pos);
        if (!(state.getBlock() instanceof SkullDisplayBlock)) return;

        Camera camera = event.getCamera();
        PoseStack pose = event.getPoseStack();
        MultiBufferSource buffers = event.getMultiBufferSource();

        pose.pushPose();
        double camX = camera.getPosition().x();
        double camY = camera.getPosition().y();
        double camZ = camera.getPosition().z();
        pose.translate(pos.getX() + 0.5 - camX, pos.getY() - camY, pos.getZ() + 0.5 - camZ);

        SkullDisplayBlockEntity tile = (SkullDisplayBlockEntity) camera.getEntity().level.getBlockEntity(pos);
        if (tile != null && tile.hasData()) {
            pose.mulPose(com.mojang.math.Vector3f.YP.rotationDegrees(tile.getAngle()));
        }
        pose.translate(-0.5, 0.0, -0.5);
        AABB box = state.getShape(camera.getEntity().level, pos).bounds().inflate(0.002);
        LevelRenderer.renderLineBox(pose, buffers.getBuffer(RenderType.lines()), box, 0.0F, 0.0F, 0.0F, 0.4F);
        pose.popPose();
        event.setCanceled(true);
    }
}