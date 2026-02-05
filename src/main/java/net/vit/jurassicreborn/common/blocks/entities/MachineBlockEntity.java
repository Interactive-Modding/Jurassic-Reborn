package net.vit.jurassicreborn.common.blocks.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.Nameable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.CapabilityItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.FluidHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.SerializableSingleFluidTank;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.List;


/**
 * Base class for all(read: most) of our block entities.
 */
public abstract class MachineBlockEntity extends BlockEntity implements Nameable {
    private LazyOptional<IItemHandlerModifiable> itemHandlerCapability = LazyOptional.empty();
    private LazyOptional<IFluidHandler> fluidHandlerCapability = LazyOptional.empty();
    private final EnumMap<Direction, LazyOptional<IItemHandler>> sidedItemHandlerCapabilities = new EnumMap<>(Direction.class);

    protected MachineBlockEntity(BlockEntityType<?> pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
    }

    private void updateCapabilities() {
        if (this instanceof ItemHandlerBlockEntity itemHandlerBlockEntity) {
            IItemHandlerModifiable handler = itemHandlerBlockEntity.getItemHandler();
            itemHandlerCapability = LazyOptional.of(() -> handler);

            sidedItemHandlerCapabilities.values().forEach(LazyOptional::invalidate);
            sidedItemHandlerCapabilities.clear();

            if (handler instanceof MachineItemStackHandler machineHandler) {
                for (Direction direction : Direction.values()) {
                    sidedItemHandlerCapabilities.put(direction, LazyOptional.of(() -> new MachineItemHandlerSideWrapper(machineHandler, direction)));
                }
            }
        } else {
            itemHandlerCapability = LazyOptional.empty();
            sidedItemHandlerCapabilities.values().forEach(LazyOptional::invalidate);
            sidedItemHandlerCapabilities.clear();
        }

        if (this instanceof FluidHandlerBlockEntity fluidHandlerBlockEntity) {
            fluidHandlerCapability = LazyOptional.of(fluidHandlerBlockEntity::getFluidHandler);
        } else {
            fluidHandlerCapability = LazyOptional.empty();
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        updateCapabilities();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        invalidateCaps();
    }

    @Override
    public void reviveCaps() {
        super.reviveCaps();
        updateCapabilities();
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerCapability.invalidate();
        fluidHandlerCapability.invalidate();
        sidedItemHandlerCapabilities.values().forEach(LazyOptional::invalidate);
        sidedItemHandlerCapabilities.clear();
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && this instanceof ItemHandlerBlockEntity) {
            if (side != null) {
                LazyOptional<IItemHandler> sidedHandler = sidedItemHandlerCapabilities.get(side);
                if (sidedHandler != null) {
                    if (!sidedHandler.isPresent()) {
                        updateCapabilities();
                        sidedHandler = sidedItemHandlerCapabilities.get(side);
                    }
                    if (sidedHandler != null) {
                        return sidedHandler.cast();
                    }
                }
            }
            if (!itemHandlerCapability.isPresent()) {
                updateCapabilities();
            }
            return itemHandlerCapability.cast();
        }

        if (cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && this instanceof FluidHandlerBlockEntity) {
            if (!fluidHandlerCapability.isPresent()) {
                updateCapabilities();
            }
            return fluidHandlerCapability.cast();
        }

        return super.getCapability(cap, side);
    }

    /**
     * This gets saved data OTHER than the machine's inventory, I.E. process time or a list of other data relating to each slot
     * @see MachineBlockEntity#readMachineData(Tag)
     */
    public abstract Tag getMachineData();

    /**
     * This is the method that handles loading in saved data.
     * @param machineData Saved NBT data OTHER than the machine's inventory.
     * @see MachineBlockEntity#getMachineData()
     */
    public abstract void readMachineData(Tag machineData);


    @Override
    public @NotNull Component getName() {
        if (hasCustomName()) return getCustomName();
        return getDefaultName();
    }

    protected abstract Component getDefaultName();

