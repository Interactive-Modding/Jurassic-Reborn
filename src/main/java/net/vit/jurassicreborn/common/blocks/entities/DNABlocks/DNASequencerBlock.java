package net.vit.jurassicreborn.common.blocks.entities.DNABlocks;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;

public class DNASequencerBlock extends BaseMachineBlock {
    //todo: block entities, recipies, DNA

    public static final MapCodec<DNASequencerBlock> CODEC =
            Block.simpleCodec(DNASequencerBlock::new);

    public DNASequencerBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
