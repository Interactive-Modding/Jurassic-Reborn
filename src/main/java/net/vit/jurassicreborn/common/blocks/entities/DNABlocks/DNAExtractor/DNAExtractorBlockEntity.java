package net.vit.jurassicreborn.common.blocks.entities.DNABlocks.DNAExtractor;

import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.entities.MachineItemStackHandler;
import net.vit.jurassicreborn.common.blocks.entities.grinder.FossilGrinderBlockEntity;
import net.vit.jurassicreborn.common.blocks.inventory.DNAExtractorHandler;
import net.vit.jurassicreborn.common.blocks.inventory.ItemHandlerBlockEntity;
import net.vit.jurassicreborn.common.blocks.entities.MachineBlockEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.items.Food.DinosaurMeatItem;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.genetics.StorageDiscItem;
import net.vit.jurassicreborn.common.network.Network;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import java.util.Random;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.common.util.api.DinosaurItem;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities.DNA_EXTRACTOR_BLOCK_ENTITY;

public class DNAExtractorBlockEntity extends MachineBlockEntity implements MenuProvider,ItemHandlerBlockEntity, IAnimatable {

    public static final int SLOTS = 6;
    public static final int[] INPUTS = new int[]{0, 1};
    public static final int[] OUTPUTS = new int[]{2, 3, 4, 5};
    public static final int PROCESS_TIME = 2000;

    protected final MachineItemStackHandler machineItemStackHandler = new DNAExtractorHandler(SLOTS,INPUTS,OUTPUTS);

    @Override
    public IItemHandlerModifiable getItemHandler() {
        return machineItemStackHandler;
    }

    public final ContainerData data = new ContainerData() {
        @Override
        public int get(int pIndex) {
            if(pIndex == 0){
                return DNAExtractorBlockEntity.this.extractionTime;
            }
            return -1;
        }

        @Override
        public void set(int pIndex, int pValue) {
            if(pIndex == 0){
                DNAExtractorBlockEntity.this.extractionTime = pValue;
            }
        }

        @Override
        public int getCount() {
            return 1;
        }
    };

    private int extractionTime = 0;