    /**
     * This handles saving machine data.
     * @param pTag Input tag provided by Minecraft
     */
    @Override
    public void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);


        CompoundTag machineData = new CompoundTag();
        Tag tag = this.getMachineData();
        if(tag != null)
            machineData.put("Data", tag);

        if (this instanceof ItemHandlerBlockEntity itemHandlerBlockEntity) {
            if (itemHandlerBlockEntity.getItemHandler() instanceof ItemStackHandler handler) {
                machineData.put("item_inventory",handler.serializeNBT());
            }
        }

        if (this instanceof FluidHandlerBlockEntity fluidHandlerBlockEntity) {
            if (fluidHandlerBlockEntity.getFluidHandler() instanceof SerializableSingleFluidTank tank) {
                machineData.put("fluid_inventory", tank.serializeNBT());
            }
        }

        pTag.put("MachineData", machineData);

    }

    /**
     * This handles loading machine data.
     * @param pTag Input tag provided by Minecraft.
     */

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);

        CompoundTag data = pTag.getCompound("MachineData");

        if (this instanceof ItemHandlerBlockEntity itemHandlerBlockEntity) {
            if (itemHandlerBlockEntity.getItemHandler() instanceof ItemStackHandler handler) {
                CompoundTag itemInventoryTag = data.getCompound("item_inventory");
                handler.deserializeNBT(itemInventoryTag);
            }
        }

        if (this instanceof FluidHandlerBlockEntity fluidHandlerBlockEntity) {
            if (fluidHandlerBlockEntity.getFluidHandler() instanceof SerializableSingleFluidTank tank) {
                CompoundTag fluidInventoryTag = data.getCompound("fluid_inventory");
                tank.deserializeNBT(fluidInventoryTag);
            }
        }

        if(data.contains("Data"))
            this.readMachineData(data.get("Data"));




    }


    /**
     * This method should return weather or not the machine block entity should process the inputs given in the {@code ItemStack... inputs} param.
     * The way I've thought about this is that this method should be given an ordered list of item stacks with all inputs in an order
     * the coder devises. <br><br>
     * For example input #1 is a DNA syringe, input #2 is an egg, this method should return {@code true} if the machine is
     * an embryo calcification machine, the dino referenced from the DNA syringe lays an egg, AND input #2 is an egg.
     * @param inputs A given list of inputs from a machine. This should be for an individual input/alt. input set in a multi-processed machine.
     * @return true IF AND ONLY IF the machine is supposed to produce an output for the given list of inputs.
     */
    public abstract boolean canProcess(ItemStack... inputs);


    /**
     * This method should return the result of processing the given inputs, in itemstack form. However, handling placing these
     * items should be handled by the tick function. I also suggest handing off decreasing the item counts in the container
     * to the tick function, but that's less important: Do What Works.
     * @param inputs A given list of inputs from a machine. This should be for an individual input/alt. input set in a multi-processed machine.
     * @return Unordered list of output items to be handled by the tick function.
     */
    @NotNull
    public abstract List<ItemStack> processItem(ItemStack... inputs);

    protected void mergeStack(int slot, ItemStack stack) {
        if (this instanceof ItemHandlerBlockEntity itemHandlerBlockEntity) {
            IItemHandlerModifiable handlerModifiable = itemHandlerBlockEntity.getItemHandler();
            ItemStack previous = handlerModifiable.getStackInSlot(slot);
            if (previous.isEmpty()) {
                handlerModifiable.setStackInSlot(slot, stack);
            } else if (ItemStack.isSameItemSameTags(previous, stack)) {
                previous.setCount(previous.getCount() + stack.getCount());
            }
        }
    }

//    @Override
//    public CompoundTag getUpdateTag() {
//        return super.getUpdateTag();
//    }


    /**
     * This method impliments a Packet to sync our BlockEntity's inventory with the client! this replaces the system I have in Network
     *
     * @return The packet that syncs our inevntory with the client's
     */
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {

        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithoutMetadata);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        CompoundTag data = pkt.getTag();

        this.load(data);
    }

}
