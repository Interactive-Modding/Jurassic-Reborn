package net.vit.jurassicreborn.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class ClearGlassPaneBlock extends IronBarsBlock {

    public ClearGlassPaneBlock(BlockBehaviour.Properties props) {
        super(props);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockGetter level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        FluidState fluid = level.getFluidState(pos);

        BlockPos nPos = pos.north();
        BlockPos sPos = pos.south();
        BlockPos wPos = pos.west();
        BlockPos ePos = pos.east();

        BlockState n = level.getBlockState(nPos);
        BlockState s = level.getBlockState(sPos);
        BlockState w = level.getBlockState(wPos);
        BlockState e = level.getBlockState(ePos);

        return this.defaultBlockState()
                .setValue(NORTH, attachsToDir(n, n.isFaceSturdy(level, nPos, Direction.SOUTH), Direction.NORTH))
                .setValue(SOUTH, attachsToDir(s, s.isFaceSturdy(level, sPos, Direction.NORTH), Direction.SOUTH))
                .setValue(WEST,  attachsToDir(w, w.isFaceSturdy(level, wPos, Direction.EAST),  Direction.WEST))
                .setValue(EAST,  attachsToDir(e, e.isFaceSturdy(level, ePos, Direction.WEST),  Direction.EAST))
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction face, BlockState facingState,
                                  LevelAccessor level, BlockPos currentPos, BlockPos facingPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        if (face.getAxis().isHorizontal()) {
            Property<Boolean> prop = PROPERTY_BY_DIRECTION.get(face);
            boolean connect = attachsToDir(
                    facingState,
                    facingState.isFaceSturdy(level, facingPos, face.getOpposite()),
                    face
            );
            return state.setValue(prop, connect);
        }
        return super.updateShape(state, face, facingState, level, currentPos, facingPos);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    private boolean attachsToDir(BlockState state, boolean solidSide, Direction direction) {
        Block block = state.getBlock();
        if (direction.getAxis().isHorizontal()) {
            if (block instanceof ClearGlassBlock) return true;
            if (block instanceof ClearGlassPaneBlock) return true;
        }
        return super.attachsTo(state, solidSide);
    }
}
