package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.InventoryUtil;
import org.jetbrains.annotations.Nullable;

import static net.minecraft.world.level.block.DoublePlantBlock.copyWaterloggedFrom;

public class CultivatorBlock extends BaseEntityBlock {

    /* --------------------------------------------------------------------- */
    /* CODEC (REQUIRED IN 1.21) */
    /* --------------------------------------------------------------------- */
    public static final MapCodec<CultivatorBlock> CODEC =
            MapCodec.unit(() -> ModBlocks.CULTIVATE_BOTTOM.get());

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /* --------------------------------------------------------------------- */
    public static final EnumProperty<DyeColor> COLOR =
            EnumProperty.create("color", DyeColor.class);

    /** true = top, false = bottom */
    public final boolean half;

    public CultivatorBlock(Properties properties, boolean half) {
        super(properties);
        this.half = half;
        this.registerDefaultState(
                this.stateDefinition.any().setValue(COLOR, DyeColor.WHITE)
        );
    }

    /* --------------------------------------------------------------------- */
    /* DOUBLE BLOCK PLACEMENT */
    /* --------------------------------------------------------------------- */
    public static void placeBottomAt(LevelAccessor level, BlockState state, BlockPos pos, int flags) {
        BlockPos above = pos.above();
        level.setBlock(pos,
                copyWaterloggedFrom(level, pos,
                        ModBlocks.CULTIVATE_BOTTOM.get().defaultBlockState()),
                flags);
        level.setBlock(above,
                copyWaterloggedFrom(level, above,
                        ModBlocks.CULTIVATE_TOP.get().defaultBlockState()),
                flags);
    }

    /* --------------------------------------------------------------------- */
    /* REMOVAL */
    /* --------------------------------------------------------------------- */
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
        if (level.isClientSide) return;

        if (state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, moving);
            return;
        }

        BlockPos bottomPos = half ? pos.below() : pos;

        if (!half) {
            BlockEntity be = level.getBlockEntity(bottomPos);
            if (be instanceof CultivatorBlockEntity entity) {
                InventoryUtil.dropContents(level, bottomPos, entity.getItemHandler());
                level.updateNeighbourForOutputSignal(bottomPos, this);
            }
            var item = ModItems.CULTIVATORS.get(state.getValue(COLOR));
            if (item != null) {
                popResource(level, bottomPos, item.get().getDefaultInstance());
            }
        }

        level.destroyBlock(half ? bottomPos : bottomPos.above(), false);
        super.onRemove(state, level, pos, newState, moving);
    }

    /* --------------------------------------------------------------------- */
    /* GUI (1.21 CORRECT) */
    /* --------------------------------------------------------------------- */
    @Override
    public InteractionResult useWithoutItem(
            BlockState state,
            Level level,
            BlockPos pos,
            Player player,
            BlockHitResult hit
    ) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(half ? pos.below() : pos);
            if (be instanceof CultivatorBlockEntity entity) {
                player.openMenu(entity);
            }
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    /* --------------------------------------------------------------------- */
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return half || level.getBlockState(pos.above()).isAir();
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState old, boolean moving) {
        if (!level.isClientSide && !half) {
            level.setBlock(
                    pos.above(),
                    ModBlocks.CULTIVATE_TOP.get().defaultBlockState()
                            .setValue(COLOR, state.getValue(COLOR)),
                    3
            );
        }
    }

    /* --------------------------------------------------------------------- */
    @Override
    public MapColor getMapColor(BlockState state, BlockGetter level, BlockPos pos, MapColor fallback) {
        return state.getValue(COLOR).getMapColor();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return null;
    }

    /* --------------------------------------------------------------------- */
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            Level level, BlockState state, BlockEntityType<T> type
    ) {
        return level.isClientSide ? null :
                createTickerHelper(
                        type,
                        ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE.get(),
                        CultivatorBlockEntity::tick
                );
    }

    /* --------------------------------------------------------------------- */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(COLOR);
    }
}
