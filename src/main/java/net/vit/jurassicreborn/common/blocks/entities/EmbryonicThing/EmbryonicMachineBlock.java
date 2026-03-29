package net.vit.jurassicreborn.common.blocks.entities.EmbryonicThing;

import com.mojang.serialization.MapCodec;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;

public class EmbryonicMachineBlock extends BaseMachineBlock {
    //todo: recipies, blocks
    public static final MapCodec<EmbryonicMachineBlock> CODEC =
            Block.simpleCodec(EmbryonicMachineBlock::new);

    public EmbryonicMachineBlock(Properties p_52591_) {
        super(p_52591_);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }
}
