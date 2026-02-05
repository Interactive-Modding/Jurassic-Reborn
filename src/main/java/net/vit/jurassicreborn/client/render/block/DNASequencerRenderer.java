package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerBlockEntity;
import net.vit.jurassicreborn.common.network.Network;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayDeque;

public class DNASequencerRenderer implements BlockEntityRenderer<DNASequencerBlockEntity> {

    public DNASequencerRenderer() {
        super();
    }


    @Override
    public void render(DNASequencerBlockEntity blockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBufferSource, int pPackedLight, int pPackedOverlay) {

        Level level = blockEntity.getLevel();

        if (level == null) {
            return;
        }

        BlockState state = level.getBlockState(blockEntity.getBlockPos());

        if (state.getBlock() != ModBlocks.DNA_SEQUENCER.get())
            return;

        ArrayDeque<ItemStack> slots = new ArrayDeque<>();

        Direction facing = state.getValue(DNASequencerBlock.FACING);

        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }
        float rotation = facing.toYRot();
        float scale = 0.375F;

//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 0));
//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 2));
//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 4));

        for (int i : DNASequencerBlockEntity.DNA_INPUT) {
            ItemStack stack = ItemStack.EMPTY;
            var st = blockEntity.getItem(i);
            if(st != null){
                stack = st;
            }
            slots.push(stack);
        }


        double index = 0;

        while (!slots.isEmpty()) {

            pPoseStack.pushPose();

            Vec3 vector = new Vec3(0.2, ((float) index) * (-0.25f) + 0.66f, 0.2 ).yRot(rotation * Mth.DEG_TO_RAD);//rotate where the items should appear to the right quadrant

            vector = vector.add(0.5, 0, 0.5);//center the model in the block

            pPoseStack.translate(vector.x(), vector.y(), vector.z());//apply offsets to poseStack

            pPoseStack.scale(scale, scale, scale);//scale pose stack

            pPoseStack.mulPose(Vector3f.XP.rotation(Mth.PI / 2));//make the item model face up


            ItemStack currentInput = slots.removeLast();//get the right item from the deque

            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();//get the item renderer


            itemRenderer.renderStatic(currentInput, ItemTransforms.TransformType.NONE, pPackedLight, pPackedOverlay, pPoseStack, pBufferSource, 0);//render the item


            pPoseStack.popPose();//render? something? the item

            index++;//get ready for next item
        }
    }
}
