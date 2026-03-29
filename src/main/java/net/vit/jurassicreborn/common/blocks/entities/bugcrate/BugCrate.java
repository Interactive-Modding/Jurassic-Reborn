package net.vit.jurassicreborn.common.blocks.entities.bugcrate;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;

import org.jetbrains.annotations.Nullable;

public class BugCrate extends HorizontalDirectionalBlock implements EntityBlock {

    /* ---------------------------------------------------------------------
       CODEC (REQUIRED IN 1.21 — MUST MATCH CONSTRUCTOR)
       --------------------------------------------------------------------- */
    public static final MapCodec<BugCrate> CODEC =
            RecordCodecBuilder.mapCodec(instance ->
                    instance.group(
                            BlockBehaviour.Properties.CODEC.fieldOf("properties")
                                    .forGetter(b -> b.properties)
                    ).apply(instance, BugCrate::new)
            );

    /* ---------------------------------------------------------------------
       CONSTRUCTOR (MUST TAKE Properties)
       --------------------------------------------------------------------- */
    public BugCrate(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(
                this.getStateDefinition().any().setValue(FACING, Direction.NORTH)
        );
    }

    /* ---------------------------------------------------------------------
       PLACEMENT
       --------------------------------------------------------------------- */
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState()
                .setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    /* ---------------------------------------------------------------------
       BLOCK ENTITY
       --------------------------------------------------------------------- */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BugCrateBlockEntity(pos, state);
    }

    /* ---------------------------------------------------------------------
       SERVER TICKER
       --------------------------------------------------------------------- */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type) {

        return level.isClientSide ? null :
                (lvl, p, s, be) ->
                        ((BugCrateBlockEntity) be).serverTick(
                                lvl, p, s, (BugCrateBlockEntity) be
                        );
    }

    /* ---------------------------------------------------------------------
       REMOVAL
       --------------------------------------------------------------------- */
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {

        if (!oldState.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof BugCrateBlockEntity crate) {
                Containers.dropContents(level, pos, crate);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }

    /* ---------------------------------------------------------------------
       INTERACTION (NO NetworkHooks IN 1.21)
       --------------------------------------------------------------------- */
    @Override
    public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof MenuProvider provider) {
                sp.openMenu(provider, pos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    /* ---------------------------------------------------------------------
       CODEC OVERRIDE
       --------------------------------------------------------------------- */
    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }
}
