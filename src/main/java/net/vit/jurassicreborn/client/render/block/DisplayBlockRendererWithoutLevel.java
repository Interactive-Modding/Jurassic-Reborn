package net.vit.jurassicreborn.client.render.block;

import com.mojang.blaze3d.vertex.PoseStack;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.items.misc.ActionFigureItem;
import net.vit.jurassicreborn.common.items.misc.FossilSkeletonItem;
import net.vit.jurassicreborn.common.items.misc.FreshSkeletonItem;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
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
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        Item item = pStack.getItem();
        if (item == null) return;

        ActionFigureBlockEntity blockEntity = null;

        if (item instanceof ActionFigureItem afi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = afi.getGender(pStack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(afi.getDinosaur(pStack), male, afi.isSkeleton(pStack), afi.isFossile(pStack));
        } else if (item instanceof FreshSkeletonItem fi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = fi.getGender(pStack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(fi.getDinosaur(pStack), male, fi.isSkeleton(pStack), fi.isFossile(pStack));
        } else if (item instanceof FossilSkeletonItem fi) {
            blockEntity = new ActionFigureBlockEntity(BlockPos.ZERO, ModBlocks.DISPLAY_BLOCK.get().defaultBlockState());
            int gender = fi.getGender(pStack);
            boolean male = gender > 0 ? gender == 1 : new Random().nextBoolean();
            blockEntity.setDinosaur(fi.getDinosaur(pStack), male, fi.isSkeleton(pStack), fi.isFossile(pStack));
        }

        if (blockEntity != null) {
            this.blockEntityRenderDispatcher.renderItem(blockEntity, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
        }
    }

}