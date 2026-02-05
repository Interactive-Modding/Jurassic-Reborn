package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class SevenStageAncientCrop extends AncientCrop{

    public static IntegerProperty AGE_7 = BlockStateProperties.AGE_7;

    public SevenStageAncientCrop(Properties pProperties) {
        super(pProperties, 0);
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE_7, 0));
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_7;
    }

    @Override
    public int getMaxAge() {
        return 7;
    }
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {

        pBuilder.add(AGE_7);
    }
}