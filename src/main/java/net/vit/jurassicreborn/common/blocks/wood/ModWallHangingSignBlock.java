package net.vit.jurassicreborn.common.blocks.wood;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SignBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import org.jetbrains.annotations.Nullable;

public class ModWallHangingSignBlock extends WallHangingSignBlock {

    public ModWallHangingSignBlock(WoodType type, Properties props) {
        super(type, props);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return ModBlockEntities.HANGING_SIGN.get().create(pos, state);
    }


    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level,
            BlockState state,
            BlockEntityType<T> type
    ) {
        return createTickerHelper(
                type,
                ModBlockEntities.HANGING_SIGN.get(),
                SignBlockEntity::tick
        );
    }
}
