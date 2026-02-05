package net.vit.jurassicreborn.common.blocks.ancientplants;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.ModBlocks;

public class ImplimentedAncientPlant extends AncientPlantBlock {

    private static final VoxelShape DEFAULT_BOUNDS = Block.box(2.0D, 0.0D, 2.0D, 14.0D, 13.0D, 14.0D);
    private final VoxelShape bounds;

    public ImplimentedAncientPlant() {
        super(ModBlocks.defaultPlant());
        this.bounds = DEFAULT_BOUNDS;
    }

    public ImplimentedAncientPlant(VoxelShape bounds) {
        super(ModBlocks.defaultPlant());
        this.bounds = bounds;
    }

    public ImplimentedAncientPlant(BlockBehaviour.Properties properties) {
        super(properties);
        this.bounds = DEFAULT_BOUNDS;
    }

    public ImplimentedAncientPlant(BlockBehaviour.Properties properties, VoxelShape bounds) {
        super(properties);
        this.bounds = bounds;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return bounds;
    }
}