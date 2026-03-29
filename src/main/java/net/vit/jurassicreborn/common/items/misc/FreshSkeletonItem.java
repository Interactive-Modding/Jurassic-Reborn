package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;

import net.minecraft.server.level.ServerLevel;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.items.misc.SkeletonPoseHelper;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

/**
 * Item representing a fresh dinosaur skeleton display figure.
 */
public class FreshSkeletonItem extends Item {
    private static final String TAG_FOSSILE = "IsFossile";
    private static final String TAG_VARIANT = "Variant";
    private final Dinosaur dino;
    private final boolean isSkeleton = true;
    private final boolean fresh = true;

    public FreshSkeletonItem(Properties properties, Dinosaur dino) {
        super(properties);
        this.dino = dino;
    }
    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.jurassicreborn.skeleton.fresh.dynamic";
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        tag.putBoolean(TAG_FOSSILE, false);
        tag.putByte(TAG_VARIANT, (byte)0);
        ItemStackNbtUtil.setTag(stack, tag);
        return stack;
    }

    public boolean isSkeleton() {
        return this.isSkeleton;
    }

    public boolean isSkeleton(ItemStack stack) {
        if (stack.getItem() == this) {
            return this.isSkeleton();
        } else if (stack.getItem() instanceof FreshSkeletonItem fi) {
            return fi.isSkeleton(stack);
        } else if (stack.getItem() instanceof FossilSkeletonItem fi) {
            return fi.isSkeleton(stack);
        } else if (stack.getItem() instanceof ActionFigureItem afi) {
            return afi.isSkeleton(stack);
        }
        return false;
    }

    public Dinosaur getDinosaur() {
        return this.dino;
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        if (stack.getItem() == this) {
            return this.getDinosaur();
        } else if (stack.getItem() instanceof FreshSkeletonItem fi) {
            return fi.getDinosaur();
        } else if (stack.getItem() instanceof FossilSkeletonItem fi) {
            return fi.getDinosaur();
        } else if (stack.getItem() instanceof ActionFigureItem afi) {
            return afi.getDinosaur();
        }
        return Dinosaur.EMPTY;
    }

    public int getGender(ItemStack stack) {
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        if (tag.contains("Gender")) {
            return getGender(tag.getString("Gender"));
        }
        tag.putString("Gender", "random");
        ItemStackNbtUtil.setTag(stack, tag);
        return 0;
    }

    public int getGender(String gender) {
        if (gender.equals("random"))
            return 0;
        if (gender.equals("male"))
            return 1;
        if (gender.equals("female"))
            return 2;
        return 0;
    }

    public String getGender(int gender) {
        if (gender == 0)
            return "random";
        if (gender == 1)
            return "male";
        if (gender == 2)
            return "female";
        return "";
    }

    public int changeGender(ItemStack stack) {
        int gender = getGender(stack);
        int newGender = (gender + 1) % 3;
        CompoundTag stackTag = ItemStackNbtUtil.getOrCreateTag(stack);
        stackTag.putString("Gender", getGender(newGender));
        ItemStackNbtUtil.setTag(stack, stackTag);
        return newGender;
    }

    public boolean isFresh() {
        return this.fresh;
    }

    public boolean isFresh(ItemStack stack) {
        return !isFossile(stack);
    }

    public boolean isFossile(ItemStack stack) {
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        if (!tag.contains(TAG_FOSSILE)) {
            tag.putBoolean(TAG_FOSSILE, false);
            ItemStackNbtUtil.setTag(stack, tag);
        }
        return tag.getBoolean(TAG_FOSSILE);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Dinosaur dino = this.getDinosaur(stack);

        if (dino == Dinosaur.EMPTY) {
            return Component.translatable("item.jurassicreborn.skeleton.fresh");
        }

        return Component.translatable(
                "item.jurassicreborn.skeleton.fresh.dynamic",
                dino.getTranslatedName()
        );
    }


    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(Component.translatable("lore.change_pose").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        HitResult hitResult = Item.getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);
        if (hitResult.getType() != HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        int pose = changePose(stack);
        if (level.isClientSide) {
            List<String> poses = SkeletonPoseHelper.getPoseNames(this.getDinosaur(stack));
            String name = poses.get(pose);
            player.displayClientMessage(Component.translatable("skeleton.posechange", name), false);
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public int getPose(ItemStack stack) {
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        if (!tag.contains(TAG_VARIANT)) {
            tag.putByte(TAG_VARIANT, (byte)0);
            ItemStackNbtUtil.setTag(stack, tag);
        }
        return tag.getByte(TAG_VARIANT);
    }

    public int changePose(ItemStack stack) {
        List<String> poses = SkeletonPoseHelper.getPoseNames(this.getDinosaur(stack));
        int pose = getPose(stack);
        int newPose = (pose + 1) % poses.size();
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        tag.putByte(TAG_VARIANT, (byte)newPose);
        ItemStackNbtUtil.setTag(stack, tag);
        return newPose;
    }

    protected boolean canPlace(BlockPlaceContext context, BlockState state) {
        Player player = context.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return state.canSurvive(context.getLevel(), context.getClickedPos()) && context.getLevel().isUnobstructed(state, context.getClickedPos(), collisioncontext);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Level world = context.getLevel();
        if (world.isClientSide)
            return InteractionResult.PASS;

        Block block = ModBlocks.DISPLAY_BLOCK.get();
        BlockState state = block.defaultBlockState();

        if (!canPlace(new BlockPlaceContext(context), state))
            return InteractionResult.FAIL;

        BlockState state1 = block.getStateForPlacement(new BlockPlaceContext(context));
        if (state1 == null) {
            return InteractionResult.FAIL;
        }

        world.setBlock(pos, state1, 3);
        block.setPlacedBy(world, pos, state1, context.getPlayer(), stack);
        int gender = this.getGender(stack);

        ActionFigureBlockEntity afbe = getPlacedBlockEntity(world, pos);
        if (afbe == null) {
            world.removeBlock(pos, false);
            return InteractionResult.FAIL;
        }

        afbe.setVariant((byte) getPose(stack));
        afbe.setDinosaur(this.getDinosaur(stack),
                gender > 0 ? gender == 1 : world.getRandom().nextBoolean(),
                this.isSkeleton(stack),
                this.isFossile(stack));

        afbe.setRot(180 - (int) Objects.requireNonNull(context.getPlayer()).getYHeadRot());
        world.updateNeighborsAt(pos, block);
        afbe.setChanged();

        if (!context.getPlayer().isCreative())
            stack.shrink(1);

        return InteractionResult.SUCCESS;
    }

    @Nullable
    private static ActionFigureBlockEntity getPlacedBlockEntity(Level world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof ActionFigureBlockEntity actionFigureBlockEntity) {
            return actionFigureBlockEntity;
        }

        if (world instanceof ServerLevel serverLevel) {
            BlockEntity created = serverLevel.getChunkAt(pos)
                    .getBlockEntity(pos, LevelChunk.EntityCreationType.IMMEDIATE);
            if (created instanceof ActionFigureBlockEntity actionFigureBlockEntity) {
                return actionFigureBlockEntity;
            }
        }

        return null;
    }

}
