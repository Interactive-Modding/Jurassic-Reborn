package mod.reborn.server.item;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.api.Hybrid;
import mod.reborn.server.item.guns.Glock;
import mod.reborn.server.item.guns.Remington;
import mod.reborn.server.item.guns.SPAS12;
import mod.reborn.server.item.guns.UTS15;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.util.text.TextComponentTranslation;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.block.AncientDoorItem;
import mod.reborn.server.item.vehicles.HelicopterItem;
import mod.reborn.server.util.RegistryHandler;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionEffect;

public class ItemHandler {
    public static final Map<TreeType, AncientDoorItem> ANCIENT_DOORS = new HashMap<>();

    public static final PlasterAndBandageItem PLASTER_AND_BANDAGE = new PlasterAndBandageItem();
    public static final DinosaurSpawnEggItem SPAWN_EGG = new DinosaurSpawnEggItem();

    public static final DNAItem DNA = new DNAItem();
    public static final DinosaurEggItem EGG = new DinosaurEggItem();
    public static final HatchedEggItem HATCHED_EGG = new HatchedEggItem();
    public static final SoftTissueItem SOFT_TISSUE = new SoftTissueItem();
    public static final PlantSoftTissueItem PLANT_SOFT_TISSUE = new PlantSoftTissueItem();
    public static final PaleoPadItem PALEO_PAD = new PaleoPadItem();

    public static final DinosaurMeatItem DINOSAUR_MEAT = new DinosaurMeatItem();
    public static final DinosaurSteakItem DINOSAUR_STEAK = new DinosaurSteakItem();

    public static final PaddockSignItem PADDOCK_SIGN = new PaddockSignItem();
    public static final AttractionSignItem ATTRACTION_SIGN = new AttractionSignItem();

    public static final AmberItem AMBER = new AmberItem();
    public static final FrozenLeachItem FROZEN_LEACH = new FrozenLeachItem();
    public static final BasicItem PETRI_DISH = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem PETRI_DISH_AGAR = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem EMPTY_TEST_TUBE = new BasicItem(TabHandler.ITEMS);

    public static final SyringeItem SYRINGE = new SyringeItem();
    public static final EmptySyringeItem EMPTY_SYRINGE = new EmptySyringeItem();

    public static final StorageDiscItem STORAGE_DISC = new StorageDiscItem();
    public static final BasicItem DNA_NUCLEOTIDES = new BasicItem(TabHandler.ITEMS);

    public static final PlantDNAItem PLANT_DNA = new PlantDNAItem();

    public static final BasicItem SEA_LAMPREY = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem IRON_BLADES = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem IRON_ROD = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem DISC_DRIVE = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem LASER = new BasicItem(TabHandler.ITEMS);

    public static final Item GROWTH_SERUM = new EntityRightClickItem(interaction -> {
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
    }).setCreativeTab(TabHandler.ITEMS);

