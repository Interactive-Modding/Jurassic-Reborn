package net.vit.jurassicreborn.common.blocks.entities.Embryoncis.EmbryoCalcificationMachine;

import com.google.common.primitives.Ints;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.EmbryoCalcificationMachineItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.DinosaurEggItem;
import net.vit.jurassicreborn.common.items.genetics.SyringeItem;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EmbryoCalcificationMachineBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity {
    public static final int SLOTS = 3;
    public static final int STACK_PROCESS_TIME = 200;
    public static final int[] INPUTS = new int[]{0, 1};
    public static final int[] OUTPUTS = new int[]{2};
    private int processTime = 0;

    protected final EmbryoCalcificationMachineItemHandler machineItemStackHandler = new EmbryoCalcificationMachineItemHandler(SLOTS,INPUTS,OUTPUTS);

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if (pIndex == 0) {
                return EmbryoCalcificationMachineBlockEntity.this.processTime;
            }
            return 0;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if (pIndex == 0) {
                EmbryoCalcificationMachineBlockEntity.this.processTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public EmbryoCalcificationMachineBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.EMBRYO_CALCIFICATION_MACHINE_BLOCK_ENTITY_TYPE.get(), pPos, pBlockState);

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


    public boolean canPlaceItem(int slotID, @NotNull ItemStack itemstack) {
        if (Ints.asList(INPUTS).contains(slotID)) {
            return slotID == 0 && itemstack.getItem() instanceof SyringeItem syringe && syringe.getDinosaur(itemstack).getBirthType() == Dinosaur.BirthType.EGG_LAYING
                    || slotID == 1 && itemstack.getItem() == Items.EGG;
        }

        return false;
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.embryo_calcification_machine");
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new EmbryoCalcificationMachineMenu(pContainerId, this.machineItemStackHandler, this.data, pInventory);
    }

    public boolean canProcess(ItemStack... stacks) {
        ItemStack input = stacks[0];
        ItemStack egg = stacks[1];
        ItemStack outputStack = stacks[2];

        if (!input.isEmpty() && input.getItem() instanceof SyringeItem syringe && !egg.isEmpty() && egg.getItem() == Items.EGG) {
            Dinosaur dino = syringe.getDinosaur(input);

            if (dino.getBirthType() == Dinosaur.BirthType.EGG_LAYING && (!dino.isMarineCreature() || dino == DinosaurHandler.CALYMENE|| dino == DinosaurHandler.BEELZEBUFO)) {

                if(!outputStack.isEmpty()) {
                    //assume that the output is a DinosaurEggItem
                    DinosaurEggItem outputItem = (DinosaurEggItem) outputStack.getItem();

                    return outputItem.equals(ModItems.dinoEggs.get(dino).get());
                }else{
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public @NotNull List<ItemStack> processItem(ItemStack... stacks) {
        ItemStack input = stacks[0];
        ItemStack egg = stacks[1];
        ItemStack outputStack = stacks[2];
        if (!input.isEmpty() && input.getItem() instanceof SyringeItem syringe) {
//            ItemStack output = new ItemStack(ItemHandler.EGG, 1, this.slots.get(0).getItemDamage());
            ItemStack output = ModItems.dinoEggs.get(syringe.getDinosaur(input)).get().getDefaultInstance();
            output.setTag(input.getTag());
            return List.of(output);

        }
        return List.of(ItemStack.EMPTY);
    }

    public ItemStack[] collectInputs(int... flags) {
        return new ItemStack[]{this.getItem(0), // syringe stack
                this.getItem(1), // egg stack
                this.getItem(2)}; // output stack
    }

    public static void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull EmbryoCalcificationMachineBlockEntity pBlockEntity) {

        if (pLevel.isClientSide)
            return;

        ItemStack[] inputs = pBlockEntity.collectInputs();

        if (pBlockEntity.processTime >= STACK_PROCESS_TIME && pBlockEntity.canProcess(inputs)) {

            ItemStack output = pBlockEntity.processItem(inputs).get(0);
            pBlockEntity.mergeStack(2, output);
            pBlockEntity.decreaseStackSize(0);
            pBlockEntity.decreaseStackSize(1);

            pBlockEntity.processTime = 0;

            inputs = pBlockEntity.collectInputs();

        }

        if (pBlockEntity.canProcess(inputs)) {
            pBlockEntity.processTime++;
        } else {
            pBlockEntity.processTime = 0;
        }
    }
    private void decreaseStackSize(int slot) {
        ItemStack stack = this.getItem(slot);
        stack.shrink(1);
        if (stack.getCount() <= 0) {
            this.setItem(slot, ItemStack.EMPTY);
        }
    }
}
