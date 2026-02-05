package net.vit.jurassicreborn.common.blocks.entities.trashcan;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TrashCanBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    private static final VoxelShape SHAPE = loadShape();

    public TrashCanBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(3.0F).noOcclusion().sound(net.minecraft.world.level.block.SoundType.METAL).requiresCorrectToolForDrops());
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private static VoxelShape loadShape() {
        VoxelShape shape = Shapes.empty();
        try (InputStream stream = TrashCanBlock.class.getResourceAsStream("/assets/jurassicreborn/models/block/trash_can.json")) {
            if (stream != null) {
                JsonObject json = JsonParser.parseReader(new InputStreamReader(stream)).getAsJsonObject();
                JsonArray elements = json.getAsJsonArray("elements");
                for (JsonElement element : elements) {
                    JsonObject obj = element.getAsJsonObject();
                    String name = obj.has("name") ? obj.get("name").getAsString() : "";
                    if (name.startsWith("inner")) continue;
                    JsonArray from = obj.getAsJsonArray("from");
                    JsonArray to = obj.getAsJsonArray("to");
                    shape = Shapes.or(shape, Block.box(
                            from.get(0).getAsDouble(),
                            from.get(1).getAsDouble(),
                            from.get(2).getAsDouble(),
                            to.get(0).getAsDouble(),
                            to.get(1).getAsDouble(),
                            to.get(2).getAsDouble()
                    ));
                }
            }
        } catch (IOException e) {
            shape = Shapes.block();
        }
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && player instanceof ServerPlayer sp) {
            MenuProvider provider = new SimpleMenuProvider(
                    (id, inv, p) -> new TrashCanMenu(id, inv),
                    Component.translatable("container.trash_can")
            );
            NetworkHooks.openScreen(sp, provider, pos);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