    public static final Item BREEDING_WAND = new EntityRightClickItem(interaction -> {
        ItemStack stack = interaction.getPlayer().getHeldItem(interaction.getHand());
        NBTTagCompound nbt = stack.getOrCreateSubCompound("wand_info");
        Entity entity = interaction.getPlayer().world.getEntityByID(nbt.getInteger("dino_id"));
        if (interaction.getTarget() instanceof DinosaurEntity) {
            if (nbt.hasKey("dino_id", 99)) {
                if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isMale() != ((DinosaurEntity) interaction.getTarget()).isMale() && !((DinosaurEntity) interaction.getTarget()).getDinosaur().isHybrid) {
                    ((DinosaurEntity) entity).breed((DinosaurEntity) interaction.getTarget());
                    ((DinosaurEntity) interaction.getTarget()).breed((DinosaurEntity) entity);
                } else if (entity != interaction.getTarget()) {
                    nbt.removeTag("dino_id");
                }
            } else {
                nbt.setInteger("dino_id", interaction.getTarget().getEntityId());
            }
            return true;
        }
        return false;
    }).setCreativeTab(TabHandler.CREATIVE);
    
    public static final Item BIRTHING_WAND = new EntityRightClickItem(interaction -> {
        if(interaction.getTarget() instanceof DinosaurEntity) {
            DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
            if (dino.isPregnant() && !dino.getDinosaur().isHybrid) {
                ((DinosaurEntity) interaction.getTarget()).giveBirth();
            return true;
        } else {
                interaction.getPlayer().sendStatusMessage(new TextComponentTranslation("dinosaur.birthingwand." + (dino.isMale() ? "male" : "not_pregnant")), true);
            }
        }
        return false;
    }).setCreativeTab(TabHandler.CREATIVE);

    public static final Item PREGNANCY_TEST = new EntityRightClickItem(interaction -> {
        if(interaction.getTarget() instanceof DinosaurEntity && !interaction.getPlayer().world.isRemote) {
            DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
            interaction.getPlayer().sendStatusMessage(new TextComponentTranslation("dinosaur.pregnancytest." + (dino.isMale() ? "male" : dino.isPregnant() ? "pregnant" : "not_pregnant")), true);
            return true;
        }
        return false;
    }).setCreativeTab(TabHandler.CREATIVE);

    public static final BasicItem PLANT_CELLS = new BasicItem(TabHandler.ITEMS);
    public static final PlantCallusItem PLANT_CALLUS = new PlantCallusItem();
    public static final BasicItem PLANT_CELLS_PETRI_DISH = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem TRACKER = new BasicItem(TabHandler.ITEMS);

    public static final AncientRecordItem JURASSICRAFT_THEME_DISC = new AncientRecordItem("jurassicraft_theme", SoundHandler.JURASSICRAFT_THEME);
    public static final AncientRecordItem TROODONS_AND_RAPTORS_DISC = new AncientRecordItem("troodons_and_raptors", SoundHandler.TROODONS_AND_RAPTORS);
    public static final AncientRecordItem DONT_MOVE_A_MUSCLE_DISC = new AncientRecordItem("dont_move_a_muscle", SoundHandler.DONT_MOVE_A_MUSCLE);

    public static final DisplayBlockItem DISPLAY_BLOCK = new DisplayBlockItem();

    public static final BasicItem AMBER_KEYCHAIN = new BasicItem(TabHandler.DECORATIONS);
    public static final BasicItem AMBER_CANE = new BasicItem(TabHandler.DECORATIONS);
    public static final BasicItem MR_DNA_KEYCHAIN = new BasicItem(TabHandler.DECORATIONS);

    public static final BasicItem BASIC_CIRCUIT = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem ADVANCED_CIRCUIT = new BasicItem(TabHandler.ITEMS);

    public static final Item AJUGINUCULA_SMITHII_SEEDS = new ItemSeeds(BlockHandler.AJUGINUCULA_SMITHII, Blocks.FARMLAND).setUnlocalizedName("ajuginucula_smithii_seeds").setCreativeTab(TabHandler.PLANTS);
    public static final Item AJUGINUCULA_SMITHII_LEAVES = new ItemFood(1, 0.5F, false).setUnlocalizedName("ajuginucula_smithii_leaves").setCreativeTab(TabHandler.PLANTS);
    public static final BasicItem AJUGINUCULA_SMITHII_OIL = new BasicItem(TabHandler.PLANTS);

    public static final Item WILD_ONION = new ItemSeedFood(3, 0.3F, BlockHandler.WILD_ONION, Blocks.FARMLAND).setUnlocalizedName("wild_onion").setCreativeTab(TabHandler.PLANTS);
    public static final Item WILD_POTATO_SEEDS = new ItemSeeds(BlockHandler.WILD_POTATO_PLANT, Blocks.FARMLAND).setCreativeTab(TabHandler.PLANTS);
    public static final Item RHAMNUS_SEEDS = new ItemSeeds(BlockHandler.RHAMNUS_SALICIFOLIUS_PLANT, Blocks.FARMLAND).setCreativeTab(TabHandler.PLANTS);
    public static final Item WILD_POTATO = new ItemFood(1, 0.1F, false).setCreativeTab(TabHandler.FOODS);
    public static final Item WILD_POTATO_COOKED = new ItemFood(6, 0.6F, false).setCreativeTab(TabHandler.FOODS);
    public static final Item RHAMNUS_BERRIES = new RhamnusBerriesItem(3,0.3F).setCreativeTab(TabHandler.FOODS);
    public static final Item WEST_INDIAN_LILAC_BERRIES = new WestIndianLilacBerriesItem().setCreativeTab(TabHandler.FOODS);

    public static final GracilariaItem GRACILARIA = (GracilariaItem) new GracilariaItem(BlockHandler.GRACILARIA).setCreativeTab(TabHandler.PLANTS);
    public static final BasicItem LIQUID_AGAR = new BasicItem(TabHandler.PLANTS);

    public static final DinoScannerItem DINO_SCANNER = new DinoScannerItem();

    public static final PlantFossilItem PLANT_FOSSIL = new PlantFossilItem();
    public static final TwigFossilItem TWIG_FOSSIL = new TwigFossilItem();

    public static final Map<String, FossilItem> FOSSILS = new HashMap<>();
    public static final Map<String, FossilItem> FRESH_FOSSILS = new HashMap<>();

    public static final FossilizedEggItem FOSSILIZED_EGG = new FossilizedEggItem();

    public static final BasicItem GYPSUM_POWDER = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem COMPUTER_SCREEN = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem KEYBOARD = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem DNA_ANALYZER = new BasicItem(TabHandler.ITEMS);

    public static final BasicFoodItem CHILEAN_SEA_BASS = new BasicFoodItem(10, 1.0F, false, TabHandler.FOODS);
    public static final BasicFoodItem FUN_FRIES = new BasicFoodItem(4, 2.0F, false, TabHandler.FOODS);
    public static final BasicFoodItem OILED_POTATO_STRIPS = new BasicFoodItem(1, 0.0F, false, TabHandler.FOODS);
    public static final BasicFoodItem SHARK_MEAT_RAW = new BasicFoodItem(5, 0.6F, false, TabHandler.FOODS);
    public static final BasicFoodItem SHARK_MEAT_COOKED = new BasicFoodItem(10, 1.2F, false, TabHandler.FOODS);
    public static final BasicFoodItem CRAB_MEAT_RAW = new BasicFoodItem(1, 0.3F, false, TabHandler.FOODS);
    public static final BasicFoodItem CRAB_MEAT_COOKED = new BasicFoodItem(5, 0.6F, false, TabHandler.FOODS);

    public static final FieldGuideItem FIELD_GUIDE = new FieldGuideItem();

    public static final BasicItem LUNCH_BOX = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem STAMP_SET = new BasicItem(TabHandler.ITEMS);

    public static final BasicItem CAR_CHASSIS = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem ENGINE_SYSTEM = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_SEATS = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_TIRE = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem CAR_WINDSCREEN = new BasicItem(TabHandler.ITEMS);
    public static final BasicItem UNFINISHED_CAR = new BasicItem(TabHandler.ITEMS);

    public static final JournalItem INGEN_JOURNAL = new JournalItem();

    public static final VehicleItem VEHICLE_ITEM = new VehicleItem();

    public static final MuralItem MURAL = new MuralItem();

    public static final SaplingSeedItem PHOENIX_SEEDS = (SaplingSeedItem) new SaplingSeedItem(BlockHandler.ANCIENT_SAPLINGS.get(TreeType.PHOENIX));
    public static final SeededFruitItem PHOENIX_FRUIT = (SeededFruitItem) new SeededFruitItem(PHOENIX_SEEDS, 4, 0.4F).setCreativeTab(TabHandler.FOODS);

    public static final BugItem CRICKETS = new BugItem(stack -> {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (item == Items.WHEAT_SEEDS) {
            return 1;
        } else if (block == Blocks.TALLGRASS) {
            return 2;
        } else if (item == Items.WHEAT) {
            return 3;
        } else if (block == Blocks.LEAVES || block == Blocks.LEAVES2) {
            return 7;
        } else if (block == Blocks.HAY_BLOCK) {
            return 27;
        }
        return 0;
    });

    public static final BugItem COCKROACHES = new BugItem(stack -> {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS) {
            return 1;
        } else if (item == Items.WHEAT || item == Items.PUMPKIN_SEEDS) {
            return 2;
        } else if (item == Items.MELON || item == Items.POTATO) {
            return 3;
        } else if (item == Items.CARROT) {
            return 4;
        } else if (item == Items.BREAD || item == Items.FISH) {
            return 6;
        } else if (item == Items.CHICKEN || item == Items.COOKED_CHICKEN) {
            return 7;
        } else if (item == Items.PORKCHOP || item == Items.COOKED_PORKCHOP) {
            return 8;
        } else if (item == Items.BEEF || item == Items.COOKED_BEEF) {
            return 10;
        } else if (item == ItemHandler.DINOSAUR_MEAT || item == ItemHandler.DINOSAUR_STEAK) {
            return 12;
        } else if (block == Blocks.HAY_BLOCK || block == Blocks.PUMPKIN) {
            return 16;
        } else if (block == Blocks.MELON_BLOCK) {
            return 27;
        }
        return 0;
    });

    public static final BugItem MEALWORM_BEETLES = new BugItem(stack -> {
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS) {
            return 1;
        } else if (item == Items.PUMPKIN_SEEDS || item == Items.WHEAT) {
            return 2;
        } else if (item == Items.POTATO) {
            return 3;
        } else if (block == Blocks.CARROTS) {
            return 4;
        } else if (item == Items.BREAD) {
            return 6;
        } else if (block == Blocks.HAY_BLOCK) {
            return 16;
        }
        return 0;
    });

    public static final FineNetItem FINE_NET = new FineNetItem();
    public static final SwarmItem PLANKTON = new SwarmItem(BlockHandler.PLANKTON_SWARM::getDefaultState);
    public static final SwarmItem KRILL = new SwarmItem(BlockHandler.KRILL_SWARM::getDefaultState);

    public static final BasicFoodItem GOAT_RAW = new BasicFoodItem(3, 0.3F, true, TabHandler.FOODS);
    public static final BasicFoodItem GOAT_COOKED = new BasicFoodItem(6, 1.0F, true, TabHandler.FOODS);
    
    public static final DartGun DART_GUN = new DartGun();
    public static final Dart DART_TRANQUILIZER = new Dart((entity, stack) -> entity.tranquilize(2000), 0xFFFFFF);
    public static final Dart DART_POISON_CYCASIN = new Dart((entity, stack) -> entity.addPotionEffect(new PotionEffect(MobEffects.POISON, 2000)), 0xE2E1B8);
    public static final Dart DART_POISON_EXECUTIONER_CONCOCTION = new Dart((entity, stack) -> entity.setDeathIn(200), 0x000000);
    public static final Dart DART_TIPPED_POTION = new PotionDart();
    public static final Dart TRACKER_DART = new TrackerDart();

    public static final Glock GLOCK = new Glock();
    public static final Remington REMINGTON = new Remington();
    public static final SPAS12 SPAS_12 = new SPAS12();
    public static final UTS15 UTS15 = new UTS15();
    public static final Bullet BULLET = new Bullet();
    //public static final Bullet BULLET_PACK_12 = new Bullet();

    public static void init() {
        registerItem(FOSSILIZED_EGG, "Fossilized Egg");

        for (Map.Entry<Integer, Dinosaur> entry : EntityHandler.getDinosaurs().entrySet()) {
            Dinosaur dinosaur = entry.getValue();

            String[] boneTypes = dinosaur.getBones();

            for (String boneType : boneTypes) {
                if (!(dinosaur instanceof Hybrid)) {
                    if (!FOSSILS.containsKey(boneType)) {
                        FossilItem fossil = new FossilItem(boneType, false);
                        FOSSILS.put(boneType, fossil);
                        registerItem(fossil, boneType);
                    }
                }

                if (!FRESH_FOSSILS.containsKey(boneType)) {
                    FossilItem fossil = new FossilItem(boneType, true);
                    FRESH_FOSSILS.put(boneType, fossil);
                    registerItem(fossil, boneType + " Fresh");
                }
            }
        }

        registerItem(SPAWN_EGG, "Dino Spawn Egg");
        registerItem(FIELD_GUIDE, "Field Guide");
        registerItem(AMBER, "Amber");
        registerItem(SEA_LAMPREY, "Sea Lamprey");
        registerItem(PLASTER_AND_BANDAGE, "Plaster And Bandage");
        registerItem(EMPTY_TEST_TUBE, "Empty Test Tube");
        registerItem(EMPTY_SYRINGE, "Empty Syringe");
        registerItem(GROWTH_SERUM, "Growth Serum");

        registerItem(BREEDING_WAND, "Breeding Wand");
        registerItem(BIRTHING_WAND, "Birthing_Wand");
        registerItem(PREGNANCY_TEST, "Pregnancy Test");
        registerItem(STORAGE_DISC, "Storage Disc");
        registerItem(DISC_DRIVE, "Disc Reader");
        registerItem(LASER, "Laser");
        registerItem(DNA_NUCLEOTIDES, "DNA Base Material");
        registerItem(PETRI_DISH, "Petri Dish");
        registerItem(PETRI_DISH_AGAR, "Petri Dish Agar");
        registerItem(PLANT_CELLS_PETRI_DISH, "Plant Cells Petri Dish");
        registerItem(PADDOCK_SIGN, "Paddock Sign");
        registerItem(ATTRACTION_SIGN, "Attraction Sign");
        registerItem(MURAL, "Mural");
        registerItem(DNA, "DNA");
        registerItem(SOFT_TISSUE, "Soft Tissue");
        registerItem(SYRINGE, "Syringe");
        registerItem(EGG, "Dino Egg");
        registerItem(HATCHED_EGG, "Hatched Egg");
        registerItem(PLANT_SOFT_TISSUE, "Plant Soft Tissue");
        registerItem(PLANT_DNA, "Plant DNA");
        registerItem(IRON_BLADES, "Iron Blades");
        registerItem(IRON_ROD, "Iron Rod");
        registerItem(PLANT_CELLS, "Plant Cells");
        registerItem(PLANT_CALLUS, "Plant Callus");
        registerItem(TRACKER, "Tracker");
        registerItem(BASIC_CIRCUIT, "Basic Circuit");
        registerItem(ADVANCED_CIRCUIT, "Advanced Circuit");
        registerItem(COMPUTER_SCREEN, "Computer Screen");
        registerItem(KEYBOARD, "Keyboard");
        registerItem(DNA_ANALYZER, "DNA Analyzer");

        registerItem(DINOSAUR_MEAT, "Dinosaur Meat");
        registerItem(DINOSAUR_STEAK, "Dinosaur Steak");

        registerItem(PLANT_FOSSIL, "Plant Fossil");
        registerItem(TWIG_FOSSIL, "Twig Fossil");


//        registerItem(MINIGUN_MODULE, "Helicopter Minigun");

        registerItem(PALEO_PAD, "Paleo Pad");

        registerItem(AMBER_CANE, "Amber Cane");
        registerItem(AMBER_KEYCHAIN, "Amber Keychain");
        registerItem(MR_DNA_KEYCHAIN, "Mr DNA Keychain");

        registerItem(DISPLAY_BLOCK, "Display Block Item");

       registerItem(DINO_SCANNER, "Dino Scanner");

        registerItem(GYPSUM_POWDER, "Gypsum Powder");

        registerItem(AJUGINUCULA_SMITHII_SEEDS, "Ajuginucula Smithii Seeds");
        registerItem(AJUGINUCULA_SMITHII_LEAVES, "Ajuginucula Smithii Leaves");
        registerItem(AJUGINUCULA_SMITHII_OIL, "Ajuginucula Smithii Oil");

        registerItem(WILD_ONION, "Wild Onion");
        registerItem(WILD_POTATO_SEEDS, "Wild Potato Seeds");
        registerItem(WILD_POTATO, "Wild Potato");
        registerItem(WILD_POTATO_COOKED, "Wild Potato Cooked");

        registerItem(RHAMNUS_SEEDS, "Rhamnus Salicifolius Seeds");
        registerItem(RHAMNUS_BERRIES, "Rhamnus Salicifolius Berries");

        registerItem(GRACILARIA, "Gracilaria");
        registerItem(LIQUID_AGAR, "Liquid Agar");

        registerItem(CHILEAN_SEA_BASS, "Chilean Sea Bass");
	    registerItem(CRAB_MEAT_RAW, "Raw Crab Meat");
        registerItem(CRAB_MEAT_COOKED, "Cooked Crab Meat");
        registerItem(SHARK_MEAT_RAW, "Raw Shark Meat");
        registerItem(SHARK_MEAT_COOKED, "Cooked Shark Meat");
        registerItem(PHOENIX_FRUIT, "Phoenix Fruit");
        registerItem(PHOENIX_SEEDS, "Phoenix Seeds");

        registerItem(FINE_NET, "Fine Net");
        registerItem(PLANKTON, "Plankton");
        registerItem(KRILL, "Krill");

        registerItem(CRICKETS, "Crickets");
        registerItem(COCKROACHES, "Cockroaches");
        registerItem(MEALWORM_BEETLES, "Mealworm Beetles");

        registerItem(CAR_CHASSIS, "Car Chassis");
        registerItem(ENGINE_SYSTEM, "Engine System");
        registerItem(CAR_SEATS, "Car Seats");
        registerItem(CAR_TIRE, "Car Tire");
        registerItem(CAR_WINDSCREEN, "Car Windscreen");
        registerItem(UNFINISHED_CAR, "Unfinished Car");
        registerItem(VEHICLE_ITEM, "Vehicle Item");

        registerItem(JURASSICRAFT_THEME_DISC, "Disc JurassiCraft Theme");
        registerItem(TROODONS_AND_RAPTORS_DISC, "Disc Troodons And Raptors");
        registerItem(DONT_MOVE_A_MUSCLE_DISC, "Disc Don't Move A Muscle");

        registerItem(GOAT_RAW, "Goat Raw");
        registerItem(GOAT_COOKED, "Goat Cooked");

        registerItem(FUN_FRIES, "Fun Fries");
        registerItem(OILED_POTATO_STRIPS, "Oiled Potato Strips");

        registerItem(WEST_INDIAN_LILAC_BERRIES, "West Indian Lilac Berries");

        registerItem(LUNCH_BOX, "Lunch Box");
        registerItem(STAMP_SET, "Stamp Set");

        registerItem(INGEN_JOURNAL, "InGen Journal");
        
        registerItem(DART_GUN, "Dart Gun");
        registerItem(DART_TRANQUILIZER, "Dart Tranquilizer");
        registerItem(DART_POISON_CYCASIN, "Dart Poison Cycasin");
        registerItem(DART_POISON_EXECUTIONER_CONCOCTION, "Dart Poison Executioner Concoction");
        registerItem(DART_TIPPED_POTION, "Dart Tipped Potion");
        registerItem(TRACKER_DART, "Tracking Dart");

        registerItem(GLOCK, "Glock");
        registerItem(REMINGTON, "remington");
        registerItem(SPAS_12, "spas_12");
        registerItem(UTS15, "uts15");
        registerItem(BULLET, "Bullet");
      // registerItem(BULLET_PACK_12, "Bullet_Pack");
        for (TreeType type : TreeType.values()) {
            registerTreeType(type);
        }
    }

    public static void registerOres()
    {
    	//Kept as the diamond nugget will be implemented soon BY THIS PR
    }

    public static void registerTreeType(TreeType type) {
        String typeName = type.name();
        AncientDoorItem door = new AncientDoorItem(BlockHandler.ANCIENT_DOORS.get(type));
        ANCIENT_DOORS.put(type, door);
        registerItem(door, typeName + " Door Item");
    }

    public static void registerItem(Item item, String name) {
        String formattedName = name.toLowerCase(Locale.ENGLISH).replaceAll(" ", "_").replaceAll("'", "");
        item.setUnlocalizedName(formattedName);
        RegistryHandler.registerItem(item, formattedName);
    }
}
