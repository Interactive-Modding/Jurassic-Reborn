package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.world.item.Item;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.misc.ActionFigureItem;
import net.vit.jurassicreborn.common.util.NbtBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ActionFigureBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ActionFigureBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ActionFigureBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getBounds(level, pos);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        return this.defaultBlockState().setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    private static VoxelShape getBounds(BlockGetter world, BlockPos pos) {
        BlockEntity entity = world.getBlockEntity(pos);
        if (entity instanceof ActionFigureBlockEntity displayEntity) {
            DinosaurEntity dinoEntity = displayEntity.getEntity();
            if (dinoEntity == null)
                return Shapes.empty();
            Dinosaur dinosaur = dinoEntity.getDinosaur();
            if (!displayEntity.isSkeleton()) {
                Dinosaur metadata = dinosaur;
                float width = Mth.clamp(metadata.getAdultSizeX() * 0.25F, 0.1F, 1.0F);
                float height = Mth.clamp(metadata.getAdultSizeY() * 0.25F, 0.1F, 1.0F);
                float halfWidth = width / 2.0F;
                width *= 16;
                height *= 16;
                halfWidth *= 16;
                return Block.box(8 - halfWidth, 0, 8 - halfWidth, halfWidth + 8, height, halfWidth + 8);
            } else {
                return Block.box(0.01, 0.01, 0.01, 15.99, 15.99, 15.99);
            }
        }
        return Shapes.empty();
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return super.canSurvive(state, level, pos) && canBlockStay(level, pos);
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

    private void checkAndDropBlock(Level world, BlockPos pos, BlockState state) {
        if (!canBlockStay(world, pos)) {
            List drops = getDrops(state, new LootContext.Builder((ServerLevel) world)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withOptionalParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, null)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, world.getBlockEntity(pos)));

            for (Object drop : drops) {
                Block.popResource(world, pos, (ItemStack) drop);
            }

            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
        }
    }

    private static boolean canBlockStay(LevelReader world, BlockPos pos) {
        return world.getBlockState(pos.below()).getMaterial().isSolid();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return getItemFromTile(getTile((Level) level, pos));
    }

    public ActionFigureBlockEntity getTile(Level world, BlockPos pos) {
        return (ActionFigureBlockEntity) world.getBlockEntity(pos);
    }

    private static ItemStack getItemFromTile(ActionFigureBlockEntity tile) {
        byte variant = tile.getVariant();
        if (tile.isSkeleton()) {
            variant = tile.isMale() ? (byte) 1 : (byte) 2;
        }

        Dinosaur dino = tile.getEntity().getDinosaur();
        boolean skeleton = tile.isSkeleton();

        if (skeleton && tile.isFossile() && dino.isHybrid()) {
            return ItemStack.EMPTY;
        }

        Item item;
        if (skeleton) {
            if (tile.isFossile()) {
                item = ModItems.FOSSIL_SKELETONS.get(dino).get();
            } else {
                item = ModItems.FRESH_SKELETONS.get(dino).get();
            }
        } else {
            item = ModItems.ACTION_FIGURES.get(dino).get();
        }

        ItemStack stack = new ItemStack(item);

        // --- Always write *all* relevant tags! ---
        CompoundTag nbt = new CompoundTag();
        nbt.putByte("Gender", (byte) (tile.isMale() ? 1 : 2));
        nbt.putBoolean("IsFossile", tile.isFossile());
        nbt.putBoolean("IsSkeleton", tile.isSkeleton());
        nbt.putString("Dinosaur", dino.getName());
        nbt.putByte("Variant", tile.getVariant());

        stack.setTag(nbt);
        return stack;
    }

    @Override
    public List getDrops(BlockState state, LootContext.Builder builder) {
        List drops = new ArrayList<>(1);
        BlockPos pos = new BlockPos(builder.getParameter(LootContextParams.ORIGIN));
        BlockEntity blockEntity = builder.getLevel().getBlockEntity(pos);

        if (blockEntity instanceof ActionFigureBlockEntity tile) {
            Dinosaur dino = tile.getEntity().getDinosaur();
            if (tile.isFossile() && tile.isSkeleton() && dino.isHybrid()) {
                return drops; // empty, nothing dropped
            }
            ItemStack item = getItemFromTile(tile);
            if (!item.isEmpty()) {
                drops.add(item);
            }
        }

        return drops;
    }
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack stack) {
        if (blockEntity instanceof ActionFigureBlockEntity) {
            ItemStack itemStack = getItemFromTile((ActionFigureBlockEntity) blockEntity);
            Block.popResource(level, pos, itemStack);
        }
        super.playerDestroy(level, player, pos, state, blockEntity, stack);
    }

    @Nullable
    @Override
    public BlockEntityTicker getTicker(Level level, BlockState state, BlockEntityType blockEntityType) {
        return (level1, pos, state1, blockEntity) -> {
            if (blockEntity instanceof ActionFigureBlockEntity actionFigure) {
                actionFigure.tick(level1, pos, state1, actionFigure);
            }
        };
    }
}
