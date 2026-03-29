package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlock;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor.DNAExtractorBlockEntity;
import net.vit.jurassicreborn.common.util.block.ModdedModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class DNAExtractorRenderer implements BlockEntityRenderer<DNAExtractorBlockEntity> {

    private final GeoBlockRenderer<DNAExtractorBlockEntity> delegate;

    public DNAExtractorRenderer(BlockEntityRendererProvider.Context ctx) {
        this.delegate = new GeoBlockRenderer<>(new ModdedModel<>(
                JurassicReborn.resource("geo/dna_extractor.geo.json"),
                JurassicReborn.resource("textures/block/dna_extractor.png"),
                JurassicReborn.resource("animations/dna_extractor.animation.json")
        )) {};
    }

    @Override
    public void render(DNAExtractorBlockEntity blockEntity,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {

        delegate.render(blockEntity, partialTick, poseStack, bufferSource, packedLight, packedOverlay);

        Level level = blockEntity.getLevel();
        if (level == null) return;

        BlockState state = level.getBlockState(blockEntity.getBlockPos());
        if (state.getBlock() != ModBlocks.DNA_EXTRACTOR.get()) return;

        ItemStack stack = blockEntity.getItem(0);
        if (stack.isEmpty()) return;

        Direction facing = state.getValue(DNAExtractorBlock.FACING);
        if (facing == Direction.NORTH || facing == Direction.SOUTH) {
            facing = facing.getOpposite();
        }

        float rotation = facing.toYRot();
        float scale = 0.375F;

        poseStack.pushPose();

        Vec3 offset = new Vec3(-0.2, 0.2725, -0.2)
                .yRot(rotation * Mth.DEG_TO_RAD)
                .add(0.5, 0.0, 0.5);

        poseStack.translate(offset.x, offset.y, offset.z);
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.XP.rotation(Mth.HALF_PI));
        poseStack.scale(scale, scale, scale);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
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
    }
}
