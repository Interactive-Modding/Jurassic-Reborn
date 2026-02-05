package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class SmallPlantBlock extends ImplimentedAncientPlant {

    public SmallPlantBlock() {
        super(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noOcclusion().strength(0.2F));
    }

    public SmallPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

}