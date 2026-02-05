
package net.vit.jurassicreborn.common.blocks.fossil.dinosaurs;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.vit.jurassicreborn.common.blocks.fossil.FossilBlock;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;

public class AchillobatorFossilBlock extends Block implements FossilBlock {
    public static final IntegerProperty BONE_COUNT = IntegerProperty.create("bone_count", 1, 11);

    public AchillobatorFossilBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(BONE_COUNT, 11));
    }

   @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(BONE_COUNT);
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        int randomBoneCount = 1 + context.getLevel().getRandom().nextInt(11);
        return this.defaultBlockState().setValue(BONE_COUNT, randomBoneCount);
    }
    @Override
    public boolean mustBandage() {
        return true;
    }

    @Override
    public Dinosaur getDinosaur() {
        return DinosaurHandler.ACHILLOBATOR;
    }
}
