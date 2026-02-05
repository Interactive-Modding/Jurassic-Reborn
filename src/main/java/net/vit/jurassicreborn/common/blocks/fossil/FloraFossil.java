package net.vit.jurassicreborn.common.blocks.fossil;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class FloraFossil extends Block implements FossilBlock {

    public static IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 4);


    public FloraFossil(Properties p_49795_) {
        super(p_49795_);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
    }

    @Override
    public boolean mustBandage() {
        return false;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int randomVariant = context.getLevel().getRandom().nextInt(5);
        return this.defaultBlockState().setValue(VARIANT, randomVariant);
    }
}
