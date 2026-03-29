package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASynthesizer;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.DNASynthesizerHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.api.SynthesizableItem;
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

public class DNASynthesizerBlockEntity extends MachineBlockEntity implements ItemHandlerBlockEntity, MenuProvider {

    public static final int SLOTS = 7;
    public static final int[] INPUTS = new int[] { 0, 1, 2 };
    public static final int[] OUTPUTS = new int[] { 3, 4, 5, 6 };

    private int synthesizeTime = 0;

    protected final DNASynthesizerHandler machineItemStackHandler = new DNASynthesizerHandler(SLOTS,INPUTS,OUTPUTS);

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    public ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if(pIndex == 0){
                return DNASynthesizerBlockEntity.this.synthesizeTime;
            }
            return -1;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if(pIndex == 0){
                DNASynthesizerBlockEntity.this.synthesizeTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    public DNASynthesizerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DNA_SYNTHESIZER_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public Tag getMachineData() {
        CompoundTag machineData = new CompoundTag();
        machineData.putInt("SynthesizeTime", synthesizeTime);
        return machineData;
    }

    @Override
    public void readMachineData(Tag data) {
        CompoundTag machineData = (CompoundTag) data;
        this.synthesizeTime = machineData.getInt("SynthesizeTime");
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("block.jurassicreborn.dna_synthesizer");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new DNASynthesizerMenu(pContainerId, this.machineItemStackHandler, this.data, pInventory);
    }

    public int getOpenOutput(ItemStack output){
        for(int i : OUTPUTS){
            if(this.getItem(i).isEmpty() || (ItemStack.isSameItemSameComponents(output, this.getItem(i)) && ItemStack.isSameItemSameComponents(this.getItem(i), output))){
                return i;
            }
        }
        return -1;
    }



    public boolean hasSpace(){
        for(int i : OUTPUTS){
            if(this.getItem(i).isEmpty()){
                return true;
            }
        }
        return false;
    }


    //should be made complient with superclass docs but not rn - gamma
    public @NotNull List<ItemStack> processItem(ItemStack... inputs){
        ItemStack storageDisc = this.getItem(0);

        ItemStack output = SynthesizableItem.getSynthesizableItem(storageDisc).getSynthesizedItem(storageDisc, this.level.getRandom());

        int outputSlot = this.getOpenOutput(output);

        if (outputSlot != -1) {
//            this.setItem(outputSlot, output);
            this.mergeStack(outputSlot, output);

            ItemStack tube = this.getItem(1);
            ItemStack dnaBaseMaterial = this.getItem(2);
            tube.shrink(1);
            dnaBaseMaterial.shrink(1);
            this.setItem(1, tube);
            this.setItem(2, dnaBaseMaterial);

        }
        return List.of(ItemStack.EMPTY);
    }

    public static void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull DNASynthesizerBlockEntity pBlockEntity) {

        if(pLevel.isClientSide)
            return;

        if(!pBlockEntity.canProcess()){
            pBlockEntity.synthesizeTime = 0;
            return;
        }else{
            pBlockEntity.synthesizeTime++;
        }

        if(pBlockEntity.synthesizeTime >= 2000){
            pBlockEntity.processItem();
            pBlockEntity.synthesizeTime = 0;

        }
    }

    //SHOuld be made compliant with superclass docs but not rn
    public boolean canProcess(ItemStack... inputs) {
        return this.getItem(0).getItem() == ModItems.STORAGE_DISC.get() && this.getItem(1).getItem() == ModItems.EMPTY_TEST_TUBE.get()
                && this.getItem(2).getItem() == ModItems.DNA_NUCLEOTIDES.get();

    }
}
