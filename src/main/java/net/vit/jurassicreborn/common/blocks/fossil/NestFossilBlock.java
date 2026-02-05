package net.vit.jurassicreborn.common.blocks.fossil;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class NestFossilBlock extends Block {
    public static IntegerProperty VARIANT = IntegerProperty.create("variant", 0, 2);

    public NestFossilBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(VARIANT, 0));
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VARIANT);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int randomVariant = context.getLevel().getRandom().nextInt(3); // 0–3
        return this.defaultBlockState().setValue(VARIANT, randomVariant);
    }
}
