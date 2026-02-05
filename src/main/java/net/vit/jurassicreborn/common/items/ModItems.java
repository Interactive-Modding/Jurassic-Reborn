package net.vit.jurassicreborn.common.items;

import net.minecraft.world.level.block.*;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.SkullDisplayBlock;
import net.vit.jurassicreborn.common.blocks.ancientplants.*;
import net.vit.jurassicreborn.common.blocks.ancientplants.DoublePlantBlock;
import net.vit.jurassicreborn.common.blocks.ancientplants.moss.PeatBlock;
import net.vit.jurassicreborn.common.blocks.entities.trashcan.TrashCanBlock;
import net.vit.jurassicreborn.common.blocks.fossil.FossilBlock;
import net.vit.jurassicreborn.common.blocks.parkBlocks.ParkBenchBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.entities.item.AttractionSignEntity;
import net.vit.jurassicreborn.common.items.Fossils.*;
import net.vit.jurassicreborn.common.items.genetics.*;
import net.vit.jurassicreborn.common.items.guns.*;
import net.vit.jurassicreborn.common.items.misc.*;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.entities.paleobale.PaleoBaleBlock;
import net.vit.jurassicreborn.common.blocks.fossil.AncientCoralBlock;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.items.Food.DinosaurMeatItem;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.vit.jurassicreborn.common.plants.WestIndianLilacBlock;
import net.minecraftforge.common.ForgeSpawnEggItem;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;

public class ModItems {
    public static void init(){

    }

