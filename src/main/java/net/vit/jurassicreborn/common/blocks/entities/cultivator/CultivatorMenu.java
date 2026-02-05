package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModMenuTypes;
import net.vit.jurassicreborn.common.blocks.inventory.CultivatorItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public class CultivatorMenu extends AbstractContainerMenu {

    private final MachineItemStackHandler owner;
    private final ContainerData ownerData;

    public CultivatorMenu(int containerId, Inventory playerInventory) {
        this(containerId, new CultivatorItemHandler(4, CultivatorBlockEntity.INPUTS, CultivatorBlockEntity.OUTPUTS), new SimpleContainerData(10), playerInventory);
    }

    public CultivatorMenu(int containerId, CultivatorItemHandler owner, ContainerData data, Inventory playerInventory) {
        super(ModMenuTypes.CULTIVATOR.get(), containerId);
        this.owner = owner;
        this.ownerData = data;

        // 0 syringe, 1 food, 2 water-in, 3 empty buckets-out, 4 output egg
        this.addSlot(new SlotItemHandler(this.owner, 0, 122, 44));
        this.addSlot(new SlotItemHandler(this.owner, 1, 207, 20));
        this.addSlot(new SlotItemHandler(this.owner, 2, 12,  20));
        this.addSlot(new SlotItemHandler(this.owner, 3, 12,  68));

        addPlayerInventory(playerInventory, this::addSlot);
        this.addDataSlots(this.ownerData);
    }

    public static void addPlayerInventory(Inventory inv, Function<Slot, Slot> adder) {
        for (int l = 0; l < 3; ++l)
            for (int j = 0; j < 9; ++j)
                adder.apply(new Slot(inv, j + l * 9 + 9, 8 + j * 18, 106 + l * 18));
        for (int i = 0; i < 9; ++i)
            adder.apply(new Slot(inv, i, 8 + i * 18, 164));
    }

    public int getField(int i){ return this.ownerData.get(i); }
    public void setField(int i,int v){ this.ownerData.set(i,v); }
    public BlockPos getBlockPos(){ return new BlockPos(this.getField(7), this.getField(8), this.getField(9)); }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int index) {
        ItemStack transferred = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        int machineSlots = this.slots.size() - 36;

        if (slot.hasItem()) {
            ItemStack current = slot.getItem();
            transferred = current.copy();

            if (index < machineSlots) {
                if (!this.moveItemStackTo(current, machineSlots, this.slots.size(), true)) return ItemStack.EMPTY;
            } else if (!this.moveItemStackTo(current, 0, machineSlots, false)) {
                return ItemStack.EMPTY;
            }

            if (current.getCount() == 0) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }
        return transferred;
    }

    @Override public boolean stillValid(@NotNull Player p) { return true; }
}
