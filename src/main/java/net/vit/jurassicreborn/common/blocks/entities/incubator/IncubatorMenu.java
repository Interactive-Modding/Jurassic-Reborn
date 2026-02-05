package net.vit.jurassicreborn.common.blocks.entities.incubator;

import net.minecraftforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer.DNASequencerMenu;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.IncubatorItemHandler;
import net.vit.jurassicreborn.common.items.IncubatorEnvironmentItem;
import net.vit.jurassicreborn.common.items.genetics.DinosaurEggItem;
import net.vit.jurassicreborn.common.util.slot.CustomSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class IncubatorMenu extends AbstractContainerMenu {

    protected IncubatorItemHandler owner;

    protected ContainerData ownerData;

    public IncubatorMenu(int pContainerId, Inventory playerInventory){
        this(pContainerId, playerInventory, new IncubatorItemHandler(6,IncubatorBlockEntity.INPUTS,IncubatorBlockEntity.ENVIRONMENT),
                new SimpleContainerData(13));
    }

    protected IncubatorMenu(int pContainerId, Inventory playerInventory, IncubatorItemHandler machine, ContainerData ownerData) {
        super(ModMenuTypes.INCUBATOR.get(), pContainerId);
        this.owner = machine;
        this.ownerData = ownerData;

//        this.incubator = (IncubatorBlockEntity) tileEntity;
        Predicate<ItemStack> eggPredicate = stack -> stack.getItem() instanceof DinosaurEggItem;
        this.addSlot(new SlotItemHandler(this.owner, 0, 33, 28));
        this.addSlot(new SlotItemHandler(this.owner, 1, 56, 21));
        this.addSlot(new SlotItemHandler(this.owner, 2, 79, 14));
        this.addSlot(new SlotItemHandler(this.owner, 3, 102, 21));
        this.addSlot(new SlotItemHandler(this.owner, 4, 125, 28));
        this.addSlot(new SlotItemHandler(this.owner, 5, 79, 49));
        DNASequencerMenu.addPlayerInventory(playerInventory, this::addSlot);

        this.addDataSlots(this.ownerData);


    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int slotIndex) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        int otherSlots = this.slots.size() - 36;

        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (slotIndex < otherSlots) {
                if (!this.moveItemStackTo(current, otherSlots, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(current, 0, otherSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return transferred;
    }

    public BlockPos getBlockPos(){
        return new BlockPos(this.ownerData.get(10), this.ownerData.get(11), this.ownerData.get(12));
    }

    public int getField(int index){
        return this.ownerData.get(index);
    }

    public void setField(int index, int value){
        this.ownerData.set(index, value);
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        if(!player.level.isClientSide){
            BlockEntity blockEntity = player.level.getBlockEntity(this.getBlockPos());
            if(blockEntity instanceof IncubatorBlockEntity incubator){
                incubator.setMenuOpen(false);
            }
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return true;//todo, check block
    }
}
