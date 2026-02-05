package net.vit.jurassicreborn.common.blocks.entities.cleaner;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.CleanerItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.FluidHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.SerializableSingleFluidTank;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.recipes.FluidAndItemRecipeWrapper;
import net.vit.jurassicreborn.common.recipes.cleaner.CleaningRecipe;
import net.vit.jurassicreborn.common.util.api.CleanableItem;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;

public class CleanerBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity,FluidHandlerBlockEntity {

    public static final int SLOTS = 8;
    public static final int[] INPUTS = new int[]{0, 1};
    public static final int[] OUTPUTS = new int[]{2, 3, 4, 5, 6, 7};
    @Nullable
    CleaningRecipe currentRecipe;
    private boolean usingCleaningRecipe = true;
    private int progress = 0;

    protected final MachineItemStackHandler machineItemStackHandler = new CleanerItemHandler(SLOTS, INPUTS, OUTPUTS);

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    protected final SerializableSingleFluidTank tank = new SerializableSingleFluidTank(2000,fluidStack -> fluidStack.getFluid() == Fluids.WATER);

    @Override
    public IFluidHandler getFluidHandler() {
        return tank;
    }

    public final ContainerData dataAccess = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if(pIndex == 0){
                return CleanerBlockEntity.this.tank.getFluidAmount();
            }else if(pIndex == 1){
                return CleanerBlockEntity.this.getProgress();
            }else{
                return 0;
            }
        }

        @Override
        public void set(int pIndex, int pValue) {
            if(pIndex == 0){
                tank.getFluid().setAmount(pValue);
            }else if(pIndex == 1){
                CleanerBlockEntity.this.progress = pValue;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    };

    public CleanerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CLEANING_STATION.get(), pos, state);
        Network.ENTITIES.add(this);
        this.machineItemStackHandler.setChangeListener(() -> {
            setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        });
    }

    public static void tick(Level world, BlockPos pos, BlockState state, CleanerBlockEntity instance) {
        ItemStack input = instance.getItemHandler().getStackInSlot(0);

        CleanableItem cleanable = CleanableItem.getCleanableItem(input);

        if(instance.progress >= 200) {
            if (instance.currentRecipe != null) {
                instance.addItem(instance.currentRecipe.assemble(new FluidAndItemRecipeWrapper(instance.machineItemStackHandler,instance.tank)));
                instance.currentRecipe = null;
                input.shrink(1);
            } else if (cleanable != null) {
                instance.addItem(cleanable.getCleanedItem(input, world.getRandom()));
                input.shrink(1);
            }
            instance.progress = 0;
        }

        if (instance.isCleaning()) {
            instance.progress++;
            instance.tank.getFluid().setAmount(instance.tank.getFluidAmount() - 1 );
        }

        // Reset recipe usage flag
        instance.usingCleaningRecipe = cleanable == null;

        if (input.isEmpty()) {
            instance.progress = 0;
        }

        if(instance.currentRecipe == null && instance.usingCleaningRecipe) {
            for (CleaningRecipe recipe : world.getRecipeManager().getAllRecipesFor(CleaningRecipe.CLEANING)) {
                if (recipe.matches(new FluidAndItemRecipeWrapper(instance.machineItemStackHandler,instance.tank), world) && instance.hasSpace()) {
                    instance.currentRecipe = recipe;
                    instance.progress = 0;
                    break;
                }
            }
        }

        if(instance.tank.getFluidAmount() <= 0 && instance.machineItemStackHandler.getStackInSlot(1).is(Items.WATER_BUCKET)){
            instance.tank.setFluid(new FluidStack(Fluids.WATER, 1000));
            ItemStack emptyBucket = Items.BUCKET.getDefaultInstance();
            if(instance.addItem(emptyBucket.copy())) {
                instance.machineItemStackHandler.setStackInSlot(1, ItemStack.EMPTY);
            } else {
                instance.machineItemStackHandler.setStackInSlot(1, emptyBucket);
            }
        }
    }

    public boolean isCleaning(){
        ItemStack input = machineItemStackHandler.getStackInSlot(0);
        return ((CleanableItem.getCleanableItem(input) != null) && hasSpace() && tank.getFluid().getAmount() > 0) || currentRecipe != null;
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    public boolean hasSpace() {
        for(var i = 2; i < SLOTS; i++) {
            if (machineItemStackHandler.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public int getProgress(){
        return this.progress;
    }

    public boolean addItem(ItemStack stack){
        for(var i = 2; i < SLOTS; i++){
            if(machineItemStackHandler.getStackInSlot(i).isEmpty()){
                machineItemStackHandler.setStackInSlot(i, stack);
                return true;
            }
        }
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory,Player player) {
        return new CleanerMenu(i, inventory, this.machineItemStackHandler, this.dataAccess);
    }

    @Override
    protected Component getDefaultName() {
        return new TranslatableComponent("block.JurassicReborn.cleaner_block_name");
    }

    @Override
    public Tag getMachineData() {
        CompoundTag pTag = new CompoundTag();
        pTag.putInt("Progress", this.progress);
        return pTag;
    }

    @Override
    public void readMachineData(Tag machineData) {
        if(machineData instanceof CompoundTag pTag) {
            this.progress = pTag.getInt("Progress");
        }
    }

    @Override
    public boolean canProcess(ItemStack... inputs) {
        return false;
    }

    @Override
    public @NotNull List<ItemStack> processItem(ItemStack... inputs) {
        return null;
    }
}