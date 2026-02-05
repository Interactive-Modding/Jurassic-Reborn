package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.FluidHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.SerializableSingleFluidTank;
import net.vit.jurassicreborn.common.util.block.TemperatureControl;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CultivatorTopBlockEntity extends MachineBlockEntity implements MenuProvider, ItemHandlerBlockEntity, FluidHandlerBlockEntity, TemperatureControl {
    @Nullable public CultivatorBlockEntity bottomEntity;

    private void ensureBottomEntity() {
        if (bottomEntity == null && this.level != null) {
            if (this.level.getBlockEntity(this.getBlockPos().below()) instanceof CultivatorBlockEntity be) bottomEntity = be;
        }
    }

    public CultivatorTopBlockEntity(BlockPos pos, BlockState state) { super(ModBlockEntities.CULTIVATOR_TOP_BLOCK_ENTITY_TYPE.get(), pos, state); }

    @Override public IItemHandlerModifiable getItemHandler() { ensureBottomEntity(); return bottomEntity != null ? bottomEntity.getItemHandler() : new MachineItemStackHandler(0,new int[0],new int[0]); }
    @Override public IFluidHandler getFluidHandler()          { ensureBottomEntity(); return bottomEntity != null ? bottomEntity.getFluidHandler() : new SerializableSingleFluidTank(0); }
    @Override public void onLoad() { ensureBottomEntity(); }
    public void setBottomEntity(CultivatorBlockEntity bottom) { this.bottomEntity = bottom; }

    @Override public Tag getMachineData() { return null; }
    @Override public void readMachineData(Tag tag) { ensureBottomEntity(); if (bottomEntity != null) bottomEntity.readMachineData(tag); }
    @Override public boolean canProcess(ItemStack... inputs) { ensureBottomEntity(); return bottomEntity != null && bottomEntity.canProcess(inputs); }
    @Override public @NotNull List<ItemStack> processItem(ItemStack... inputs) { ensureBottomEntity(); return bottomEntity != null ? bottomEntity.processItem(inputs) : new ArrayList<>(); }
    @Override public void setTemperature(int i,int v){ ensureBottomEntity(); if (bottomEntity!=null) bottomEntity.setTemperature(i,v); }
    @Override public int getTemperature(int i){ ensureBottomEntity(); return bottomEntity!=null? bottomEntity.getTemperature(i):0; }
    @Override public int getTemperatureCount(){ ensureBottomEntity(); return bottomEntity!=null? bottomEntity.getTemperatureCount():0; }
    @Override protected Component getDefaultName(){ ensureBottomEntity(); return bottomEntity!=null? bottomEntity.getDefaultName(): Component.translatable("container.cultivator"); }
    @Override public Component getDisplayName(){ ensureBottomEntity(); return bottomEntity!=null? bottomEntity.getDisplayName(): Component.translatable("container.cultivator"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player){ ensureBottomEntity(); return bottomEntity!=null? bottomEntity.createMenu(id, inv, player): null; }
}
