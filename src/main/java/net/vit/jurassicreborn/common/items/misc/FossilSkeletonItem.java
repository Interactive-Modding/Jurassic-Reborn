package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraftforge.client.IItemRenderProperties;
import net.vit.jurassicreborn.client.JurassicClient;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.LangUtil;
import net.vit.jurassicreborn.common.items.misc.SkeletonPoseHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.function.Consumer;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * Item representing a fossilized dinosaur skeleton display figure.
 */
public class FossilSkeletonItem extends Item {
    private static final String TAG_FOSSILE = "IsFossile";
    private static final String TAG_VARIANT = "Variant";
    private final Dinosaur dino;
    private final boolean isSkeleton = true;
    private final boolean fresh = false;

    public FossilSkeletonItem(Properties properties, Dinosaur dino) {
        super(properties);
        this.dino = dino;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        CompoundTag tag = stack.getOrCreateTag();
        tag.putBoolean(TAG_FOSSILE, true);
        tag.putByte(TAG_VARIANT, (byte)0);
        stack.setTag(tag);
        return stack;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void initializeClient(@NotNull Consumer<IItemRenderProperties> consumer) {
        super.initializeClient(consumer);
        IItemRenderProperties prop = new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return JurassicClient.displayBlockRendererWithoutLevel;
            }
        };
        consumer.accept(prop);
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
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Gender")) {
            return getGender(tag.getString("Gender"));
        }
        tag.putString("Gender", "random");
        stack.setTag(tag);
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
        CompoundTag stackTag = stack.getOrCreateTag();
        stackTag.putString("Gender", getGender(newGender));
        stack.setTag(stackTag);
        return newGender;
    }

    public boolean isFresh() {
        return this.fresh;
    }

    public boolean isFresh(ItemStack stack) {
        return !isFossile(stack);
    }

    public boolean isFossile(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(TAG_FOSSILE)) {
            tag.putBoolean(TAG_FOSSILE, true);
            stack.setTag(tag);
        }
        return tag.getBoolean(TAG_FOSSILE);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        return LangUtil.replaceWithDinoName(this.getDinosaur(stack), "item.JurassicReborn.skeleton.fossil");
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.add(new TranslatableComponent("lore.change_pose").withStyle(ChatFormatting.BLUE));
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
            player.displayClientMessage(new TranslatableComponent("skeleton.posechange", name), false);
        }

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);
    }

    public int getPose(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains(TAG_VARIANT)) {
            tag.putByte(TAG_VARIANT, (byte)0);
            stack.setTag(tag);
        }
        return tag.getByte(TAG_VARIANT);
    }

    public int changePose(ItemStack stack) {
        List<String> poses = SkeletonPoseHelper.getPoseNames(this.getDinosaur(stack));
        int pose = getPose(stack);
        int newPose = (pose + 1) % poses.size();
        CompoundTag tag = stack.getOrCreateTag();
        tag.putByte(TAG_VARIANT, (byte)newPose);
        stack.setTag(tag);
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

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        if (category == this.getItemCategory() || category == CreativeModeTab.TAB_SEARCH) {
            ItemStack defaultStack = this.getDefaultInstance();
            CompoundTag tag = defaultStack.getOrCreateTag();
            tag.putString("Gender", "random");
            defaultStack.setTag(tag);
            items.add(defaultStack);
        }
    }
}