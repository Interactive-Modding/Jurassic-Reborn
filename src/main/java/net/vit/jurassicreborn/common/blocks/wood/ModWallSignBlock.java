package net.vit.jurassicreborn.common.blocks.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;

public class ModWallSignBlock extends WallSignBlock {

    public ModWallSignBlock(WoodType type, Properties props) {
        super(type, props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.SIGN.get().create(pos, state);
    }

}
