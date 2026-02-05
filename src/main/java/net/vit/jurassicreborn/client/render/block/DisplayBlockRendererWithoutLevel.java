package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.item.ItemDisplayContext;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.items.misc.ActionFigureItem;
import net.vit.jurassicreborn.common.items.misc.FossilSkeletonItem;
import net.vit.jurassicreborn.common.items.misc.FreshSkeletonItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.Random;

public class DisplayBlockRendererWithoutLevel extends BlockEntityWithoutLevelRenderer {
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public DisplayBlockRendererWithoutLevel(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
        this.blockEntityRenderDispatcher = pBlockEntityRenderDispatcher;

    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext displayContext, PoseStack poseStack,
                             MultiBufferSource buffer, int packedLight, int packedOverlay) {
        Item item = stack.getItem();
        if (item == null) return;

        ActionFigureBlockEntity blockEntity = null;

        if (item instanceof ActionFigureItem afi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = afi.getGender(stack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(afi.getDinosaur(stack), male, afi.isSkeleton(stack), afi.isFossile(stack));
        } else if (item instanceof FreshSkeletonItem fi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = fi.getGender(stack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(fi.getDinosaur(stack), male, fi.isSkeleton(stack), fi.isFossile(stack));
        } else if (item instanceof FossilSkeletonItem fi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = fi.getGender(stack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(fi.getDinosaur(stack), male, fi.isSkeleton(stack), fi.isFossile(stack));
        }

        if (blockEntity != null) {
            this.blockEntityRenderDispatcher.renderItem(blockEntity, poseStack, buffer, packedLight, packedOverlay);
        }
    }

}