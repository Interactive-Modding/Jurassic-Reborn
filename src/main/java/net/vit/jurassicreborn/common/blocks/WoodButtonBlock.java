package net.vit.jurassicreborn.common.blocks;

import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class WoodButtonBlock extends ButtonBlock {

    public WoodButtonBlock(BlockBehaviour.Properties properties) {
        super(properties, BlockSetType.OAK, 30, true);
    }
}
