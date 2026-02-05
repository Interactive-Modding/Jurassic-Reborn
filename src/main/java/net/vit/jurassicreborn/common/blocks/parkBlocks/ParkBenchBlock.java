package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.entities.ParkBenchSeatLeftEntity;
import net.vit.jurassicreborn.common.entities.ParkBenchSeatRightEntity;

import javax.annotation.Nullable;

public class ParkBenchBlock extends HorizontalDirectionalBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<BenchPart> PART =
            net.minecraft.world.level.block.state.properties.EnumProperty.create("part", BenchPart.class);

    private static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12, 16);

    public ParkBenchBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(PART, BenchPart.LEFT));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, PART);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        Direction facing = state.getValue(FACING);
        BlockState out = state.setValue(FACING, mirror.mirror(facing));
        if (mirror == Mirror.LEFT_RIGHT) {
            BenchPart part = out.getValue(PART);
            out = out.setValue(PART, part == BenchPart.LEFT ? BenchPart.RIGHT : BenchPart.LEFT);
        }
        return out;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        Direction perp = facing.getClockWise();
        BlockPos pos = ctx.getClickedPos();
        BlockPos other = pos.relative(perp);
        if (!ctx.getLevel().getBlockState(other).canBeReplaced(ctx)) return null;
        return this.defaultBlockState().setValue(FACING, facing).setValue(PART, BenchPart.LEFT);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (level.isClientSide) return;
        Direction facing = state.getValue(FACING);
        Direction perp = facing.getClockWise();
        BlockPos other = pos.relative(perp);
        BlockState rightHalf = state.setValue(PART, BenchPart.RIGHT);
        level.setBlock(other, rightHalf, Block.UPDATE_ALL);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BenchPart part = state.getValue(PART);
        Direction facing = state.getValue(FACING);
        Direction perp = facing.getClockWise();
        BlockPos otherPos = (part == BenchPart.LEFT) ? pos.relative(perp) : pos.relative(perp.getOpposite());
        BlockState other = level.getBlockState(otherPos);
        if (!level.isClientSide && !player.isCreative()) {
            BlockPos dropAt = (part == BenchPart.LEFT) ? pos : otherPos; // drop at the LEFT half’s spot
            popResource(level, dropAt, new ItemStack(this.asItem()));
        }
        if (other.is(this) && other.getValue(PART) != part) {
            if (!level.isClientSide) level.destroyBlock(otherPos, false, player);
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state,
                              @Nullable net.minecraft.world.level.block.entity.BlockEntity be, ItemStack tool) {
        // Intentionally empty — handled in playerWillDestroy
    }


    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        Direction perp = facing.getClockWise();
        BenchPart part = state.getValue(PART);

        BlockPos expectedPartner = (part == BenchPart.LEFT) ? pos.relative(perp) : pos.relative(perp.getOpposite());
        if (neighborPos.equals(expectedPartner)) {
            if (!neighbor.is(this) || neighbor.getValue(PART) == part || neighbor.getValue(FACING) != facing) {
                level.removeBlock(pos, false);
            }
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter g, BlockPos pos, CollisionContext c) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter g, BlockPos pos, CollisionContext c) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        if (player.isPassenger()) return InteractionResult.PASS;

        Direction facing = state.getValue(FACING);
        Direction perp = facing.getClockWise();
        BenchPart part = state.getValue(PART);

        final double side = 0.0;
        final double depth = -0.20;
        double leftX  = perp.getStepX() * (-side) + facing.getStepX() * depth;
        double leftZ  = perp.getStepZ() * (-side) + facing.getStepZ() * depth;
        double rightX = perp.getStepX() * ( side) + facing.getStepX() * depth;
        double rightZ = perp.getStepZ() * ( side) + facing.getStepZ() * depth;

        if (part == BenchPart.LEFT) {
            if (tryMountLeft(level, pos, facing, leftX, leftZ, player))  return InteractionResult.sidedSuccess(false);
            if (tryMountRight(level, pos, facing, rightX, rightZ, player)) return InteractionResult.sidedSuccess(false);
        } else {
            if (tryMountRight(level, pos, facing, rightX, rightZ, player)) return InteractionResult.sidedSuccess(false);
            if (tryMountLeft(level, pos, facing, leftX, leftZ, player))  return InteractionResult.sidedSuccess(false);
        }
        return InteractionResult.PASS;
    }

    private boolean tryMountLeft(Level level, BlockPos pos, Direction facing, double xOff, double zOff, Player player) {
        ParkBenchSeatLeftEntity seat = getOrCreateLeftSeat(level, pos, facing, xOff, zOff);
        if (seat != null && seat.getPassengers().isEmpty()) {
            player.startRiding(seat, true);
            return true;
        }
        return false;
    }

    private boolean tryMountRight(Level level, BlockPos pos, Direction facing, double xOff, double zOff, Player player) {
        ParkBenchSeatRightEntity seat = getOrCreateRightSeat(level, pos, facing, xOff, zOff);
        if (seat != null && seat.getPassengers().isEmpty()) {
            player.startRiding(seat, true);
            return true;
        }
        return false;
    }

    private ParkBenchSeatLeftEntity getOrCreateLeftSeat(Level level, BlockPos benchPos, Direction facing, double xOff, double zOff) {
        Vec3 seatPos = new Vec3(benchPos.getX() + 0.5 + xOff, benchPos.getY() + 0.30, benchPos.getZ() + 0.5 + zOff);
        ParkBenchSeatLeftEntity existing = level.getEntitiesOfClass(
                ParkBenchSeatLeftEntity.class,
                new AABB(seatPos.x - 0.12, seatPos.y - 0.12, seatPos.z - 0.12,
                        seatPos.x + 0.12, seatPos.y + 0.12, seatPos.z + 0.12)
        ).stream().findFirst().orElse(null);
        if (existing != null) return existing;

        ParkBenchSeatLeftEntity seat = new ParkBenchSeatLeftEntity(level, benchPos, xOff, zOff);
        seat.setYRot(facing.toYRot());
        level.addFreshEntity(seat);
        return seat;
    }

    private ParkBenchSeatRightEntity getOrCreateRightSeat(Level level, BlockPos benchPos, Direction facing, double xOff, double zOff) {
        Vec3 seatPos = new Vec3(benchPos.getX() + 0.5 + xOff, benchPos.getY() + 0.30, benchPos.getZ() + 0.5 + zOff);
        ParkBenchSeatRightEntity existing = level.getEntitiesOfClass(
                ParkBenchSeatRightEntity.class,
                new AABB(seatPos.x - 0.12, seatPos.y - 0.12, seatPos.z - 0.12,
                        seatPos.x + 0.12, seatPos.y + 0.12, seatPos.z + 0.12)
        ).stream().findFirst().orElse(null);
        if (existing != null) return existing;

        ParkBenchSeatRightEntity seat = new ParkBenchSeatRightEntity(level, benchPos, xOff, zOff);
        seat.setYRot(facing.toYRot());
        level.addFreshEntity(seat);
        return seat;
    }
}
