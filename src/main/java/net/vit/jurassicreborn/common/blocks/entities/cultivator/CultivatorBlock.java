// 1.19.2
package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.vit.jurassicreborn.common.items.ModItems;

import static net.minecraft.world.level.block.DoublePlantBlock.copyWaterloggedFrom;

public class CultivatorBlock extends BaseMachineBlock {
    public static final EnumProperty<DyeColor> COLOR = EnumProperty.create("color", DyeColor.class);
    /** true = top, false = bottom */
    public final boolean half;

    public CultivatorBlock(Properties properties, boolean blockHalf) {
        super(properties);
        this.half = blockHalf;
        this.registerDefaultState(this.getSetDefaultValues().setValue(COLOR, DyeColor.WHITE));
    }

    public static void placeBottomAt(LevelAccessor level, BlockState state, BlockPos pos, int flags) {
        BlockPos blockpos = pos.above();
        level.setBlock(pos, copyWaterloggedFrom(level, pos, ModBlocks.CULTIVATE_BOTTOM.get().defaultBlockState()), flags);
        level.setBlock(blockpos, copyWaterloggedFrom(level, blockpos, ModBlocks.CULTIVATE_TOP.get().defaultBlockState()), flags);
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (level.isClientSide) return;

        if (state.is(newState.getBlock())) {
            super.onRemove(state, level, pos, newState, isMoving);
            return;
        }

        // normalize to bottom block for inventory drop
        BlockPos bottomPos = half ? pos.below() : pos;
        BlockEntity be = level.getBlockEntity(bottomPos);
        if (be instanceof CultivatorBlockEntity entity) {
            Containers.dropContents(level, bottomPos, new RecipeWrapper(entity.getItemHandler()));
            level.updateNeighbourForOutputSignal(bottomPos, this);
        }
        if (!level.isClientSide && !half) {
            var registryObject = ModItems.CULTIVATORS.get(state.getValue(COLOR));
            if (registryObject != null) {
                ItemStack drop = new ItemStack(registryObject.get());
                popResource(level, bottomPos, drop);
            }
        }
        // destroy the other half
        BlockPos otherHalf = half ? bottomPos : bottomPos.above();
        level.destroyBlock(otherHalf, false);

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public MapColor getMapColor(BlockState state, BlockGetter level, BlockPos pos, MapColor defaultColor) {
        return state.getValue(COLOR).getMapColor();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) { return RenderShape.MODEL; }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            if (level.getBlockEntity(pos) instanceof CultivatorBlockEntity e) {
                player.openMenu(e);
            } else if (level.getBlockEntity(pos) instanceof CultivatorTopBlockEntity e) {
                player.openMenu(e); // top safely delegates to bottom internally
            }
            return InteractionResult.CONSUME;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        // bottom requires air above; top is placed by bottom
        return !this.half ? level.getBlockState(pos.above()).isAir() : true;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (level.isClientSide) return;
        if (half) return; // top is placed by bottom
        level.setBlock(pos.above(), ModBlocks.CULTIVATE_TOP.get().defaultBlockState().setValue(COLOR, state.getValue(COLOR)), 3);
    }

    public boolean areBothHalvesValid(BlockState state, BlockPos pos, Level level) {
        if (!(state.getBlock() instanceof CultivatorBlock cultivator)) return false;
        pos = cultivator.half ? pos.below() : pos.above();
        if (!(level.getBlockState(pos).getBlock() instanceof CultivatorBlock otherHalf)) return false;
        return cultivator.half != otherHalf.half;
    }

    @SuppressWarnings("unchecked")
    public static <E extends BlockEntity, A extends BlockEntity>
    BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> given, BlockEntityType<E> expected, BlockEntityTicker<? super E> ticker) {
        return expected == given ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        super.createBlockStateDefinition(b);
        b.add(COLOR);
    }
}