    public DNAExtractorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(DNA_EXTRACTOR_BLOCK_ENTITY.get(), pPos, pBlockState);
        // Sync inventory changes so the renderer can display items
        this.machineItemStackHandler.setChangeListener(() -> {
            setChanged();
            if (this.level != null) {
                this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
            }
        });
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return new TranslatableComponent("block.jurassicreborn.dna_extractor");
    }

    @Override
    public Component getDisplayName() {
        return super.getDisplayName();
    }

    @Override
    public @NotNull AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pInventory, Player player) {
        return new DNAExtractorMenu(pContainerId, this.machineItemStackHandler, this.data, pInventory);
    }

    //this should be made to be complient with the docs listed in the superclass at another time but not rn

    public boolean canProcess(ItemStack... inputs){
        return !this.getItem(0).isEmpty() && !this.getItem(1).isEmpty() && this.hasSpace();
    }

    public boolean hasSpace(){
        for(int i : OUTPUTS){
            if(this.getItem(i).isEmpty())
                return true;
        }
        return false;
    }

    public static void tick(Level pLevel, BlockPos pPos, BlockState pState, DNAExtractorBlockEntity pBlockEntity) {
        if(pLevel.isClientSide){
            return;
        }
//        for (int input = 0; input < INPUTS.length; input++) {


        if(!pBlockEntity.canProcess(pBlockEntity.machineItemStackHandler.getStackInSlot(0))){
            pBlockEntity.extractionTime = 0;
            return;
        }else{
            pBlockEntity.extractionTime++;
        }

        if(pBlockEntity.extractionTime >= PROCESS_TIME){
            pBlockEntity.processItem(pBlockEntity.machineItemStackHandler.getStackInSlot(0));
            pBlockEntity.extractionTime = 0;
        }
    }
    private static DinoDNA readDNAFromItem(ItemStack src, Dinosaur fallbackDino, Random rand) {
        CompoundTag in = src.getTag();
        if (in != null) {
            DinoDNA existing = DinoDNA.readFromNBT(in); // reads canonical "DNA" subtag if present
            if (existing != null) return existing;

            int q = in.contains("DNAQuality") ? in.getInt("DNAQuality") : 100;
            String g = in.contains("Genetics") ? in.getString("Genetics") : GeneticsHelper.randomGenetics(rand);
            return new DinoDNA(fallbackDino, q, g);
        }
        return new DinoDNA(fallbackDino, 100, GeneticsHelper.randomGenetics(rand));
    }

    //this should be made to be complient with the docs listed in the superclass at SOME POINT:tm: but im not doing that rn

    @Override
    public @NotNull List<ItemStack> processItem(ItemStack... input) {
        Random rand = Objects.requireNonNull(this.level).getRandom();
        ItemStack src = input[0];
        Item item = src.getItem();

        ItemStack disc = ItemStack.EMPTY;

        if (item == ModItems.MOSQUITO_AMBER.get()) {
            // land dinos from mosquito amber
            List<Dinosaur> possible = DinosaurHandler.getDinosaursFromAmber();
            Dinosaur dino = possible.get(rand.nextInt(possible.size()));
            disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
            new DinoDNA(dino, 50 + rand.nextInt(50), GeneticsHelper.randomGenetics(rand))
                    .writeToNBT(disc.getOrCreateTag());
            StorageDiscItem.applyCustomModelData(disc);

        } else if (item == ModItems.SEA_LAMPREY.get()) {
            // marine creatures from sea lamprey
            List<Dinosaur> possible = DinosaurHandler.getMarineCreatures();
            Dinosaur dino = possible.get(rand.nextInt(possible.size()));
            disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
            new DinoDNA(dino, 50 + rand.nextInt(50), GeneticsHelper.randomGenetics(rand))
                    .writeToNBT(disc.getOrCreateTag());
            StorageDiscItem.applyCustomModelData(disc);

        } else if (item == ModItems.FROZEN_LEECH_ITEM.get()) {
            // mammals from frozen leech
            List<Dinosaur> possible = DinosaurHandler.getMammalCreatures();
            Dinosaur dino = possible.get(rand.nextInt(possible.size()));
            disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
            new DinoDNA(dino, 50 + rand.nextInt(50), GeneticsHelper.randomGenetics(rand))
                    .writeToNBT(disc.getOrCreateTag());
            StorageDiscItem.applyCustomModelData(disc);

        } else if (item == ModItems.APHID_AMBER.get()) {
            // plants from aphid amber
            List<Plant> possiblePlants = PlantHandler.getPrehistoricPlantsAndTrees();
            Plant plant = possiblePlants.get(rand.nextInt(possiblePlants.size()));
            disc = ModItems.STORAGE_DISC.get().getDefaultInstance();
            new PlantDNA(JurassicReborn.resource(plant.getName().toLowerCase(Locale.ROOT).replace(" ", "_")),
                    50 + rand.nextInt(50))
                    .writeToNBT(disc.getOrCreateTag());
            StorageDiscItem.applyCustomModelData(disc);

        } else if (item instanceof DinosaurMeatItem meat) {
            ItemStack discOut = ModItems.STORAGE_DISC.get().getDefaultInstance();
            FossilGrinderBlockEntity.copyDNA(input[0], discOut);
            disc = discOut;
            StorageDiscItem.applyCustomModelData(disc);
        }

        if (!disc.isEmpty()) {
            // consume inputs
            src.shrink(1);
            this.machineItemStackHandler.setStackInSlot(0, src);

            ItemStack inputDisc = this.getItem(1);
            inputDisc.shrink(1);
            this.setItem(1, inputDisc);

            // output
            this.setItem(getOpenSlot(), disc);
        }

        return List.of(ItemStack.EMPTY);
    }

    public int getOpenSlot(){
        for(int i = 2; i <= 6; i++){
            if(this.getItem(i).isEmpty()){
                return i;
            }
        }
        return -1;
    }


    //model stuff
    protected static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("animation.dna_extractor.idle");
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "idle", 0, this::idleController));
    }

    protected <E extends DNAExtractorBlockEntity> PlayState idleController(final AnimationEvent<E> event){
        event.getController().setAnimation(IDLE);

        return PlayState.STOP;
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }

    @Override
    public Tag getMachineData() {
        CompoundTag data = new CompoundTag();
        data.putInt("ExtractionTime", extractionTime);


        return data;
    }

    @Override
    public void readMachineData(Tag tag){
        CompoundTag machineData = (CompoundTag) tag;
        this.extractionTime = machineData.getInt("ExtractionTime");
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        Network.ENTITIES.remove(this);
    }
}
