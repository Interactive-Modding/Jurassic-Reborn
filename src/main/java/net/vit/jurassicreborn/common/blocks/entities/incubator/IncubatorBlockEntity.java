package net.vit.jurassicreborn.common.blocks.entities.incubator;

import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.IncubatorItemHandler;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.DinosaurEggItem;
import net.vit.jurassicreborn.common.util.block.TemperatureControl;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class IncubatorBlockEntity extends MachineBlockEntity implements TemperatureControl, GeoBlockEntity, MenuProvider, ItemHandlerBlockEntity {


    public static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4 };
    public static final int[] ENVIRONMENT = new int[] { 5 };
    private static final int[] HANDLER_INPUTS = IntStream.concat(Arrays.stream(INPUTS), Arrays.stream(ENVIRONMENT)).toArray();
    public static final int[] OUTPUTS = Arrays.copyOf(INPUTS, INPUTS.length);

    public static final int PROCESS_TIME = 4000;

    private int[] temperature = new int[5];

    private int[] eggIncubationTime = new int[5];

    private final IncubatorItemHandler machineItemStackHandler = new IncubatorItemHandler(6, HANDLER_INPUTS, OUTPUTS);

    private boolean menuOpen = false;

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    private int currentProcess = 0;


    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if(pIndex < 5){
                return IncubatorBlockEntity.this.temperature[pIndex];
            }else if(pIndex < 10){
                return IncubatorBlockEntity.this.eggIncubationTime[pIndex-5];
            }else if(pIndex < 13){
                return blockPosToArr(IncubatorBlockEntity.this.getBlockPos())[pIndex-10];
            }
            return -1;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if(pIndex < 5){
                IncubatorBlockEntity.this.temperature[pIndex] = pValue;
            }else if(pIndex < 10){
                IncubatorBlockEntity.this.eggIncubationTime[pIndex - 5] = pValue;
            }
        }

        protected int[] blockPosToArr(BlockPos pos){
            return new int[]{pos.getX(), pos.getY(), pos.getZ()};
        }

        @Override
        public int getCount() {
            return 13;
        }
    };

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    public IncubatorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.INCUBATOR_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Tag getMachineData() {
        CompoundTag data = new CompoundTag();
        ListTag dataList = new ListTag();

        for (int i = 0; i < 5; i++) {
            CompoundTag dataEntry = new CompoundTag();
            dataEntry.putInt("temperature", this.temperature[i]);
            dataEntry.putInt("incubationTime", this.eggIncubationTime[i]);


            dataList.add(dataEntry);
        }
        data.put("SlotDataEntryList", dataList);
        data.putBoolean("menuOpen", this.menuOpen);

        return data;
    }

    @Override
    public void readMachineData(Tag data) {
        CompoundTag machineData = (CompoundTag) data;
        ListTag dataList =  machineData.getList("SlotDataEntryList", 10);
        for (int i = 0; i < 5; i++) {
            CompoundTag dataEntry = dataList.getCompound(i);
            this.temperature[i] = dataEntry.getInt("temperature");
            this.eggIncubationTime[i] = dataEntry.getInt("incubationTime");
        }
        if(machineData.contains("menuOpen")){
            this.menuOpen = machineData.getBoolean("menuOpen");
        }
    }

    @Override
    public void setTemperature(int index, int value) {
        this.temperature[index] = value;
    }

    @Override
    public int getTemperature(int index) {
        return this.temperature[index];
    }

    @Override
    public int getTemperatureCount() {
        return 5;
    }

    public boolean isProcessing(){
        return Arrays.stream(this.eggIncubationTime).anyMatch((i) -> i != 0);
    }




    @Override
        protected @NotNull Component getDefaultName() {
            return Component.translatable("container.incubator");
    }

    @Override
    @NotNull
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        this.setMenuOpen(true);
        return new IncubatorMenu(pContainerId, pInventory, machineItemStackHandler, this.data);
    }

    public void setMenuOpen(boolean open){
        this.menuOpen = open;
        if(this.level != null){
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public boolean isMenuOpen(){
        return menuOpen;
    }

    @NotNull
    public List<ItemStack> processItem(ItemStack... inputs) {
        if (this.canProcess(inputs[0]) && !this.level.isClientSide) {
            ItemStack egg = inputs[0];
            DinosaurEggItem dinoEgg = (DinosaurEggItem)(egg.getItem());
            ItemStack incubatedEgg = new ItemStack(ModItems.hatchedDinoEggs.get(dinoEgg.getDino()).get(), 1);
            CompoundTag compound = new CompoundTag();
            compound.putBoolean("Gender", this.temperature[this.currentProcess] > 50);

            if (egg.getTag() != null) {
                var dna = net.vit.jurassicreborn.common.genetics.DinoDNA.readFromNBT(egg.getTag());
                if (dna != null) {
                    dna.writeToNBT(compound); // writes into "DNA" subtag (DNAQuality, Genetics, Dinosaur, StorageId)
                }
            }

            incubatedEgg.setTag(compound);
            this.decreaseStackSize(ENVIRONMENT[0]);
//            this.setItem(this.currentProcess, incubatedEgg);
            return List.of(incubatedEgg);
        }
        return List.of(ItemStack.EMPTY);

    }

    public boolean canProcess(ItemStack... inputs) {
        ItemStack environment = machineItemStackHandler.getStackInSlot(ENVIRONMENT[0]);
        boolean hasEnvironment = false;

        if (!environment.isEmpty()) {
            Item item = environment.getItem();

            if (IncubatorItemHandler.isEnvironment(5, item)) {
                hasEnvironment = true;
            }
        }

        return hasEnvironment && !inputs[0].isEmpty() && inputs[0].getCount() > 0 && inputs[0].getItem() instanceof DinosaurEggItem;
    }

    public static void tick(@NotNull Level pLevel, @NotNull BlockPos pPos, @NotNull BlockState pState, @NotNull IncubatorBlockEntity pBlockEntity) {
        if(pLevel.isClientSide){
            return;
        }
        for (int input = 0; input < INPUTS.length; input++) {
            pBlockEntity.currentProcess = input;
            int time = pBlockEntity.eggIncubationTime[input];
            int egg = INPUTS[input];

            if(!pBlockEntity.canProcess(pBlockEntity.machineItemStackHandler.getStackInSlot(egg))){
                pBlockEntity.eggIncubationTime[input] = 0;
                continue;
            }else{

                pBlockEntity.eggIncubationTime[input]++;
            }

            if(time >= PROCESS_TIME){
                ItemStack output = pBlockEntity.processItem(pBlockEntity.machineItemStackHandler.getStackInSlot(egg)).get(0);
                if(output.isEmpty()) {
                    pBlockEntity.eggIncubationTime[input] = 0;
                    continue;
                }

                pBlockEntity.machineItemStackHandler.setStackInSlot(egg, output);

                pBlockEntity.eggIncubationTime[input] = 0;
            }
        }
    }

    private void decreaseStackSize(int slot) {
        ItemStack stack = this.machineItemStackHandler.getStackInSlot(slot);
        stack.shrink(1);
        if (stack.getCount() <= 0) {
            this.machineItemStackHandler.setStackInSlot(slot, ItemStack.EMPTY);
        }
    }

    //model stuff
    private static final RawAnimation INACTIVE = RawAnimation.begin().thenLoop("animation.incubator.inactive");
    private static final RawAnimation ACTIVE = RawAnimation.begin().thenLoop("animation.incubator.active");
    private static final RawAnimation TRANSITION_ACTIVE = RawAnimation.begin()
            .thenPlay("animation.incubator.transition_active")
            .thenLoop("animation.incubator.active");
    private static final RawAnimation TRANSITION_INACTIVE = RawAnimation.begin()
            .thenPlay("animation.incubator.transition_inactive")
            .thenLoop("animation.incubator.inactive");

    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);
    private boolean lastMenuState = false;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 16, this::controller));
    }

    private PlayState controller(AnimationState<IncubatorBlockEntity> state) {
        AnimationController<IncubatorBlockEntity> controller = state.getController();

        if (controller.getCurrentAnimation() == null) {
            controller.setAnimation(INACTIVE);
            lastMenuState = menuOpen;
            return PlayState.CONTINUE;
        }

        if (menuOpen != lastMenuState) {
            controller.setAnimation(menuOpen ? TRANSITION_ACTIVE : TRANSITION_INACTIVE);
            lastMenuState = menuOpen;
            return PlayState.CONTINUE;
        }

        if (menuOpen) {
            controller.setAnimation(ACTIVE);
        } else {
            controller.setAnimation(INACTIVE);
        }

        return PlayState.CONTINUE;
    }



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animationCache;
    }


}
