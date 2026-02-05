package net.vit.jurassicreborn.common.blocks.entities.incubator;

import net.minecraft.world.Containers;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import net.vit.jurassicreborn.common.blocks.base.BaseMachineBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer.DNACombinatorHybridizerBlockEntity;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class IncubatorBlock extends BaseMachineBlock {


    public static DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static VoxelShape MODEL_SHAPE_NORTH = Stream.of(
            Block.box(0, 0, 0, 16, 4, 16),
            Stream.of(
                    Block.box(1.5, 6, 1.5, 14.5, 20, 14.5),
                    Block.box(1, 6, 2, 15, 20, 14),
                    Block.box(2, 6, 1, 14, 20, 15),
                    Block.box(5.5, 10.5, 13.1, 10.5, 16.5, 15.1),
                    Stream.of(
                            Stream.of(
                                    Block.box(2.5, 17, 0, 13.5, 21, 16),
                                    Block.box(1, 17, 1, 15, 21, 15),
                                    Block.box(1.5, 17, 0.5, 14.5, 21, 15.5),
                                    Block.box(0.5, 17, 1.5, 15.5, 21, 14.5),
                                    Block.box(0, 17, 2.5, 16, 21, 13.5)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Stream.of(
            Block.box(13.975, 19, 14.975, 15, 23.025, 16),
            Block.box(14.975, 19, 13.975, 16, 23.025, 15),
            Block.box(14.475, 19, 14.475, 15.5, 23.025, 15.5),
            Block.box(13.975, 19, 13.975, 15, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(14.975, 19, 1, 16, 23.025, 2.025),
            Block.box(13.975, 19, 0, 15, 23.025, 1.025),
            Block.box(14.475, 19, 0.5, 15.5, 23.025, 1.525),
            Block.box(13.975, 19, 1, 15, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(1, 19, 0, 2.025, 23.025, 1.025),
            Block.box(0, 19, 1, 1.025, 23.025, 2.025),
            Block.box(0.5, 19, 0.5, 1.525, 23.025, 1.525),
            Block.box(1, 19, 1, 2.025, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0, 19, 13.975, 1.025, 23.025, 15),
            Block.box(1, 19, 14.975, 2.025, 23.025, 16),
            Block.box(0.5, 19, 14.475, 1.525, 23.025, 15.5),
            Block.box(1, 19, 13.975, 2.025, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(2, 19, 14.5, 14, 23, 16.5),
            Block.box(2, 19, -0.5, 14, 23, 1.5),
            Block.box(-0.5, 19, 2, 1.5, 23, 14),
            Block.box(14.5, 19, 2, 16.5, 23, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(4.4999999999999964, 19.5, 16, 11.5, 22.5, 17),
            Stream.of(
            Block.box(2.9999999999999964, 19, -7, 13, 21, 0),
            Block.box(5.5, 20.25, -6.5000000000000036, 10.5, 21.25, -3.5),
            Block.box(4, 20.955073749564608, -2.9514187779635215, 12, 21.455073749564608, -1.9514187779635215),
            Block.box(4, 21.455073749564608, -2.7514187779635186, 12, 21.955073749564608, -1.7514187779635186),
            Block.box(4, 21.955073749564608, -2.551418777963523, 12, 22.455073749564608, -1.551418777963523),
            Block.box(4, 22.455073749564608, -2.3264187779635215, 12, 22.955073749564608, -1.3264187779635215),
            Block.box(4, 22.955073749564615, -2.1264187779635186, 12, 23.455073749564615, -1.1264187779635186),
            Block.box(4, 23.455073749564615, -1.9264187779635158, 12, 23.955073749564615, -0.9264187779635158),
            Block.box(4, 23.955073749564615, -1.7264187779635165, 12, 24.455073749564615, -0.7264187779635165)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(5.9999999999999964, 22.75, 16.249999999999996, 9.999999999999996, 23.25, 16.749999999999996),
            Stream.of(
            Block.box(-0.5, 0, 1, 16.5, 4, 15),
            Block.box(1, 0, -0.5, 15, 4, 16.5),
            Block.box(2, 0, -1, 14, 4, 17),
            Block.box(-1, 0, 2, 17, 4, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0.5, 4, 0.5, 15.5, 6, 15.5),
            Block.box(0, 4, 1, 16, 6, 15),
            Block.box(1, 4, 0, 15, 6, 16),
            Block.box(2, 4, -0.5, 14, 6, 16.5),
            Block.box(-0.5, 4, 2, 16.5, 6, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_WEST = Stream.of(
            Block.box(0, 0, 0, 16, 4, 16),
            Stream.of(
                    Block.box(1.5, 6, 1.5, 14.5, 20, 14.5),
                    Block.box(2, 6, 1, 14, 20, 15),
                    Block.box(1, 6, 2, 15, 20, 14),
                    Block.box(13.1, 10.5, 5.5, 15.1, 16.5, 10.5),
                    Stream.of(
                            Stream.of(
                                    Block.box(0, 17, 2.5, 16, 21, 13.5),
                                    Block.box(1, 17, 1, 15, 21, 15),
                                    Block.box(0.5, 17, 1.5, 15.5, 21, 14.5),
                                    Block.box(1.5, 17, 0.5, 14.5, 21, 15.5),
                                    Block.box(2.5, 17, 0, 13.5, 21, 16)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Stream.of(
            Block.box(14.975, 19, 1, 16, 23.025, 2.025),
            Block.box(13.975, 19, 0, 15, 23.025, 1.0250000000000004),
            Block.box(14.475, 19, 0.5, 15.5, 23.025, 1.5250000000000004),
            Block.box(13.975, 19, 1, 15, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(1, 19, 0, 2.025, 23.025, 1.0250000000000004),
            Block.box(0, 19, 1, 1.0250000000000004, 23.025, 2.025),
            Block.box(0.5, 19, 0.5, 1.5250000000000004, 23.025, 1.5250000000000004),
            Block.box(1, 19, 1, 2.025, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0, 19, 13.975, 1.0250000000000004, 23.025, 15),
            Block.box(1, 19, 14.975, 2.025, 23.025, 16),
            Block.box(0.5, 19, 14.475, 1.5250000000000004, 23.025, 15.5),
            Block.box(1, 19, 13.975, 2.025, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(13.975, 19, 14.975, 15, 23.025, 16),
            Block.box(14.975, 19, 13.975, 16, 23.025, 15),
            Block.box(14.475, 19, 14.475, 15.5, 23.025, 15.5),
            Block.box(13.975, 19, 13.975, 15, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(14.499999999999998, 19, 2, 16.5, 23, 14),
            Block.box(-0.5, 19, 2, 1.5, 23, 14),
            Block.box(2, 19, 14.499999999999998, 14, 23, 16.5),
            Block.box(2, 19, -0.5, 14, 23, 1.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(16, 19.5, 4.5, 17, 22.5, 11.5),
            Stream.of(
            Block.box(-7, 19, 3, 0, 21, 13),
            Block.box(-6.5, 20.25, 5.5, -3.5, 21.25, 10.5),
            Block.box(-2.951418777963518, 20.955073749564608, 4, -1.951418777963518, 21.455073749564608, 12),
            Block.box(-2.751418777963515, 21.455073749564608, 4, -1.751418777963515, 21.955073749564608, 12),
            Block.box(-2.5514187779635193, 21.955073749564608, 4, -1.5514187779635193, 22.455073749564608, 12),
            Block.box(-2.326418777963518, 22.455073749564608, 4, -1.326418777963518, 22.955073749564608, 12),
            Block.box(-2.126418777963515, 22.955073749564615, 4, -1.126418777963515, 23.455073749564615, 12),
            Block.box(-1.9264187779635122, 23.455073749564615, 4, -0.9264187779635122, 23.955073749564615, 12),
            Block.box(-1.726418777963513, 23.955073749564615, 4, -0.726418777963513, 24.455073749564615, 12)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(16.25, 22.75, 6, 16.75, 23.25, 10),
            Stream.of(
            Block.box(1, 0, -0.5, 15, 4, 16.5),
            Block.box(-0.5, 0, 1, 16.5, 4, 15),
            Block.box(-1, 0, 2, 17, 4, 14),
            Block.box(2, 0, -1, 14, 4, 17)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0.5, 4, 0.5, 15.5, 6, 15.5),
            Block.box(1, 4, 0, 15, 6, 16),
            Block.box(0, 4, 1, 16, 6, 15),
            Block.box(-0.5, 4, 2, 16.5, 6, 14),
            Block.box(2, 4, -0.5, 14, 6, 16.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_SOUTH = Stream.of(
            Block.box(0, 0, 0, 16, 4, 16),
            Stream.of(
                    Block.box(1.5, 6, 1.5, 14.5, 20, 14.5),
                    Block.box(1, 6, 2, 15, 20, 14),
                    Block.box(2, 6, 1, 14, 20, 15),
                    Block.box(5.5, 10.5, 0.9, 10.5, 16.5, 2.9),
                    Stream.of(
                            Stream.of(
                                    Block.box(2.5, 17, 0, 13.5, 21, 16),
                                    Block.box(1, 17, 1, 15, 21, 15),
                                    Block.box(1.5, 17, 0.5, 14.5, 21, 15.5),
                                    Block.box(0.5, 17, 1.5, 15.5, 21, 14.5),
                                    Block.box(0, 17, 2.5, 16, 21, 13.5)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Stream.of(
            Block.box(1, 19, 0, 2.025, 23.025, 1.025),
            Block.box(0, 19, 1, 1.025, 23.025, 2.025),
            Block.box(0.5, 19, 0.5, 1.525, 23.025, 1.525),
            Block.box(1, 19, 1, 2.025, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0, 19, 13.975, 1.025, 23.025, 15),
            Block.box(1, 19, 14.975, 2.025, 23.025, 16),
            Block.box(0.5, 19, 14.475, 1.525000000000004, 23.025, 15.5),
            Block.box(1, 19, 13.975, 2.025, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(13.975, 19, 14.975, 15, 23.025, 16),
            Block.box(14.975, 19, 13.975, 16, 23.025, 15),
            Block.box(14.475, 19, 14.475, 15.5, 23.025, 15.5),
            Block.box(13.975, 19, 13.975, 15, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(14.975, 19, 1, 16, 23.025, 2.025),
            Block.box(13.975, 19, 0, 15, 23.025, 1.025),
            Block.box(14.475, 19, 0.5, 15.5, 23.025, 1.525),
            Block.box(13.975, 19, 1, 15, 23.025, 2.025)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(2, 19, -0.5, 14, 23, 1.4999999999999982),
            Block.box(2, 19, 14.5, 14, 23, 16.5),
            Block.box(14.500000000000002, 19, 2, 16.5, 23, 14),
            Block.box(-0.5, 19, 2, 1.5, 23, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(4.5, 19.5, -1, 11.5, 22.5, 0),
            Stream.of(
            Block.box(3, 19, 16, 13, 21, 23),
            Block.box(5.5, 20.25, 19.5, 10.5, 21.25, 22.5),
            Block.box(4, 20.955073749564608, 17.951418777963514, 12, 21.455073749564608, 18.951418777963514),
            Block.box(4, 21.455073749564608, 17.75141877796351, 12, 21.955073749564608, 18.75141877796351),
            Block.box(4, 21.955073749564608, 17.551418777963516, 12, 22.455073749564608, 18.551418777963516),
            Block.box(4, 22.455073749564608, 17.326418777963514, 12, 22.955073749564608, 18.326418777963514),
            Block.box(4, 22.955073749564615, 17.12641877796351, 12, 23.455073749564615, 18.12641877796351),
            Block.box(4, 23.455073749564615, 16.92641877796351, 12, 23.955073749564615, 17.92641877796351),
            Block.box(4, 23.955073749564615, 16.72641877796351, 12, 24.455073749564615, 17.72641877796351)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(6, 22.75, -0.75, 10, 23.25, -0.25),
            Stream.of(
            Block.box(-0.5, 0, 1, 16.5, 4, 15),
            Block.box(1, 0, -0.5, 15, 4, 16.5),
            Block.box(2, 0, -1, 14, 4, 17),
            Block.box(-1, 0, 2, 17.000000000000004, 4, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0.5, 4, 0.5, 15.5, 6, 15.5),
            Block.box(0, 4, 1, 16, 6, 15),
            Block.box(1, 4, 0, 15, 6, 16),
            Block.box(2, 4, -0.5, 14, 6, 16.5),
            Block.box(-0.5, 4, 2, 16.5, 6, 14)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();
    public static final VoxelShape MODEL_SHAPE_EAST = Stream.of(
            Block.box(0, 0, 0, 16, 4, 16),
            Stream.of(
                    Block.box(1.5, 6, 1.5, 14.5, 20, 14.499999999999993),
                    Block.box(2, 6, 1, 14, 20, 15),
                    Block.box(1, 6, 2, 15, 20, 14),
                    Block.box(0.9000000000000004, 10.5, 5.499999999999993, 2.9000000000000004, 16.5, 10.499999999999993),
                    Stream.of(
                            Stream.of(
                                    Block.box(0, 17, 2.499999999999993, 16, 21, 13.499999999999993),
                                    Block.box(1, 17, 1, 15, 21, 15),
                                    Block.box(0.5, 17, 1.5, 15.5, 21, 14.499999999999993),
                                    Block.box(1.5, 17, 0.5, 14.5, 21, 15.5),
                                    Block.box(2.5, 17, 0, 13.5, 21, 16)
                            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Stream.of(
            Block.box(0, 19, 13.974999999999993, 1.0250000000000004, 23.025, 15),
            Block.box(1, 19, 14.974999999999993, 2.025, 23.025, 16),
            Block.box(0.5, 19, 14.474999999999993, 1.5250000000000004, 23.025, 15.5),
            Block.box(1, 19, 13.974999999999993, 2.025, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(13.975, 19, 14.974999999999993, 15, 23.025, 16),
            Block.box(14.975, 19, 13.974999999999993, 16, 23.025, 15),
            Block.box(14.475, 19, 14.474999999999993, 15.5, 23.025, 15.5),
            Block.box(13.975, 19, 13.974999999999993, 15, 23.025, 15)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(14.975, 19, 1, 16, 23.025, 2.0249999999999932),
            Block.box(13.975, 19, 0, 15, 23.025, 1.0249999999999932),
            Block.box(14.475, 19, 0.5, 15.5, 23.025, 1.5249999999999932),
            Block.box(13.975, 19, 1, 15, 23.025, 2.0249999999999932)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(1, 19, 0, 2.025, 23.025, 1.0249999999999932),
            Block.box(0, 19, 1, 1.0250000000000004, 23.025, 2.0249999999999932),
            Block.box(0.5, 19, 0.5, 1.5250000000000004, 23.025, 1.5249999999999932),
            Block.box(1, 19, 1, 2.025, 23.025, 2.0249999999999932)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(-0.5, 19, 2, 1.5000000000000018, 23, 14),
            Block.box(14.5, 19, 2, 16.5, 23, 14),
            Block.box(2, 19, -0.5, 14, 23, 1.4999999999999947),
            Block.box(2, 19, 14.5, 14, 23, 16.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(-1, 19.5, 4.499999999999993, 0, 22.5, 11.5),
            Stream.of(
            Block.box(16, 19, 2.999999999999993, 23, 21, 12.999999999999993),
            Block.box(19.5, 20.25, 5.499999999999993, 22.5, 21.25, 10.499999999999993),
            Block.box(17.951418777963518, 20.955073749564608, 4, 18.951418777963518, 21.455073749564608, 11.999999999999995),
            Block.box(17.751418777963515, 21.455073749564608, 4, 18.751418777963515, 21.955073749564608, 11.999999999999995),
            Block.box(17.55141877796352, 21.955073749564608, 4, 18.55141877796352, 22.455073749564608, 11.999999999999995),
            Block.box(17.326418777963518, 22.455073749564608, 4, 18.326418777963518, 22.955073749564608, 11.999999999999995),
            Block.box(17.126418777963515, 22.955073749564615, 4, 18.126418777963515, 23.455073749564615, 11.999999999999995),
            Block.box(16.926418777963512, 23.455073749564615, 4, 17.926418777963512, 23.955073749564615, 11.999999999999995),
            Block.box(16.726418777963513, 23.955073749564615, 4, 17.726418777963513, 24.455073749564615, 11.999999999999995)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Block.box(-0.75, 22.75, 6, -0.25, 23.25, 10),
            Stream.of(
            Block.box(1, 0, -0.5, 15, 4, 16.5),
            Block.box(-0.5, 0, 1, 16.5, 4, 15),
            Block.box(-1, 0, 2, 17, 4, 14),
            Block.box(2, 0, -1, 14, 4, 17)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get(),
            Stream.of(
            Block.box(0.5, 4, 0.5, 15.5, 6, 15.5),
            Block.box(1, 4, 0, 15, 6, 16),
            Block.box(0, 4, 1, 16, 6, 15),
            Block.box(-0.5, 4, 2, 16.5, 6, 14),
            Block.box(2, 4, -0.5, 14, 6, 16.5)
            ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get()
).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();


    public IncubatorBlock(Properties p_52591_) {
        super(p_52591_);

//        this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH));

    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new IncubatorBlockEntity(pPos, pState);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return switch (pState.getValue(FACING)){
            case EAST -> MODEL_SHAPE_EAST.move(0, 0.01, 0);
            case WEST -> MODEL_SHAPE_WEST.move(0, 0.01, 0);
            case NORTH -> MODEL_SHAPE_NORTH.move(0, 0.01, 0);
            case SOUTH -> MODEL_SHAPE_SOUTH.move(0, 0.01, 0);
            default -> MODEL_SHAPE_NORTH.move(0, 0.01, 0);

        };
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (pLevel.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
//                MenuProvider menuprovider = this.getMenuProvider(pState, pLevel, pPos);
            if (pLevel.getBlockEntity(pPos) instanceof IncubatorBlockEntity e ) {
                pPlayer.openMenu(e);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return (pLevel1, pPos, pState1, pBlockEntity) -> {
            if(pLevel1.getBlockEntity(pPos) instanceof IncubatorBlockEntity incubator){
                incubator.tick(pLevel1, pPos, pState1, incubator);
            }else{
                IncubatorBlock.super.getTicker(pLevel, pState, pBlockEntityType);
            }
        };
    }
    @Override
    public void onRemove(BlockState oldState, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!oldState.is(newState.getBlock())) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof IncubatorBlockEntity incubatorBlock) {
                Containers.dropContents(level, pos, new RecipeWrapper(incubatorBlock.getItemHandler()));
                level.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(oldState, level, pos, newState, isMoving);
        }
    }
}
