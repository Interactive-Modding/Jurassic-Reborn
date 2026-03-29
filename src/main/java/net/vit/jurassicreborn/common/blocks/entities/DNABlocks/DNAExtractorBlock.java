package net.vit.jurassicreborn.common.blocks.entities.DNABlocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;

public class DNAExtractorBlock extends BaseMachineBlock {
    public static final MapCodec<DNAExtractorBlock> CODEC =
            Block.simpleCodec(DNAExtractorBlock::new);

    public DNAExtractorBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
