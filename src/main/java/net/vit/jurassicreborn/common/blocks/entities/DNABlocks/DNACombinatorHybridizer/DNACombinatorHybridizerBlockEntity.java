package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNACombinatorHybridizer;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.DNACombinatorHybridizerItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;
import net.vit.jurassicreborn.common.network.Network;
import net.minecraft.core.BlockPos;
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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class DNACombinatorHybridizerBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity {
    public static final int SLOTS = 12;
    public static final int[] HYBRIDIZER_INPUTS = new int[]{0, 1, 2, 3, 4, 5, 6, 7};
    public static final int[] COMBINATOR_INPUTS = new int[]{8, 9};
    public static final int[] HYBRIDIZER_OUTPUTS = new int[]{10};
    public static final int[] COMBINATOR_OUTPUTS = new int[]{11};

    public int processTime;

    protected final DNACombinatorHybridizerItemHandler machineItemStackHandler = new DNACombinatorHybridizerItemHandler(SLOTS,new int[]{0,1,2,3,4,5,6,7,8,9},new int[]{10,11});

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if (pIndex >= 2) {
                if (pIndex == 2) {
                    return DNACombinatorHybridizerBlockEntity.this.getMode() ? 1 : 0;
                } else if (pIndex == 3) {
                    return DNACombinatorHybridizerBlockEntity.this.getBlockPos().getX();
                } else if (pIndex == 4) {
                    return DNACombinatorHybridizerBlockEntity.this.getBlockPos().getY();
                } else if (pIndex == 5) {
                    return DNACombinatorHybridizerBlockEntity.this.getBlockPos().getZ();
                }
                return 0;
            } else if (pIndex == 0) {
                return DNACombinatorHybridizerBlockEntity.this.processTime;
            } else if (pIndex == 1) {
                return DNACombinatorHybridizerBlockEntity.this.getTotalProcessTime();
            }
            return 0;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if (pIndex >= 2) {
                if (pIndex == 2 && pValue < 2 && pValue > -1) {
                    DNACombinatorHybridizerBlockEntity.this.setMode(pValue == 1);
                }
            }
        }

        @Override
        public int getCount() {
            return 6;
        }
    };


    public DNACombinatorHybridizerBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DNA_COMBINATOR_HYBRIDIZER.get(), pPos, pBlockState);
