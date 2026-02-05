package net.vit.jurassicreborn.common.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.vit.jurassicreborn.common.items.misc.SwarmItem;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class SwarmBlock extends Block {

    public static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 0.5D, 16.0D);

    private RegistryObject<SwarmItem> item;

    public SwarmBlock(RegistryObject<SwarmItem> item, Properties properties) {
        super(properties);
        this.item = item;
    }
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        BlockPos below = pos.below();
        BlockState stateBelow = level.getBlockState(below);
        if (stateBelow.getBlock() == Blocks.WATER && stateBelow.getFluidState().isSource() && level.getBlockState(pos).isAir()) {
            return this.defaultBlockState();
        }
        return null;
    }


    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public void onNeighborChange(BlockState state, LevelReader level, BlockPos pos, BlockPos neighbor) {
        super.onNeighborChange(state, level, pos, neighbor);
        this.checkForDrop((Level)level, pos, state);
    }

    @Override
    public boolean canSurvive(BlockState pState, LevelReader world, BlockPos pPos) {
        BlockState stateBelow = world.getBlockState(pPos.below());
        return stateBelow.getBlock() == Blocks.WATER && stateBelow.getFluidState().isSource();
    }


    private boolean checkForDrop(Level world, BlockPos pos, BlockState state) {
        if (!this.canSurvive(state, world, pos)) {
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(this.item.get())));
            world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return this.getAddationalDrops(state, super.getDrops(state, builder)); //that was easy, might cause a bug - gamma_02
    }

    public List<ItemStack> getAddationalDrops(BlockState state, List<ItemStack> originalDrops) {
        originalDrops.add(new ItemStack(this.item.get()));
        return originalDrops;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, RandomSource rand) {
        super.randomTick(state, world, pos, rand);
        this.checkForDrop(world, pos, state);
        if (rand.nextInt(10) == 0) {
            ItemEntity item = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 0.8, pos.getZ() + 0.5, new ItemStack(this.item.get()));
            item.setDeltaMovement((rand.nextFloat() - 0.5F) * 0.5F, 0.2F, (rand.nextFloat() - 0.5F) * 0.5F);
            world.addFreshEntity(item);
        }
        if (rand.nextInt(8) == 0) {
            BlockPos spread = pos.offset(rand.nextInt(3) - 1, 0, rand.nextInt(3) - 1);
            if (!spread.equals(pos) && world.getBlockState(spread).isAir() &&
                    world.getBlockState(spread.below()).getBlock() == Blocks.WATER) {
                world.setBlock(spread, this.defaultBlockState(), 2);
            }
        }
    }


    @Override
    public Item asItem() {
        return this.item.get();
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        return new ItemStack(this.item.get());
    }
}
