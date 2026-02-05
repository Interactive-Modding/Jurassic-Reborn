package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class EncasedFossilBlockEntity extends BlockEntity {
    public static String LEVEL_NBT = "YLevelCollectedFrom";

    public static String VARIANT_KEY = "Dinosaur";

    protected final int YLevel;

    @Nullable
    protected final ResourceLocation dino;

//    @Deprecated
//    public EncasedFossilBlockEntity( BlockPos pWorldPosition, BlockState pBlockState) {
//        super(ModBlockEntities.FOSSIL.get(), pWorldPosition, pBlockState);
//        this.YLevel = -65;//invaidate the constructor enity
//        dino = null;
//    }


    public EncasedFossilBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int YLevel) {
        super(pType, pWorldPosition, pBlockState);
        this.YLevel = YLevel;
        dino = null;
    }

    public EncasedFossilBlockEntity(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState, int YLevel, ResourceLocation Dino) {
        super(pType, pWorldPosition, pBlockState);
        this.YLevel = YLevel;
        dino = Dino;
    }

    @Override
    public void saveToItem(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(LEVEL_NBT, YLevel);
        tag.putString(VARIANT_KEY, dino != null ? dino.toString() : "minecraft:air");
        super.saveToItem(stack);
    }

}
