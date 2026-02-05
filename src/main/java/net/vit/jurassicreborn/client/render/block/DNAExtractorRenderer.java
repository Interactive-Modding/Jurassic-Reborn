package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
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

import java.util.ArrayDeque;

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
                       PoseStack pPoseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        delegate.render(blockEntity, partialTick, pPoseStack, bufferSource, packedLight, packedOverlay);

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

        ItemStack stack = blockEntity.getItem(0);
        slots.push(stack);

        pPoseStack.pushPose();

        Vec3 vector = new Vec3(-0.2, 0.2725, -0.2).yRot(rotation * Mth.DEG_TO_RAD);

        vector = vector.add(0.5, 0, 0.5);

        pPoseStack.translate(vector.x(), vector.y(), vector.z());

        pPoseStack.scale(scale, scale, scale);

        pPoseStack.mulPose(Axis.XP.rotation(Mth.PI / 2));

        ItemStack currentInput = slots.removeLast();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        itemRenderer.renderStatic(currentInput, ItemTransforms.TransformType.NONE, packedLight, packedOverlay, pPoseStack, bufferSource, 0);

        pPoseStack.popPose();
    }
}
