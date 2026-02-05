package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.vit.jurassicreborn.client.screens.HologramSelectScreen;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HologramBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.block();

    public HologramBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new HologramBlockEntity(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) {
            openClientGui(pos);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    private void openClientGui(BlockPos pos) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> doOpenScreen(pos));
    }

    @OnlyIn(Dist.CLIENT)
    private void doOpenScreen(BlockPos pos) {
        net.minecraft.client.Minecraft mc = net.minecraft.client.Minecraft.getInstance();
        BlockEntity be = mc.level.getBlockEntity(pos);

        int current = 0;
        int pose = 0;
        boolean rotating = true;
        int rotation = 0;

        if (be instanceof HologramBlockEntity hologram) {
            current = hologram.getDinoIndex();
            pose = hologram.getPoseIndex();
            rotating = hologram.isRotating();
            rotation = hologram.getRot();
        }

        mc.setScreen(new HologramSelectScreen(pos, current, pose, rotating, rotation));
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        System.out.println("[Hologram] setPlacedBy called - isClient=" + level.isClientSide);
        System.out.println("[Hologram] Stack has tag: " + stack.hasTag());
        if (stack.hasTag()) {
            System.out.println("[Hologram] Stack full tag: " + stack.getTag());
        }

        if (level.isClientSide) {
            return;
        }

        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof HologramBlockEntity hologram)) {
            System.out.println("[Hologram] BlockEntity is not HologramBlockEntity!");
            return;
        }

        CompoundTag blockEntityTag = stack.getTagElement("BlockEntityTag");
        if (blockEntityTag != null && !blockEntityTag.isEmpty()) {
            System.out.println("[Hologram] Found BlockEntityTag: " + blockEntityTag);

            // Use the entity's normalizer + sync
            hologram.applySettingsFromTag(blockEntityTag, /*sync*/ true);

            System.out.println("[Hologram] Applied BlockEntityTag - dino=" + hologram.getDinoIndex() +
                    ", pose=" + hologram.getPoseIndex() +
                    ", rotating=" + hologram.isRotating() +
                    ", rotation=" + hologram.getRot());
        } else {
            System.out.println("[Hologram] No BlockEntityTag found - using defaults");
        }
    }

    private ItemStack createItemStack(HologramBlockEntity hologram) {
        ItemStack stack = new ItemStack(this);

        // Create the BlockEntityTag with hologram-specific data
        CompoundTag blockEntityTag = stack.getOrCreateTagElement("BlockEntityTag");
        blockEntityTag.putInt(HologramBlockEntity.TAG_DINO_INDEX, hologram.getDinoIndex());
        blockEntityTag.putInt(HologramBlockEntity.TAG_POSE_INDEX, hologram.getPoseIndex());
        blockEntityTag.putBoolean(HologramBlockEntity.TAG_ROTATING, hologram.isRotating());
        blockEntityTag.putInt(HologramBlockEntity.TAG_ROTATION, hologram.getRot());

        // Also save the base ActionFigure data
        CompoundTag fullTag = new CompoundTag();
        hologram.saveAdditional(fullTag);

        // Merge the full tag into BlockEntityTag
        for (String key : fullTag.getAllKeys()) {
            if (!blockEntityTag.contains(key)) {
                blockEntityTag.put(key, fullTag.get(key));
            }
        }

        System.out.println("Created item stack with NBT: " + blockEntityTag);
        return stack;
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof HologramBlockEntity hologram) {
            ItemStack stack = createItemStack(hologram);
            System.out.println("Middle-click: Created stack with NBT");
            return stack;
        }
        System.out.println("Middle-click: No hologram found, returning default");
        return super.getCloneItemStack(state, target, level, pos, player);
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        BlockEntity be = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (be instanceof HologramBlockEntity hologram) {
            System.out.println("Block broken: Created drop with NBT (code path)");
            return List.of(createItemStack(hologram));
        }
        System.out.println("Block broken: No hologram found, returning default (code path)");
        return super.getDrops(state, builder);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type == ModBlockEntities.HOLOGRAM_BLOCK_ENTITY.get()) {
            return (lvl, pos1, state1, be) -> {
                if (be instanceof HologramBlockEntity hologram) {
                    if (lvl.isClientSide) {
                        hologram.clientTick(lvl, pos1, state1);
                    } else {
                        hologram.serverTick(lvl, pos1, state1);
                    }
                }
            };
        }
        return null;
    }
}