    public static final DeferredRegister<Item> MOD_ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, JurassicReborn.MODID);

    public static RegistryObject<Item> PLASTER_AND_BANDAGE = MOD_ITEMS.register("plaster_and_bandage", () -> new PlasterAndBandageItem(new Item.Properties().tab(TabHandler.ITEMS)));

    public static RegistryObject<Item> AMBER = MOD_ITEMS.register("amber", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static RegistryObject<BlockItem> MOSQUITO_AMBER = MOD_ITEMS.register("amber_mosquito", () -> new BlockItem(ModBlocks.AMBER_MOSQUITO.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static RegistryObject<BlockItem> APHID_AMBER = MOD_ITEMS.register("amber_aphid", () -> new BlockItem(ModBlocks.AMBER_APHID.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<BlockItem> FROZEN_LEECH_ITEM = MOD_ITEMS.register("frozen_leech", () -> new BlockItem(ModBlocks.FROZEN_LEECH.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static RegistryObject<BlockItem> SEA_LAMPREY = MOD_ITEMS.register("sea_lamprey", () -> new BlockItem(ModBlocks.SEA_LAMPREY.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<CageItem> CAGE = MOD_ITEMS.register("cage", CageItem::new);
    public static final RegistryObject<AquaticCageItem> AQUATIC_CAGE = MOD_ITEMS.register("aquatic_cage", AquaticCageItem::new);
//    public static RegistryObject<Item> DEFAULT_BONE = modItems.register("missing_bone", () -> new Item(new Item.Properties()));
    //FOODS
    public static final FoodProperties SHARK_MEAT_RAW_PROP = new FoodProperties.Builder().meat().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties SHARK_MEAT_COOKED_PROP = new FoodProperties.Builder().meat().nutrition(10).saturationMod(1.2F).build();
    public static final FoodProperties CRAB_MEAT_RAW_PROP = new FoodProperties.Builder().meat().nutrition(1).saturationMod(0.3F).build();
    public static final FoodProperties CRAB_MEAT_COOKED_PROP = new FoodProperties.Builder().meat().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties GOAT_RAW_PROP = new FoodProperties.Builder().meat().nutrition(3).saturationMod(0.3F).build();
    public static final FoodProperties GOAT_COOKED_PROP = new FoodProperties.Builder().meat().nutrition(6).saturationMod(10.3F).build();
    public static final FoodProperties PHOENIX_FRUIT_PROPERTIES = new FoodProperties.Builder().nutrition(4).saturationMod(0.3F).build();
    public static final RegistryObject<FossilizedEggItem> FOSSILIZED_EGG_1 = MOD_ITEMS.register("fossilized_egg_1", FossilizedEggItem::new);
    public static final RegistryObject<FossilizedEggItem> FOSSILIZED_EGG_2 = MOD_ITEMS.register("fossilized_egg_2", FossilizedEggItem::new);
    public static final RegistryObject<FossilizedEggItem> FOSSILIZED_EGG_3 = MOD_ITEMS.register("fossilized_egg_3", FossilizedEggItem::new);
    public static final FoodProperties CHILEAN_SEA_BASS_PROPERTIES /*why from chile in particular*/ = new FoodProperties.Builder().meat().nutrition(10).saturationMod(1.0f).build();
    public static final FoodProperties FUN_FRIES_PROPERTIES = new FoodProperties.Builder().nutrition(4).saturationMod(2.0f).build();
    public static final FoodProperties OILED_POTATO_STRIPS_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationMod(0.0f).build();

    public static final FoodProperties WILD_ONION_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties WILD_POTATO_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).build();
    public static final FoodProperties WILD_POTATO_COOKED_PROPERTIES = new FoodProperties.Builder().nutrition(6).saturationMod(0.6f).build();
    public static final FoodProperties RHAMNUS_BERRIES_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationMod(0.3f).build();
    public static final FoodProperties WEST_INDIAN_LILAC_BERRIES_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationMod(0.1f).effect(() -> new MobEffectInstance(MobEffects.POISON, 1400, 1), 1.0f).build();
    public static final FoodProperties AJUGINUCULA_SMITHII_LEAVES_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationMod(0.5f).build();

    public static final RegistryObject<Item> SHARK_MEAT_RAW = MOD_ITEMS.register("raw_shark_meat", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(SHARK_MEAT_RAW_PROP)));
    public static final RegistryObject<Item> SHARK_MEAT_COOKED = MOD_ITEMS.register("cooked_shark_meat", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(SHARK_MEAT_COOKED_PROP)));
    public static final RegistryObject<Item> CRAB_MEAT_RAW = MOD_ITEMS.register("raw_crab_meat", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(CRAB_MEAT_RAW_PROP)));
    public static final RegistryObject<Item> CRAB_MEAT_COOKED = MOD_ITEMS.register("cooked_crab_meat", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(CRAB_MEAT_COOKED_PROP)));
    public static final RegistryObject<Item> CHILEAN_SEA_BASS = MOD_ITEMS.register("chilean_sea_bass", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(CHILEAN_SEA_BASS_PROPERTIES)));
    public static final RegistryObject<Item> OILED_POTATO_STRIPS = MOD_ITEMS.register("oiled_potato_strips", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(OILED_POTATO_STRIPS_PROPERTIES)));
    public static final RegistryObject<Item> FUN_FRIES = MOD_ITEMS.register("fun_fries", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(FUN_FRIES_PROPERTIES)));
    public static final RegistryObject<Item> WILD_POTATO = MOD_ITEMS.register("wild_potato", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(WILD_POTATO_PROPERTIES)));
    public static final RegistryObject<Item> WILD_POTATO_COOKED = MOD_ITEMS.register("wild_potato_cooked", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(WILD_POTATO_COOKED_PROPERTIES)));
    public static final RegistryObject<Item> RHAMNUS_BERRIES = MOD_ITEMS.register("rhamnus_salicifolius_berries", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(RHAMNUS_BERRIES_PROPERTIES)));
    public static final RegistryObject<Item> WEST_INDIAN_LILAC_BERRIES = MOD_ITEMS.register("west_indian_lilac_berries", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(WEST_INDIAN_LILAC_BERRIES_PROPERTIES)));
    public static final RegistryObject<Item> PHOENIX_FRUIT = MOD_ITEMS.register("phoenix_fruit", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(PHOENIX_FRUIT_PROPERTIES)));
    public static final RegistryObject<ItemNameBlockItem> AJUGINUCULA_SMITHII_SEEDS = MOD_ITEMS.register("ajuginucula_smithii_seeds", () -> new ItemNameBlockItem(ModBlocks.AJUGINUCULA_SMITHII.get(), new Item.Properties()));
    public static final RegistryObject<Item> AJUGINUCULA_SMITHII_LEAVES = MOD_ITEMS.register("ajuginucula_smithii_leaves", () -> new Item(new Item.Properties().tab(TabHandler.PLANTS).food(AJUGINUCULA_SMITHII_LEAVES_PROPERTIES)));
    public static final RegistryObject<Item> AJUGINUCULA_SMITHII_OIL = MOD_ITEMS.register("ajuginucula_smithii_oil", () -> new Item(new Item.Properties().tab(TabHandler.PLANTS)));


    public static final RegistryObject<Item> GOAT_RAW = MOD_ITEMS.register("goat_raw", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(GOAT_RAW_PROP)));
    public static final RegistryObject<Item> GOAT_COOKED = MOD_ITEMS.register("goat_cooked", () -> new Item(new Item.Properties().tab(TabHandler.FOODS).food(GOAT_COOKED_PROP)));
    public static final RegistryObject<BugItem> CRICKETS = MOD_ITEMS.register("crickets", () ->
            new BugItem(stack -> {
                Item item = stack.getItem();
                Block block = Block.byItem(item);
                if (item == Items.WHEAT_SEEDS) return 1;
                else if (block == Blocks.GRASS) return 2;
                else if (item == Items.WHEAT) return 3;
                else if (block == Blocks.OAK_LEAVES) return 7;
                else if (block == Blocks.HAY_BLOCK) return 27;
                return 0;
            }));

    public static final RegistryObject<BugItem> COCKROACHES = MOD_ITEMS.register("cockroaches",
            () -> new BugItem(stack -> {
                Item item = stack.getItem();
                Block block = Block.byItem(item);
                if (item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS) return 1;
                else if (item == Items.WHEAT || item == Items.PUMPKIN_SEEDS) return 2;
                else if (item == Items.MELON || item == Items.POTATO) return 3;
                else if (item == Items.CARROT) return 4;
                else if (item == Items.BREAD || item == Items.COD) return 6;
                else if (item == Items.CHICKEN || item == Items.COOKED_CHICKEN) return 7;
                else if (item == Items.PORKCHOP || item == Items.COOKED_PORKCHOP) return 8;
                else if (item == Items.BEEF || item == Items.COOKED_BEEF) return 10;
                else if (ModItems.ALL_MEATS.stream().map(Supplier::get).anyMatch(i -> i == item)) return 12;
                else if (block == Blocks.HAY_BLOCK || block == Blocks.PUMPKIN) return 16;
                else if (block == Blocks.MELON) return 27;
                return 0;
            }));

    public static final RegistryObject<BugItem> MEALWORM_BEETLES = MOD_ITEMS.register("mealworm_beetles",
            () -> new BugItem(stack -> {
                Item item = stack.getItem();
                Block block = Block.byItem(item);
                if (item == Items.WHEAT_SEEDS || item == Items.MELON_SEEDS) return 1;
                else if (item == Items.PUMPKIN_SEEDS || item == Items.WHEAT) return 2;
                else if (item == Items.POTATO) return 3;
                else if (block == Blocks.CARROTS) return 4;
                else if (item == Items.BREAD) return 6;
                else if (block == Blocks.HAY_BLOCK) return 16;
                return 0;
            }));
    public static final RegistryObject<Item> JOURNAL_CHEF_ALEJANDRO = MOD_ITEMS.register("journal_chef_alejandro", () -> new JournalItem(JournalItem.JournalType.CHEF_ALEJANDRO));
    public static final RegistryObject<Item> JOURNAL_DENNIS_NEDRY = MOD_ITEMS.register("journal_dennis_nedry", () -> new JournalItem(JournalItem.JournalType.DENNIS_NEDRY));
    public static final RegistryObject<Item> JOURNAL_DR_GERRY_HARDING = MOD_ITEMS.register("journal_dr_gerry_harding", () -> new JournalItem(JournalItem.JournalType.DR_GERRY_HARDING));
    public static final RegistryObject<Item> JOURNAL_DR_HENRY_WU = MOD_ITEMS.register("journal_dr_henry_wu", () -> new JournalItem(JournalItem.JournalType.DR_HENRY_WU));
    public static final RegistryObject<Item> JOURNAL_DR_LAURA_SORKIN = MOD_ITEMS.register("journal_dr_laura_sorkin", () -> new JournalItem(JournalItem.JournalType.DR_LAURA_SORKIN));
    public static final RegistryObject<Item> JOURNAL_ED_REGIS = MOD_ITEMS.register("journal_ed_regis", () -> new JournalItem(JournalItem.JournalType.ED_REGIS));
    public static final RegistryObject<Item> JOURNAL_JOHN_HAMMOND = MOD_ITEMS.register("journal_john_hammond", () -> new JournalItem(JournalItem.JournalType.JOHN_HAMMOND));
    public static final RegistryObject<Item> JOURNAL_RAY_ARNOLD = MOD_ITEMS.register("journal_ray_arnold", () -> new JournalItem(JournalItem.JournalType.RAY_ARNOLD));
    public static final RegistryObject<Item> JOURNAL_ROBERT_MULDOON = MOD_ITEMS.register("journal_robert_muldoon", () -> new JournalItem(JournalItem.JournalType.ROBERT_MULDOON));
    public static final RegistryObject<SwarmItem> PLANKTON = MOD_ITEMS.register("plankton", () -> new SwarmItem(ModBlocks.PLANKTON_SWARM.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<SwarmItem> KRILL = MOD_ITEMS.register("krill", () -> new SwarmItem(ModBlocks.KRILL_SWARM.get(), new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> FIELD_GUIDE = MOD_ITEMS.register("field_guide", () -> new FieldGuideItem(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<RecordItem> JURASSICREBORN_THEME_DISC = MOD_ITEMS.register("disc_jurassicreborn_theme", () -> new RecordItem(101/*dont ask*/, () -> SoundHandler.JURASSICREBORN_THEME, new Item.Properties().tab(TabHandler.ITEMS).rarity(Rarity.RARE), 4740));
    public static final RegistryObject<RecordItem> TROODONS_AND_RAPTORS_DISC = MOD_ITEMS.register("disc_troodons_and_raptors", () -> new RecordItem(102, () -> SoundHandler.TROODONS_AND_RAPTORS, new Item.Properties().tab(TabHandler.ITEMS).rarity(Rarity.RARE), 1760));
    public static final RegistryObject<RecordItem> DONT_MOVE_A_MUSCLE_DISC = MOD_ITEMS.register("disc_dont_move_a_muscle", () -> new RecordItem(103, () -> SoundHandler.DONT_MOVE_A_MUSCLE, new Item.Properties().tab(TabHandler.ITEMS).rarity(Rarity.RARE), 2040));
    public static final RegistryObject<Item> PALEO_PAD = MOD_ITEMS.register("paleo_pad", () -> new PaleoPadItem());
    public static final RegistryObject<StorageDiscItem> STORAGE_DISC = MOD_ITEMS.register("storage_disc", () -> new StorageDiscItem(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<BlockItem> GYPSUM_BRICKS = registerBlockItem("gypsum_bricks", ModBlocks.GYPSUM_BRICKS);
    public static final RegistryObject<BlockItem> HOLOGRAM_BLOCK = MOD_ITEMS.register("hologram_block", () -> new BlockItem(ModBlocks.HOLOGRAM_BLOCK.get(), new Item.Properties().tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> EMPTY_TEST_TUBE = MOD_ITEMS.register("empty_test_tube", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> DNA_NUCLEOTIDES = MOD_ITEMS.register("dna_base_material", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<FaunaFossilBlockItem> FAUNA_FOSSIL_BLOCK = MOD_ITEMS.register("fauna_fossil_block_item", () -> new FaunaFossilBlockItem(ModBlocks.FAUNA_FOSSIL.get(), new Item.Properties()));

    public static final RegistryObject<IncubatorEnvironmentItem> PEAT_MOSS_BLOCK = MOD_ITEMS.register("peat_moss", () -> new IncubatorEnvironmentItem(ModBlocks.PEAT_MOSS.get(), new Item.Properties().tab(TabHandler.BLOCKS)));

    //PLANTS
    public static final RegistryObject<PlantCallusItem> PLANT_CALLUS = MOD_ITEMS.register("plant_callus", () -> new PlantCallusItem(new Item.Properties().tab(TabHandler.PLANTS)));
    public static final RegistryObject<Item> LIQUID_AGAR = MOD_ITEMS.register("liquid_agar", () -> new Item(new Item.Properties().tab(TabHandler.PLANTS)));
    public static final RegistryObject<ItemNameBlockItem> WILD_ONION = MOD_ITEMS.register("wild_onion", () -> new ItemNameBlockItem(ModBlocks.WILD_ONION.get(), new Item.Properties().food(WILD_ONION_PROPERTIES)));
    public static final RegistryObject<ItemNameBlockItem> WILD_POTATO_SEEDS = MOD_ITEMS.register("wild_potato_seeds", () -> new ItemNameBlockItem(ModBlocks.WILD_POTATO_PLANT.get(), new Item.Properties()));
    public static final RegistryObject<ItemNameBlockItem> RHAMNUS_SEEDS = MOD_ITEMS.register("rhamnus_salicifolius_seeds", () -> new ItemNameBlockItem(ModBlocks.RHAMNUS_SALICIFOLIUS.get(), new Item.Properties()));

    // Make sure DartGun, Dart, PotionDart, TrackerDart extend Item (and use RegistryObject!)
    public static final RegistryObject<Item> DART_GUN = MOD_ITEMS.register("dart_gun", DartGun::new);
    public static final RegistryObject<Item> DART_TRANQUILIZER = MOD_ITEMS.register("dart_tranquilizer", () -> new Dart((entity, stack) -> entity.tranquilize(2000), 0xFFFFFF));
    public static final RegistryObject<Item> DART_POISON_CYCASIN = MOD_ITEMS.register("dart_poison_cycasin", () -> new Dart((entity, stack) -> entity.addEffect(new MobEffectInstance(MobEffects.POISON, 2000)), 0xE2E1B8));
    public static final RegistryObject<Item> DART_POISON_EXECUTIONER_CONCOCTION = MOD_ITEMS.register("dart_poison_executioner_concoction", () -> new Dart((entity, stack) -> entity.setDeathIn(200), 0x000000));
    public static final RegistryObject<Item> DART_TIPPED_POTION = MOD_ITEMS.register("dart_tipped_potion", PotionDart::new);
    public static final RegistryObject<Item> TRACKER_DART = MOD_ITEMS.register("tracker_dart", TrackerDart::new);
    public static final RegistryObject<Item> FINE_NET = MOD_ITEMS.register("fine_net", FineNetItem::new);

        public static final RegistryObject<Item> BULLET = MOD_ITEMS.register("bullet", Bullet::new);
        public static final RegistryObject<Item> GLOCK = MOD_ITEMS.register("glock", Glock::new);
        public static final RegistryObject<Item> REMINGTON = MOD_ITEMS.register("remington", Remington::new);
        public static final RegistryObject<Item> SPAS12 = MOD_ITEMS.register("spas_12", SPAS12::new);
        public static final RegistryObject<Item> UTS15 = MOD_ITEMS.register("uts15", UTS15::new);


    //ITEMS
    public static final Map<AttractionSignEntity.AttractionSignType, RegistryObject<Item>> ATTRACTION_SIGNS = registerAttractionSigns();
    public static final RegistryObject<Item> PADDOCK_SIGN = MOD_ITEMS.register("paddock_sign", () -> new PaddockSignItem(new Item.Properties().stacksTo(1).tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> BLUEPRINT = MOD_ITEMS.register("blueprint", () -> new BlueprintItem(new Item.Properties().stacksTo(1).tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> GOAT_SPAWN_EGG = MOD_ITEMS.register("goat_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.GOAT, 0xEFEDE7, 0x7B3E20, new Item.Properties().tab(TabHandler.SPAWN_EGGS)));
    public static final RegistryObject<Item> CRAB_SPAWN_EGG = MOD_ITEMS.register("crab_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.CRAB, 0xEFEDE7, 0x7B3E20, new Item.Properties().tab(TabHandler.SPAWN_EGGS)));
    public static final RegistryObject<Item> SHARK_SPAWN_EGG = MOD_ITEMS.register("shark_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.SHARK, 0x808080, 0x404040, new Item.Properties().tab(TabHandler.SPAWN_EGGS)));
    public static final RegistryObject<Item> MURAL = MOD_ITEMS.register("mural", () -> new MuralItem(new Item.Properties().tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> GYROSPHERE_INTERIOR = MOD_ITEMS.register("gyrosphere_interior", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> GYROSPHERE_SEATS = MOD_ITEMS.register("gyrosphere_seats", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> GYROSPHERE_HOOP = MOD_ITEMS.register("gyrosphere_hoop", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PETRI_DISH = MOD_ITEMS.register("petri_dish", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PETRI_DISH_AGAR = MOD_ITEMS.register("petri_dish_agar", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PLANT_CELLS = MOD_ITEMS.register("plant_cells", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PLANT_CELLS_PETRI_DISH = MOD_ITEMS.register("plant_cells_petri_dish", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<EmptySyringeItem> EMPTY_SYRINGE = MOD_ITEMS.register("empty_syringe", () -> new EmptySyringeItem(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> TWIG_FOSSIL = MOD_ITEMS.register("twig_fossil", PlantFossilItem::new);
    public static final RegistryObject<Item> PLANT_FOSSIL = MOD_ITEMS.register("plant_fossil", PlantFossilItem::new);
    public static final RegistryObject<Item> PLANT_FOSSIL_0 = MOD_ITEMS.register("plant_fossil_0", PlantFossilItem::new);
    public static final RegistryObject<Item> PLANT_FOSSIL_1 = MOD_ITEMS.register("plant_fossil_1", PlantFossilItem::new);
    public static final RegistryObject<Item> PLANT_FOSSIL_2 = MOD_ITEMS.register("plant_fossil_2", PlantFossilItem::new);
    public static final RegistryObject<Item> PLANT_FOSSIL_3 = MOD_ITEMS.register("plant_fossil_3", PlantFossilItem::new);
    public static final RegistryObject<Item> IRON_BLADES = MOD_ITEMS.register("iron_blades", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> IRON_ROD = MOD_ITEMS.register("iron_rod", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));

    public static final RegistryObject<Item> DISC_DRIVE = MOD_ITEMS.register("disc_reader", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> COMPUTER_SCREEN = MOD_ITEMS.register("computer_screen", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> KEYBOARD = MOD_ITEMS.register("keyboard", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> LASER = MOD_ITEMS.register("laser", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> BASIC_CIRCUIT = MOD_ITEMS.register("basic_circuit", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> ADVANCED_CIRCUIT = MOD_ITEMS.register("advanced_circuit", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));

    public static final RegistryObject<Item> GYPSUM_POWDER = MOD_ITEMS.register("gypsum_powder", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> DNA_ANALYZER = MOD_ITEMS.register("dna_analyzer", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> LUNCH_BOX = MOD_ITEMS.register("lunch_box", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> STAMP_SET = MOD_ITEMS.register("stamp_set", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));

    public static final RegistryObject<Item> CAR_CHASSIS = MOD_ITEMS.register("car_chassis", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> ENGINE_SYSTEM = MOD_ITEMS.register("engine_system", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> CAR_SEATS = MOD_ITEMS.register("car_seats", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> CAR_TIRE = MOD_ITEMS.register("car_tire", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> CAR_WINDSCREEN = MOD_ITEMS.register("car_windscreen", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> UNFINISHED_CAR = MOD_ITEMS.register("unfinished_car", () -> new Item(new Item.Properties().tab(TabHandler.ITEMS)));

    public static final RegistryObject<UseOnEntityItem> GROWTH_SERUM = MOD_ITEMS.register("growth_serum", () -> new UseOnEntityItem(new Item.Properties().tab(TabHandler.ITEMS), (interaction) -> {
        if(interaction.getPlayer().getLevel().isClientSide)
            return InteractionResult.PASS;
        if (interaction.getTarget() instanceof DinosaurEntity dinosaur) {
            if (!dinosaur.isCarcass()) {
                dinosaur.increaseGrowthSpeed();
                interaction.getStack().shrink(1);
                if (!interaction.getPlayer().isCreative()) {
                    interaction.getPlayer().getInventory().add(new ItemStack(ModItems.EMPTY_SYRINGE.get()));
                }
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.FAIL;
    }));
    public static final RegistryObject<Item> BREEDING_WAND = MOD_ITEMS.register("breeding_wand", () -> new UseOnEntityItem(new Item.Properties().tab(TabHandler.ITEMS), interaction -> {
        if(interaction.getPlayer().getLevel().isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = interaction.getPlayer().getItemInHand(interaction.getHand());
        CompoundTag nbt = stack.getOrCreateTagElement("wand_info");
        Entity entity = interaction.getPlayer().getLevel().getEntity(nbt.getInt("dino_id"));
        if (interaction.getTarget() instanceof DinosaurEntity) {
            if (nbt.contains("dino_id", 99)) {
                if (entity instanceof DinosaurEntity && ((DinosaurEntity) entity).isMale() != ((DinosaurEntity) interaction.getTarget()).isMale() && !((DinosaurEntity) interaction.getTarget()).getDinosaur().isHybrid) {
                    ((DinosaurEntity) entity).breed((DinosaurEntity) interaction.getTarget());
                    ((DinosaurEntity) interaction.getTarget()).breed((DinosaurEntity) entity);
                } else if (entity != interaction.getTarget()) {
                    nbt.remove("dino_id");
                }
            } else {
                nbt.putInt("dino_id", interaction.getTarget().getId());
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }));

    //CREATIVE
    public static final RegistryObject<Item> BIRTHING_WAND = MOD_ITEMS.register("birthing_wand", () -> new UseOnEntityItem(new Item.Properties().tab(TabHandler.ITEMS), interaction -> {
        if(interaction.getPlayer().getLevel().isClientSide)
            return InteractionResult.PASS;
        if(interaction.getTarget() instanceof DinosaurEntity) {
            DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
            if (dino.isPregnant() && !dino.getDinosaur().isHybrid) {
                ((DinosaurEntity) interaction.getTarget()).giveBirth();
                return InteractionResult.SUCCESS;
            } else {
                String key = "dinosaur.birthingwand.";
                if(dino.isPregnant() && dino.getDinosaur().isHybrid) {
                    key += "hybrid";
                } else {
                    key += (dino.isMale() ? "male" : "not_pregnant");
                }
                interaction.getPlayer().displayClientMessage(Component.translatable(key), true);            }
        }
        return InteractionResult.FAIL;
    }));
    public static final RegistryObject<Item> PREGNANCY_TEST = MOD_ITEMS.register("pregnancy_test", () -> new UseOnEntityItem(new Item.Properties().tab(TabHandler.ITEMS), (interaction) -> {
        if(interaction.getPlayer().getLevel().isClientSide){
            return InteractionResult.PASS;
        }
        if(interaction.getTarget() instanceof DinosaurEntity) {//why was this the only one to have a remote check and even then it did it wrong
            DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
            interaction.getPlayer().displayClientMessage(Component.translatable("dinosaur.pregnancytest." + (dino.isMale() ? "male" : dino.isPregnant() ? "pregnant" : "not_pregnant")), true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }));

    public static final RegistryObject<Item> FORD_EXPLORER = MOD_ITEMS.register("ford_explorer", () -> new VehicleSpawnItem(ModEntities.FORD_EXPLORER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> FORD_EXPLORER_SNOW = MOD_ITEMS.register("ford_explorer_snow", () -> new VehicleSpawnItem(ModEntities.FORD_EXPLORER_SNOW.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> MONORAIL = MOD_ITEMS.register("monorail", () -> new VehicleSpawnItem(ModEntities.MONORAIL.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> JEEP_WRANGLER = MOD_ITEMS.register("jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> BLACK_JEEP_WRANGLER = MOD_ITEMS.register("black_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.BLACK_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> BLUE_JEEP_WRANGLER = MOD_ITEMS.register("blue_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.BLUE_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> GREEN_JEEP_WRANGLER = MOD_ITEMS.register("green_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.GREEN_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> LIME_JEEP_WRANGLER = MOD_ITEMS.register("lime_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.LIME_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PINK_JEEP_WRANGLER = MOD_ITEMS.register("pink_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.PINK_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> PURPLE_JEEP_WRANGLER = MOD_ITEMS.register("purple_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.PURPLE_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> SORNA_JEEP_WRANGLER = MOD_ITEMS.register("sorna_jeep_wrangler", () -> new VehicleSpawnItem(ModEntities.SORNA_JEEP_WRANGLER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> GYROSPHERE = MOD_ITEMS.register("gyrosphere", () -> new VehicleSpawnItem(ModEntities.GYROSPHERE.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
    public static final RegistryObject<Item> HELICOPTER = MOD_ITEMS.register("helicopter", () -> new VehicleSpawnItem(ModEntities.HELICOPTER.get(), new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));

    //WOOD BOATS
    public static final RegistryObject<Item> ARAUCARIA_BOAT = registerBoat("araucaria_boat", ModBoatType.ARAUCARIA, false);
    public static final RegistryObject<Item> ARAUCARIA_CHEST_BOAT = registerBoat("araucaria_chest_boat", ModBoatType.ARAUCARIA, true);
    public static final RegistryObject<Item> CALAMITES_BOAT = registerBoat("calamites_boat", ModBoatType.CALAMITES, false);
    public static final RegistryObject<Item> CALAMITES_CHEST_BOAT = registerBoat("calamites_chest_boat", ModBoatType.CALAMITES, true);
    public static final RegistryObject<Item> GINKGO_BOAT = registerBoat("ginkgo_boat", ModBoatType.GINKGO, false);
    public static final RegistryObject<Item> GINKGO_CHEST_BOAT = registerBoat("ginkgo_chest_boat", ModBoatType.GINKGO, true);
    public static final RegistryObject<Item> MAGNOLIA_BOAT = registerBoat("magnolia_boat", ModBoatType.MAGNOLIA, false);
    public static final RegistryObject<Item> MAGNOLIA_CHEST_BOAT = registerBoat("magnolia_chest_boat", ModBoatType.MAGNOLIA, true);
    public static final RegistryObject<Item> PHOENIX_BOAT = registerBoat("phoenix_boat", ModBoatType.PHOENIX, false);
    public static final RegistryObject<Item> PHOENIX_CHEST_BOAT = registerBoat("phoenix_chest_boat", ModBoatType.PHOENIX, true);
    public static final RegistryObject<Item> PSARONIUS_BOAT = registerBoat("psaronius_boat", ModBoatType.PSARONIUS, false);
    public static final RegistryObject<Item> PSARONIUS_CHEST_BOAT = registerBoat("psaronius_chest_boat", ModBoatType.PSARONIUS, true);

    //DECORATIONS
    public static final RegistryObject<Item> AMBER_KEYCHAIN = MOD_ITEMS.register("amber_keychain", () -> new Item(new Item.Properties().tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> AMBER_CANE = MOD_ITEMS.register("amber_cane", () -> new Item(new Item.Properties().tab(TabHandler.DECORATIONS)));
    public static final RegistryObject<Item> MR_DNA_KEYCHAIN = MOD_ITEMS.register("mr_dna_keychain", () -> new Item(new Item.Properties().tab(TabHandler.DECORATIONS)));


    public static final ArrayList<RegistryObject<BlockItem>> modBlocks = new ArrayList<>();

    public static final LinkedHashMap<Dinosaur, RegistryObject<DinosaurSpawnEggItem>> DINO_SPAWN_EGGS = new LinkedHashMap<>();
    public static final HashMap<Dinosaur, RegistryObject<DinosaurEggItem>> dinoEggs = new LinkedHashMap<>();
    public static final HashMap<Dinosaur, RegistryObject<HatchedEggItem>> hatchedDinoEggs = new LinkedHashMap<>();

    public static String getSpawnEggBaseName(Dinosaur dinosaur) {
        return dinosaur.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
    }

    public static String getSpawnEggItemId(Dinosaur dinosaur) {
        return "spawn_egg/" + getSpawnEggBaseName(dinosaur) + "_spawn_egg";
    }

    public static String getSpawnEggModelName(Dinosaur dinosaur) {
        return getSpawnEggBaseName(dinosaur) + "_spawn_egg";
    }

    @Nullable
    public static RegistryObject<DinosaurSpawnEggItem> getSpawnEgg(Dinosaur dinosaur) {
        return DINO_SPAWN_EGGS.get(dinosaur);
    }

    //BLOCK ITEMS
    public static final RegistryObject<SignItem> ARAUCARIA_SIGN = MOD_ITEMS.register("araucaria_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.ARAUCARIA_SIGN.get(),WoodBlocks.ARAUCARIA_WALL_SIGN.get()));
    public static final RegistryObject<SignItem> CALAMITES_SIGN = MOD_ITEMS.register("calamites_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.CALAMITES_SIGN.get(),WoodBlocks.CALAMITES_WALL_SIGN.get()));
    public static final RegistryObject<SignItem> GINKGO_SIGN = MOD_ITEMS.register("ginkgo_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.GINKGO_SIGN.get(),WoodBlocks.GINKGO_WALL_SIGN.get()));
    public static final RegistryObject<SignItem> MAGNOLIA_SIGN = MOD_ITEMS.register("magnolia_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.MAGNOLIA_SIGN.get(),WoodBlocks.MAGNOLIA_SIGN.get()));
    public static final RegistryObject<SignItem> PHOENIX_SIGN = MOD_ITEMS.register("phoenix_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.PHOENIX_SIGN.get(),WoodBlocks.PHOENIX_SIGN.get()));
    public static final RegistryObject<SignItem> PSARONIUS_SIGN = MOD_ITEMS.register("psaronius_sign",() -> new SignItem(
            new Item.Properties().stacksTo(16).tab(TabHandler.BLOCKS), WoodBlocks.PSARONIUS_SIGN.get(),WoodBlocks.PSARONIUS_WALL_SIGN.get()));
    public static HashMap<Dinosaur, LinkedHashMap<String, RegistryObject<Item>>> BONES = new HashMap<>();
    public static HashMap<Dinosaur, LinkedHashMap<String, RegistryObject<Item>>> FRESH_BONES = new HashMap<>();
    public static ArrayList<RegistryObject<Item>> ALL_BONES = new ArrayList<>();
    public static HashMap<Dinosaur, RegistryObject<DNAItem>> DINOSAUR_DNA = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<SoftTissueItem>> SOFT_TISSUE = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<Item>> MEATS = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<Item>> STEAKS = new HashMap<>();
    public static HashMap<DyeColor, RegistryObject<CultivatorItem>> CULTIVATORS = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<ActionFigureItem>> ACTION_FIGURES = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<FossilSkeletonItem>> FOSSIL_SKELETONS = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<FreshSkeletonItem>> FRESH_SKELETONS = new HashMap<>();
    public static List<Supplier<? extends Item>> ALL_MEATS = new ArrayList<>();
    public static ArrayList<String> USED_IDS = new ArrayList<>();
    public static HashMap<Plant, RegistryObject<Item>> PLANT_DNAS = new HashMap<>();
    public static HashMap<Dinosaur, RegistryObject<SyringeItem>> SYRINGES = new HashMap<>();
//    public static void printAllBonePaths() {
//        for (RegistryObject<Item> bone : ALL_BONES) {
//            ResourceLocation id = bone.getId();
//            if (!id.getPath().contains("fresh")) { // Exclude fresh bones
//                System.out.println("\"" + id.getNamespace() + ":" + id.getPath() + "\",");
//            }
//        }
//    }



    public static void register(IEventBus bus) {


        for(RegistryObject<Block>/*auto*/ a : ModBlocks.MOD_BLOCKS.getEntries()){
            ResourceLocation location = a.getId();
            if(hasNoItem(location)){
                continue;
            }
            if ("amber_mosquito".equals(location.getPath())) {
                // already registered above with custom properties
                modBlocks.add(MOSQUITO_AMBER);
                continue;
            }
            if ("amber_aphid".equals(location.getPath())) {
                // already registered above with custom properties
                modBlocks.add(APHID_AMBER);
                continue;
            }
            if ("sea_lamprey".equals(location.getPath())) {
                // already registered above with custom properties
                modBlocks.add(SEA_LAMPREY);
                continue;
            }
            if ("frozen_leech".equals(location.getPath())) {
                // already registered above with custom properties
                modBlocks.add(FROZEN_LEECH_ITEM);
                continue;
            }


            if (a.getId().getPath().endsWith("_sign")) {
                //do not autoregister sign blockitems
            } else {
                modBlocks.add(registerBlockItem(location.getPath(), a));
            }
        }

        for(Dinosaur a : Dinosaur.DINOSAUR_IDS.keySet()){

            if(a == Dinosaur.EMPTY)
                continue;

            String dinoName = a.getName();

            String formattedName = dinoName.toLowerCase(Locale.ROOT).replaceAll(" ", "_");

            RegistryObject<DinosaurSpawnEggItem> spawnEgg = registerDinosaurSpawnEgg(a, formattedName);
            if (spawnEgg != null) {
                DINO_SPAWN_EGGS.put(a, spawnEgg);
            }

            if(!a.givesDirectBirth()) {

                RegistryObject<DinosaurEggItem> egg = MOD_ITEMS.register("egg/egg_" + formattedName, () -> new DinosaurEggItem(new Item.Properties(), a));
                dinoEggs.put(a, egg);

            }

            RegistryObject<HatchedEggItem> hatchedEgg = MOD_ITEMS.register("hatched_egg/egg_" + formattedName, () -> new HatchedEggItem(new Item.Properties(), a));
            RegistryObject<DNAItem> dinoDna = MOD_ITEMS.register("dna/dna_" + formattedName, () -> new DNAItem(new Item.Properties(), a));
            RegistryObject<SoftTissueItem> softTissue = MOD_ITEMS.register("soft_tissue/soft_tissue_" + formattedName, () -> new SoftTissueItem(new Item.Properties().tab(TabHandler.DNA), a));
            RegistryObject<SyringeItem> dinoSyringe = MOD_ITEMS.register("syringe/syringe_" + formattedName, () -> new SyringeItem(new Item.Properties().tab(TabHandler.DNA), a));
            RegistryObject<ActionFigureItem> actionFigure = MOD_ITEMS.register("action_figure/action_figure_" + formattedName, () -> new ActionFigureItem(new Item.Properties().tab(TabHandler.DECORATIONS), a, false, true));
            RegistryObject<FreshSkeletonItem> freshSkeleton = MOD_ITEMS.register("skeleton/fresh/skeleton_fresh_" + formattedName, () -> new FreshSkeletonItem(new Item.Properties().tab(TabHandler.DECORATIONS), a));

            if (!a.isHybrid()) {
                RegistryObject<FossilSkeletonItem> fossilSkeleton = MOD_ITEMS.register("skeleton/fossil/skeleton_fossil_" + formattedName, () -> new FossilSkeletonItem(new Item.Properties().tab(TabHandler.DECORATIONS), a));
                ModItems.FOSSIL_SKELETONS.put(a, fossilSkeleton);
            }



            SOFT_TISSUE.put(a, softTissue);
            DINOSAUR_DNA.put(a, dinoDna);
            SYRINGES.put(a, dinoSyringe);
            hatchedDinoEggs.put(a, hatchedEgg);

            ACTION_FIGURES.put(a, actionFigure);
            FRESH_SKELETONS.put(a, freshSkeleton);


            //Register other dinosaur-dependent items
            registerFossilBonesForDino(a);
            registerFreshBonesForDino(a);
            registerMeatsForDino(a);
//            printAllBonePaths();

        }

        for(Plant p : PlantHandler.getPrehistoricPlantsAndTrees()){
            String name = p.getFormattedName();
            String formattedPlantName = name.toLowerCase(Locale.ROOT).replaceAll(" ", "_");

            String dnaPath = "dna/plants/dna_" + formattedPlantName;
            PLANT_DNAS.put(p, MOD_ITEMS.register(dnaPath, () -> new PlantDNAItem(p, new Item.Properties())));

            String softTissuePath = "soft_tissue/plants/soft_tissue_" + formattedPlantName;
            MOD_ITEMS.register(softTissuePath,
                    () -> new PlantSoftTissueItem(new Item.Properties().tab(TabHandler.DNA), p));
        }

        for(DyeColor d : DyeColor.values()){
            String name = "cultivate/cultivate_bottom_" + d.getName();
            System.out.println(name);

            CULTIVATORS.put(d, MOD_ITEMS.register(name, () -> new CultivatorItem(new Item.Properties().tab(TabHandler.BLOCKS), d)));
        }

        MOD_ITEMS.register(bus);

    }


    private static String correct(String path) {//wtf - gamma
        return path;
    }

    private static boolean hasNoItem(ResourceLocation location) {
        return location.getPath().equals("display_block") || location.getPath().equals("gypsum_bricks")
                || location.getPath().equals("hologram_block")
                || location.getPath().equals("peat_moss")
                || location.getPath().equals("krill_swarm")
                || location.getPath().equals("plankton_swarm")
                || location.getPath().startsWith("potted_")
                || location.getPath().equals("cultivator_bottom") || location.getPath().equals("cultivator_tob")
                || location.getPath().equals("cultivate_bottom") || location.getPath().equals("cultivate_top")
                || MOD_ITEMS.getEntries().contains(location);
    }


    @Nullable
    private static RegistryObject<DinosaurSpawnEggItem> registerDinosaurSpawnEgg(Dinosaur dinosaur, String formattedName) {
        RegistryObject<? extends EntityType<? extends DinosaurEntity>> entityType =
                DinosaurEntity.CLASS_TYPE_LIST.get(dinosaur.getDinosaurClass());
        if (entityType == null) {
            JurassicReborn.getLogger().warn("No entity type registered for dinosaur {}", dinosaur.getName());
            return null;
        }
        String id = "spawn_egg/" + formattedName + "_spawn_egg";
        return MOD_ITEMS.register(id, () -> new DinosaurSpawnEggItem(dinosaur, entityType));
    }

    @Nullable
    public static RegistryObject<Item> registerSingleBone(String boneName, Supplier<Item> sup, Dinosaur dino, boolean fresh){
        if(dino == DinosaurHandler.BLUE || dino == DinosaurHandler.CHARLIE || dino == DinosaurHandler.DELTA || dino == DinosaurHandler.ECHO){
            return null;
        }
        String formattedDinoName = dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_");
        String id = "/" + formattedDinoName + "_" + boneName;
        if(fresh){
            id = "fresh_bones".concat(id);
        }else{
            id = "bones".concat(id);
        }



        try {
            if(USED_IDS.contains(id))
                return null;
            USED_IDS.add(id);
            RegistryObject<Item> item = MOD_ITEMS.register(id, sup);
            ALL_BONES.add(item);
            return item;
        }catch(IllegalArgumentException e){
            JurassicReborn.getLogger().error("GOT YEETED BY " + e);
        }

        return null;
    }

    public static void registerFossilBonesForDino(Dinosaur dinosaur){

        if(dinosaur.isHybrid || dinosaur == Dinosaur.EMPTY || dinosaur.getBones() == null)//make sure the dinosaur isn't a hybrid
            return;



        LinkedHashMap<String, RegistryObject<Item>> DINO_BONES = new LinkedHashMap<>();
        for(String s : dinosaur.getBones()){
            RegistryObject<Item> item = registerSingleBone(s, () -> new FossilItem(new Item.Properties().tab(TabHandler.FOSSILS), s, false, dinosaur), dinosaur, false);
            if(item != null)
                DINO_BONES.put(s, item);
        }
        BONES.put(dinosaur, DINO_BONES);
    }

    public static void registerFreshBonesForDino(Dinosaur dino){

        LinkedHashMap<String, RegistryObject<Item>> fresh_bones = new LinkedHashMap<>();
        if(dino.getBones() == null){
            return;
        }
        for(String s : dino.getBones()){
            RegistryObject<Item> item = registerSingleBone(s, () -> new FossilItem(new Item.Properties().tab(TabHandler.FOSSILS), s, true, dino), dino, true);
            if(item != null)
                fresh_bones.put(s, item);
        }

        FRESH_BONES.put(dino, fresh_bones);
    }


    public static RegistryObject<Item> registerSingleRawMeat(Supplier<Item> sup, Dinosaur dino){
        return MOD_ITEMS.register("meat/meat_" + dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"), sup);

    }

    public static RegistryObject<Item> registerSingleSteak(Supplier<Item> sup, Dinosaur dino){
        return MOD_ITEMS.register("meat/steak_" + dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"), sup);
    }

    public static void registerMeatsForDino(Dinosaur dino){
        List<MobEffectInstance> cookedEffects = dino.applyMeatEffect(new ArrayList<>(), true);
        FoodProperties.Builder cookedProperties = new FoodProperties.Builder().nutrition(8).meat();
        for(MobEffectInstance i : cookedEffects){
            cookedProperties.effect(() -> i, 0.9f);
        }

        List<MobEffectInstance> rawEffects = dino.applyMeatEffect(new ArrayList<>(), false);
        FoodProperties.Builder rawProperties = new FoodProperties.Builder().nutrition(3).meat();
        for(MobEffectInstance i : rawEffects){
            rawProperties.effect(() -> i, 0.9f);
        }
        RegistryObject<Item> rawMeat = registerSingleRawMeat(() -> new DinosaurMeatItem(new Item.Properties().food(rawProperties.build()).tab(TabHandler.FOODS), false, dino), dino);
        RegistryObject<Item> steak = registerSingleSteak(() -> new DinosaurMeatItem(new Item.Properties().food(cookedProperties.build()).tab(TabHandler.FOODS), true, dino), dino);
        MEATS.put(dino, rawMeat);
        STEAKS.put(dino, steak);
        ALL_MEATS.add(rawMeat);
        ALL_MEATS.add(steak);
    }

    public static RegistryObject<BlockItem> registerBlockItem(String name, Supplier<Block> blockSupplier) {
        return MOD_ITEMS.register(name, () -> new BlockItem(blockSupplier.get(),
                new Item.Properties().tab(determineTab(blockSupplier.get()))));
    }

    private static RegistryObject<Item> registerBoat(String name, ModBoatType type, boolean hasChest) {
        RegistryObject<Item> registryObject = MOD_ITEMS.register(name, () -> new JurassicBoatItem(hasChest, type,
                new Item.Properties().stacksTo(1).tab(TabHandler.ITEMS)));
        if (hasChest) {
            type.setChestBoatItem(() -> registryObject.get());
        } else {
            type.setBoatItem(() -> registryObject.get());
        }
        return registryObject;
    }

    private static CreativeModeTab determineTab(Block block) {
        if (block instanceof AncientPlantBlock
                || block instanceof AncientCoralBlock
                || block instanceof WestIndianLilacBlock
                || block instanceof PaleoBaleBlock
                || block instanceof SmallPlantBlock
                || block instanceof DoublePlantBlock
                || block instanceof PeatBlock
                || block instanceof HeliconiaBlock
                || block instanceof MossBlock
                || block instanceof BaseCoralPlantBlock
                || block instanceof AncientCrop) {
            return TabHandler.PLANTS;
        } else if (block instanceof FossilBlock) {
            return TabHandler.FOSSILS;
        } else if (block instanceof ParkBenchBlock || block instanceof TrashCanBlock) {
                return TabHandler.DECORATIONS;
        } else if (block instanceof SkullDisplayBlock) {
            return null;
        } else {
            return TabHandler.BLOCKS;
        }
    }


    private static Map<AttractionSignEntity.AttractionSignType, RegistryObject<Item>> registerAttractionSigns() {
        Map<AttractionSignEntity.AttractionSignType, RegistryObject<Item>> map = new EnumMap<>(AttractionSignEntity.AttractionSignType.class);
        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            String name = "attraction_sign_" + type.name().toLowerCase(Locale.ROOT);
            map.put(
                    type,
                    MOD_ITEMS.register(
                            name,
                            () -> new AttractionSignItem(type, new Item.Properties().stacksTo(1).tab(TabHandler.DECORATIONS))
                    )
            );
        }
        return map;
    }
}
