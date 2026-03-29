package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
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
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.ActionFigureBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.util.LangUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class ActionFigureItem extends Item {

    //todo: get this to work

    private static final String TAG_FOSSILE = "IsFossile";

    private final Dinosaur dino;
    private final boolean isSkeleton;

    private final boolean fresh;
    //NBT fields: variant, mode

    public ActionFigureItem(Properties properties, Dinosaur dino, boolean isSkeleton, boolean fresh) {
        super(properties);
        this.dino = dino;
        this.isSkeleton = isSkeleton;
        this.fresh = !isSkeleton || fresh;
    }

    @Override
    public ItemStack getDefaultInstance() {
        ItemStack stack = super.getDefaultInstance();
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        tag.putBoolean(TAG_FOSSILE, !this.fresh);
        ItemStackNbtUtil.setTag(stack, tag);
        return stack;
    }
    @Override
    public String getDescriptionId(ItemStack stack) {
        return "item.jurassicreborn.action_figure.dynamic";
    }



    public boolean isSkeleton(){
        return this.isSkeleton;
    }

    public boolean isSkeleton(ItemStack pStack){
        if(pStack.getItem() == this){
            return this.isSkeleton();
        }else if(pStack.getItem() instanceof ActionFigureItem afi){
            return afi.isSkeleton(pStack);
        }
        return false;//default value
    }

    public Dinosaur getDinosaur(){
        return this.dino;
    }

    public Dinosaur getDinosaur(ItemStack pStack){
        if(pStack.getItem() == this){
            return this.getDinosaur();
        }else if(pStack.getItem() instanceof ActionFigureItem afi){
            return afi.getDinosaur();
        }
        return Dinosaur.EMPTY;
    }

    public int getGender(ItemStack stack){
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);

        if(tag.contains("Gender")){
            return getGender(tag.getString("Gender"));
        }
        //init gender if the stack doesn't have one
        tag.putString("Gender", "random");
        ItemStackNbtUtil.setTag(stack, tag);
        return 0;
    }

    public int getGender(String gender){
        if(gender.equals("random"))
            return 0;
        if (gender.equals("male"))
            return 1;
        if(gender.equals("female"))
            return 2;
        return 0;
    }
    public String getGender(int gender){
        if(gender == 0)
            return "random";
        if(gender == 1)
            return "male";
        if(gender == 2)
            return "female";
        return "";
    }

    public int changeGender(ItemStack stack){
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

    public boolean isFresh(ItemStack stack){
        return !isFossile(stack);
    }

    public boolean isFossile(ItemStack stack){
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        if(!tag.contains(TAG_FOSSILE)){
            tag.putBoolean(TAG_FOSSILE, !this.fresh);
            ItemStackNbtUtil.setTag(stack, tag);
        }
        return tag.getBoolean(TAG_FOSSILE);
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack stack) {
        Dinosaur dino = this.getDinosaur(stack);

        if (dino == Dinosaur.EMPTY) {
            return Component.translatable("item.jurassicreborn.action_figure");
        }

        return Component.translatable(
                "item.jurassicreborn.action_figure.dynamic",
                dino.getTranslatedName()
        );
    }
    @Override
    public void appendHoverText(@NotNull ItemStack pStack, Item.TooltipContext context, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if(this.isSkeleton)
            return;

        pTooltipComponents.add(Component.translatable("lore.change_gender").withStyle(ChatFormatting.BLUE));
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pUsedHand) {
        ItemStack stack = pPlayer.getItemInHand(pUsedHand);

        HitResult hitResult = Item.getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        if (hitResult.getType() != HitResult.Type.MISS) {
            return InteractionResultHolder.pass(stack);
        }

        if(this.isSkeleton(stack))
            return InteractionResultHolder.pass(stack);

        int gender = this.changeGender(stack);

        if(pLevel.isClientSide)
            pPlayer.displayClientMessage(Component.translatable("actionfigure.genderchange", LangUtil.getGender(gender).getString()), false);

        return new InteractionResultHolder<>(InteractionResult.SUCCESS, stack);

    }
    //yoinked from BlockItem
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        Player player = pContext.getPlayer();
        CollisionContext collisioncontext = player == null ? CollisionContext.empty() : CollisionContext.of(player);
        return (pState.canSurvive(pContext.getLevel(), pContext.getClickedPos())) && pContext.getLevel().isUnobstructed(pState, pContext.getClickedPos(), collisioncontext);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        BlockPos pos = context.getClickedPos().relative(context.getClickedFace());
        Level world = context.getLevel();
        if(context.getLevel().isClientSide)
            return InteractionResult.PASS;

        Block block = ModBlocks.DISPLAY_BLOCK.get();
        BlockState state = block.defaultBlockState();

        if(!canPlace(new BlockPlaceContext(context), state))
            return InteractionResult.FAIL;

        BlockState state1 = block.getStateForPlacement(new BlockPlaceContext(context));

        if (state1 == null) {
            return InteractionResult.FAIL;
        }
        // If there is already an action figure block at the target position, destroy it
        // so that its contents are dropped instead of being silently replaced.
        if (!world.isEmptyBlock(pos)) {
            boolean drop = context.getPlayer() == null || !context.getPlayer().isCreative();
            world.destroyBlock(pos, drop);
        }

        world.setBlock(pos, state1, 3);
        block.setPlacedBy(world, pos, state1, context.getPlayer(), stack);
//        block.onPlace(state1, world, pos, Blocks.AIR.defaultBlockState(), false);
        int gender = this.getGender(stack);

        ActionFigureBlockEntity afbe = getPlacedBlockEntity(world, pos);
        if (afbe == null) {
            world.removeBlock(pos, false);
            return InteractionResult.FAIL;
        }

        afbe.setDinosaur(this.getDinosaur(stack),
                gender > 0 ? gender == 1 : world.getRandom().nextBoolean(),
                this.isSkeleton(stack),
                this.isFossile(stack));

        afbe.setRot(180 - (int) Objects.requireNonNull(context.getPlayer()).getYHeadRot());

        world.updateNeighborsAt(pos, block);

        afbe.setChanged();

        if(!context.getPlayer().isCreative())
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
