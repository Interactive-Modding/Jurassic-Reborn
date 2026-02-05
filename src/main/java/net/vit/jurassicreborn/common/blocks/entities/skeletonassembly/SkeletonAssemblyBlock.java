package net.vit.jurassicreborn.common.blocks.entities.skeletonassembly;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;

import javax.annotation.Nullable;

/**
 * 2-block skeleton assembler: MAIN (model) + DUMMY (invisible).
 */
public class SkeletonAssemblyBlock extends BaseEntityBlock {

    public static final DirectionProperty     FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final EnumProperty<BlockHalf> HALF  = EnumProperty.create("half", BlockHalf.class);

    public SkeletonAssemblyBlock() {
        super(BlockBehaviour.Properties
                .copy(Blocks.OAK_PLANKS)
                .strength(2.0F)
                .sound(SoundType.METAL)
                .noOcclusion());                       // not full cube

        this.registerDefaultState(this.defaultBlockState()
                .setValue(FACING, Direction.NORTH)
                .setValue(HALF, BlockHalf.MAIN));
    }

    /* ------------------------------------------------------------------ */
    /*  INTERACTION (opens GUI from either half)                          */
    /* ------------------------------------------------------------------ */

    @Override
    public InteractionResult use(BlockState state, Level level,
                                 BlockPos pos, Player player,
                                 InteractionHand hand, BlockHitResult hit) {

        // Resolve main-half position once, store in a final variable
        final BlockPos menuPos = (state.getValue(HALF) == BlockHalf.DUMMY)
                ? getOpposite(pos, state)   // click came from dummy
                : pos;                      // click came from main

        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(menuPos);
            if (be instanceof MenuProvider provider && player instanceof ServerPlayer sp) {
                NetworkHooks.openScreen(sp, provider, menuPos);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /* ------------------------------------------------------------------ */
    /*  PLACEMENT                                                         */
    /* ------------------------------------------------------------------ */

    @Nullable
    @Override
    public BlockState getStateForPlacement(net.minecraft.world.item.context.BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        BlockPos  pos    = ctx.getClickedPos();
        Level     level  = ctx.getLevel();

        BlockState main = this.defaultBlockState().setValue(FACING, facing);

        if (!level.getBlockState(getOpposite(pos, main)).canBeReplaced(ctx)) {
            return null; // abort placement
        }
        return main;
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state,
                            @Nullable net.minecraft.world.entity.LivingEntity placer,
                            net.minecraft.world.item.ItemStack stack) {

        BlockPos other = getOpposite(pos, state);
        level.setBlock(other, state.setValue(HALF, BlockHalf.DUMMY), 3);
    }

    /* ------------------------------------------------------------------ */
    /*  BREAK BOTH HALVES                                                 */
    /* ------------------------------------------------------------------ */

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockPos other = getOpposite(pos, state);
            if (level.getBlockState(other).getBlock() == this) {
                level.removeBlock(other, false);
            }
            // drop inventory from main block entity
            BlockPos mainPos = state.getValue(HALF) == BlockHalf.MAIN ? pos : other;
            BlockEntity be = level.getBlockEntity(mainPos);
            if (be instanceof SkeletonAssemblerBlockEntity assembler) {
                Containers.dropContents(level, mainPos, (Container) assembler.getItemHandler());
                level.updateNeighbourForOutputSignal(mainPos, this);
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    /* ------------------------------------------------------------------ */
    /*  STATE / META                                                      */
    /* ------------------------------------------------------------------ */

    private BlockPos getOpposite(BlockPos pos, BlockState state) {
        Direction dir   = state.getValue(FACING);
        BlockHalf half  = state.getValue(HALF);
        return pos.relative(half == BlockHalf.MAIN ? dir.getClockWise()
                : dir.getCounterClockWise());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HALF);
    }

    /* ------------------------------------------------------------------ */
    /*  RENDERING                                                         */
    /* ------------------------------------------------------------------ */

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return state.getValue(HALF) == BlockHalf.MAIN
                ? RenderShape.MODEL
                : RenderShape.INVISIBLE;
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState adjacent, Direction side) {
        return false; // always render sides (cut-out)
    }

    /* Full-bright cutout layer is defined via blockstate JSON / client config in 1.19 */
    /* ------------------------------------------------------------------ */

    /* ------------------------------------------------------------------ */
    /*  ENUM                                                              */
    /* ------------------------------------------------------------------ */

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(HALF) == BlockHalf.MAIN
                ? new SkeletonAssemblerBlockEntity(pos, state)
                : null;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level lvl, BlockState state, BlockEntityType<T> type) {
        return state.getValue(HALF) == BlockHalf.MAIN && type == ModBlockEntities.SKELETON_ASSEMBLY_ENTITY.get()
                ? (w, p, s, be) -> SkeletonAssemblerBlockEntity.tick(w, p, s, (SkeletonAssemblerBlockEntity) be)
                : null;
    }

    public enum BlockHalf implements StringRepresentable {
        MAIN, DUMMY;

        @Override public String getSerializedName() { return this == MAIN ? "main" : "dummy"; }
    }
}
