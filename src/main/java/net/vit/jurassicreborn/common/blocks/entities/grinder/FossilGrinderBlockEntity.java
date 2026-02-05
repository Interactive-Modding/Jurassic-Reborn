package net.vit.jurassicreborn.common.blocks.entities.grinder;

import com.google.common.primitives.Ints;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.FossilGrinderItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.util.api.GrindableItem;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;
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

import java.util.List;
import java.util.Random;

public class FossilGrinderBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity {
    public static final int SLOTS = 12;
    public static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4, 5 };
    public static final int[] OUTPUTS = new int[] { 6, 7, 8, 9, 10, 11 };
    public static final int PROCESS_TIME = 200;

    protected final FossilGrinderItemHandler machineItemStackHandler = FossilGrinderItemHandler.instance();

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if(pIndex == 0)
                return FossilGrinderBlockEntity.this.grindTime;
            return 0;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if(pIndex == 0){
                FossilGrinderBlockEntity.this.grindTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };



    private int grindTime = 0;

    public FossilGrinderBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.FOSSIL_GRINDER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Tag getMachineData() {
        CompoundTag data = new CompoundTag();
        data.putInt("GrindTime", this.grindTime);
        return data;
    }

    @Override
    public void readMachineData(Tag machineData) {
        if(machineData instanceof CompoundTag dataTag)
            this.grindTime = dataTag.getInt("GrindTime");
        else
            this.grindTime = 0;
    }


    //should be complient with superclass docs but can't do it rn -gamma

    public boolean canProcess(ItemStack... inputs) {
        for (int inputIndex = 0; inputIndex < 6; inputIndex++) {
            ItemStack input = this.getItem(inputIndex);

            GrindableItem grindableItem = GrindableItem.getGrindableItem(input);

            if (grindableItem != null && grindableItem.isGrindable(input)) {
                for (int outputIndex = 6; outputIndex < 12; outputIndex++) {
                    if (this.getItem(outputIndex).isEmpty()) {
                        return true;
                    }
                }
            }
        }

        return false;
    }


    @NotNull
    public List<ItemStack> processItem(ItemStack... inputs) {
        Random rand = new Random();

        ItemStack input = ItemStack.EMPTY;
        int index = 0;

        for (int inputIndex = 0; inputIndex < 6; inputIndex++) {
            input = this.getItem(inputIndex);

            if (!input.isEmpty()) {
                index = inputIndex;
                break;
            }
        }

        if (!input.isEmpty()) {
            GrindableItem grindableItem = GrindableItem.getGrindableItem(input);


            ItemStack output = grindableItem.getGroundItem(input, rand);

            int emptySlot = this.getOutputSlot(output);
            if (emptySlot != -1) {
                this.mergeStack(emptySlot, output);
                this.decreaseStackSize(index);
            }
        }

        return List.of(ItemStack.EMPTY);
    }
    public static void copyDNA(ItemStack from, ItemStack to) {
        CompoundTag in = from.getTag();
        if (in != null && in.contains("DNA")) {
            CompoundTag out = to.getOrCreateTag();
            out.put("DNA", in.getCompound("DNA").copy());
            to.setTag(out);
            StorageDiscItem.applyCustomModelData(to);
        }
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.fossil_grinder");
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new FossilGrinderMenu(pContainerId, pInventory, this.machineItemStackHandler, this.data);
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, FossilGrinderBlockEntity pBlockEntity) {

        boolean isWorking = pBlockEntity.grindTime > 0;

        boolean hasInputs = pBlockEntity.hasInputs();

        if(pLevel.isClientSide){
            return;
        }

        if(!hasInputs && isWorking){//if we are working but we don't have inputs, we want to set the progress to 0
            pBlockEntity.grindTime = 0;
            pBlockEntity.setChanged();
            return;
        }

        if(hasInputs && pBlockEntity.canProcess()){
            pBlockEntity.grindTime++;

            if(pBlockEntity.grindTime >= PROCESS_TIME){
                pBlockEntity.grindTime = 0;
//                int total = 0;
//                for( int i : INPUTS){
//                    var stack = pBlockEntity.inventory.get(i);
//
//                    if(stack.isEmpty()){
//
//                    }
//
//                }
                pBlockEntity.processItem();
            }

        }











//        super.tick(pLevel, pPos, pState, (MachineBlockEntity) pBlockEntity);
    }


    public boolean hasInputs(){
        for(int i : INPUTS){
            if(!this.getItem(i).isEmpty()){
                return true;
            }
        }
        return false;
    }

    protected void mergeStack(int slot, ItemStack stack) {

        ItemStack previous = getItem(slot);
        if (previous.isEmpty()) {
            setItem(slot, stack);
        } else if (ItemStack.isSame(previous, stack) && ItemStack.isSame(previous, stack)) {
            previous.setCount(previous.getCount() + stack.getCount());
        }
    }

    protected void decreaseStackSize(int slot) {

        getItem(slot).shrink(1);

        if (getItem(slot).getCount() <= 0) {
            setItem(slot, ItemStack.EMPTY);
        }
    }

    public int getOutputSlot(ItemStack output) {
        for (int slot : OUTPUTS) {
            ItemStack stack = getItem(slot);
            if (stack.isEmpty() || ((ItemStack.isSame(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()) && stack.getItem() == output.getItem() && stack.getDamageValue() == output.getDamageValue())) {
                return slot;
            }
        }
        return -1;
    }


}
