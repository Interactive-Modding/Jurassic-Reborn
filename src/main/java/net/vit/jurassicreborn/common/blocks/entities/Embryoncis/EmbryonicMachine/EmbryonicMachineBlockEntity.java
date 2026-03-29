package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryonicMachine;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.EmbryonicMachineItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.DNAItem;
import net.vit.jurassicreborn.common.items.genetics.PlantDNAItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class EmbryonicMachineBlockEntity extends MachineBlockEntity implements ItemHandlerBlockEntity, MenuProvider {

    public static final int SLOTS = 7;
    public static final int[] INPUTS = new int[] { 0, 1, 2 };
    public static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };
    public static final int STACK_PROCESS_TIME = 200;
    protected int processTime = 0;

    public MachineItemStackHandler machineItemStackHandler = EmbryonicMachineItemHandler.instance();

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if(index == 0)
                return EmbryonicMachineBlockEntity.this.processTime;

            return -1;
        }

        @Override
        public void set(int index, int value) {
            if(index == 0)
                EmbryonicMachineBlockEntity.this.processTime = value;
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public EmbryonicMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.EMBRYONIC_MACHINE_BLOCK_ENTITY.get(), pPos, pBlockState);

        this.machineItemStackHandler.setChangeListener(() -> {
            setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        });
    }

    @Override
    public Tag getMachineData() {
        CompoundTag data = new CompoundTag();
        data.putInt("ProcessTime", this.processTime);
        return data;
    }

    @Override
    public void readMachineData(Tag data) {
        CompoundTag machineData = (CompoundTag) data;

        this.processTime = machineData.getInt("ProcessTime");
        
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("container.embryonic_machine");
    }

    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory, Player player) {
        return new EmbryonicMachineMenu(pContainerId, this.machineItemStackHandler, this.data, pInventory);
    }
    
    public boolean canProcess(ItemStack... stacks){
        ItemStack dna = this.machineItemStackHandler.getStackInSlot(0);
        ItemStack petridish = this.machineItemStackHandler.getStackInSlot(1);
        ItemStack syringe = this.machineItemStackHandler.getStackInSlot(2);

        if (syringe.getItem() == ModItems.EMPTY_SYRINGE.get()) {
            ItemStack output = null;

            if (petridish.getItem() == ModItems.PETRI_DISH.get() && dna.getItem() instanceof DNAItem dnaItem) {
                output = new ItemStack(ModItems.SYRINGES.get(dnaItem.dinosaur).get(), 1);

                ItemStackNbtUtil.setTag(output, ItemStackNbtUtil.getTag(dna));
            } else if (petridish.getItem() == ModItems.PLANT_CELLS_PETRI_DISH.get() && dna.getItem() instanceof PlantDNAItem) {
                output = new ItemStack(ModItems.PLANT_CALLUS.get());
                ItemStackNbtUtil.setTag(output, ItemStackNbtUtil.getTag(dna));
            }

            return output != null && this.hasOutputSlot(output);
        }

        return false;
        
    }

    public boolean hasOutputSlot(ItemStack output) {
        return this.getOutputSlot(output) != -1;
    }

    public int getOutputSlot(ItemStack output) {
        for (int slot : OUTPUTS) {
            ItemStack stack = machineItemStackHandler.getStackInSlot(slot);
            if (stack.isEmpty() || ((ItemStack.isSameItemSameComponents(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem())) {
                return slot;
            }
        }
        return -1;
    }

    @NotNull
    public List<ItemStack> processItem(ItemStack... inputs) {
        if (this.canProcess()) {
            ItemStack dna = this.machineItemStackHandler.getStackInSlot(0);
            ItemStack petriDish = this.machineItemStackHandler.getStackInSlot(1);
            ItemStack output = null;

            if (dna.getItem() instanceof DNAItem dinoDna && petriDish.getItem() == ModItems.PETRI_DISH.get()) {
                output = new ItemStack(ModItems.SYRINGES.get(dinoDna.dinosaur).get(), 1);

            } else if (dna.getItem() instanceof PlantDNAItem && petriDish.getItem() == ModItems.PLANT_CELLS_PETRI_DISH.get()) {
                output = new ItemStack(ModItems.PLANT_CALLUS.get(), 1);

            }


            ItemStackNbtUtil.setTag(output, ItemStackNbtUtil.getTag(dna));

            int emptySlot = this.getOutputSlot(output);

            if (emptySlot != -1) {
                this.mergeStack(emptySlot, output);

                this.decreaseStackSize(0);
                this.decreaseStackSize(1);
                this.decreaseStackSize(2);
            }
        }
        return List.of(ItemStack.EMPTY);
    }

    protected void mergeStack(int slot, ItemStack stack) {

        ItemStack previous = machineItemStackHandler.getStackInSlot(slot);
        if (previous.isEmpty()) {
            machineItemStackHandler.setStackInSlot(slot, stack);
        } else if ( ItemStack.isSameItemSameComponents(previous, stack)) {
            previous.setCount(previous.getCount() + stack.getCount());
        }
    }


    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, EmbryonicMachineBlockEntity pBlockEntity) {

        if(pLevel.isClientSide)
            return;


        if(pBlockEntity.processTime >= STACK_PROCESS_TIME){

            pBlockEntity.processItem();

            pBlockEntity.processTime = 0;

        }

        if(!pBlockEntity.canProcess()){
            pBlockEntity.processTime = 0;
        }else{
            pBlockEntity.processTime++;
        }
    }
    private void decreaseStackSize(int slot) {
        ItemStack stack = this.machineItemStackHandler.getStackInSlot(slot);
        stack.shrink(1);
        if (stack.getCount() <= 0) {
            this.machineItemStackHandler.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }
}
