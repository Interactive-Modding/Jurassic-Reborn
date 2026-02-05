package net.vit.jurassicreborn.common.blocks.entities;

import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class EncasedFaunaFossilBlockEntity extends BlockEntity {

    public static final String DINO_KEY = "Dinosaur";

    @Nullable
    protected Dinosaur dino;

    public EncasedFaunaFossilBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.ENCASED_FAUNA_FOSSIL.get(), pos, state);
    }

    public void setDino(@Nullable Dinosaur dino) {
        this.dino = dino;
    }

    @Nullable
    public Dinosaur getDino() {
        return dino;
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString(DINO_KEY, dino != null ? dino.getName() : Dinosaur.EMPTY.getName());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        if (tag.contains(DINO_KEY)) {
            this.dino = Dinosaur.getDinosaurByName(tag.getString(DINO_KEY));
        }
    }

    @Override
    public void saveToItem(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString(DINO_KEY, dino != null ? dino.getName() : Dinosaur.EMPTY.getName());
        super.saveToItem(stack);
    }
}
