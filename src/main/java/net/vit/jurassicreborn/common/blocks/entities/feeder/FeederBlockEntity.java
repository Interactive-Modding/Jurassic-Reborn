package net.vit.jurassicreborn.common.blocks.entities.feeder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.util.networking.BlockUpdateUtils;
import net.vit.jurassicreborn.common.util.networking.Syncable;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class FeederBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, MenuProvider, Syncable {

    public static final int meatSlot = 8;
    public static final int plantSlot = 17;

    public AABB feederBoundingBox;
    public int prevOpenAnimation;
    public int openAnimation;

    private NonNullList<ItemStack> slots = NonNullList.withSize(18, ItemStack.EMPTY);

    private int stayOpen;
    private boolean open;
    private DinosaurEntity feeding;
    private int feedingExpire;

    private int closeDelay;
    private boolean servedThisOpenCycle;

    private static final int CLAIM_TTL = 30;
    private static final int SERVE_TICK = 10;
    private static final int CLOSE_DELAY_TICKS = 6;

    protected FeederBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.feederBoundingBox = new AABB(pos).inflate(32);
    }

    public FeederBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.FEEDER.get(), pos, state);
    }

    private final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int idx) {
            return 0;
        }

        @Override
        public void set(int idx, int val) {
        }

        @Override
        public int getCount() {
            return 0;
        }
    };

    @Override
    protected NonNullList<ItemStack> getItems() {
        return slots;
    }

    @Override
    protected void setItems(NonNullList<ItemStack> stacks) {
        this.slots = stacks;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        this.setChanged();
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Feeder");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new FeederMenu(id, playerInv, this, this.dataAccess);
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new FeederMenu(id, playerInv, this, this.dataAccess);
    }

    public boolean isStockedFor(DinosaurEntity dino) {
        return this.getFood(dino) != -1;
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        if (!stack.isEmpty()) {
            this.setChanged();
        }
        return stack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            BlockUpdateUtils.sendBlockEntityUpdate(this.level, this.worldPosition);
        }
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level != null
                && this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(
                this.worldPosition.getX() + 0.5D,
                this.worldPosition.getY() + 0.5D,
                this.worldPosition.getZ() + 0.5D
        ) <= 64.0D;
    }

    @Override
    public void clearContent() {
        this.slots.clear();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : this.slots) {
            if (!s.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public void setOpen(boolean open) {
        if (!this.level.isClientSide && this.open != open) {
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 0, open ? 1 : 0);
        }
        this.open = open;
        if (!open) {
            this.feeding = null;
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 0) {
            this.open = (type == 1);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    @Nullable
    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    public void onDataPacket(Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null && this.level != null) {
            this.loadAdditional(pkt.getTag(), this.level.registryAccess());
        }
    }

    @Override
    public NonNullList getSyncFields(NonNullList fields) {
        for (int i = 0; i < 18; i++) {
            fields.add(this.slots.get(i));
        }
        return fields;
    }

    @Override
    public void packetDataHandler(ByteBuf fields) {
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (this.level != null && !this.level.isClientSide) {
            FeederRegistry.register(this.level, this.worldPosition);
        }
    }

    @Override
    public void setRemoved() {
        if (this.level != null && !this.level.isClientSide) {
            FeederRegistry.unregister(this.level, this.worldPosition);
        }
        super.setRemoved();
    }

    public boolean canServe(DinosaurEntity dino) {
        if (dino == null || dino.isRemoved() || dino.isCarcass()) {
            return false;
        }
        return (this.feeding == null || this.feeding == dino) && this.getFoodForDinosaur(dino) != -1;
    }

    public boolean tryClaim(DinosaurEntity dino) {
        if (!this.canServe(dino)) {
            return false;
        }

        if (this.feeding != dino) {
            this.feeding = dino;
            this.servedThisOpenCycle = false;
            this.closeDelay = 0;
        }

        this.feedingExpire = CLAIM_TTL;
        this.setChanged();
        return true;
    }

    public void keepClaimAlive(DinosaurEntity dino) {
        if (this.feeding == dino) {
            this.feedingExpire = CLAIM_TTL;
        }
    }

    public boolean isClaimedBy(DinosaurEntity dino) {
        return this.feeding == dino;
    }

    public void releaseClaim(DinosaurEntity dino) {
        if (this.feeding == null) {
            return;
        }
        if (dino != null && this.feeding != dino) {
            return;
        }

        this.feeding = null;
        this.feedingExpire = 0;
        this.servedThisOpenCycle = false;
        this.closeDelay = 0;
        this.setOpen(false);
        this.setChanged();
    }

    public Vec3 getFeedingPos(DinosaurEntity dino) {
        Direction face = this.getBlockState().hasProperty(FeederBlock.FACING)
                ? this.getBlockState().getValue(FeederBlock.FACING)
                : Direction.NORTH;

        Vec3 center = Vec3.atCenterOf(this.worldPosition);
        boolean aquatic = dino.usesAquaticFeederLogic();
        boolean groundedFlyer = dino instanceof FlyingDinosaurEntity
                && ((FlyingDinosaurEntity) dino).isTouchingGround();

        double forward;
        double vertical;

        if (aquatic) {
            forward = 0.55D;
            vertical = 0.0D;
        } else if (dino instanceof FlyingDinosaurEntity && !groundedFlyer) {
            forward = 1.20D;
            vertical = 0.75D;
        } else {
            forward = 1.10D;
            vertical = 0.25D;
        }

        if (face == Direction.UP) {
            return center.add(0.0D, aquatic ? 0.35D : 0.95D, 0.0D);
        }

        if (face == Direction.DOWN) {
            return center.add(0.0D, aquatic ? -0.15D : -0.35D, 0.0D);
        }

        return center.add(
                face.getStepX() * forward,
                vertical,
                face.getStepZ() * forward
        );
    }

    public double getFeedReach(DinosaurEntity dino) {
        if (dino.usesAquaticFeederLogic()) {
            return Math.max(1.9D, dino.getBbWidth() * 2.6D);
        }
        if (dino instanceof FlyingDinosaurEntity) {
            return Math.max(2.0D, dino.getBbWidth() * 2.6D);
        }
        return Math.max(1.6D, dino.getBbWidth() * 2.1D);
    }

    private Vec3 getFeedingReferencePos(DinosaurEntity dino) {
        if (dino.usesAquaticFeederLogic()) {
            return dino.getEyePosition();
        }
        return dino.position();
    }

    public void tick(Level world, BlockPos pos, BlockState state, FeederBlockEntity instance) {
        if (this.level == null) {
            return;
        }

        this.feederBoundingBox = new AABB(pos).inflate(16.0D);
        this.prevOpenAnimation = this.openAnimation;

        if (this.open && this.openAnimation < 20) {
            this.openAnimation++;
        } else if (!this.open && this.openAnimation > 0) {
            this.openAnimation--;
        }

        if (this.level.isClientSide) {
            return;
        }

        if (this.feeding != null) {
            if (this.feedingExpire > 0) {
                this.feedingExpire--;
            }

            if (this.feedingExpire <= 0
                    || this.feeding.isRemoved()
                    || this.feeding.isCarcass()
                    || !this.feeding.isAlive()
                    || !this.feeding.getMetabolism().isHungry()
                    || this.getFoodForDinosaur(this.feeding) == -1) {
                this.releaseClaim(this.feeding);
            }
        }

        if (this.feeding == null) {
            if (this.open) {
                this.setOpen(false);
            }
            return;
        }

        Vec3 feedingPos = this.getFeedingPos(this.feeding);
        double reach = this.getFeedReach(this.feeding);
        double distSq = this.getFeedingReferencePos(this.feeding).distanceToSqr(feedingPos);

        if (distSq > reach * reach) {
            if (this.open) {
                this.setOpen(false);
            }
            return;
        }

        if (!this.feeding.usesAquaticFeederLogic()) {
            this.feeding.getNavigation().stop();
        }

        this.feeding.getLookControl().setLookAt(
                feedingPos.x,
                feedingPos.y,
                feedingPos.z,
                30.0F,
                30.0F
        );

        if (!this.open) {
            this.servedThisOpenCycle = false;
            this.setOpen(true);
        }

        if (!this.servedThisOpenCycle && this.openAnimation >= SERVE_TICK) {
            if (this.feedClaimant()) {
                this.servedThisOpenCycle = true;
                this.closeDelay = CLOSE_DELAY_TICKS;
            } else {
                this.releaseClaim(this.feeding);
                return;
            }
        }

        if (this.servedThisOpenCycle) {
            if (this.closeDelay > 0) {
                this.closeDelay--;
            } else {
                this.releaseClaim(this.feeding);
            }
        }
    }

    private boolean feedClaimant() {
        if (this.feeding == null) {
            return false;
        }

        int slot = this.getFoodForDinosaur(this.feeding);
        if (slot < 0) {
            return false;
        }

        ItemStack stack = this.getItem(slot);
        if (stack.isEmpty()) {
            return false;
        }

        ItemStack bite = stack.copy();
        bite.setCount(1);

        this.feeding.feedFromFeeder(bite);
        this.removeItem(slot, 1);
        this.setChanged();

        return true;
    }

    private boolean spawnOneItemTowardsFace(int feedSlot, ItemStack stack) {
        if (this.level == null || this.level.isClientSide) {
            return false;
        }

        Direction face = this.level.getBlockState(this.getBlockPos()).getValue(FeederBlock.FACING);
        Random random = new Random();

        float ox = 0.5F, oy = 0.5F, oz = 0.5F;
        float mx = 0.0F, my = 0.0F, mz = 0.0F;

        switch (face) {
            case UP -> {
                oy = 1.0F;
                my = 1.0F;
                mx = random.nextFloat() - 0.5F;
                mz = random.nextFloat() - 0.5F;
            }
            case DOWN -> oy = -1.0F;
            case NORTH -> {
                oz = -1.0F;
                my = 0.5F;
                mz = -0.5F;
            }
            case SOUTH -> {
                oz = 1.0F;
                my = 0.5F;
                mz = 0.5F;
            }
            case WEST -> {
                ox = -1.0F;
                my = 0.5F;
                mx = -0.5F;
            }
            case EAST -> {
                ox = 1.0F;
                my = 0.5F;
                mx = 0.5F;
            }
        }

        double spawnX = this.getBlockPos().getX() + ox;
        double spawnY = this.getBlockPos().getY() + oy;
        double spawnZ = this.getBlockPos().getZ() + oz;

        double duplicateRadius = 0.75D;
        AABB duplicateCheck = new AABB(
                spawnX - duplicateRadius,
                spawnY - duplicateRadius,
                spawnZ - duplicateRadius,
                spawnX + duplicateRadius,
                spawnY + duplicateRadius,
                spawnZ + duplicateRadius
        );

        boolean duplicateOnGround = !this.level.getEntitiesOfClass(
                ItemEntity.class,
                duplicateCheck,
                ie -> !ie.getItem().isEmpty() && ItemStack.isSameItemSameComponents(ie.getItem(), stack)
        ).isEmpty();

        if (duplicateOnGround) {
            return false;
        }

        ItemStack out = new ItemStack(stack.getItem(), 1);
        ItemEntity itemEntity = new ItemEntity(this.level, spawnX, spawnY, spawnZ, out);
        itemEntity.setDefaultPickUpDelay();
        float vx = mx * 0.3F, vy = my * 0.3F, vz = mz * 0.3F;
        itemEntity.setDeltaMovement(vx, vy, vz);
        this.level.addFreshEntity(itemEntity);

        this.removeItem(feedSlot, 1);

        if (this.feeding != null) {
            this.feeding.getNavigation().moveTo(
                    itemEntity.getX() + vx,
                    itemEntity.getY() + vy,
                    itemEntity.getZ() + vz,
                    0.8D
            );
        }

        return true;
    }

    public int getFood(DinosaurEntity feeding) {
        if (feeding == null) {
            return -1;
        }

        for (int i = 0; i <= plantSlot; i++) {
            ItemStack st = this.getItem(i);
            if (!st.isEmpty() && FoodHelper.isEdible(feeding, feeding.getDinosaur().getDiet(), st.getItem())) {
                return i;
            }
        }
        return -1;
    }

    public boolean canFeedDinosaur(DinosaurEntity dinosaur) {
        return this.getFoodForDinosaur(dinosaur) != -1;
    }

    private int getFoodForDinosaur(DinosaurEntity dinosaur) {
        int i = 0;
        for (ItemStack stack : this.slots) {
            if (!stack.isEmpty()) {
                Dinosaur meta = dinosaur.getDinosaur();
                if (FoodHelper.isEdible(dinosaur, meta.getDiet(), stack.getItem())) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }

    public void setFeeding(@Nullable DinosaurEntity feeding) {
        this.feeding = feeding;
        this.feedingExpire = (feeding != null) ? CLAIM_TTL : 0;
    }

    @Nullable
    public DinosaurEntity getFeeding() {
        return this.feeding;
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack stack) {
        return isItemValidForSlot(slot, stack);
    }

    public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
        if (itemstack == null || itemstack.isEmpty()) {
            return false;
        }

        if (isMeatSlot(slotID)) {
            return FoodHelper.isFoodType(itemstack.getItem(), FoodType.MEAT)
                    || FoodHelper.isFoodType(itemstack.getItem(), FoodType.FISH)
                    || FoodHelper.isFoodType(itemstack.getItem(), FoodType.INSECT)
                    || FoodHelper.isFoodType(itemstack.getItem(), FoodType.FILTER);
        } else if (isPlantSlot(slotID)) {
            return FoodHelper.isFoodType(itemstack.getItem(), FoodType.PLANT)
                    || FoodHelper.isFoodType(itemstack.getItem(), FoodType.FILTER);
        }
        return false;
    }

    public static boolean isMeatSlot(int slot) {
        return slot >= 0 && slot <= 8;
    }

    public static boolean isPlantSlot(int slot) {
        return slot >= 9 && slot <= 17;
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] out = new int[18];
        for (int i = 0; i < 18; i++) {
            out[i] = i;
        }
        return out;
    }

    @Override
    public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) {
        return canPlaceItem(slot, stack);
    }

    @Override
    public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag items = new ListTag();
        for (int i = 0; i < this.slots.size(); ++i) {
            ItemStack st = this.slots.get(i);
            if (!st.isEmpty()) {
                CompoundTag it = (CompoundTag) st.save(provider);
                it.putByte("Slot", (byte) i);
                items.add(it);
            }
        }
        tag.put("Items", items);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        this.slots = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ListTag items = tag.getList("Items", 10);

        for (int i = 0; i < items.size(); ++i) {
            CompoundTag it = items.getCompound(i);
            int slot = it.getByte("Slot") & 255;
            if (slot >= 0 && slot < this.slots.size()) {
                this.slots.set(slot, ItemStack.parseOptional(provider, it));
            }
        }
    }
}
