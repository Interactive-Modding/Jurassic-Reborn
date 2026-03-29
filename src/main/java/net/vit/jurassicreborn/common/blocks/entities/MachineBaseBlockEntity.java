package net.vit.jurassicreborn.common.blocks.entities;

import io.netty.buffer.ByteBuf;
import net.vit.jurassicreborn.common.util.networking.Syncable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public abstract class MachineBaseBlockEntity<A extends MachineBaseBlockEntity> extends RandomizableContainerBlockEntity implements Syncable, BlockEntityTicker<A>, WorldlyContainer {
    protected String customName;

    protected int[] processTime = new int[this.getProcessCount()];
    protected int[] totalProcessTime = new int[this.getProcessCount()];

    protected MachineBaseBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

//    @OnlyIn(Dist.CLIENT)todo: Legacy, look into uses
//    public static boolean isProcessing(Inventory inventory, int index) {
//        return inventory.get(index) > 0;
//    }

    @Override
    protected void loadAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.loadAdditional(compound, provider);

        NonNullList<ItemStack> inventory = NonNullList.create();
        if (compound.contains("Items")) {
            ContainerHelper.loadAllItems(compound.getCompound("Items"), inventory, provider);
        }

        for (int i = 0; i < this.getProcessCount(); i++) {
            this.processTime[i] = compound.getShort("ProcessTime" + i);
            this.totalProcessTime[i] = compound.getShort("ProcessTimeTotal" + i);
        }
        if (compound.contains("CustomName", 8)) {
            this.customName = compound.getString("CustomName");
        }
        this.setSlots(inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag compound, HolderLookup.Provider provider) {
        super.saveAdditional(compound, provider);

        for (int i = 0; i < this.getProcessCount(); i++) {
            compound.putInt("ProcessTime" + i, (short) this.processTime[i]);
            compound.putInt("ProcessTimeTotal" + i, (short) this.totalProcessTime[i]);
        }

        NonNullList<ItemStack> slots = this.getSlots();
        CompoundTag items = new CompoundTag();
        ContainerHelper.saveAllItems(items, slots, provider);
        compound.put("Items", items);

//        NBTTagList itemList = new NBTTagList();
//
//        for (int slot = 0; slot < this.getSizeInventory(); ++slot) {
//            if (!slots.get(slot).isEmpty()) {
//                NBTTagCompound itemTag = new NBTTagCompound();
//                itemTag.setByte("Slot", (byte) slot);
//
//                slots.get(slot).writeToNBT(itemTag);
//                itemList.appendTag(itemTag);
//            }
//        }
//
//        ItemStackNbtUtil.setTag(compound, "Items", itemList);

        if (this.hasCustomName()) {
            compound.putString("CustomName", this.customName);
        }

//        return compound;
    }

    @Override
    public ItemStack getItem(int index) {
        try {
            if (this.getSlots().size() >= index || !(this.getSlots().size() == index))
                return this.getSlots().get(index);
        }catch(IndexOutOfBoundsException e){
            e.printStackTrace();
        }
        return ItemStack.EMPTY;
    }


//    public ItemStack decrStackSize(int index, int count) {   what is this? possibly needs updating - gamma
//        NonNullList<ItemStack> slots = this.getSlots();
//        return ItemStackHelper.getAndSplit(slots, index, count);
//    }


    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        this.unpackLootTable((Player)null);
        return ContainerHelper.removeItem(this.getItems(), pIndex, pCount);
    }

    @Override
    public NonNullList getSyncFields(NonNullList fields)
    {

        return fields;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.getItems(), 0);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();

        boolean stacksEqual = !stack.isEmpty() && stack.is(slots.get(index).getItem()) && ItemStack.isSameItemSameComponents(stack, slots.get(index));
        slots.set(index, stack);

        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!stacksEqual) {
            int process = this.getProcess(index);
            if (process >= 0 && process < this.getProcessCount()) {
                this.totalProcessTime[process] = this.getStackProcessTime(stack);
                if (!this.canProcess(process)) {
                    this.processTime[process] = 0;
                }
                this.setChanged();
            }
            this.onSlotUpdate();
        }
    }




    private boolean isInput(int slot) {
        int[] inputs = this.getInputs();

        for (int input : inputs) {
            if (input == slot) {
                return true;
            }
        }

        return false;
    }

    private boolean isOutput(int slot) {
        int[] outputs = this.getOutputs();

        for (int output : outputs) {
            if (output == slot) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean hasCustomName() {
        return this.customName != null && this.customName.length() > 0;
    }

    public void setCustomInventoryName(String customName) {
        this.customName = customName;
    }

    @Override
    public int getContainerSize() {
        return this.getSlots().size();
    }



    public boolean isProcessing(int index) {
        return this.processTime[index] > 0;
    }



    @Override
    public void tick(Level world, BlockPos pPos, BlockState pState, A pBlockEntity) {

        NonNullList<ItemStack> slots = this.getSlots();

        if(!world.isClientSide) {
            for (int process = 0; process < this.getProcessCount(); process++) {
                boolean flag = this.isProcessing(process);
                boolean dirty = false;

                boolean hasInput = false;

                for (int input : this.getInputs(process)) {
                    if (slots.size() > input && !slots.get(input).isEmpty()) {//added catch for index out of bounds
                        hasInput = true;
                        break;
                    }
                }

                if (hasInput && this.canProcess(process)) {
                    this.processTime[process]++;

                    if (this.processTime[process] >= this.totalProcessTime[process]) {
                        this.processTime[process] = 0;
                        int total = 0;
                        for (int input : this.getInputs()) {
                            ItemStack stack = slots.get(input);
                            if (!stack.isEmpty()) {
                                total = this.getStackProcessTime(stack);
                                break;
                            }
                        }
                        this.totalProcessTime[process] = total;
                        if (!world.isClientSide)
                        {
                            this.processItem(process);
                            this.onSlotUpdate();
                        }

                    }

                    dirty = true;
                } else if (this.isProcessing(process)) {
                    if (this.shouldResetProgress()) {
                        this.processTime[process] = 0;
                    } else if (this.processTime[process] > 0) {
                        this.processTime[process]--;
                    }

                    dirty = true;
                }

                if (flag != this.isProcessing(process)) {
                    dirty = true;
                }

                if (dirty && !world.isClientSide) {
                    this.setChanged();
                }
            }
        }
    }


//    @Override
//    public boolean isUsableByPlayer(Player player) {
//        return this.world.getTileEntity(this.pos) == this && player.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
//    }

    @Override
    public boolean canOpen(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
    }




    @Override
    public boolean canPlaceItem(int pIndex, ItemStack pStack) {
        return !this.isOutput(pIndex);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? this.getOutputs() : this.getInputs();
    }



    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack stack, Direction direction) {
        return this.canPlaceItem(index, stack);
    }

    protected int getProcess(int slot) {
        return slot;
    }

    protected boolean canProcess(int process) {
        return true;
    }

    protected void processItem(int process) {
    }

    protected int getMainOutput(int process) {
        return 0;
    }

    protected int getStackProcessTime(ItemStack stack) {
        return 0;
    }

    protected int getProcessCount() {
        return 0;
    }

    protected abstract int[] getInputs();

    protected abstract int[] getInputs(int process);

    protected abstract int[] getOutputs();

    protected abstract NonNullList<ItemStack> getSlots();

    public boolean hasOutputSlot(ItemStack output) {
        return this.getOutputSlot(output) != -1;
    }

    public int getOutputSlot(ItemStack output) {
        NonNullList<ItemStack> slots = this.getSlots();
        int[] outputs = this.getOutputs();
        for (int slot : outputs) {
            ItemStack stack = slots.get(slot);
            if (stack.isEmpty() || ((ItemStack.isSameItemSameComponents(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem() && stack.getDamageValue() == output.getDamageValue())) {
                return slot;
            }
        }
        return -1;
    }

    public int getField(int id) {
        int processCount = this.getProcessCount();
        if (id < processCount) {
            return this.processTime[id];
        } else if (id < processCount * 2) {
            return this.totalProcessTime[id - processCount];
        }

        return 0;
    }

    public void setField(int id, int value) {
        int processCount = this.getProcessCount();

        if (id < processCount) {
            this.processTime[id] = value;
        } else if (id < processCount * 2) {
            this.totalProcessTime[id - processCount] = value;
        }
    }

    public int getFieldCount() {
        return this.getProcessCount() * 2;
    }

    public void clear() {
        NonNullList<ItemStack> slots = this.getSlots();

        for (int i = 0; i < slots.size(); ++i) {
            slots.set(i, ItemStack.EMPTY);
        }
    }

    @Override
    public void clearContent() {
        this.clear();
    }


    @Override
    public boolean canTakeItemThroughFace(int pIndex, ItemStack pStack, Direction pDirection) {
        return true;
    }

    protected void mergeStack(int slot, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();

        ItemStack previous = slots.get(slot);
        if (previous.isEmpty()) {
            slots.set(slot, stack);
        } else if (ItemStack.isSameItemSameComponents(previous, stack) && ItemStack.isSameItemSameComponents(previous, stack)) {
            previous.setCount(previous.getCount() + stack.getCount());
        }
    }


    protected void decreaseStackSize(int slot) {
        NonNullList<ItemStack> slots = this.getSlots();

        slots.get(slot).shrink(1);

        if (slots.get(slot).getCount() <= 0) {
            slots.set(slot, ItemStack.EMPTY);
        }
    }


    public int getInventoryStackLimit() {
        return getMaxStackSize();
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    //    @Override
//    public SPacketUpdateTileEntity getUpdatePacket() {
//        return new SPacketUpdateTileEntity(this.pos, 0, this.getUpdateTag());
//    }

    public Packet<ClientGamePacketListener> getUpdatePacket() {
        // Will get tag from #getUpdateTag
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.saveWithoutMetadata(provider);
    }



//    @Override
//    public void onDataPacket(NetworkManager networkManager, SPacketUpdateTileEntity packet) {
//        this.readFromNBT(packet.getNbtCompound());
//    }

    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        if (pkt.getTag() != null && this.level != null) {
            this.loadAdditional(pkt.getTag(), this.level.registryAccess());
        }
    }

    protected boolean shouldResetProgress() {
        return true;
    }

    protected void setSlots(NonNullList<ItemStack> slots) {}

    protected void onSlotUpdate() {}

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public Component getDefaultName() {

        //noinspection NoTranslation >:( being annoying; it exists you just don't FEEL like it does idiot inspection
        return Component.translatable("jurassicreborn:machine_base_block");
    }



//    @Override
//    public Container createContainer(Inventory playerInventory, Play playerIn) {
//        return null;
//    }

//    @Nullable
//    @Override
//    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
//        return null;
//    }

//    @Override
//    public String getGuiID() {
//        return null;
//    }


//    @Override
//    public void packetDataHandler(ByteBuf dataStream) {}


    @Override
    public void packetDataHandler(ByteBuf fields) {}//lol
}
