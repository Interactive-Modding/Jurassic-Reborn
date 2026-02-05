package net.vit.jurassicreborn.common.blocks.parkBlocks;

import com.google.common.collect.Lists;
import net.vit.jurassicreborn.common.entities.vehicle.VehicleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.Predicate;

import static net.minecraft.core.Direction.*;

public class TourRailBlock extends Block implements EntityBlock {

    public static EnumProperty<EnumRailDirection> SHAPE = EnumProperty.create("shape", EnumRailDirection.class);

    private static final ThreadLocal<Set<BlockPos>> UPDATING_RAILS = ThreadLocal.withInitial(HashSet::new);

    public static final VoxelShape FLAT = Block.box(0.0D, 0.0D, 0.0D, 16D - 0.0001, 2.0D, 16.0D);
    public static final VoxelShape ASCENDING = Block.box(0.0D, 0.0D, 0.0D, 16.0D-0.0001, 8.0D, 16.0D);

    private final SpeedType speedType;

    public TourRailBlock(Properties p_49795_, TourRailBlock.SpeedType speedType) {
        super(p_49795_);
        this.speedType = speedType;
        this.registerDefaultState(this.getStateDefinition().any().setValue(SHAPE, EnumRailDirection.NORTH_SOUTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(SHAPE);
    }
    public SpeedType getSpeedType() {
        return speedType;
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TourRailBlockEntity(pos, state);//todo: entities
    }



    public static TourRailBlock.EnumRailDirection getRailDirection(Level level, BlockPos pos) {
        BlockState st = level.getBlockState(pos);
        if (!(st.getBlock() instanceof TourRailBlock)) return EnumRailDirection.NORTH_SOUTH;
        if (st.hasProperty(SHAPE)) return st.getValue(SHAPE);
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TourRailBlockEntity tr) return tr.getDirection();
        return EnumRailDirection.NORTH_SOUTH;
    }


    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {

        return pState.getValue(SHAPE).isAscending() ? ASCENDING : FLAT;
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader worldIn, BlockPos pos) {
        return worldIn.getBlockState(pos.below()).isFaceSturdy(worldIn, pos.below(), Direction.UP);
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        if (pLevel.isClientSide) return;
        Set<BlockPos> updating = UPDATING_RAILS.get();
        if (updating.contains(pPos)) return;

        updating.add(pPos);
        try {
            this.updateDir(pLevel, pPos, pState, true);
            pState.neighborChanged(pLevel, pPos, this, pPos, pIsMoving);
        } finally {
            updating.remove(pPos);
        }
    }

    @Override
    public BlockState getStateAtViewpoint(BlockState state, BlockGetter level, BlockPos pos, Vec3 viewpoint) {
        return level.getBlockEntity(pos) instanceof TourRailBlockEntity ? state.setValue(SHAPE, getRailDirection(level, pos)) : this.defaultBlockState();
    }

    public static TourRailBlock.EnumRailDirection getRailDirection(BlockGetter world, BlockPos pos) {
        return world.getBlockEntity(pos) instanceof  TourRailBlockEntity entity? entity.getDirection() : EnumRailDirection.NORTH_SOUTH;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
        if (!pLevel.isClientSide) {
            pLevel.markAndNotifyBlock(pPos, pLevel.getChunkAt(pPos), pState, pNewState, 3, 512);

        }
    }

    private BlockState updateDir(Level worldIn, BlockPos pos, BlockState state, boolean initialPlacement)
    {
        return (new TourRailBlock.Rail(worldIn, pos, state)).place(initialPlacement).getBlockState();
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean pIsMoving) {
        if (worldIn.isClientSide)
            return;

        TourRailBlock.EnumRailDirection dir = getRailDirection(worldIn, pos);
        boolean flag = false;

        if (!worldIn.getBlockState(pos.below()).isFaceSturdy(worldIn, pos.below(), Direction.UP))
        {
            flag = true;
        }

        if (dir == TourRailBlock.EnumRailDirection.ASCENDING_EAST && !worldIn.getBlockState(pos.east()).isFaceSturdy(worldIn, pos.east(), Direction.UP))
        {
            flag = true;
        }
        else if (dir == TourRailBlock.EnumRailDirection.ASCENDING_WEST && !worldIn.getBlockState(pos.west()).isFaceSturdy(worldIn, pos.west(), Direction.UP))
        {
            flag = true;
        }
        else if (dir == TourRailBlock.EnumRailDirection.ASCENDING_NORTH && !worldIn.getBlockState(pos.north()).isFaceSturdy(worldIn, pos.north(), Direction.UP))
        {
            flag = true;
        }
        else if (dir == TourRailBlock.EnumRailDirection.ASCENDING_SOUTH && !worldIn.getBlockState(pos.south()).isFaceSturdy(worldIn, pos.south(), Direction.UP))
        {
            flag = true;
        }

        if (flag && !worldIn.getBlockState(pos).isAir()) {
            worldIn.destroyBlock(pos, true);
        } else {
            this.updateDir(worldIn, pos, state, false);
        }



    }

    public static boolean isRailBlock(Level worldIn, BlockPos pos)
    {
        return isRailBlock(worldIn.getBlockState(pos));
    }

    public static boolean isRailBlock(BlockState state)
    {
        Block block = state.getBlock();
        return block instanceof TourRailBlock;
    }

    public enum EnumRailDirection implements Serializable, StringRepresentable {
        NORTH_SOUTH         ( 0,  0, -1,  0,  0,  1, 0, "tour_rail_straight"),//0
        EAST_WEST           (-1,  0,  0,  1,  0,  0, 90, "tour_rail_straight"),//1



        SOUTH_EAST          ( 0,  0,  1,  1,  0,  0, 270, "tour_rail_corner"),//2
        SOUTH_WEST          ( 0,  0,  1, -1,  0,  0, 180, "tour_rail_corner"),//3
        NORTH_WEST          ( 0,  0, -1, -1,  0,  0, 90, "tour_rail_corner"),//4
        NORTH_EAST          ( 0,  0, -1,  1,  0,  0, 0, "tour_rail_corner"),//5

        ASCENDING_EAST      (-1, -1,  0,  1,  0,  0, 90, "tour_rail_slope"),//6
        ASCENDING_WEST      (-1,  0,  0,  1, -1,  0, 270, "tour_rail_slope"),//7
        ASCENDING_NORTH     ( 0,  0, -1,  0, -1,  1, 180, "tour_rail_slope"),//8
        ASCENDING_SOUTH     ( 0, -1, -1,  0,  0,  1, 0, "tour_rail_slope"),//9

        DIAGONAL_NE_SW      ( 0.5F, 0, -0.5F, -0.5F, 0,  0.5F, 90, "tour_rail_diagonal"),
        DIAGONAL_NW_SE      ( 0.5F, 0, 0.5F,  -0.5F, 0, -0.5F, 0, "tour_rail_diagonal"),

        HORIZONTAL_NE       (NORTH, EAST, DIAGONAL_NE_SW, EAST_WEST, 90, "tour_rail_diagonal_turn_right"),//12
        HORIZONTAL_NW       (NORTH, WEST, DIAGONAL_NW_SE, EAST_WEST, 270, "tour_rail_diagonal_turn_left"),//13
        HORIZONTAL_SE       (SOUTH, EAST, DIAGONAL_NW_SE, EAST_WEST, 90, "tour_rail_diagonal_turn_left"),//14
        HORIZONTAL_SW       (SOUTH, WEST, DIAGONAL_NE_SW, EAST_WEST, 270, "tour_rail_diagonal_turn_right"),//15

        VERTICAL_NE         (NORTH, EAST, DIAGONAL_NE_SW, NORTH_SOUTH, 180, "tour_rail_diagonal_turn_left"),//16
        VERTICAL_NW         (NORTH, WEST, DIAGONAL_NW_SE, NORTH_SOUTH, 180, "tour_rail_diagonal_turn_right"),//17
        VERTICAL_SE         (SOUTH, EAST, DIAGONAL_NW_SE, NORTH_SOUTH, 0, "tour_rail_diagonal_turn_right"),//18
        VERTICAL_SW         (SOUTH, WEST, DIAGONAL_NE_SW, NORTH_SOUTH, 0, "tour_rail_diagonal_turn_left");//18

        private final Type type;

        private float forwardX;
        private float forwardY;
        private float forwardZ;
        private float backwardsX;
        private float backwardsY;
        private float backwardsZ;

        private Predicate<Direction> facingPredicate; //Dosnt really need to be a predicate
        private EnumRailDirection diagonalDirection;
        private EnumRailDirection straightDirection;
        public final int rotation;
        public final String modelName;

        EnumRailDirection(float forwardX, float forward_y, float forward_z, float backwards_x, float backwards_y, float backwards_z, int rotation, String modelId) {
            this.forwardX = forwardX;
            this.forwardY = forward_y;
            this.forwardZ = forward_z;
            this.backwardsX = backwards_x;
            this.backwardsY = backwards_y;
            this.backwardsZ = backwards_z;
            this.type = Type.VALUE;
            this.rotation = rotation;
            this.modelName = modelId;
        }

        EnumRailDirection(Direction facing, Direction facing2, EnumRailDirection diagonalDirection, EnumRailDirection straightDirection, int rotation, String modelId) {
            type = Type.COPYCAT;
            this.facingPredicate = face -> face == facing || face == facing2;
            this.diagonalDirection = diagonalDirection;
            this.straightDirection = straightDirection;
            this.rotation = rotation;
            this.modelName = modelId;
        }

        public boolean isAscending() {
            return this == ASCENDING_NORTH || this == ASCENDING_EAST || this == ASCENDING_SOUTH || this == ASCENDING_WEST;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }

        public float getForwardX(Direction face) {
            if(type == Type.COPYCAT) {
                return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getForwardX(null);
            }
            return forwardX;
        }

        public float getForwardY(Direction face) {
            if(type == Type.COPYCAT) {
                if(type == Type.COPYCAT) {
                    return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getForwardY(null);
                }
            }
            return forwardY;
        }

        public float getForwardZ(Direction face) {
            if(type == Type.COPYCAT) {
                if(type == Type.COPYCAT) {
                    return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getForwardZ(null);
                }
            }
            return forwardZ;
        }

        public float getBackwardsX(Direction face) {
            if(type == Type.COPYCAT) {
                if(type == Type.COPYCAT) {
                    return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getBackwardsX(null);
                }
            }
            return backwardsX;
        }

        public float getBackwardsY(Direction face) {
            if(type == Type.COPYCAT) {
                if(type == Type.COPYCAT) {
                    return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getBackwardsY(null);
                }
            }
            return backwardsY;
        }

        public float getBackwardsZ(Direction face) {
            if(type == Type.COPYCAT) {
                if(type == Type.COPYCAT) {
                    return (facingPredicate.test(face) ? this.diagonalDirection : this.straightDirection).getBackwardsZ(null);
                }
            }
            return backwardsZ;
        }

        public Direction getFacing(){
            return switch (this){
                case ASCENDING_EAST -> EAST;
                case ASCENDING_WEST -> WEST;
                case ASCENDING_NORTH -> NORTH;
                case ASCENDING_SOUTH -> SOUTH;
                default -> NORTH;
            };
        }


        @Override
        public String getSerializedName() {
            return this.toString();
        }

        private enum Type {
            VALUE, COPYCAT
        }

        public String getName(){
            return this.name();
        }
    }

    public class Rail
    {
        private final Level world;
        private final BlockPos pos;
        private final TourRailBlock block;
        private BlockState state;
        private final List<BlockPos> connectedRails = Lists.newArrayList();

        public Rail(Level worldIn, BlockPos pos, BlockState state)
        {
            this.world = worldIn;
            this.pos = pos;
            this.state = state;
            this.block = (TourRailBlock)state.getBlock();
            TourRailBlock.EnumRailDirection TourRailBlock$enumraildirection = getRailDirection(worldIn, pos);
            this.updateConnectedRails(TourRailBlock$enumraildirection);
        }

        public List<BlockPos> getConnectedRails()
        {
            return this.connectedRails;
        }

        private void updateConnectedRails(TourRailBlock.EnumRailDirection railDirection)
        {
            this.connectedRails.clear();

            switch (railDirection)
            {
                case NORTH_SOUTH:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south());
                    break;
                case EAST_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east());
                    break;
                case ASCENDING_EAST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.east().above());
                    break;
                case ASCENDING_WEST:
                    this.connectedRails.add(this.pos.west().above());
                    this.connectedRails.add(this.pos.east());
                    break;
                case ASCENDING_NORTH:
                    this.connectedRails.add(this.pos.north().above());
                    this.connectedRails.add(this.pos.south());
                    break;
                case ASCENDING_SOUTH:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().above());
                    break;
                case SOUTH_EAST:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.south());
                    break;
                case SOUTH_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.south());
                    break;
                case NORTH_WEST:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.north());
                    break;
                case NORTH_EAST:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.north());
                    break;
                case DIAGONAL_NE_SW:
                    this.connectedRails.add(this.pos.north().east());
                    this.connectedRails.add(this.pos.south().west());
                    break;
                case DIAGONAL_NW_SE:
                    this.connectedRails.add(this.pos.north().west());
                    this.connectedRails.add(this.pos.south().east());
                    break;
                case HORIZONTAL_NE:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.north().east());
                    break;
                case HORIZONTAL_SE:
                    this.connectedRails.add(this.pos.west());
                    this.connectedRails.add(this.pos.south().east());
                    break;
                case HORIZONTAL_NW:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.north().west());
                    break;
                case HORIZONTAL_SW:
                    this.connectedRails.add(this.pos.east());
                    this.connectedRails.add(this.pos.south().west());
                    break;
                case VERTICAL_NE:
                    this.connectedRails.add(this.pos.south());
                    this.connectedRails.add(this.pos.north().east());
                    break;
                case VERTICAL_SE:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().east());
                    break;
                case VERTICAL_NW:
                    this.connectedRails.add(this.pos.south());
                    this.connectedRails.add(this.pos.north().west());
                    break;
                case VERTICAL_SW:
                    this.connectedRails.add(this.pos.north());
                    this.connectedRails.add(this.pos.south().west());
                    break;
            }
        }

        private void removeSoftConnections()
        {
            for (int i = 0; i < this.connectedRails.size(); ++i)
            {
                TourRailBlock.Rail TourRailBlock$rail = this.findRailAt(this.connectedRails.get(i));

                if (TourRailBlock$rail != null && TourRailBlock$rail.isConnectedToRail(this))
                {
                    this.connectedRails.set(i, TourRailBlock$rail.pos);
                }
                else
                {
                    this.connectedRails.remove(i--);
                }
            }
        }

        private boolean hasRailAt(BlockPos pos)
        {
            return TourRailBlock.isRailBlock(this.world, pos) || TourRailBlock.isRailBlock(this.world, pos.above()) || TourRailBlock.isRailBlock(this.world, pos.below());
        }

        @Nullable
        private TourRailBlock.Rail findRailAt(BlockPos pos)
        {
            BlockState iblockstate = this.world.getBlockState(pos);

            if (TourRailBlock.isRailBlock(iblockstate))
            {
                return TourRailBlock.this.new Rail(this.world, pos, iblockstate);
            }
            else
            {
                BlockPos lvt_2_1_ = pos.above();
                iblockstate = this.world.getBlockState(lvt_2_1_);

                if (TourRailBlock.isRailBlock(iblockstate))
                {
                    return TourRailBlock.this.new Rail(this.world, lvt_2_1_, iblockstate);
                }
                else
                {
                    lvt_2_1_ = pos.below();
                    iblockstate = this.world.getBlockState(lvt_2_1_);
                    return TourRailBlock.isRailBlock(iblockstate) ? TourRailBlock.this.new Rail(this.world, lvt_2_1_, iblockstate) : null;
                }
            }
        }

        private boolean isConnectedToRail(TourRailBlock.Rail rail)
        {
            return this.isConnectedTo(rail.pos);
        }

        private boolean isConnectedTo(BlockPos posIn)
        {
            for (int i = 0; i < this.connectedRails.size(); ++i)
            {
                BlockPos blockpos = this.connectedRails.get(i);

                if (blockpos.getX() == posIn.getX() && blockpos.getZ() == posIn.getZ())
                {
                    return true;
                }
            }

            return false;
        }

        /**
         * Counts the number of rails adjacent to this rail.
         */
        protected int countAdjacentRails()
        {
            int i = 0;

            for (Direction enumfacing : Direction.Plane.HORIZONTAL)
            {
                if (this.hasRailAt(this.pos.relative(enumfacing)))
                {
                    ++i;
                }
            }

            for(int i1 = 0; i1 < 4; i1++) {
                if(this.hasRailAt(this.pos.north(i1 % 2 == 0 ? 1 : -1).east(Math.floorDiv(i1, 2) == 0 ? 1 : -1))) {
                    ++i;
                }
            }

            return i;
        }

        private boolean canConnectTo(TourRailBlock.Rail rail)
        {
            return this.isConnectedToRail(rail) || this.connectedRails.size() <= 2;
        }

        private void connectTo(TourRailBlock.Rail rail)
        {
            this.connectedRails.add(rail.pos);
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.isConnectedTo(blockpos);
            boolean flag1 = this.isConnectedTo(blockpos1);
            boolean flag2 = this.isConnectedTo(blockpos2);
            boolean flag3 = this.isConnectedTo(blockpos3);

            TourRailBlock.EnumRailDirection railDirection = null;

            if (flag || flag1)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
            }

            if (flag2 || flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.EAST_WEST;
            }

            if (flag1 && flag3 && !flag && !flag2)
            {
                railDirection = TourRailBlock.EnumRailDirection.SOUTH_EAST;
            }

            if (flag1 && flag2 && !flag && !flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.SOUTH_WEST;
            }

            if (flag && flag2 && !flag1 && !flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_WEST;
            }

            if (flag && flag3 && !flag1 && !flag2)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_EAST;
            }

            if (railDirection == TourRailBlock.EnumRailDirection.NORTH_SOUTH)
            {
                if (TourRailBlock.isRailBlock(this.world, blockpos.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_NORTH;
                }

                if (TourRailBlock.isRailBlock(this.world, blockpos1.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (railDirection == TourRailBlock.EnumRailDirection.EAST_WEST)
            {
                if (TourRailBlock.isRailBlock(this.world, blockpos3.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_EAST;
                }

                if (TourRailBlock.isRailBlock(this.world, blockpos2.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (railDirection == null) {
                BlockPos nw = this.pos.north().west();
                BlockPos ne = this.pos.north().east();
                BlockPos sw = this.pos.south().west();
                BlockPos se = this.pos.south().east();

                boolean can_nw = this.isConnectedTo(nw);
                boolean can_ne = this.isConnectedTo(ne);
                boolean can_sw = this.isConnectedTo(sw);
                boolean can_se = this.isConnectedTo(se);
                if(can_ne || can_sw) {
                    railDirection = EnumRailDirection.DIAGONAL_NE_SW;
                } else if(can_nw || can_se) {
                    railDirection = EnumRailDirection.DIAGONAL_NW_SE;
                } else {
                    railDirection = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
                }
            }

            boolean connectDiag = false;
            String connectionName = "";

            if(railDirection == EnumRailDirection.NORTH_SOUTH) {
                connectionName += "VERTICAL_";
                boolean connectedNorth = this.isConnectedTo(pos.north());
                boolean connectedSouth = this.isConnectedTo(pos.south());
                if(!connectedNorth || !connectedSouth) {
                    connectionName += connectedNorth ? "S" : "N";
                    BlockPos connectPos = pos.north(connectedNorth ? -1 : 1);
                    if(this.isConnectedTo(connectPos.east())) {
                        connectionName += "E";
                        connectDiag = true;
                    } else if(this.isConnectedTo(connectPos.west())) {
                        connectionName += "W";
                        connectDiag = true;
                    }

                }
            } else if(railDirection == EnumRailDirection.EAST_WEST) {
                connectionName += "HORIZONTAL_";
                boolean connectedEast = this.isConnectedTo(pos.east());
                boolean connectedWest = this.isConnectedTo(pos.west());
                if(!connectedEast || !connectedWest) {
                    String suffix = connectedEast ? "W" : "E";
                    BlockPos connectPos = pos.east(connectedEast ? -1 : 1);
                    if(this.isConnectedTo(connectPos.north())) {
                        connectionName += "N";
                        connectDiag = true;
                    } else if(this.isConnectedTo(connectPos.south())) {
                        connectionName += "S";
                        connectDiag = true;
                    }
                    connectionName += suffix;
                }
            }

            if(connectDiag) {
                railDirection = EnumRailDirection.valueOf(connectionName);
            }


            this.state = this.state.setValue(SHAPE, railDirection);
            this.applyRailDirection(railDirection, false);
        }

        private boolean hasNeighborRail(BlockPos posIn)
        {
            TourRailBlock.Rail TourRailBlock$rail = this.findRailAt(posIn);

            if (TourRailBlock$rail == null)
            {
                return false;
            }
            else
            {
                TourRailBlock$rail.removeSoftConnections();
                return TourRailBlock$rail.canConnectTo(this);
            }
        }

        public TourRailBlock.Rail place(boolean initialPlacement)
        {
            BlockPos blockpos = this.pos.north();
            BlockPos blockpos1 = this.pos.south();
            BlockPos blockpos2 = this.pos.west();
            BlockPos blockpos3 = this.pos.east();
            boolean flag = this.hasNeighborRail(blockpos);
            boolean flag1 = this.hasNeighborRail(blockpos1);
            boolean flag2 = this.hasNeighborRail(blockpos2);
            boolean flag3 = this.hasNeighborRail(blockpos3);
            TourRailBlock.EnumRailDirection railDirection = null;

            if ((flag || flag1) && !flag2 && !flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
            }

            if ((flag2 || flag3) && !flag && !flag1)
            {
                railDirection = TourRailBlock.EnumRailDirection.EAST_WEST;
            }

            if (flag1 && flag3 && !flag && !flag2)
            {
                railDirection = TourRailBlock.EnumRailDirection.SOUTH_EAST;
            }

            if (flag1 && flag2 && !flag && !flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.SOUTH_WEST;
            }

            if (flag && flag2 && !flag1 && !flag3)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_WEST;
            }

            if (flag && flag3 && !flag1 && !flag2)
            {
                railDirection = TourRailBlock.EnumRailDirection.NORTH_EAST;
            }

            if (railDirection == null)
            {
                if (flag || flag1)
                {
                    railDirection = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
                }

                if (flag2 || flag3)
                {
                    railDirection = TourRailBlock.EnumRailDirection.EAST_WEST;
                }
            }

            if (railDirection == TourRailBlock.EnumRailDirection.NORTH_SOUTH)
            {
                if (TourRailBlock.isRailBlock(this.world, blockpos.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_NORTH;
                }

                if (TourRailBlock.isRailBlock(this.world, blockpos1.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_SOUTH;
                }
            }

            if (railDirection == TourRailBlock.EnumRailDirection.EAST_WEST)
            {
                if (TourRailBlock.isRailBlock(this.world, blockpos3.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_EAST;
                }

                if (TourRailBlock.isRailBlock(this.world, blockpos2.above()))
                {
                    railDirection = TourRailBlock.EnumRailDirection.ASCENDING_WEST;
                }
            }

            if (railDirection == null) {
                BlockPos nw = this.pos.north().west();
                BlockPos ne = this.pos.north().east();
                BlockPos sw = this.pos.south().west();
                BlockPos se = this.pos.south().east();

                boolean can_nw = this.hasNeighborRail(nw);
                boolean can_ne = this.hasNeighborRail(ne);
                boolean can_sw = this.hasNeighborRail(sw);
                boolean can_se = this.hasNeighborRail(se);
                if(can_ne || can_sw) {
                    railDirection = EnumRailDirection.DIAGONAL_NE_SW;
                } else if(can_nw || can_se) {
                    railDirection = EnumRailDirection.DIAGONAL_NW_SE;
                } else {
                    railDirection = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
                }
            }

            boolean connectDiag = false;
            StringBuilder connectionName = new StringBuilder();

            if(railDirection == EnumRailDirection.NORTH_SOUTH) {
                connectionName.append("VERTICAL_");
                boolean connectedNorth = this.hasNeighborRail(pos.north());
                boolean connectedSouth = this.hasNeighborRail(pos.south());
                if(!connectedNorth || !connectedSouth) {
                    connectionName.append(connectedNorth ? "S" : "N");
                    BlockPos connectPos = pos.north(connectedNorth ? -1 : 1);
                    if(this.hasNeighborRail(connectPos.east())) {
                        connectionName.append("E");
                        connectDiag = true;
                    } else if(this.hasNeighborRail(connectPos.west())) {
                        connectionName.append("W");
                        connectDiag = true;
                    }

                }
            } else if(railDirection == EnumRailDirection.EAST_WEST) {
                connectionName.append("HORIZONTAL_");
                boolean connectedEast = this.hasNeighborRail(pos.east());
                boolean connectedWest = this.hasNeighborRail(pos.west());
                if(!connectedEast || !connectedWest) {
                    String suffix = connectedEast ? "W" : "E";
                    BlockPos connectPos = pos.east(connectedEast ? -1 : 1);
                    if(this.hasNeighborRail(connectPos.north())) {
                        connectionName.append("N");
                        connectDiag = true;
                    } else if(this.hasNeighborRail(connectPos.south())) {
                        connectionName.append("S");
                        connectDiag = true;
                    }
                    connectionName.append(suffix);
                }
            }

            if(connectDiag) {
                railDirection = EnumRailDirection.valueOf(connectionName.toString());
            }

            this.state = this.state.setValue(SHAPE, railDirection);
            this.updateConnectedRails(railDirection);

            boolean forceUpdate = initialPlacement;
            BlockState currentState = this.world.getBlockState(this.pos);
            if (!forceUpdate) {
                if (!currentState.is(this.block) || (currentState.hasProperty(SHAPE) && currentState.getValue(SHAPE) != railDirection)) {
                    forceUpdate = true;
                }
            }
            BlockEntity blockEntity = this.world.getBlockEntity(pos);
            if (!forceUpdate && blockEntity instanceof TourRailBlockEntity tourRailBlockEntity && tourRailBlockEntity.getDirection() != railDirection) {
                forceUpdate = true;
            }

            this.applyRailDirection(railDirection, forceUpdate);

            if (forceUpdate)
            {
                for (int i = 0; i < this.connectedRails.size(); ++i)
                {
                    TourRailBlock.Rail TourRailBlock$rail = this.findRailAt(this.connectedRails.get(i));

                    if (TourRailBlock$rail != null)
                    {
                        TourRailBlock$rail.removeSoftConnections();

                        if (TourRailBlock$rail.canConnectTo(this))
                        {
                            TourRailBlock$rail.connectTo(this);
                        }
                    }
                }
            }

            return this;
        }



        public BlockState getBlockState()
        {
            return this.state;
        }

        private void applyRailDirection(EnumRailDirection railDirection, boolean forceStateUpdate) {
            BlockState currentState = this.world.getBlockState(this.pos);
            boolean sameShape = currentState.is(this.block) && currentState.hasProperty(SHAPE) && currentState.getValue(SHAPE) == railDirection;

            if (forceStateUpdate || !sameShape) {
                Set<BlockPos> updating = UPDATING_RAILS.get();
                if (!updating.contains(this.pos)) {
                    updating.add(this.pos);
                    try {
                        this.world.setBlock(this.pos, this.state, 3);
                    } finally {
                        updating.remove(this.pos);
                    }
                }
            }

            BlockEntity blockEntity = this.world.getBlockEntity(this.pos);
            if (blockEntity instanceof TourRailBlockEntity tourRail && (forceStateUpdate || tourRail.getDirection() != railDirection)) {
                tourRail.setDirection(railDirection);
            }
        }
    }

    public enum SpeedType {
        NONE(null, -1),
        SLOW(VehicleEntity.Speed.SLOW, 0xa80000), //RED
        MEDIUM(VehicleEntity.Speed.MEDIUM, 0xff7600), //ORANGE
        FAST(VehicleEntity.Speed.FAST, 0x00a800); //GREEN

        private final VehicleEntity.Speed speed;
        private final int color;

        SpeedType(VehicleEntity.Speed speed, int color) {
            this.speed = speed;
            this.color = color;
        }

        @Nonnull
        public VehicleEntity.Speed getSpeed(VehicleEntity.Speed defaultSpeed) {
            return speed == null ? defaultSpeed : speed;
        }


        @Nonnull
        public int getColor() {
            return this.color;
        }
    }

//    @Override
//    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
//        return (pLevel1, pPos, pState1, pBlockEntity) -> {
//            if(pLevel1.getBlockEntity(pPos) instanceof TourRailBlockEntity rail){
//                pState1.getBlock().neighborChanged(pState1, pLevel1, pPos, pState1.getBlock(), null, false);
//            }
//        };
//    }
}
