package net.vit.jurassicreborn.common.blocks.entities.fence;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.Sensing;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

/** Forge 1.19.2 port of the old ElectricFenceWireBlock. */
public class ElectricFenceWireBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final MapCodec<ElectricFenceWireBlock> LOW_CODEC =
            MapCodec.unit(() -> ModBlocks.LOW_SECURITY_FENCE_WIRE.get());
    public static final MapCodec<ElectricFenceWireBlock> MED_CODEC =
            MapCodec.unit(() -> ModBlocks.MED_SECURITY_FENCE_WIRE.get());
    public static final MapCodec<ElectricFenceWireBlock> HIGH_CODEC =
            MapCodec.unit(() -> ModBlocks.HIGH_SECURITY_FENCE_WIRE.get());

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return switch (type) {
            case LOW -> LOW_CODEC;
            case MED -> MED_CODEC;
            case HIGH -> HIGH_CODEC;
        };
    }

    /* ────── properties ────── */
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty WEST  = BooleanProperty.create("west");
    public static final BooleanProperty EAST  = BooleanProperty.create("east");
    public static final DirectionProperty UP_DIRECTION = DirectionProperty.create("up");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private final FenceType type;

    /* ────── ctor ────── */
    public ElectricFenceWireBlock(FenceType type, BlockBehaviour.Properties props) {
        super(props.noOcclusion().strength(2.0F));
        this.type = type;

        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(WEST,  false)
                .setValue(EAST,  false)
                .setValue(UP_DIRECTION, Direction.DOWN)
                .setValue(WATERLOGGED, false));
    }

    /* ────── shapes ────── */
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level,
                               BlockPos pos, CollisionContext ctx) {

        boolean n = state.getValue(NORTH);
        boolean s = state.getValue(SOUTH);
        boolean w = state.getValue(WEST);
        boolean e = state.getValue(EAST);

        if (!n && !s && !w && !e) n = s = w = e = true;   // stand-alone wire

        double minX = w ? 0.0 : 6.4;
        double maxX = e ? 16.0 : 9.6;
        double minZ = n ? 0.0 : 6.4;
        double maxZ = s ? 16.0 : 9.6;

        return Block.box(minX, 0.0, minZ, maxX, 16.0, maxZ);
    }
    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.setBlock(pos, rebuildConnections(level, pos, state), Block.UPDATE_ALL);
        }
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level,
                                        BlockPos pos, CollisionContext ctx) {

        // If the call is coming from Sensing (server-side AI LOS check), return empty.
        for (StackTraceElement e : Thread.currentThread().getStackTrace()) {
            if (e.getClassName().equals(Sensing.class.getName()))
                return Shapes.empty();
        }
        return super.getCollisionShape(state, level, pos, ctx);
    }
    public boolean isLadder(BlockState state, LevelReader level, BlockPos pos, LivingEntity entity) {
        return true;
    }
    /* ────── render ────── */
    @Override public RenderShape getRenderShape(BlockState s) { return RenderShape.MODEL; }

    /* ────── state container ────── */
    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(NORTH, SOUTH, WEST, EAST, UP_DIRECTION, WATERLOGGED);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    /* ────── placement & neighbour updates ────── */
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        boolean water = ctx.getLevel().getFluidState(ctx.getClickedPos()).getType() == Fluids.WATER;
        return rebuildConnections(ctx.getLevel(), ctx.getClickedPos(),
                defaultBlockState().setValue(WATERLOGGED, water));
    }
    public boolean isPathfindable(BlockState state, BlockGetter level, BlockPos pos,
                                  PathComputationType type) {
        return false;
    }

    public PathType getBlockPathType(BlockState state, BlockGetter level,
                                     BlockPos pos, @Nullable Mob mob) {
        return PathType.DAMAGE_OTHER;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbour,
                                  LevelAccessor level, BlockPos pos, BlockPos neighbourPos) {
        BlockState updated = rebuildConnections(level, pos, state);

        if (updated.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return updated;
    }

    /* ────── connection logic ────── */
    protected BlockState rebuildConnections(LevelReader world, BlockPos pos, BlockState state) {

        boolean n = false, s = false, w = false, e = false;
        Direction up = Direction.DOWN;

        // 1. Horizontal connections to other poles/wires
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            BlockPos offset = pos.relative(dir);
            if (canConnect(world, pos, offset, world.getBlockState(offset))) {
                switch (dir) {
                    case NORTH -> n = true;
                    case SOUTH -> s = true;
                    case WEST  -> w = true;
                    case EAST  -> e = true;
                }
            }
        }

        BlockPos.MutableBlockPos basePos = pos.mutable();
        findBase(world, basePos);

        if (world.getBlockState(basePos).getBlock() instanceof ElectricFenceBaseBlock) {
            for (Direction dir : Direction.Plane.HORIZONTAL) {
                BlockPos offset = basePos.relative(dir);
                BlockPos top = offset.above()
                        .above(getFenceHeight(world.getBlockState(offset.above()),
                                world, offset.above().mutable()) + 1);
                if (canConnect(world.getBlockState(top))) {
                    BlockPos.MutableBlockPos otherBase = top.mutable();
                    findBase(world, otherBase);
                    if (otherBase.getY() >= basePos.getY() &&
                            world.getBlockState(otherBase).getBlock() instanceof ElectricFenceBaseBlock &&
                            !(world.getBlockState(offset).getBlock() instanceof ElectricFenceBaseBlock)) {
                        up = dir;
                        break;
                    }
                }
            }
        }

        if (!n && !s && !w && !e) n = s = w = e = true;   // isolated => draw all 4 sides

        return state.setValue(NORTH, n).setValue(SOUTH, s)
                .setValue(WEST,  w).setValue(EAST,  e)
                .setValue(UP_DIRECTION, up);
    }

    private void findBase(BlockGetter level, BlockPos.MutableBlockPos pos) {
        int drift = 0;
        while (pos.getY() > 1 &&
                !(level.getBlockState(pos).getBlock() instanceof ElectricFenceBaseBlock) &&
                drift < 5) {
            pos.move(Direction.DOWN);
            drift = (level.getBlockState(pos).getBlock() instanceof ElectricFenceWireBlock) ? 0 : drift + 1;
        }
    }

    private int getFenceHeight(BlockState state, BlockGetter level, BlockPos.MutableBlockPos pos) {
        int height = 0;
        if (state.getBlock() instanceof ElectricFenceWireBlock) {
            // climb up
            while (pos.getY() < 255 && level.getBlockState(pos).getBlock() instanceof ElectricFenceWireBlock) {
                pos.move(Direction.UP);
                height++;
            }
            // reset & climb down
            pos.move(Direction.DOWN, height);
            while (pos.getY() > 1 && level.getBlockState(pos).getBlock() instanceof ElectricFenceWireBlock) {
                pos.move(Direction.DOWN);
                height++;
            }
        }
        return height;
    }

    /* ────── misc helpers ────── */
    private boolean canConnect(BlockState state) {
        return (state.getBlock() instanceof ElectricFenceWireBlock wire && wire.type == this.type) ||
                state.getBlock() instanceof ElectricFencePoleBlock;
    }

    private boolean canConnect(BlockGetter level, BlockPos current, BlockPos pos, BlockState state) {
        if (canConnect(state)) return true;

        BlockState down = level.getBlockState(pos.below());
        return down.getBlock() instanceof ElectricFenceWireBlock &&
                Math.abs(current.getY() - pos.below().getY()) == 1;
    }

    /* ────── BE / neighbour events ────── */
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ElectricFenceWireBlockEntity(pos, state);
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader world,
                                 BlockPos pos, BlockPos neighbour) {
        if (!world.isClientSide()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof ElectricFenceWireBlockEntity wire)
                wire.checkDisconnect();
        }
    }

    /* ────── entity collision (shocks) ────── */
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (level.isClientSide || !entity.isAlive() || !(entity instanceof LivingEntity liv))
            return;

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof ElectricFenceWireBlockEntity wire) || !wire.isPowered())
            return;

        // apply damage by fence strength
        switch (type) {
            case LOW  -> entity.hurt(entity.damageSources().lightningBolt(), 1.0F);
            case MED  -> entity.hurt(entity.damageSources().lightningBolt(), 2.0F);
            case HIGH -> entity.hurt(entity.damageSources().lightningBolt(), 3.0F);
        }

        // extra dino side-effects
        if (entity instanceof DinosaurEntity dino) {
            if (dino.wireTicks < 2) {
                dino.wireTicks++;
                dino.disableHerdingTicks = 200;
            }
        }

        // play zap every 10 ticks
        if (entity.tickCount % 10 == 0)
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                    SoundHandler.FENCE_SHOCK, SoundSource.BLOCKS,
                    0.25F, 1.0F);
    }

    /* ────── accessors ────── */
    public FenceType getType() { return type; }

    /* Always allow tall entities to collide with the hit-box above head height */
    @Override
    public boolean collisionExtendsVertically(BlockState s, BlockGetter l, BlockPos p, Entity e) {
        return true;
    }
}