//        this.getMode() = pBlockState.getValue(DNACombinatorHybridizerBlock.MODE);
    }

    protected int getProcess(int slot) {
        return 0;
    }

    public boolean isProcessing() {
        return this.processTime > 0;
    }

    private Dinosaur getHybrid() {
        //0-7;
        ItemStack[] stacks = IntStream.range(0,8).mapToObj(this::getItem).toArray(ItemStack[]::new);
        return this.getHybrid(stacks);
    }

    private Dinosaur getHybrid(ItemStack... discs) {
        Dinosaur hybrid = null;

        Dinosaur[] dinosaurs = new Dinosaur[discs.length];

        for (int i = 0; i < dinosaurs.length; i++) {
            dinosaurs[i] = this.getDino(discs[i]);
        }

        for (Dinosaur dino : Dinosaur.DINOS) {
            if (dino instanceof Hybrid) {
                Hybrid dinoHybrid = (Hybrid) dino;

                int count = 0;
                boolean extra = false;

                List<Class<? extends Dinosaur>> usedGenes = new ArrayList<>();

                for (Dinosaur discDinosaur : dinosaurs) {
                    Class match = null;

                    for (Class clazz : dinoHybrid.getDinosaurs()) {
                        if (clazz.isInstance(discDinosaur) && !usedGenes.contains(clazz)) {
                            match = clazz;
                        }
                    }

                    if (match != null && match.isInstance(discDinosaur)) {

                        usedGenes.add(match);
                        count++;
                    } else if (discDinosaur != null) {
                        extra = true;
                    }
                }

                if (!extra && count == dinoHybrid.getDinosaurs().length) {
                    hybrid = dino;

                    break;
                }
            }
        }
        return hybrid;
    }

    private Dinosaur getDino(ItemStack disc) {
        if (!disc.isEmpty() && disc.hasTag()) {
            DinoDNA data = DinoDNA.readFromNBT(disc.getTag());

            if (data == null) {
                return Dinosaur.EMPTY;
            }

            return data.getDNAQuality() == 100 ? data.getDinosaur() : null;
        } else {
            return null;
        }
    }


    //this should be made to be complient with the docs listed in the superclass at SOME POINT:tm: but im not doing that rn
    public boolean canProcess(ItemStack... inputs) {
        if (this.getMode()) {
            return this.getItem(10).isEmpty() && this.getHybrid() != null;
        } else {
            final ItemStack left = this.getItem(8);
            final ItemStack right = this.getItem(9);

            if (!left.isEmpty() && left.getItem() == ModItems.STORAGE_DISC.get() && !right.isEmpty() && right.getItem() == ModItems.STORAGE_DISC.get()) {
                if (left.getTag() != null && right.getTag() != null && this.getItem(11).isEmpty()) {
                    //this is causing issues! I changed how DNA storage works so that it's in it's own DNA tag!
                    final String leftID = left.getTag().getCompound("DNA").getString("StorageId");
                    final String rightID = right.getTag().getCompound("DNA").getString("StorageId");
                    if(!leftID.equals(rightID))
                        return false;

                    if (leftID.equals("DinoDNA")) {
                        DinoDNA dna1 = DinoDNA.readFromNBT(left.getTag());
                        DinoDNA dna2 = DinoDNA.readFromNBT(right.getTag());
                        if (dna1 == null || dna2 == null) {
                            return false;
                        }

                        return dna1.getDinosaur() == dna2.getDinosaur();

                    } else if (leftID.equals("PlantDNA")) {
                        PlantDNA dna1 = PlantDNA.readFromNBT(left.getTag());
                        PlantDNA dna2 = PlantDNA.readFromNBT(right.getTag());
                        return dna1.getPlant().equals(dna2.getPlant());
                    }
                    return false;
                }
            }

            return false;
        }
    }

    public @NotNull List<ItemStack> processItem(ItemStack... inputs) {
        if(this.level == null)
            return List.of(ItemStack.EMPTY);

        if (this.canProcess()) {
            if (this.getMode()) {
                Dinosaur hybrid = this.getHybrid();

                CompoundTag nbt = new CompoundTag();
                DinoDNA dna = new DinoDNA(getHybrid(), 100, GeneticsHelper.randomGenetics(level.random));

                DinoDNA firstDNA = DinoDNA.readFromNBT(this.getItem(0).getTag());

                if(firstDNA != null)
                    dna = new DinoDNA(hybrid, 100, firstDNA.getGenetics());

                dna.writeToNBT(nbt);

                ItemStack output = new ItemStack(ModItems.STORAGE_DISC.get());
                output.setTag(nbt);
                StorageDiscItem.applyCustomModelData(output);

                this.mergeStack(this.getOutputSlot(output), output);
            } else {
                ItemStack output = new ItemStack(ModItems.STORAGE_DISC.get());

                String storageId = this.getItem(8).getOrCreateTag().getCompound("DNA").getString("StorageId");

                if (storageId.equals("DinoDNA")) {
                    DinoDNA dna1 = DinoDNA.readFromNBT(this.getItem(8).getTag());
                    DinoDNA dna2 = DinoDNA.readFromNBT(this.getItem(9).getTag());

                    if(dna1 == null || dna2 == null)//this shouldn't happen but the game shouldn't crash if it does
                        return List.of(ItemStack.EMPTY);

                    int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                    if (newQuality > 100) {
                        newQuality = 100;
                    }

                    DinoDNA newDNA = new DinoDNA(dna1.getDinosaur(), newQuality, dna1.getGenetics());

                    CompoundTag outputTag = new CompoundTag();
                    newDNA.writeToNBT(outputTag);
                    output.setTag(outputTag);
                    StorageDiscItem.applyCustomModelData(output);

                } else if (storageId.equals("PlantDNA")) {
                    PlantDNA dna1 = PlantDNA.readFromNBT(this.getItem(8).getTag());
                    PlantDNA dna2 = PlantDNA.readFromNBT(this.getItem(9).getTag());

                    if(dna1 == null || dna2 == null)//this shouldn't happen but the game shouldn't crash if it does
                        return List.of(ItemStack.EMPTY);

                    int newQuality = dna1.getDNAQuality() + dna2.getDNAQuality();

                    if (newQuality > 100) {
                        newQuality = 100;
                    }

                    PlantDNA newDNA = new PlantDNA(dna1.getPlant(), newQuality);

                    CompoundTag outputTag = new CompoundTag();
                    newDNA.writeToNBT(outputTag);
                    output.setTag(outputTag);
                    StorageDiscItem.applyCustomModelData(output);
                }

                this.mergeStack(11, output);

                this.decreaseStackSize(8);
                this.decreaseStackSize(9);
            }
        }
        return List.of(ItemStack.EMPTY);
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
        var stack = this.getItem(slot);
        stack.shrink(1);
        this.setItem(slot, stack);
    }


    public int getTotalProcessTime() {
        return 1000;
    }



    protected int[] getInputs() {
        return this.getMode() ? HYBRIDIZER_INPUTS : COMBINATOR_INPUTS;
    }

    protected int[] getOutputs() {
        return this.getMode() ? HYBRIDIZER_OUTPUTS : COMBINATOR_OUTPUTS;
    }

