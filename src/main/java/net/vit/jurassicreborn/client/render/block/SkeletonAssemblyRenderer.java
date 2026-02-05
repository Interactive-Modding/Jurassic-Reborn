package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.skeletonassembly.SkeletonAssemblerBlockEntity;
import net.vit.jurassicreborn.common.util.block.ModdedModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SkeletonAssemblyRenderer implements BlockEntityRenderer<SkeletonAssemblerBlockEntity>, BlockEntityRendererProvider {
    private final GeoBlockRenderer<SkeletonAssemblerBlockEntity> delegate;

    public SkeletonAssemblyRenderer(Context rendererProvider) {
        this.delegate = new GeoBlockRenderer<>(new ModdedModel<>(
                JurassicReborn.resource("geo/skeleton_assembly.geo.json"),
                JurassicReborn.resource("textures/block/skeleton_assembly.png"),
                JurassicReborn.resource("animations/skeleton_assembly.animation.json")
        )) {};
    }

    @Override
    public BlockEntityRenderer create(Context pContext) {
        return new SkeletonAssemblyRenderer(pContext);
    }

    @Override
    public void render(SkeletonAssemblerBlockEntity tile,
                       float partialTick,
                       PoseStack poseStack,
                       MultiBufferSource bufferSource,
                       int packedLight,
                       int packedOverlay) {
        delegate.render(tile, partialTick, poseStack, bufferSource, packedLight, packedOverlay);
    }
}
