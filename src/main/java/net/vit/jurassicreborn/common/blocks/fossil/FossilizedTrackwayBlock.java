package net.vit.jurassicreborn.common.blocks.fossil;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.block.DirectionalBlock;

public class FossilizedTrackwayBlock extends DirectionalBlock {
    public static final MapCodec<FossilizedTrackwayBlock> CODEC = simpleCodec(FossilizedTrackwayBlock::new);

    public FossilizedTrackwayBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected MapCodec<? extends DirectionalBlock> codec() {
        return CODEC;
    }
}