//    protected void setInventory(NonNullList<ItemStack> inventory) {
//        this.inventory = inventory;
//    }

//    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
//        return new DNACombinatorHybridizerContainer(playerInventory, this);
//    }


//    public String getGuiID() {
//        return JurassicReborn.MODID + ":dna_comb_hybrid";
//    }





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

    public boolean getMode() {
        if (this.getLevel() == null) {
            return false;
        }

        BlockState state = this.getLevel().getBlockState(this.getBlockPos());
        return state.hasProperty(DNACombinatorHybridizerBlock.MODE) &&
                state.getValue(DNACombinatorHybridizerBlock.MODE);
    }

    public void setMode(boolean mode) {

        if(this.getLevel() == null)
            return;

        Network.switchHybridizerCombinerMode(mode, this.getBlockPos(), this.getLevel().dimension());
        this.processTime = 0;

        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState().setValue(DNACombinatorHybridizerBlock.MODE, mode), 0);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return this.hasCustomName() ? this.getName() : new TranslatableComponent(this.getMode() ? "container.dna_hybridizer" : "container.dna_combinator");
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return new TranslatableComponent(this.getMode() ? "container.dna_hybridizer" : "container.dna_combinator");
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new DNACombinatorHybridizerMenu(pContainerId, pInventory, this.machineItemStackHandler, this.data);
    }

    public int getOutputSlot(ItemStack output) {
        int[] outputs = this.getOutputs();
        for (int slot : outputs) {
            ItemStack stack = getItem(slot);
            //if the slot is empty or contains a stack that can combine with ours, return the slot
            if (stack.isEmpty() || ((ItemStack.isSame(stack, output) && stack.getCount() + output.getCount() <= stack.getMaxStackSize()))) {
                return slot;
            }
        }
        return -1;
    }

//    @Override
//    public NonNullList<Integer> getSyncFields(NonNullList fields) {
//        NonNullList<Integer> actualList;
//        if (!fields.isEmpty() && !(fields.get(0) instanceof Integer) || fields.isEmpty()) {
//            actualList = NonNullList.of(0);
//        } else {
//            actualList = fields;
//        }
//        actualList.add(processTime);
//        actualList.add(this.getMode() ? 1 : 0);
//        return null;
//    }
//
//    @Override
//    public void packetDataHandler(ByteBuf fields) {
//
//    }

    public static void tick(Level level, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull DNACombinatorHybridizerBlockEntity pBlockEntity) {

        if (level.isClientSide)
            return;

        boolean flag = pBlockEntity.isProcessing();
        boolean dirty = false;

        boolean hasInput = false;

        for (int input : pBlockEntity.getInputs()) {
            if (SLOTS > input && !pBlockEntity.getItem(input).isEmpty()) {//added catch for index out of bounds
                hasInput = true;
                break;
            }
        }

        if (hasInput && pBlockEntity.canProcess()) {
                pBlockEntity.processTime++;

            if (pBlockEntity.processTime >= pBlockEntity.getTotalProcessTime()) {
                pBlockEntity.processItem();

                pBlockEntity.processTime = 0;


            }

            dirty = true;
        } else if (pBlockEntity.isProcessing()) {
            if (pBlockEntity.shouldResetProgress()) {
                pBlockEntity.processTime = 0;
            } else if (pBlockEntity.processTime > 0) {
                pBlockEntity.processTime--;
            }

            dirty = true;
        }

        if (flag != pBlockEntity.isProcessing()) {
            dirty = true;
        }

        if (dirty)
            pBlockEntity.setChanged();
    }

    protected boolean shouldResetProgress() {
        return true;
    }
}
