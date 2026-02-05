package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class AncientCrop extends CropBlock {

    public static IntegerProperty AGE_5 = BlockStateProperties.AGE_5;

    public int maxAge = 5;
    public final int ageChoice;



    public AncientCrop(Properties pProperties) {
        super(pProperties);
        this.ageChoice = 1;
        this.registerDefaultState(this.getStateDefinition().any().setValue(AGE_5, 0));
    }
    public AncientCrop(Properties pProperties, int dif) {
        super(pProperties);
        this.ageChoice = 1;

    }
    @Override
    public IntegerProperty getAgeProperty() {
        return AGE_5;
    }


    @Override
    public int getMaxAge() {
        return this.maxAge;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {

        pBuilder.add(AGE_5);
    }
}