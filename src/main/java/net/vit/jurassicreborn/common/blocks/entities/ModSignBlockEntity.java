package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ModSignBlockEntity extends SignBlockEntity {

    public ModSignBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SIGN.get(), pos, state);
    }
}
