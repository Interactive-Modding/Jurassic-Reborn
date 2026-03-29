package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class SmallPlantBlock extends ImplimentedAncientPlant {

    public SmallPlantBlock() {
        super(BlockBehaviour.Properties.ofFullCopy(Blocks.FERN));
    }

    public SmallPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

}