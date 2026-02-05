package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNASequencer;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.DNASequencerItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.SoftTissueItem;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.util.api.SequencableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import java.util.Objects;
import java.util.Random;

public class DNASequencerBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity {

    public static final int SLOTS = 9;
    public static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4, 5 };
    public static final int[] INPUTS_PROCESS_1 = new int[] { 0, 1 };
    private static final int[] INPUTS_PROCESS_2 = new int[] { 2, 3 };
    private static final int[] INPUTS_PROCESS_3 = new int[] { 4, 5 };

    private static final int[] DISCS_INPUT = new int[]{1, 3, 5};
    public static final int[] DNA_INPUT = new int[]{0, 2, 4};

    public static final int[] OUTPUTS = new int[] { 6, 7, 8 };

    private int[] sequencingTime = new int[3];

    protected final DNASequencerItemHandler machineItemStackHandler = DNASequencerItemHandler.instance();

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    private ContainerData sequencerData = new ContainerData() {
        @Override
        public int get(int pIndex) {
            return DNASequencerBlockEntity.this.sequencingTime[pIndex];
        }

        @Override
        public void set(int pIndex, int pValue) {
            DNASequencerBlockEntity.this.sequencingTime[pIndex] = pValue;
        }

        @Override
        public int getCount() {
            return 3;
        }
    };

    public DNASequencerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DNA_SEQUENCER_BLOCK_ENTITY.get(), pPos, pBlockState);

        Network.ENTITIES.add(this);
        // Propagate inventory updates to clients so items render properly
        this.machineItemStackHandler.setChangeListener(() -> {
            setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        });
    }

    @Override
    public Tag getMachineData() {
        CompoundTag machineData = new CompoundTag();
        machineData.putIntArray("SequencingTime", sequencingTime);


        return machineData;
    }

    @Override
    public void readMachineData(Tag data) {
        CompoundTag machineData = (CompoundTag) data;

        this.sequencingTime = machineData.getIntArray("SequencingTime");
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return new TranslatableComponent("block.jurassicreborn.dna_sequencer");
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new DNASequencerMenu(pContainerId, this.machineItemStackHandler, this.sequencerData, pInventory);
    }

    //see superclass for explanation :D
    public @NotNull List<ItemStack> processItem(ItemStack... inputs){
        NonNullList<ItemStack> outputs = NonNullList.create();
        ItemStack tissue = inputs[0];
        ItemStack disc = inputs[1];

        Random rand = Objects.requireNonNull(this.level).getRandom();

//        this.mergeStack(process + 6, SequencableItem.getSequencableItem(sequencableStack).getSequenceOutput(sequencableStack, rand));
        outputs.add(SequencableItem.getSequencableItem(tissue).getSequenceOutput( tissue, rand ));
//        this.setItem(OUTPUTS[(input+1)/2], output); | future gamma: how the hell did this work, what??? | 20 seconds later future gamma: ohHHH im so stupid
        tissue.shrink(1);
        outputs.add(tissue);//please please be at index 1
//        ItemStack disc = this.getItem(input + 1);
        disc.shrink(1);
//        this.setItem(input + 1, disc);
        outputs.add(disc); //please please be at index 2
//        BlockPos pos = this.pos;
        return outputs;
    }

    //We can assume that
    public boolean canProcess(ItemStack... inputs){
        return (!inputs[0].isEmpty() && inputs[0].getItem() instanceof SequencableItem) && (!inputs[1].isEmpty() && inputs[1].getItem() == ModItems.STORAGE_DISC.get()) && inputs[2].isEmpty();
    }

    /**
     * @param flags list of indices following: [tissue slot index, disc slot index, output slot index]
     * @return ready ItemStack array for passing into Process functions. Should follow: [ItemStack#getItem instanceof SoftTissueItem, Empty StorageDiscItem, ItemStack.EMPTY]
     */
    public ItemStack[] collectInputs(int... flags) {
        return new ItemStack[]{
                this.getItem(flags[0]),
                this.getItem(flags[1]),
                this.getItem(flags[2])
        };
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DNASequencerBlockEntity pBlockEntity) {

        if(pLevel.isClientSide){
            return;
        }
        for (int input = 0; input < DNA_INPUT.length; input++) {
            int time = pBlockEntity.sequencingTime[input];
            int dna = DNA_INPUT[input];

            ItemStack[] processInputs = pBlockEntity.collectInputs(dna, dna + 1, OUTPUTS[input]);


            if(time >= 2000 && pBlockEntity.canProcess(processInputs)){
                var list = pBlockEntity.processItem(processInputs);
                pBlockEntity.mergeStack(OUTPUTS[input], list.get(0) );

                pBlockEntity.setItem(dna, list.get(1));
                pBlockEntity.setItem(dna + 1, list.get(2));
                time = 0;
                pBlockEntity.sequencingTime[input] = time;

                processInputs = pBlockEntity.collectInputs(dna, dna + 1, OUTPUTS[input] );

            }

            if(!pBlockEntity.canProcess(processInputs)){
                pBlockEntity.sequencingTime[input] = 0;
                continue;
            }else{
                time++;
                pBlockEntity.sequencingTime[input] = time;
            }





        }



    }
}
