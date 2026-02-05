package net.vit.jurassicreborn.common.blocks.entities.cultivator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import java.util.Random;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.vit.jurassicreborn.common.blocks.inventory.CultivatorItemHandler;
import net.vit.jurassicreborn.common.blocks.inventory.FluidHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.SerializableSingleFluidTank;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.SyringeItem;
import net.vit.jurassicreborn.common.util.block.TemperatureControl;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CultivatorBlockEntity extends MachineBlockEntity implements MenuProvider, TemperatureControl,
        ItemHandlerBlockEntity, FluidHandlerBlockEntity {

    public static final int[] INPUTS  = new int[]{0, 1, 2, 3};
    public static final int[] OUTPUTS = new int[] {0, 3};
    public static final int MAX_NUTRIENTS = 3000;
    public static final int STACK_PROCESS_TIME = 2000;

    private final CultivatorItemHandler machineItemStackHandler = new CultivatorItemHandler(4, INPUTS, OUTPUTS);
    private final SerializableSingleFluidTank tank = new SerializableSingleFluidTank(2000, fs -> fs.getFluid() == Fluids.WATER);

    private int lipids;
    private int proximates;
    private int minerals;
    private int vitamins;
    private int temperature;
    private int processTime;

    private DinosaurEntity dinosaurEntity; // cached render entity

    static { FoodNutrients.register(); }



    public CultivatorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CULTIVATOR_BLOCK_ENTITY_TYPE.get(), pos, state);
        this.machineItemStackHandler.setChangeListener(this::pushSync);
    }
    @Override public IFluidHandler getFluidHandler() { return tank; }
    @Override public IItemHandlerModifiable getItemHandler() { return machineItemStackHandler; }

    private final ContainerData cultivatorBlockData = new ContainerData() {
        @Override public int get(int i) {
            return switch (i) {
                case 0 -> CultivatorBlockEntity.this.tank.getFluid().getAmount();
                case 1 -> CultivatorBlockEntity.this.lipids;
                case 2 -> CultivatorBlockEntity.this.proximates;
                case 3 -> CultivatorBlockEntity.this.minerals;
                case 4 -> CultivatorBlockEntity.this.vitamins;
                case 5 -> CultivatorBlockEntity.this.temperature;
                case 6 -> CultivatorBlockEntity.this.processTime;
                case 7 -> CultivatorBlockEntity.this.worldPosition.getX();
                case 8 -> CultivatorBlockEntity.this.worldPosition.getY();
                case 9 -> CultivatorBlockEntity.this.worldPosition.getZ();
                default -> 0;
            };
        }
        @Override public void set(int i, int v) {
            switch (i) {
                case 0 -> CultivatorBlockEntity.this.tank.getFluid().setAmount(v);
                case 1 -> CultivatorBlockEntity.this.lipids = v;
                case 2 -> CultivatorBlockEntity.this.proximates = v;
                case 3 -> CultivatorBlockEntity.this.minerals = v;
                case 4 -> CultivatorBlockEntity.this.vitamins = v;
                case 5 -> CultivatorBlockEntity.this.temperature = v;
                case 6 -> CultivatorBlockEntity.this.processTime = v;
            }
            pushSync();
        }
        public BlockPos getBlockPos(){ return new BlockPos(this.get(7), this.get(8), this.get(9)); }
        @Override public int getCount() { return 10; }
    };

    // ---- networking helpers ----
    private void pushSync() {
        if (this.level != null && !this.level.isClientSide) {
            this.setChanged();
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override public CompoundTag getUpdateTag() { return this.saveWithoutMetadata(); }
    @Override public void handleUpdateTag(CompoundTag tag){
        super.handleUpdateTag(tag);
        if(tag.contains("MachineData", Tag.TAG_COMPOUND)){
            CompoundTag machineData = tag.getCompound("MachineData");
            if(machineData.contains("Data", Tag.TAG_COMPOUND)) readMachineData(machineData.getCompound("Data"));
        }        if(this.level != null && this.level.isClientSide){
            if(this.processTime > 0) this.getRenderEntity();
            else this.dinosaurEntity = null;
        }
    }
    @Override public ClientboundBlockEntityDataPacket getUpdatePacket(){ return ClientboundBlockEntityDataPacket.create(this); }
    @Override public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt){ handleUpdateTag(pkt.getTag()); }
    // ----------------------------

    @Override
    public Tag getMachineData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Lipids", lipids);
        tag.putInt("Minerals", minerals);
        tag.putInt("Vitamins", vitamins);
        tag.putInt("Proximates", proximates);
        tag.putInt("Temperature", temperature);
        tag.putInt("ProcessTime", processTime);
        return tag;
    }

    @Override
    public void readMachineData(Tag data) {
        if (!(data instanceof CompoundTag tag)) return;
        lipids      = tag.getInt("Lipids");
        proximates  = tag.getInt("Proximates");
        minerals    = tag.getInt("Minerals");
        vitamins    = tag.getInt("Vitamins");
        temperature = tag.getInt("Temperature");
        processTime = tag.getInt("ProcessTime");
        if(this.processTime == 0) this.dinosaurEntity = null;
        this.setChanged();
    }

    @Override
    public boolean canProcess(ItemStack... inputs) {
        // slot 0 must contain a valid syringe
        ItemStack syringe = inputs[0];
        if (syringe.isEmpty() || !(syringe.getItem() instanceof SyringeItem syringeItem)) {
            return false;
        }
        // need at least one bucket of water
        if (this.tank.getFluidAmount() < FluidAttributes.BUCKET_VOLUME) {
            return false;
        }

        // must resolve to a dinosaur
        Dinosaur dino = syringeItem.getDinosaur(syringe);
        if (dino == null) {
            return false;
        }

        return this.lipids     >= dino.getLipids()
                && this.minerals   >= dino.getMinerals()
                && this.proximates >= dino.getProximates()
                && this.vitamins   >= dino.getVitamins();
    }

    @Override
    public @NotNull List<ItemStack> processItem(ItemStack... inputs) {
        ItemStack syringe = inputs[0];
        if (!(syringe.getItem() instanceof SyringeItem syringeItem)) return List.of(ItemStack.EMPTY);
        Dinosaur dino = syringeItem.getDinosaur(syringe);
        if (dino == null) return List.of(ItemStack.EMPTY);

        ItemStack hatchedEgg = ModItems.hatchedDinoEggs.get(dino).get().getDefaultInstance();

        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("Gender", this.temperature > 50);

        DinoDNA dna = DinoDNA.fromStack(syringe);
        if (dna == null) dna = new DinoDNA(dino, 100, GeneticsHelper.randomGenetics(this.level.getRandom()));
        dna.writeToNBT(nbt);

        hatchedEgg.setTag(nbt);
        decrementResources(dino);

        return List.of(hatchedEgg);
    }

    private void decrementResources(Dinosaur dino) {
        lipids     -= dino.getLipids();
        minerals   -= dino.getMinerals();
        vitamins   -= dino.getVitamins();
        proximates -= dino.getProximates();
        tank.getFluid().shrink(FluidAttributes.BUCKET_VOLUME); // consume 1000 mB
        pushSync();
    }

    @Override protected @NotNull Component getDefaultName() { return new TranslatableComponent("container.cultivator"); }
    @Override public Component getDisplayName() { return super.getDisplayName(); }
    @Override public @NotNull AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, Player player) { return new CultivatorMenu(id, machineItemStackHandler, this.cultivatorBlockData, inv); }

    public ItemStack[] collectInputs(int... flags) { return new ItemStack[]{ getItem(0), getItem(1), getItem(2), getItem(3) }; }

    public static void tick(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull CultivatorBlockEntity be) {
        if (world.isClientSide) return;

        // consume food items before processing so nutrient usage is visible
        ItemStack foodItem = be.getItem(1);
        if (!foodItem.isEmpty() && (be.proximates < MAX_NUTRIENTS || be.minerals < MAX_NUTRIENTS || be.vitamins < MAX_NUTRIENTS || be.lipids < MAX_NUTRIENTS)) {
            be.consumeNutrients();
        }

        ItemStack[] inputs = be.collectInputs();

        // finish
        if (be.processTime >= STACK_PROCESS_TIME && be.canProcess(inputs)) {
            ItemStack output = be.processItem(inputs).get(0);
            be.setItem(0, output);
            be.processTime = 0;
            be.dinosaurEntity = null;
            be.pushSync();
            inputs = be.collectInputs();
        }

        // progress
        if (be.canProcess(inputs)) {
            if (be.processTime == 0) {
                be.getRenderEntity();
            }
            be.processTime++;

            if (be.processTime == 1 || (be.processTime % 20) == 0) {
                be.pushSync();
            }
        } else if (be.processTime != 0) {
            be.processTime = 0;
            be.dinosaurEntity = null;
            be.pushSync(); // stopped
        }
        // auto-fill water via slot 2; empty buckets -> slot 3
        if (be.tank.getFluid().getAmount() < be.tank.getCapacity()
                && be.getItem(2).getItem() == Items.WATER_BUCKET
                && (be.getItem(3).getCount() < be.getItem(3).getMaxStackSize() || be.getItem(3).isEmpty())) {

            int filled = be.tank.fill(new FluidStack(Fluids.WATER, FluidAttributes.BUCKET_VOLUME), IFluidHandler.FluidAction.EXECUTE);
            if (filled > 0) {
                ItemStack bucket = be.getItem(2);
                bucket.shrink(1);
                be.setItem(2, bucket.isEmpty() ? ItemStack.EMPTY : bucket);

                if (be.getItem(3).isEmpty()) be.setItem(3, Items.BUCKET.getDefaultInstance());
                else be.getItem(3).grow(1);

                be.pushSync();
            }
        }
    }

    private void consumeNutrients() {
        if (this.level == null) return;
        ItemStack foodStack = getItem(1);
        FoodNutrients nutrients = FoodNutrients.get(foodStack.getItem());
        if (nutrients == null) return;

        if (foodStack.getItem() instanceof MilkBucketItem) setItem(1, new ItemStack(Items.BUCKET));
        else foodStack.shrink(1);

        Random random = this.level.getRandom();
        if (proximates < MAX_NUTRIENTS) proximates = Math.min((int)(proximates + (800 + random.nextInt(201)) * nutrients.getProximate()), MAX_NUTRIENTS);
        if (minerals   < MAX_NUTRIENTS) minerals   = Math.min((int)(minerals   + (900 + random.nextInt(101)) * nutrients.getMinerals()),   MAX_NUTRIENTS);
        if (vitamins   < MAX_NUTRIENTS) vitamins   = Math.min((int)(vitamins   + (900 + random.nextInt(101)) * nutrients.getVitamins()),   MAX_NUTRIENTS);
        if (lipids     < MAX_NUTRIENTS) lipids     = Math.min((int)(lipids     + (980 + random.nextInt(101)) * nutrients.getLipids()),     MAX_NUTRIENTS);

        pushSync();
    }

    @Nullable
    private DinosaurEntity createEntity() {
        if (!(getItem(0).getItem() instanceof SyringeItem item) || (this.level == null)) return null;

        this.dinosaurEntity = DinosaurEntity.CLASS_TYPE_LIST
                .get(item.getDinosaur(getItem(0)).getDinosaurClass())
                .get().create(this.level);

        if (dinosaurEntity == null) return null;

        dinosaurEntity.setMale(this.temperature > 50);
        dinosaurEntity.setFullyGrown();
        dinosaurEntity.getLegacyAttributes().setScaleModifier(1f);
        return dinosaurEntity;
    }


    @Nullable
    public DinosaurEntity getRenderEntity() {
        if (this.dinosaurEntity == null) {
            if (this.level != null && (!this.level.isClientSide || this.processTime > 0)) {
                this.dinosaurEntity = createEntity();
            }
        }
        return this.dinosaurEntity;
    }
    public boolean isProcessing(){ return this.processTime > 0; }
    public int getProcessTime(){ return this.processTime; }
    public int getMaxNutrients(){ return MAX_NUTRIENTS; }
    public int getProximates(){ return this.proximates; }
    public int getMinerals(){ return this.minerals; }
    public int getVitamins(){ return this.vitamins; }
    public int getLipids(){ return this.lipids; }

    // TemperatureControl
    @Override public void setTemperature(int index, int value){ if(index==0){ this.temperature = value; pushSync(); } }
    @Override public int getTemperature(int index){ return index==0 ? this.temperature : -1; }
    @Override public int getTemperatureCount(){ return 1; }

    // Nutrient table
    public static class FoodNutrients {
        public static final Map<Item, FoodNutrients> NUTRIENTS = new HashMap<>();
        private final double proximate, minerals, vitamins, lipids;
        private final Item food;

        public FoodNutrients(Item food, double prox, double min, double vit, double lip) { this.food = food; this.proximate = prox; this.minerals = min; this.vitamins = vit; this.lipids = lip; }
        public static void register() {
            if (!NUTRIENTS.isEmpty()) return; // idempotent
            register(Items.APPLE, 0.060, 0.065, 0.100, 0.010);
            register(Items.POTATO, 0.100, 0.200, 0.160, 0.020);
            register(Items.BREAD, 0.300, 0.400, 0.430, 0.180);
            register(Items.CHICKEN, 0.390, 0.350, 0.280, 0.450);
            register(Items.COOKED_CHICKEN, 0.490, 0.425, 0.335, 0.555);
            register(Items.PORKCHOP, 0.460, 0.310, 0.390, 0.380);
            register(Items.COOKED_PORKCHOP, 0.580, 0.390, 0.490, 0.470);
            register(Items.BEEF, 0.460, 0.310, 0.390, 0.380);
            register(Items.BEEF, 0.460, 0.310, 0.390, 0.380);
            register(Items.COOKED_BEEF, 0.520, 0.330, 0.410, 0.400);
            register(Items.COD, 0.480, 0.430, 0.140, 0.240);
            register(Items.COOKED_COD, 0.500, 0.450, 0.200, 0.280);
            register(Items.SALMON, 0.480, 0.430, 0.140, 0.240);
            register(Items.COOKED_SALMON, 0.500, 0.450, 0.200, 0.280);
            register(Items.TROPICAL_FISH, 0.480, 0.430, 0.140, 0.240);
            register(Items.MILK_BUCKET, 0.180, 0.260, 0.220, 0.600);
            register(Items.EGG, 0.050, 0.030, 0.050, 0.250);
            register(Items.CARROT, 0.070, 0.170, 0.350, 0.010);
            register(Items.SUGAR, 0.200, 0.010, 0.010, 0.010);
            register(Items.MELON, 0.060, 0.060, 0.060, 0.010);
            register(Items.WHEAT, 0.100, 0.220, 0.100, 0.030);
            register(Items.MUTTON, 0.460, 0.310, 0.390, 0.380);
            register(Items.COOKED_MUTTON, 0.580, 0.390, 0.490, 0.470);
            register(Items.RABBIT, 0.460, 0.310, 0.390, 0.380);
            register(Items.COOKED_RABBIT, 0.580, 0.390, 0.490, 0.470);
            register(ModItems.GOAT_RAW.get(), 0.460, 0.310, 0.390, 0.380);
            register(ModItems.GOAT_COOKED.get(), 0.580, 0.390, 0.490, 0.470);
            register(ModItems.CRAB_MEAT_RAW.get(), 0.260, 0.150, 0.290, 0.280);
            register(ModItems.CRAB_MEAT_COOKED.get(), 0.480, 0.230, 0.390, 0.370);
            register(ModItems.SHARK_MEAT_RAW.get(), 0.460, 0.310, 0.390, 0.380);
            register(ModItems.SHARK_MEAT_COOKED.get(), 0.580, 0.390, 0.490, 0.470);
            ModItems.MEATS.forEach((dino, item) -> register(item.get(), 0.460, 0.310, 0.390, 0.380));
            ModItems.STEAKS.forEach((dino, item) -> register(item.get(), 0.580, 0.390, 0.490, 0.470));
        }
        public static void register(Item item, double prox, double min, double vit, double lip) {
            NUTRIENTS.put(item, new FoodNutrients(item, prox, min, vit, lip));
        }
        public static FoodNutrients get(Item item) { return NUTRIENTS.get(item); }
        public double getProximate(){ return proximate; } public double getMinerals(){ return minerals; }
        public double getVitamins(){ return vitamins; } public double getLipids(){ return lipids; }
    }
}