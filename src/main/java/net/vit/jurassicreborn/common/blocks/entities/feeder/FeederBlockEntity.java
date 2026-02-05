package net.vit.jurassicreborn.common.blocks.entities.feeder;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.EntityType;
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
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.items.Food.FoodHelper;
import net.vit.jurassicreborn.common.util.networking.BlockUpdateUtils;
import net.vit.jurassicreborn.common.util.networking.Syncable;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FeederBlockEntity extends RandomizableContainerBlockEntity implements WorldlyContainer, MenuProvider, Syncable {

    public static final int meatSlot  = 8;   // last meat index
    public static final int plantSlot = 17;  // last plant index

    public AABB feederBoundingBox;
    public int prevOpenAnimation;
    public int openAnimation;

    private NonNullList<ItemStack> slots = NonNullList.withSize(18, ItemStack.EMPTY);

    private int     stayOpen;
    private boolean open;

    private DinosaurEntity feeding;
    private int feedingExpire;

    private ArrayList<DinosaurEntity> prospectiveFeeders = new ArrayList<>();
    private int scanCooldown = 0;

    protected FeederBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.feederBoundingBox = new AABB(pos).inflate(32);
    }

    public FeederBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.FEEDER.get(), pos, state);
    }

    // --- Menu / container wiring ------------------------------------------------

    private final ContainerData dataAccess = new ContainerData() {
        @Override public int  get(int idx)          { return 0; }
        @Override public void set(int idx, int val) { }
        @Override public int  getCount()            { return 0; }
    };

    @Override protected NonNullList<ItemStack> getItems() { return slots; }
    @Override protected void setItems(NonNullList<ItemStack> stacks) { this.slots = stacks; }

    @Override
    public void setItem(int slot, ItemStack stack) {
        super.setItem(slot, stack);
        this.setChanged();
    }

    @Override protected Component getDefaultName() { return Component.literal("Feeder"); }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInv, Player player) {
        return new FeederMenu(id, playerInv, this, this.dataAccess);
    }
    public boolean isStockedFor(DinosaurEntity dino) {
        return this.getFood(dino) != -1;
    }
    /** Required by RandomizableContainerBlockEntity (MC 1.19.x overload). */
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory playerInv) {
        return new FeederMenu(id, playerInv, this, this.dataAccess);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        ItemStack stack = super.removeItem(slot, amount);
        if (!stack.isEmpty()) this.setChanged();
        return stack;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (this.level != null) {
            BlockUpdateUtils.sendBlockEntityUpdate(this.level, this.worldPosition);
        }
    }

    @Override public int  getContainerSize() { return 18; }
    @Override public int  getMaxStackSize()  { return 64; }

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

    @Override public void clearContent() { this.slots.clear(); }

    @Override
    public boolean isEmpty() {
        for (ItemStack s : this.slots) if (!s.isEmpty()) return false;
        return true;
    }

    // --- Open/close animation ---------------------------------------------------

    public void setOpen(boolean open) {
        if (!this.level.isClientSide && this.open != open) {
            this.level.blockEvent(this.worldPosition, this.getBlockState().getBlock(), 0, open ? 1 : 0);
        }
        this.open = open;
        if (!open) this.feeding = null;
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 0) {
            this.open = (type == 1);
            return true;
        }
        return super.triggerEvent(id, type);
    }

    // --- Networking -------------------------------------------------------------

    @Override public CompoundTag getUpdateTag() { return this.saveWithoutMetadata(); }

    @Nullable
    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null) this.load(pkt.getTag());
    }

    @Override
    public NonNullList getSyncFields(NonNullList fields) {
        for (int i = 0; i < 18; i++) fields.add(this.slots.get(i));
        return fields;
    }

    @Override
    public void packetDataHandler(ByteBuf fields) {
        // no-op
    }

    // --- Ticking (invoked by BlockEntityTicker) --------------------------------

    public void tick(Level world, BlockPos pos, BlockState state, FeederBlockEntity instance) {
        if (this.level == null) return;

        this.feederBoundingBox = new AABB(pos).inflate(32);

        // refresh nearby dinos at low frequency
        if (--scanCooldown <= 0) {
            this.refreshProspectiveFeeders(world);
            scanCooldown = 20; // 1s
        }

        this.prevOpenAnimation = this.openAnimation;

        if (this.open && this.openAnimation < 20)      this.openAnimation++;
        else if (!this.open && this.openAnimation > 0) this.openAnimation--;

        if (this.open && this.openAnimation == 19) this.stayOpen = 20;

        if (this.feeding != null && (this.feeding.isCarcass() || this.feeding.isDeadOrDying())) {
            this.feeding = null;
        }

        if (this.feeding != null) {
            if (this.feedingExpire > 0) this.feedingExpire--;
            else this.feeding = null;
        }

        if (this.open && this.openAnimation == 20) {
            if (this.stayOpen > 0) {
                this.stayOpen--;

                // dispense exactly once when half-way through the open state
                if (this.stayOpen == 10 && this.feeding != null) {
                    int feedSlot = this.getFood(this.feeding);
                    if (feedSlot >= 0) {
                        ItemStack stack = this.slots.get(feedSlot);
                        if (!stack.isEmpty()) {
                            // only block if *this exact item* is already on the ground,
                            // not if any item from any feeder slot exists
                            if (spawnOneItemTowardsFace(feedSlot, stack)) {
                                // one feeding per open cycle
                                this.feeding = null;
                            } else {
                                // give the feeder a brief moment to try again before closing
                                this.stayOpen = Math.max(this.stayOpen, 12);
                            }
                        } else {
                            this.feeding = null;
                        }
                    } else {
                        this.feeding = null;
                    }
                }
            } else if (!this.level.isClientSide) {
                this.open = false;
            }
        }
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
            case UP -> { oy = 1.0F; my = 1.0F; mx = random.nextFloat() - 0.5F; mz = random.nextFloat() - 0.5F; }
            case DOWN -> { oy = -1.0F; }
            case NORTH -> { oz = -1.0F; my = 0.5F; mz = -0.5F; }
            case SOUTH -> { oz =  1.0F; my = 0.5F; mz =  0.5F; }
            case WEST  -> { ox = -1.0F; my = 0.5F; mx = -0.5F; }
            case EAST  -> { ox =  1.0F; my = 0.5F; mx =  0.5F; }
        }

        double spawnX = this.getBlockPos().getX() + ox;
        double spawnY = this.getBlockPos().getY() + oy;
        double spawnZ = this.getBlockPos().getZ() + oz;

        // Only block dispensing if an identical item is already resting directly in front of the feeder.
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
                ie -> !ie.getItem().isEmpty() && ItemStack.isSameItemSameTags(ie.getItem(), stack)
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
            this.feeding.getNavigation().moveTo(itemEntity.getX() + vx, itemEntity.getY() + vy, itemEntity.getZ() + vz, 0.8);
        }
        return true;
    }

    private void refreshProspectiveFeeders(Level world) {
        // keep lightweight; dinos decide reachability themselves
        List<DinosaurEntity> nearby = world.getEntitiesOfClass(DinosaurEntity.class, this.feederBoundingBox);
        this.prospectiveFeeders = new ArrayList<>(nearby);
    }

    /** returns first slot index with edible food for this dino, or -1 if none */
    public int getFood(DinosaurEntity feeding) {
        if (feeding == null) return -1;
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
                if (FoodHelper.isEdible(dinosaur, meta.getDiet(), stack.getItem())) return i;
            }
            i++;
        }
        return -1;
    }

    public void setFeeding(@Nullable DinosaurEntity feeding) {
        this.feeding = feeding;
        this.feedingExpire = (feeding != null) ? 400 : 0;
    }

    @Nullable public DinosaurEntity getFeeding() { return this.feeding; }

    @Override public boolean canPlaceItem(int slot, ItemStack stack) { return isItemValidForSlot(slot, stack); }

    public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
        if (itemstack == null || itemstack.isEmpty()) return false;

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

    public static boolean isMeatSlot(int slot)  { return slot >= 0 && slot <= 8; }
    public static boolean isPlantSlot(int slot) { return slot >= 9 && slot <= 17; }

    @Override
    public int[] getSlotsForFace(Direction side) {
        int[] out = new int[18];
        for (int i = 0; i < 18; i++) out[i] = i;
        return out;
    }

    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, @Nullable Direction dir) { return canPlaceItem(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction dir) { return true; }

    // --- Save / load ------------------------------------------------------------

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ListTag items = new ListTag();
        for (int i = 0; i < this.slots.size(); ++i) {
            ItemStack st = this.slots.get(i);
            if (!st.isEmpty()) {
                CompoundTag it = new CompoundTag();
                it.putByte("Slot", (byte) i);
                st.save(it);
                items.add(it);
            }
        }
        tag.put("Items", items);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.slots = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ListTag items = tag.getList("Items", 10);
        for (int i = 0; i < items.size(); ++i) {
            CompoundTag it = items.getCompound(i);
            int slot = it.getByte("Slot") & 255;
            if (slot >= 0 && slot < this.slots.size()) {
                this.slots.set(slot, ItemStack.of(it));
            }
        }
    }
}