package net.vit.jurassicreborn.common.blocks.entities.EmbryonicThing;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;

public class EmbryoCalcificationMachineBlock extends BaseMachineBlock {
    public static final MapCodec<EmbryoCalcificationMachineBlock> CODEC =
            Block.simpleCodec(EmbryoCalcificationMachineBlock::new);

    public EmbryoCalcificationMachineBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
