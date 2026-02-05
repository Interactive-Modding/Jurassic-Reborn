package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CoralPlantBlock;

import java.util.function.Supplier;

public class AncientCoralBlock extends CoralPlantBlock {
    private final Supplier<Block> deadBlock;

    public AncientCoralBlock(Supplier<Block> deadBlock, Properties properties) {
        super(deadBlock.get(), properties);
        this.deadBlock = deadBlock;
    }
}