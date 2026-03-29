package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

/**
 * Base machine BlockEntity for Jurassic Reborn (NeoForge 1.21+)
 *
 * FEATURES:
 * - Packet-based syncing (no caps, no LazyOptional)
 * - Shared processing lifecycle
 * - Name / menu compatibility
 * - Inventory & fluid hooks (implemented by subclasses)
 */
public abstract class MachineBlockEntity extends BlockEntity {

    /* --------------------------------------------------------------------- */
    /* PROCESS STATE */
    /* --------------------------------------------------------------------- */

    protected int processTime;
    protected int processTimeTotal;

    protected MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /* --------------------------------------------------------------------- */
    /* NETWORK SYNC */
    /* --------------------------------------------------------------------- */

    @Override
    public @Nullable ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }

    /* --------------------------------------------------------------------- */
    /* SAVE / LOAD */
    /* --------------------------------------------------------------------- */

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);

        CompoundTag machine = new CompoundTag();
        machine.putInt("ProcessTime", processTime);
        machine.putInt("ProcessTimeTotal", processTimeTotal);

        Tag extra = getMachineData();
        if (extra != null) {
            machine.put("Data", extra);
        }

        writeInventory(machine, provider);
        writeFluids(machine, provider);

        tag.put("MachineData", machine);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);

        if (!tag.contains("MachineData", Tag.TAG_COMPOUND)) return;

        CompoundTag machine = tag.getCompound("MachineData");
        this.processTime = machine.getInt("ProcessTime");
        this.processTimeTotal = machine.getInt("ProcessTimeTotal");

        if (machine.contains("Data")) {
            readMachineData(machine.get("Data"));
        }

        readInventory(machine, provider);
        readFluids(machine, provider);
    }

    /* --------------------------------------------------------------------- */
    /* MACHINE CONTRACT (CRITICAL — USED BY ALL MACHINES) */
    /* --------------------------------------------------------------------- */
    protected void mergeStack(int slot, ItemStack stack) {
        if (!(this instanceof ItemHandlerBlockEntity handler)) {
            return;
        }

        ItemStack previous = handler.getItem(slot);
        if (previous.isEmpty()) {
            handler.setItem(slot, stack);
        } else if (ItemStack.isSameItemSameComponents(previous, stack) && ItemStack.isSameItemSameComponents(previous, stack)) {
            previous.setCount(previous.getCount() + stack.getCount());
        }
    }
    /**
     * Can the machine process the given inputs?
     * Subclasses MUST override.
     */
    public boolean canProcess(ItemStack... inputs) {
        return false;
    }

    /**
     * Perform processing and return produced outputs.
     * Subclasses MUST override.
     */
    public @NotNull List<ItemStack> processItem(ItemStack... inputs) {
        return Collections.emptyList();
    }

    /**
     * Called every server tick while processing.
     * Subclasses may override.
     */
    protected void tickProcessing(Level level) {
    }

    /* --------------------------------------------------------------------- */
    /* NAME / UI */
    /* --------------------------------------------------------------------- */

    /**
     * Default container name (used if no custom name is set).
     */
    protected @NotNull Component getDefaultName() {
        return Component.literal("Machine");
    }
    @Nullable
    protected Component customName;

    public boolean hasCustomName() {
        return this.customName != null;
    }

    @Nullable
    public Component getCustomName() {
        return this.customName;
    }

    public @NotNull Component getDisplayName() {
        return this.hasCustomName()
                ? this.getCustomName()
                : this.getDefaultName();
    }


    /* --------------------------------------------------------------------- */
    /* SUBCLASS HOOKS */
    /* --------------------------------------------------------------------- */

    protected @Nullable Tag getMachineData() {
        return null;
    }

    protected void readMachineData(Tag tag) {
    }

    protected void writeInventory(CompoundTag tag, HolderLookup.Provider provider) {
        if (!(this instanceof ItemHandlerBlockEntity handler)) {
            return;
        }

        ListTag items = new ListTag();
        var itemHandler = handler.getItemHandler();
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            ItemStack stack = itemHandler.getStackInSlot(slot);
            if (!stack.isEmpty()) {
                CompoundTag itemTag = (CompoundTag) stack.save(provider);
                itemTag.putInt("Slot", slot);
                items.add(itemTag);
            }
        }
        tag.put("Items", items);
    }

    protected void readInventory(CompoundTag tag, HolderLookup.Provider provider) {
        if (!(this instanceof ItemHandlerBlockEntity handler)) {
            return;
        }

        var itemHandler = handler.getItemHandler();
        for (int slot = 0; slot < itemHandler.getSlots(); slot++) {
            itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        }

        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag itemTag = items.getCompound(i);
            int slot = itemTag.getInt("Slot");
            if (slot >= 0 && slot < itemHandler.getSlots()) {
                itemHandler.setStackInSlot(slot, ItemStack.parseOptional(provider, itemTag));
            }
        }
    }
    protected void writeFluids(CompoundTag tag, HolderLookup.Provider provider) {
    }

    protected void readFluids(CompoundTag tag, HolderLookup.Provider provider) {
    }

    /* --------------------------------------------------------------------- */
    /* UTIL */
    /* --------------------------------------------------------------------- */

    public boolean isProcessing() {
        return processTime > 0;
    }

    protected void markDirtyAndSync() {
        setChanged();
        if (level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }
}
