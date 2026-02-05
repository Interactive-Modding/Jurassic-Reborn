package net.vit.jurassicreborn.common.blocks;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class ModStoneButtonBlock extends ButtonBlock {

    public ModStoneButtonBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.STONE, 20, false);
    }
}
