package net.vit.jurassicreborn.common.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredItem;
import net.vit.jurassicreborn.common.blocks.entities.SkullDisplayBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.Fossils.FossilItem;
import net.vit.jurassicreborn.common.items.ModItems;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SkullDisplayBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;
    private static final VoxelShape SHAPE_UP = Block.box(-2, 0, -2, 18, 20, 18);
    private static final VoxelShape SHAPE_SIDE = Block.box(0, -4, 0, 16, 16, 16);

    public SkullDisplayBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.UP).setValue(BlockStateProperties.WATERLOGGED, false));
    }
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos,
                         BlockState newState, boolean isMoving) {

        if (!state.is(newState.getBlock())) {
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SkullDisplayBlockEntity tile) {
                ItemStack drop = getItemFromTile(tile);
                if (!drop.isEmpty()) {
                    Block.popResource(level, pos, drop);
                }
            }
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SkullDisplayBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return null;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(FACING) == Direction.UP ? SHAPE_UP : SHAPE_SIDE;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return false;
    }

    @Override
    public boolean isCollisionShapeFullBlock(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return canBlockStay(level, pos, state);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving);
        this.checkAndDropBlock(level, pos, state);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.randomTick(state, level, pos, random);
        this.checkAndDropBlock(level, pos, state);
    }

    private void checkAndDropBlock(Level level, BlockPos pos, BlockState state) {
        if (!(level instanceof ServerLevel)) return;

        if (!canBlockStay(level, pos, state)) {

            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof SkullDisplayBlockEntity tile) {
                ItemStack drop = getItemFromTile(tile);
                if (!drop.isEmpty()) {
                    Block.popResource(level, pos, drop);
                }
            }

            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }




    private static boolean canBlockStay(LevelReader world, BlockPos pos, BlockState state) {
        Direction facing = state.getValue(FACING);
        if (facing.getAxis() == Direction.Axis.Y) {
            return world.getBlockState(pos.below()).canOcclude();
        } else {
            return world.getBlockState(pos.relative(mirror(facing))).canOcclude();
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidstate = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(FACING, context.getClickedFace()).setValue(BlockStateProperties.WATERLOGGED, fluidstate.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, BlockStateProperties.WATERLOGGED);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof SkullDisplayBlockEntity tile) {
            return getItemFromTile(tile);
        }
        return ItemStack.EMPTY;
    }

    private static ItemStack getItemFromTile(SkullDisplayBlockEntity tile) {
        Dinosaur dino = tile.getDinosaur();
        if (dino == null) return ItemStack.EMPTY;

        Map<String, DeferredItem<Item>> map = tile.isFossilized()
                ? ModItems.BONES.get(dino)
                : ModItems.FRESH_BONES.get(dino);
        if (map == null) return ItemStack.EMPTY;

        DeferredItem<Item> entry = map.get("skull");
        if (entry == null) return ItemStack.EMPTY;

        ItemStack stack = new ItemStack(entry.get());
        FossilItem.setHasStand(stack, tile.hasStand());
        return stack;
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = new ArrayList<>(1);

        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof SkullDisplayBlockEntity tile) {
            ItemStack stack = getItemFromTile(tile);
            if (!stack.isEmpty()) {
                drops.add(stack);
            }
        }

        return drops;
    }


    private static Direction mirror(Direction facing) {
        return switch (facing) {
            case NORTH -> Direction.SOUTH;
            case SOUTH -> Direction.NORTH;
            case EAST -> Direction.WEST;
            case WEST -> Direction.EAST;
            default -> facing;
        };
    }
}
