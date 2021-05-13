package mod.reborn.server.items;

import mod.reborn.RebornMod;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemHandler {

    public static final DeferredRegister<Item> REGISTERS = DeferredRegister.create(ForgeRegistries.ITEMS, RebornMod.MOD_ID);

    // Basic Items
    public static final RegistryObject<BasicItem> IRON_BLADES;
    public static final RegistryObject<BasicItem> IRON_ROD;
    public static final RegistryObject<BasicItem> DISC_DRIVE;
    public static final RegistryObject<BasicItem> LASER;
    public static final RegistryObject<BasicItem> SEA_LAMPREY;
    public static final RegistryObject<BasicItem> DNA_NUCLEOTIDES;
    public static final RegistryObject<BasicItem> EMPTY_TEST_TUBE;
    public static final RegistryObject<BasicItem> PETRI_DISH_AGAR;
    public static final RegistryObject<BasicItem> PETRI_DISH;
    public static final RegistryObject<BasicItem> PLANT_CELLS;
    public static final RegistryObject<BasicItem> PLANT_CELLS_PETRI_DISH;
    public static final RegistryObject<BasicItem> AMBER_KEYCHAIN;
    public static final RegistryObject<BasicItem> AMBER_CANE;
    public static final RegistryObject<BasicItem> MR_DNA_KEYCHAIN;
    public static final RegistryObject<BasicItem> BASIC_CIRCUIT;
    public static final RegistryObject<BasicItem> ADVANCED_CIRCUIT;
    public static final RegistryObject<BasicItem> AJUGINCULA_SMITHII_OIL;
    public static final RegistryObject<BasicItem> LIQUID_AGAR;
    public static final RegistryObject<BasicItem> GYPSUM_POWDER;
    public static final RegistryObject<BasicItem> COMPUTER_SCREEN;
    public static final RegistryObject<BasicItem> KEYBOARD;
    public static final RegistryObject<BasicItem> DNA_ANALYZER;
    public static final RegistryObject<BasicItem> LUNCH_BOX;
    public static final RegistryObject<BasicItem> STAMP_SET;
    public static final RegistryObject<BasicItem> CAR_CHASSIS;
    public static final RegistryObject<BasicItem> ENGINE_SYSTEM;
    public static final RegistryObject<BasicItem> CAR_SEATS;
    public static final RegistryObject<BasicItem> CAR_TIRE;
    public static final RegistryObject<BasicItem> CAR_WINDSCREEN;
    public static final RegistryObject<BasicItem> UNFINISHED_CAR;
    static {
        // Basic Items
        IRON_BLADES = REGISTERS.register("iron_blades", BasicItem::new);
        IRON_ROD = REGISTERS.register("iron_rod", BasicItem::new);
        DISC_DRIVE = REGISTERS.register("disc_drive", BasicItem::new);
        LASER = REGISTERS.register("laser", BasicItem::new);
        SEA_LAMPREY = REGISTERS.register("sea_lamprey", BasicItem::new);
        DNA_NUCLEOTIDES = REGISTERS.register("dna_nucleotides", BasicItem::new);
        EMPTY_TEST_TUBE = REGISTERS.register("empty_test_tube", BasicItem::new);
        PETRI_DISH_AGAR = REGISTERS.register("petri_dish_agar", BasicItem::new);
        PETRI_DISH = REGISTERS.register("petri_dish", BasicItem::new);
        PLANT_CELLS = REGISTERS.register("plant_cells", BasicItem::new);
        PLANT_CELLS_PETRI_DISH = REGISTERS.register("plant_cells_petri_dish", BasicItem::new);
        AMBER_KEYCHAIN = REGISTERS.register("amber_keychain", () -> new BasicItem(TabHandler.DECORATIONS));
        AMBER_CANE = REGISTERS.register("amber_cane", () -> new BasicItem(TabHandler.DECORATIONS));
        MR_DNA_KEYCHAIN = REGISTERS.register("mr_dna_keychain", () -> new BasicItem(TabHandler.DECORATIONS));
        BASIC_CIRCUIT = REGISTERS.register("basic_circuit", BasicItem::new);
        ADVANCED_CIRCUIT = REGISTERS.register("advanced_circuit", BasicItem::new);
        AJUGINCULA_SMITHII_OIL = REGISTERS.register("ajugincula_smithii_oil", () -> new BasicItem(TabHandler.PLANTS));
        LIQUID_AGAR = REGISTERS.register("liquid_agar", () -> new BasicItem(TabHandler.PLANTS));
        GYPSUM_POWDER = REGISTERS.register("gypsum_powder", BasicItem::new);
        COMPUTER_SCREEN = REGISTERS.register("computer_screen", BasicItem::new);
        KEYBOARD = REGISTERS.register("keyboard", BasicItem::new);
        DNA_ANALYZER = REGISTERS.register("dna_analyzer", BasicItem::new);
        LUNCH_BOX = REGISTERS.register("lunch_box", BasicItem::new);
        STAMP_SET = REGISTERS.register("stamp_set", BasicItem::new);
        CAR_CHASSIS = REGISTERS.register("car_chassis", BasicItem::new);
        ENGINE_SYSTEM = REGISTERS.register("engine_system", BasicItem::new);
        CAR_SEATS = REGISTERS.register("car_seats", BasicItem::new);
        CAR_TIRE = REGISTERS.register("car_tire", BasicItem::new);
        CAR_WINDSCREEN = REGISTERS.register("car_windscreen", BasicItem::new);
        UNFINISHED_CAR = REGISTERS.register("unfinished_car", BasicItem::new);
    }

    // Interaction Items
    public static final RegistryObject<EntityRightClickItem> GROWTH_SERUM;
    public static final RegistryObject<EntityRightClickItem> BREEDING_WAND;
    public static final RegistryObject<EntityRightClickItem> BIRTHING_WAND;
    public static final RegistryObject<EntityRightClickItem> PREGNANCY_TEST;
    static {
        GROWTH_SERUM = new EntityRightClickItem(interaction -> {
            if (interaction.getTarget() instanceof DinosaurEntity) {
                DinosaurEntity dinosaur = (DinosaurEntity) interaction.getTarget();
                if (!dinosaur.isCarcass()) {
                    dinosaur.increaseGrowthSpeed();
                    interaction.getStack().shrink(1);
                    if (!interaction.getPlayer().capabilities.isCreativeMode) {
                        interaction.getPlayer().inventory.addItemStackToInventory(new ItemStack(ItemHandler.EMPTY_SYRINGE));
                    }
                    return true;
                }
            }
            return false;
        }, TabHandler.ITEMS);
        BREEDING_WAND = new EntityRightClickItem(interaction -> {
            ItemStack stack = interaction.getPlayer().getHeldItem(interaction.getHand());
            CompoundNBT nbt = stack.getOrCreateChildTag("wand_info");
            Entity entity = interaction.getPlayer().world.getEntityByID(nbt.getInt("dino_id"));
            if (interaction.getTarget() instanceof DinosaurEntity) {
                if (nbt.contains("dino_id", 99)) {
                    if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isMale() != ((DinosaurEntity) interaction.getTarget()).isMale() && !((DinosaurEntity) interaction.getTarget()).getDinosaur().isHybrid) {
                        ((DinosaurEntity) entity).breed((DinosaurEntity) interaction.getTarget());
                        ((DinosaurEntity) interaction.getTarget()).breed((DinosaurEntity) entity);
                    } else if (entity != interaction.getTarget()) {
                        nbt.remove("dino_id");
                    }
                } else {
                    nbt.putInt("dino_id", interaction.getTarget().getEntityId());
                }
                return true;
            }
            return false;
        }, TabHandler.CREATIVE);
        BIRTHING_WAND = new EntityRightClickItem(interaction -> {
            if(interaction.getTarget() instanceof DinosaurEntity) {
                DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
                if (dino.isPregnant() && !dino.getDinosaur().isHybrid) {
                    ((DinosaurEntity) interaction.getTarget()).giveBirth();
                    return true;
                } else {
                    interaction.getPlayer().sendStatusMessage(new StringTextComponent("dinosaur.birthingwand." + (dino.isMale() ? "male" : "not_pregnant")), true);
                }
            }
            return false;
        }, TabHandler.CREATIVE);
        PREGNANCY_TEST = new EntityRightClickItem(interaction -> {
            if(interaction.getTarget() instanceof DinosaurEntity && !interaction.getPlayer().world.isRemote) {
                DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
                interaction.getPlayer().sendStatusMessage(new StringTextComponent("dinosaur.pregnancytest." + (dino.isMale() ? "male" : dino.isPregnant() ? "pregnant" : "not_pregnant")), true);
                return true;
            }
            return false;
        }, TabHandler.CREATIVE);
    }
}
