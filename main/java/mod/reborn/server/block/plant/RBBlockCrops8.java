package mod.reborn.server.block.plant;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;

public abstract class RBBlockCrops8 extends RBBlockCropsBase {
    private static final int MAX_AGE = 7;
    private static PropertyInteger AGE = PropertyInteger.create("age", 0, MAX_AGE);

    @Override
    protected PropertyInteger getAgeProperty() {
        return AGE;
    }

    @Override
    protected int getMaxAge() {
        return MAX_AGE;
    }

    // NOTE:  This is called on parent object construction.
    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, AGE);
    }
}
