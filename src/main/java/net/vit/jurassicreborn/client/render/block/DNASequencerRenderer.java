package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlockEntity;

import java.util.ArrayDeque;

public class DNASequencerRenderer implements BlockEntityRenderer<DNASequencerBlockEntity> {

    @Override
    public void render(DNASequencerBlockEntity blockEntity,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {

        Level level = blockEntity.getLevel();
        if (level == null) return;

        BlockState state = level.getBlockState(blockEntity.getBlockPos());
        if (state.getBlock() != ModBlocks.DNA_SEQUENCER.get()) return;

        Direction facing = state.getValue(DNASequencerBlock.FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }

        float rotation = facing.toYRot();
        float scale = 0.375F;

        ArrayDeque<ItemStack> stacks = new ArrayDeque<>();
        for (int slot : DNASequencerBlockEntity.DNA_INPUT) {
            ItemStack stack = blockEntity.getItem(slot);
            if (!stack.isEmpty()) {
                stacks.add(stack);
            }
        }

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        int index = 0;

        while (!stacks.isEmpty()) {
            ItemStack stack = stacks.removeLast();

            poseStack.pushPose();

            Vec3 offset = new Vec3(
                    0.2,
                    index * -0.25F + 0.66F,
                    0.2
            ).yRot(rotation * Mth.DEG_TO_RAD).add(0.5, 0.0, 0.5);

            poseStack.translate(offset.x, offset.y, offset.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
            poseStack.mulPose(Axis.XP.rotation(Mth.HALF_PI));
            poseStack.scale(scale, scale, scale);

            itemRenderer.renderStatic(
                    stack,
                    ItemDisplayContext.FIXED,
                    packedLight,
                    packedOverlay,
                    poseStack,
                    bufferSource,
                    level,
                    0
            );

            poseStack.popPose();
            index++;
        }
    }
}
