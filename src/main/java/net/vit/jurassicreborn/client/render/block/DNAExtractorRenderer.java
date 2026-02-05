package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlockEntity;
import net.vit.jurassicreborn.common.util.block.ModdedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

import java.util.ArrayDeque;

public class DNAExtractorRenderer extends GeoBlockRenderer<DNAExtractorBlockEntity> {
    public DNAExtractorRenderer(BlockEntityRendererProvider.Context ctx) {
        super(ctx, new ModdedModel<>(JurassicReborn.resource("geo/dna_extractor.geo.json"), JurassicReborn.resource("textures/block/dna_extractor.png"), JurassicReborn.resource("animations/dna_extractor.animation.json")));
    }


    @Override
    public void render(BlockEntity tile, float partialTick, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if(!(tile instanceof DNAExtractorBlockEntity blockEntity))
            return;

        super.render(blockEntity, partialTick, pPoseStack, bufferSource, packedLight);

        Level level = blockEntity.getLevel();

        if (level == null) {
            return;
        }

        BlockState state = level.getBlockState(blockEntity.getBlockPos());

        if (state.getBlock() != ModBlocks.DNA_EXTRACTOR.get())
            return;

        ArrayDeque<ItemStack> slots = new ArrayDeque<>();

        Direction facing = state.getValue(DNAExtractorBlock.FACING);

        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }
        float rotation = facing.toYRot();
        float scale = 0.375F;

//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 0));
//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 2));
//        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 4));

        ItemStack stack = blockEntity.getItem(0);
        slots.push(stack);


        pPoseStack.pushPose();

        Vec3 vector = new Vec3(-0.2, 0.2725, -0.2).yRot(rotation * Mth.DEG_TO_RAD);//rotate where the items should appear to the right quadrant

        vector = vector.add(0.5, 0, 0.5);//center the model in the block

        pPoseStack.translate(vector.x(), vector.y(), vector.z());//apply offsets to poseStack

        pPoseStack.scale(scale, scale, scale);//scale pose stack

        pPoseStack.mulPose(Vector3f.XP.rotation(Mth.PI / 2));//make the item model face up


        ItemStack currentInput = slots.removeLast();//get the right item from the deque

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();//get the item renderer


        itemRenderer.renderStatic(currentInput, ItemTransforms.TransformType.NONE, packedLight, packedOverlay, pPoseStack, bufferSource, 0);//render the item


        pPoseStack.popPose();//render? something? the item





    }



//        @Override
//    public void render(DNAExtractorBlockEntity blockEntity, float partialTick, PoseStack pPoseStack, MultiBufferSource bufferSource, int packedLight) {
//        super.render(blockEntity, partialTick, pPoseStack, bufferSource, packedLight);
//
//        Level level = blockEntity.getLevel();
//
//        if (level == null) {
//            return;
//        }
//
//        BlockState state = level.getBlockState(blockEntity.getBlockPos());
//
//        if (state.getBlock() != ModBlocks.DNA_SEQUENCER.get())
//            return;
//
//
//        Network.sendSlotRequest(blockEntity, 0);
//
//
//        ArrayDeque<ItemStack> slots = new ArrayDeque<>();
//
//        Direction facing = state.getValue(DNASequencerBlock.FACING);
//
//        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
//            facing = facing.getOpposite();
//        }
//        float rotation = facing.toYRot();
//        float scale = 0.375F;
//
////        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 0));
////        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 2));
////        slots.push(Network.getSlotContents(blockEntity.getBlockPos(), 4));
//
//        ItemStack stack = Network.getSlotContents(blockEntity.getBlockPos(), 0);
//        slots.push(stack);
//
//
//
//        double index = 0;
//
//        while (!slots.isEmpty()) {
//
//            pPoseStack.pushPose();
//
//            Vec3 vector = new Vec3(0.2, ((float) index) * (-0.25f) + 0.66f, 0.2 ).yRot(rotation * Mth.DEG_TO_RAD);//rotate where the items should appear to the right quadrant
//
//            vector = vector.add(0.5, 0, 0.5);//center the model in the block
//
//            pPoseStack.translate(vector.x(), vector.y(), vector.z());//apply offsets to poseStack
//
//            pPoseStack.scale(scale, scale, scale);//scale pose stack
//
//            pPoseStack.mulPose(Vector3f.XP.rotation(Mth.PI / 2));//make the item model face up
//
//
//            ItemStack currentInput = slots.removeLast();//get the right item from the deque
//
//            ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();//get the item renderer
//
//
//            itemRenderer.renderStatic(currentInput, ItemTransforms.TransformType.NONE, OverlayTexture.NO_OVERLAY, packedLight, pPoseStack, bufferSource, 0);//render the item
//
//
//            pPoseStack.popPose();//render? something? the item
//
//            index++;//get ready for next item
//
//
//        }
//    }

}
