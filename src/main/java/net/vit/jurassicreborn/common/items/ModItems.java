package net.vit.jurassicreborn.common.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.SkullDisplayBlock;
import net.vit.jurassicreborn.common.blocks.ancientplants.*;
import net.vit.jurassicreborn.common.blocks.ancientplants.DoublePlantBlock;
import net.vit.jurassicreborn.common.blocks.ancientplants.moss.PeatBlock;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorBottomBlock;
import net.vit.jurassicreborn.common.blocks.entities.cultivator.CultivatorTopBlock;
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
import net.vit.jurassicreborn.common.plants.WestIndianLilacBlock;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

import net.vit.jurassicreborn.common.entities.vehicle.boat.ModBoatType;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class ModItems {
    public static void init(){

    }

    public static final DeferredRegister.Items MOD_ITEMS = DeferredRegister.createItems(JurassicReborn.MODID);

    private static <T extends Item> DeferredItem<T> register(String name, DeferredHolder<CreativeModeTab, CreativeModeTab> tab, Supplier<T> supplier) {
        DeferredItem<T> obj = MOD_ITEMS.register(name, supplier);
        TabHandler.addToTab(tab.getId(), obj::toStack);
        return obj;
    }

    public static DeferredItem<Item> PLASTER_AND_BANDAGE = register("plaster_and_bandage", TabHandler.ITEMS, () -> new PlasterAndBandageItem(new Item.Properties()));

    public static DeferredItem<Item> AMBER = register("amber", TabHandler.ITEMS, () -> new Item(new Item.Properties()));
    public static DeferredItem<BlockItem> MOSQUITO_AMBER = register("amber_mosquito", TabHandler.ITEMS, () -> new BlockItem(ModBlocks.AMBER_MOSQUITO.get(), new Item.Properties()));
    public static DeferredItem<BlockItem> APHID_AMBER = register("amber_aphid", TabHandler.ITEMS, () -> new BlockItem(ModBlocks.AMBER_APHID.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> FROZEN_LEECH_ITEM = register("frozen_leech", TabHandler.ITEMS, () -> new BlockItem(ModBlocks.FROZEN_LEECH.get(), new Item.Properties()));
    public static DeferredItem<BlockItem> SEA_LAMPREY = register("sea_lamprey", TabHandler.ITEMS, () -> new BlockItem(ModBlocks.SEA_LAMPREY.get(), new Item.Properties()));
    public static final DeferredItem<EncasedFaunaFossilBlockItem> ENCASED_FAUNA_FOSSIL = register("encased_fauna_fossil", TabHandler.FOSSILS,
            () -> new EncasedFaunaFossilBlockItem(ModBlocks.ENCASED_FAUNA_FOSSIL.get(), Dinosaur.EMPTY, new Item.Properties()));
    public static final DeferredItem<BlockItem> FLORA_FOSSIL = register("flora_fossil", TabHandler.FOSSILS,
            () -> new BlockItem(ModBlocks.FLORA_FOSSIL.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> DEEPSLATE_FLORA_FOSSIL = register("deepslate_flora_fossil", TabHandler.FOSSILS,
            () -> new BlockItem(ModBlocks.DEEPSLATE_FLORA_FOSSIL.get(), new Item.Properties()));
    public static final DeferredItem<CageItem> CAGE = register("cage", TabHandler.ITEMS, CageItem::new);
    public static final DeferredItem<AquaticCageItem> AQUATIC_CAGE = register("aquatic_cage", TabHandler.ITEMS, AquaticCageItem::new);
//    public static DeferredItem<Item> DEFAULT_BONE = modItems.register("missing_bone", () -> new Item(new Item.Properties()));
    //FOODS
    public static final FoodProperties SHARK_MEAT_RAW_PROP = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build();
    public static final FoodProperties SHARK_MEAT_COOKED_PROP = new FoodProperties.Builder().nutrition(10).saturationModifier(1.2F).build();
    public static final FoodProperties CRAB_MEAT_RAW_PROP = new FoodProperties.Builder().nutrition(1).saturationModifier(0.3F).build();
    public static final FoodProperties CRAB_MEAT_COOKED_PROP = new FoodProperties.Builder().nutrition(5).saturationModifier(0.6F).build();
    public static final FoodProperties GOAT_RAW_PROP = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3F).build();
    public static final FoodProperties GOAT_COOKED_PROP = new FoodProperties.Builder().nutrition(6).saturationModifier(10.3F).build();
    public static final FoodProperties PHOENIX_FRUIT_PROPERTIES = new FoodProperties.Builder().nutrition(4).saturationModifier(0.3F).build();
    public static final DeferredItem<FossilizedEggItem> FOSSILIZED_EGG_1 = register("fossilized_egg_1", TabHandler.FOSSILS, () -> new FossilizedEggItem(new Item.Properties()));
    public static final DeferredItem<FossilizedEggItem> FOSSILIZED_EGG_2 = register("fossilized_egg_2", TabHandler.FOSSILS, () -> new FossilizedEggItem(new Item.Properties()));
    public static final DeferredItem<FossilizedEggItem> FOSSILIZED_EGG_3 = register("fossilized_egg_3", TabHandler.FOSSILS, () -> new FossilizedEggItem(new Item.Properties()));
    public static final FoodProperties CHILEAN_SEA_BASS_PROPERTIES /*why from chile in particular*/ = new FoodProperties.Builder().nutrition(10).saturationModifier(1.0f).build();
    public static final FoodProperties FUN_FRIES_PROPERTIES = new FoodProperties.Builder().nutrition(4).saturationModifier(2.0f).build();
    public static final FoodProperties OILED_POTATO_STRIPS_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.0f).build();

    public static final FoodProperties WILD_ONION_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties WILD_POTATO_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).build();
    public static final FoodProperties WILD_POTATO_COOKED_PROPERTIES = new FoodProperties.Builder().nutrition(6).saturationModifier(0.6f).build();
    public static final FoodProperties RHAMNUS_BERRIES_PROPERTIES = new FoodProperties.Builder().nutrition(3).saturationModifier(0.3f).build();
    public static final FoodProperties WEST_INDIAN_LILAC_BERRIES_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.1f).effect(() -> new MobEffectInstance(MobEffects.POISON, 1400, 1), 1.0f).build();
    public static final FoodProperties AJUGINUCULA_SMITHII_LEAVES_PROPERTIES = new FoodProperties.Builder().nutrition(1).saturationModifier(0.5f).build();

    public static final DeferredItem<Item> SHARK_MEAT_RAW = register("raw_shark_meat", TabHandler.FOODS, () -> new Item(new Item.Properties().food(SHARK_MEAT_RAW_PROP)));
    public static final DeferredItem<Item> SHARK_MEAT_COOKED = register("cooked_shark_meat", TabHandler.FOODS, () -> new Item(new Item.Properties().food(SHARK_MEAT_COOKED_PROP)));
    public static final DeferredItem<Item> CRAB_MEAT_RAW = register("raw_crab_meat", TabHandler.FOODS, () -> new Item(new Item.Properties().food(CRAB_MEAT_RAW_PROP)));
    public static final DeferredItem<Item> CRAB_MEAT_COOKED = register("cooked_crab_meat", TabHandler.FOODS, () -> new Item(new Item.Properties().food(CRAB_MEAT_COOKED_PROP)));
    public static final DeferredItem<Item> CHILEAN_SEA_BASS = register("chilean_sea_bass", TabHandler.FOODS, () -> new Item(new Item.Properties().food(CHILEAN_SEA_BASS_PROPERTIES)));
    public static final DeferredItem<Item> OILED_POTATO_STRIPS = register("oiled_potato_strips", TabHandler.FOODS, () -> new Item(new Item.Properties().food(OILED_POTATO_STRIPS_PROPERTIES)));
    public static final DeferredItem<Item> FUN_FRIES = register("fun_fries", TabHandler.FOODS, () -> new Item(new Item.Properties().food(FUN_FRIES_PROPERTIES)));
    public static final DeferredItem<Item> WILD_POTATO = register("wild_potato", TabHandler.FOODS, () -> new Item(new Item.Properties().food(WILD_POTATO_PROPERTIES)));
    public static final DeferredItem<Item> WILD_POTATO_COOKED = register("wild_potato_cooked", TabHandler.FOODS, () -> new Item(new Item.Properties().food(WILD_POTATO_COOKED_PROPERTIES)));
    public static final DeferredItem<Item> RHAMNUS_BERRIES = register("rhamnus_salicifolius_berries", TabHandler.FOODS, () -> new Item(new Item.Properties().food(RHAMNUS_BERRIES_PROPERTIES)));
    public static final DeferredItem<Item> WEST_INDIAN_LILAC_BERRIES = register("west_indian_lilac_berries", TabHandler.FOODS, () -> new Item(new Item.Properties().food(WEST_INDIAN_LILAC_BERRIES_PROPERTIES)));
    public static final DeferredItem<Item> PHOENIX_FRUIT = register("phoenix_fruit", TabHandler.FOODS, () -> new Item(new Item.Properties().food(PHOENIX_FRUIT_PROPERTIES)));
    public static final DeferredItem<ItemNameBlockItem> AJUGINUCULA_SMITHII_SEEDS = MOD_ITEMS.register("ajuginucula_smithii_seeds", () -> new ItemNameBlockItem(ModBlocks.AJUGINUCULA_SMITHII.get(), new Item.Properties()));
    public static final DeferredItem<Item> AJUGINUCULA_SMITHII_LEAVES = register("ajuginucula_smithii_leaves", TabHandler.PLANTS, () -> new Item(new Item.Properties().food(AJUGINUCULA_SMITHII_LEAVES_PROPERTIES)));
    public static final DeferredItem<Item> AJUGINUCULA_SMITHII_OIL = register("ajuginucula_smithii_oil", TabHandler.PLANTS, () -> new Item(new Item.Properties()));


    public static final DeferredItem<Item> GOAT_RAW = register("goat_raw", TabHandler.FOODS, () -> new Item(new Item.Properties().food(GOAT_RAW_PROP)));
    public static final DeferredItem<Item> GOAT_COOKED = register("goat_cooked", TabHandler.FOODS, () -> new Item(new Item.Properties().food(GOAT_COOKED_PROP)));
    public static final DeferredItem<BugItem> CRICKETS = register("crickets", TabHandler.ITEMS, () ->
            new BugItem(stack -> {
                Item item = stack.getItem();
                Block block = Block.byItem(item);
                if (item == Items.WHEAT_SEEDS) return 1;
                else if (block == Blocks.GRASS_BLOCK) return 2;
                else if (item == Items.WHEAT) return 3;
                else if (block == Blocks.OAK_LEAVES) return 7;
                else if (block == Blocks.HAY_BLOCK) return 27;
                return 0;
            }));

    public static final DeferredItem<BugItem> COCKROACHES = register("cockroaches", TabHandler.ITEMS,
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

    public static final DeferredItem<BugItem> MEALWORM_BEETLES = register("mealworm_beetles", TabHandler.ITEMS,
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
    public static final DeferredItem<Item> JOURNAL_CHEF_ALEJANDRO = register("journal_chef_alejandro", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.CHEF_ALEJANDRO));
    public static final DeferredItem<Item> JOURNAL_DENNIS_NEDRY = register("journal_dennis_nedry", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.DENNIS_NEDRY));
    public static final DeferredItem<Item> JOURNAL_DR_GERRY_HARDING = register("journal_dr_gerry_harding", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.DR_GERRY_HARDING));
    public static final DeferredItem<Item> JOURNAL_DR_HENRY_WU = register("journal_dr_henry_wu", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.DR_HENRY_WU));
    public static final DeferredItem<Item> JOURNAL_DR_LAURA_SORKIN = register("journal_dr_laura_sorkin", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.DR_LAURA_SORKIN));
    public static final DeferredItem<Item> JOURNAL_ED_REGIS = register("journal_ed_regis", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.ED_REGIS));
    public static final DeferredItem<Item> JOURNAL_JOHN_HAMMOND = register("journal_john_hammond", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.JOHN_HAMMOND));
    public static final DeferredItem<Item> JOURNAL_RAY_ARNOLD = register("journal_ray_arnold", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.RAY_ARNOLD));
    public static final DeferredItem<Item> JOURNAL_ROBERT_MULDOON = register("journal_robert_muldoon", TabHandler.ITEMS, () -> new JournalItem(JournalItem.JournalType.ROBERT_MULDOON));
    public static final DeferredItem<SwarmItem> PLANKTON = register("plankton", TabHandler.ITEMS,  () -> new SwarmItem(ModBlocks.PLANKTON_SWARM.get(), new Item.Properties()));
    public static final DeferredItem<SwarmItem> KRILL = register("krill", TabHandler.ITEMS,  () -> new SwarmItem(ModBlocks.KRILL_SWARM.get(), new Item.Properties()));
    public static final DeferredItem<Item> FIELD_GUIDE = register("field_guide", TabHandler.ITEMS,  () -> new FieldGuideItem(new Item.Properties()));
    public static final DeferredItem<Item> JURASSICREBORN_THEME_DISC = register("disc_jurassicreborn_theme", TabHandler.ITEMS, () -> new Item(new Item.Properties().jukeboxPlayable(ModJukeboxSongs.JURASSICREBORN_THEME).rarity(Rarity.RARE).stacksTo(1)));
    public static final DeferredItem<Item> TROODONS_AND_RAPTORS_DISC = register("disc_troodons_and_raptors", TabHandler.ITEMS, () -> new Item(new Item.Properties().jukeboxPlayable(ModJukeboxSongs.TROODONS_AND_RAPTORS).rarity(Rarity.RARE).stacksTo(1)));
    public static final DeferredItem<Item> DONT_MOVE_A_MUSCLE_DISC = register("disc_dont_move_a_muscle", TabHandler.ITEMS, () -> new Item(new Item.Properties().jukeboxPlayable(ModJukeboxSongs.DONT_MOVE_A_MUSCLE).rarity(Rarity.RARE).stacksTo(1)));
    public static final DeferredItem<Item> PALEO_PAD = register("paleo_pad", TabHandler.ITEMS, PaleoPadItem::new);
    public static final DeferredItem<StorageDiscItem> STORAGE_DISC = register("storage_disc", TabHandler.ITEMS,  () -> new StorageDiscItem(new Item.Properties()));
    public static DeferredItem<BlockItem> GYPSUM_BRICKS = register("gypsum_bricks", TabHandler.ITEMS, () -> new BlockItem(ModBlocks.GYPSUM_BRICKS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> HOLOGRAM_BLOCK = register("hologram_block", TabHandler.DECORATIONS,  () -> new BlockItem(ModBlocks.HOLOGRAM_BLOCK.get(), new Item.Properties()));
    public static final DeferredItem<Item> EMPTY_TEST_TUBE = register("empty_test_tube", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DNA_NUCLEOTIDES = register("dna_base_material", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<FaunaFossilBlockItem> FAUNA_FOSSIL_BLOCK = register("fauna_fossil_block_item", TabHandler.FOSSILS, () -> new FaunaFossilBlockItem(ModBlocks.FAUNA_FOSSIL.get(), new Item.Properties()));
    public static final DeferredItem<IncubatorEnvironmentItem> PEAT_MOSS_BLOCK = register("peat_moss", TabHandler.BLOCKS,  () -> new IncubatorEnvironmentItem(ModBlocks.PEAT_MOSS.get(), new Item.Properties()));

    //PLANTS
    public static final DeferredItem<PlantCallusItem> PLANT_CALLUS = register("plant_callus", TabHandler.PLANTS,  () -> new PlantCallusItem(new Item.Properties()));
    public static final DeferredItem<Item> LIQUID_AGAR = register("liquid_agar", TabHandler.PLANTS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<ItemNameBlockItem> WILD_ONION = MOD_ITEMS.register("wild_onion", () -> new ItemNameBlockItem(ModBlocks.WILD_ONION.get(), new Item.Properties().food(WILD_ONION_PROPERTIES)));
    public static final DeferredItem<ItemNameBlockItem> WILD_POTATO_SEEDS = MOD_ITEMS.register("wild_potato_seeds", () -> new ItemNameBlockItem(ModBlocks.WILD_POTATO_PLANT.get(), new Item.Properties()));
    public static final DeferredItem<ItemNameBlockItem> RHAMNUS_SEEDS = MOD_ITEMS.register("rhamnus_salicifolius_seeds", () -> new ItemNameBlockItem(ModBlocks.RHAMNUS_SALICIFOLIUS.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> ARAUCARIA_SAPLING = register("araucaria_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.ARAUCARIA_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> CALAMITES_SAPLING = register("calamites_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.CALAMITES_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> GINKGO_SAPLING = register("ginkgo_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.GINKGO_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> MAGNOLIA_SAPLING = register("magnolia_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.MAGNOLIA_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PHOENIX_SAPLING = register("phoenix_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.PHOENIX_SAPLING.get(), new Item.Properties()));
    public static final DeferredItem<BlockItem> PSARONIUS_SAPLING = register("psaronius_sapling", TabHandler.PLANTS,
            () -> new BlockItem(ModBlocks.PSARONIUS_SAPLING.get(), new Item.Properties()));

    // Make sure DartGun, Dart, PotionDart, TrackerDart extend Item (and use DeferredItem!)
    public static final DeferredItem<Item> DART_GUN = register("dart_gun", TabHandler.ITEMS, DartGun::new);
    public static final DeferredItem<Item> DART_TRANQUILIZER = register("dart_tranquilizer", TabHandler.ITEMS, () -> new Dart((entity, stack) -> entity.tranquilize(2000), 0xFFFFFF));
    public static final DeferredItem<Item> DART_POISON_CYCASIN = register("dart_poison_cycasin", TabHandler.ITEMS, () -> new Dart((entity, stack) -> entity.addEffect(new MobEffectInstance(MobEffects.POISON, 2000)), 0xE2E1B8));
    public static final DeferredItem<Item> DART_POISON_EXECUTIONER_CONCOCTION = register("dart_poison_executioner_concoction", TabHandler.ITEMS, () -> new Dart((entity, stack) -> entity.setDeathIn(200), 0x000000));
    public static final DeferredItem<Item> DART_TIPPED_POTION = register("dart_tipped_potion", TabHandler.ITEMS, PotionDart::new);
    public static final DeferredItem<Item> TRACKER_DART = register("tracker_dart", TabHandler.ITEMS, TrackerDart::new);
    public static final DeferredItem<Item> FINE_NET = register("fine_net", TabHandler.ITEMS, FineNetItem::new);

    public static final DeferredItem<Item> BULLET = register("bullet", TabHandler.ITEMS, Bullet::new);
    public static final DeferredItem<Item> GLOCK = register("glock", TabHandler.ITEMS, Glock::new);
    public static final DeferredItem<Item> REMINGTON = register("remington", TabHandler.ITEMS, Remington::new);
    public static final DeferredItem<Item> SPAS12 = register("spas_12", TabHandler.ITEMS, SPAS12::new);
    public static final DeferredItem<Item> UTS15 = register("uts15", TabHandler.ITEMS, UTS15::new);


    //ITEMS
    public static final Map<AttractionSignEntity.AttractionSignType, DeferredItem<Item>> ATTRACTION_SIGNS = registerAttractionSigns();
    public static final DeferredItem<Item> PADDOCK_SIGN = register("paddock_sign", TabHandler.DECORATIONS,  () -> new PaddockSignItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> BLUEPRINT = register("blueprint", TabHandler.DECORATIONS,  () -> new BlueprintItem(new Item.Properties().stacksTo(1)));
    public static final DeferredItem<Item> GOAT_SPAWN_EGG = register("goat_spawn_egg", TabHandler.SPAWN_EGGS,  () -> new DeferredSpawnEggItem(ModEntities.GOAT, 0xEFEDE7, 0x7B3E20, new Item.Properties()));
    public static final DeferredItem<Item> CRAB_SPAWN_EGG = register("crab_spawn_egg", TabHandler.SPAWN_EGGS,  () -> new DeferredSpawnEggItem(ModEntities.CRAB, 0xEFEDE7, 0x7B3E20, new Item.Properties()));
    public static final DeferredItem<Item> SHARK_SPAWN_EGG = register("shark_spawn_egg", TabHandler.SPAWN_EGGS,  () -> new DeferredSpawnEggItem(ModEntities.SHARK, 0x808080, 0x404040, new Item.Properties()));
    public static final DeferredItem<Item> MURAL = register("mural", TabHandler.DECORATIONS,  () -> new MuralItem(new Item.Properties()));
    public static final DeferredItem<Item> GYROSPHERE_INTERIOR = register("gyrosphere_interior", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GYROSPHERE_SEATS = register("gyrosphere_seats", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> GYROSPHERE_HOOP = register("gyrosphere_hoop", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PETRI_DISH = register("petri_dish", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PETRI_DISH_AGAR = register("petri_dish_agar", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLANT_CELLS = register("plant_cells", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> PLANT_CELLS_PETRI_DISH = register("plant_cells_petri_dish", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<EmptySyringeItem> EMPTY_SYRINGE = register("empty_syringe", TabHandler.ITEMS,  () -> new EmptySyringeItem(new Item.Properties()));
    public static final DeferredItem<Item> TWIG_FOSSIL = register("twig_fossil", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> PLANT_FOSSIL = register("plant_fossil", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> PLANT_FOSSIL_0 = register("plant_fossil_0", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> PLANT_FOSSIL_1 = register("plant_fossil_1", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> PLANT_FOSSIL_2 = register("plant_fossil_2", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> PLANT_FOSSIL_3 = register("plant_fossil_3", TabHandler.FOSSILS, PlantFossilItem::new);
    public static final DeferredItem<Item> IRON_BLADES = register("iron_blades", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> IRON_ROD = register("iron_rod", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> DISC_DRIVE = register("disc_reader", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> COMPUTER_SCREEN = register("computer_screen", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> KEYBOARD = register("keyboard", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LASER = register("laser", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> BASIC_CIRCUIT = register("basic_circuit", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ADVANCED_CIRCUIT = register("advanced_circuit", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> GYPSUM_POWDER = register("gypsum_powder", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> DNA_ANALYZER = register("dna_analyzer", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> LUNCH_BOX = register("lunch_box", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> STAMP_SET = register("stamp_set", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));

    public static final DeferredItem<Item> CAR_CHASSIS = register("car_chassis", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> ENGINE_SYSTEM = register("engine_system", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CAR_SEATS = register("car_seats", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CAR_TIRE = register("car_tire", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> CAR_WINDSCREEN = register("car_windscreen", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> UNFINISHED_CAR = register("unfinished_car", TabHandler.ITEMS,  () -> new Item(new Item.Properties()));

    public static final DeferredItem<UseOnEntityItem> GROWTH_SERUM = register("growth_serum", TabHandler.ITEMS, () -> new UseOnEntityItem(new Item.Properties(), (interaction) -> {
        if(interaction.getPlayer().level().isClientSide)
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
    public static final DeferredItem<Item> BREEDING_WAND = register("breeding_wand", TabHandler.ITEMS, () -> new UseOnEntityItem(new Item.Properties(), interaction -> {
        if(interaction.getPlayer().level().isClientSide)
            return InteractionResult.PASS;
        ItemStack stack = interaction.getPlayer().getItemInHand(interaction.getHand());
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            data = CustomData.of(new CompoundTag());
            stack.set(DataComponents.CUSTOM_DATA, data);
        }
        CompoundTag nbt = data.getUnsafe().getCompound("wand_info");
        data.getUnsafe().put("wand_info", nbt);
        Entity entity = interaction.getPlayer().level().getEntity(nbt.getInt("dino_id"));
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
    public static final DeferredItem<Item> BIRTHING_WAND = register("birthing_wand", TabHandler.ITEMS, () -> new UseOnEntityItem(new Item.Properties(), interaction -> {
        if(interaction.getPlayer().level().isClientSide)
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
    public static final DeferredItem<Item> PREGNANCY_TEST = register("pregnancy_test", TabHandler.ITEMS, () -> new UseOnEntityItem(new Item.Properties(), (interaction) -> {
        if(interaction.getPlayer().level().isClientSide){
            return InteractionResult.PASS;
        }
        if(interaction.getTarget() instanceof DinosaurEntity) {//why was this the only one to have a remote check and even then it did it wrong
            DinosaurEntity dino = ((DinosaurEntity)interaction.getTarget());
            interaction.getPlayer().displayClientMessage(Component.translatable("dinosaur.pregnancytest." + (dino.isMale() ? "male" : dino.isPregnant() ? "pregnant" : "not_pregnant")), true);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }));


    public static final DeferredItem<Item> FORD_EXPLORER =
            register("ford_explorer", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.FORD_EXPLORER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FORD_EXPLORER_SNOW =
            register("ford_explorer_snow", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.FORD_EXPLORER_SNOW, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> MONORAIL =
            register("monorail", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.MONORAIL, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> JEEP_WRANGLER =
            register("jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLACK_JEEP_WRANGLER =
            register("black_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.BLACK_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> BLUE_JEEP_WRANGLER =
            register("blue_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.BLUE_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> GREEN_JEEP_WRANGLER =
            register("green_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.GREEN_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> LIME_JEEP_WRANGLER =
            register("lime_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.LIME_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> PINK_JEEP_WRANGLER =
            register("pink_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.PINK_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> PURPLE_JEEP_WRANGLER =
            register("purple_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.PURPLE_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> SORNA_JEEP_WRANGLER =
            register("sorna_jeep_wrangler", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.SORNA_JEEP_WRANGLER, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> GYROSPHERE =
            register("gyrosphere", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.GYROSPHERE, new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> HELICOPTER =
            register("helicopter", TabHandler.ITEMS,
                    () -> new VehicleSpawnItem(ModEntities.HELICOPTER, new Item.Properties().stacksTo(1)));

    //WOOD BOATS
    public static final DeferredItem<Item> ARAUCARIA_BOAT = registerBoat("araucaria_boat", ModBoatType.ARAUCARIA, false);
    public static final DeferredItem<Item> ARAUCARIA_CHEST_BOAT = registerBoat("araucaria_chest_boat", ModBoatType.ARAUCARIA, true);
    public static final DeferredItem<Item> CALAMITES_BOAT = registerBoat("calamites_boat", ModBoatType.CALAMITES, false);
    public static final DeferredItem<Item> CALAMITES_CHEST_BOAT = registerBoat("calamites_chest_boat", ModBoatType.CALAMITES, true);
    public static final DeferredItem<Item> GINKGO_BOAT = registerBoat("ginkgo_boat", ModBoatType.GINKGO, false);
    public static final DeferredItem<Item> GINKGO_CHEST_BOAT = registerBoat("ginkgo_chest_boat", ModBoatType.GINKGO, true);
    public static final DeferredItem<Item> MAGNOLIA_BOAT = registerBoat("magnolia_boat", ModBoatType.MAGNOLIA, false);
    public static final DeferredItem<Item> MAGNOLIA_CHEST_BOAT = registerBoat("magnolia_chest_boat", ModBoatType.MAGNOLIA, true);
    public static final DeferredItem<Item> PHOENIX_BOAT = registerBoat("phoenix_boat", ModBoatType.PHOENIX, false);
    public static final DeferredItem<Item> PHOENIX_CHEST_BOAT = registerBoat("phoenix_chest_boat", ModBoatType.PHOENIX, true);
    public static final DeferredItem<Item> PSARONIUS_BOAT = registerBoat("psaronius_boat", ModBoatType.PSARONIUS, false);
    public static final DeferredItem<Item> PSARONIUS_CHEST_BOAT = registerBoat("psaronius_chest_boat", ModBoatType.PSARONIUS, true);

    //DECORATIONS
    public static final DeferredItem<Item> AMBER_KEYCHAIN = register("amber_keychain", TabHandler.DECORATIONS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> AMBER_CANE = register("amber_cane", TabHandler.DECORATIONS,  () -> new Item(new Item.Properties()));
    public static final DeferredItem<Item> MR_DNA_KEYCHAIN = register("mr_dna_keychain", TabHandler.DECORATIONS,  () -> new Item(new Item.Properties()));


    public static final ArrayList<DeferredItem<BlockItem>> modBlocks = new ArrayList<>();

    public static final LinkedHashMap<Dinosaur, DeferredItem<DinosaurSpawnEggItem>> DINO_SPAWN_EGGS = new LinkedHashMap<>();
    public static final HashMap<Dinosaur, DeferredItem<DinosaurEggItem>> dinoEggs = new LinkedHashMap<>();
    public static final HashMap<Dinosaur, DeferredItem<HatchedEggItem>> hatchedDinoEggs = new LinkedHashMap<>();

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
    public static DeferredItem<DinosaurSpawnEggItem> getSpawnEgg(Dinosaur dinosaur) {
        return DINO_SPAWN_EGGS.get(dinosaur);
    }

    //BLOCK ITEMS
    public static final DeferredItem<SignItem> ARAUCARIA_SIGN = register("araucaria_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.ARAUCARIA_SIGN.get(),WoodBlocks.ARAUCARIA_WALL_SIGN.get()));
    public static final DeferredItem<SignItem> CALAMITES_SIGN = register("calamites_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.CALAMITES_SIGN.get(),WoodBlocks.CALAMITES_WALL_SIGN.get()));
    public static final DeferredItem<SignItem> GINKGO_SIGN = register("ginkgo_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.GINKGO_SIGN.get(),WoodBlocks.GINKGO_WALL_SIGN.get()));
    public static final DeferredItem<SignItem> MAGNOLIA_SIGN = register("magnolia_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.MAGNOLIA_SIGN.get(),WoodBlocks.MAGNOLIA_SIGN.get()));
    public static final DeferredItem<SignItem> PHOENIX_SIGN = register("phoenix_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.PHOENIX_SIGN.get(),WoodBlocks.PHOENIX_SIGN.get()));
    public static final DeferredItem<SignItem> PSARONIUS_SIGN = register("psaronius_sign", TabHandler.BLOCKS, () -> new SignItem(
            new Item.Properties().stacksTo(16), WoodBlocks.PSARONIUS_SIGN.get(),WoodBlocks.PSARONIUS_WALL_SIGN.get()));
    public static final DeferredItem<HangingSignItem> ARAUCARIA_HANGING_SIGN = register("araucaria_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.ARAUCARIA_HANGING_SIGN.get(), WoodBlocks.ARAUCARIA_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<HangingSignItem> CALAMITES_HANGING_SIGN = register("calamites_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.CALAMITES_HANGING_SIGN.get(), WoodBlocks.CALAMITES_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<HangingSignItem> GINKGO_HANGING_SIGN = register("ginkgo_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.GINKGO_HANGING_SIGN.get(), WoodBlocks.GINKGO_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<HangingSignItem> MAGNOLIA_HANGING_SIGN = register("magnolia_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.MAGNOLIA_HANGING_SIGN.get(), WoodBlocks.MAGNOLIA_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<HangingSignItem> PHOENIX_HANGING_SIGN = register("phoenix_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.PHOENIX_HANGING_SIGN.get(), WoodBlocks.PHOENIX_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static final DeferredItem<HangingSignItem> PSARONIUS_HANGING_SIGN = register("psaronius_hanging_sign", TabHandler.BLOCKS, () -> new HangingSignItem(WoodBlocks.PSARONIUS_HANGING_SIGN.get(), WoodBlocks.PSARONIUS_WALL_HANGING_SIGN.get(), new Item.Properties().stacksTo(16)));
    public static HashMap<Dinosaur, LinkedHashMap<String, DeferredItem<Item>>> BONES = new HashMap<>();
    public static HashMap<Dinosaur, LinkedHashMap<String, DeferredItem<Item>>> FRESH_BONES = new HashMap<>();
    public static ArrayList<DeferredItem<Item>> ALL_BONES = new ArrayList<>();
    public static HashMap<Dinosaur, DeferredItem<DNAItem>> DINOSAUR_DNA = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<SoftTissueItem>> SOFT_TISSUE = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<Item>> MEATS = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<Item>> STEAKS = new HashMap<>();
    public static HashMap<DyeColor, DeferredItem<CultivatorItem>> CULTIVATORS = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<ActionFigureItem>> ACTION_FIGURES = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<FossilSkeletonItem>> FOSSIL_SKELETONS = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<FreshSkeletonItem>> FRESH_SKELETONS = new HashMap<>();
    public static List<Supplier<? extends Item>> ALL_MEATS = new ArrayList<>();
    public static ArrayList<String> USED_IDS = new ArrayList<>();
    public static HashMap<Plant, DeferredItem<Item>> PLANT_DNAS = new HashMap<>();
    public static HashMap<Dinosaur, DeferredItem<SyringeItem>> SYRINGES = new HashMap<>();
//    public static void printAllBonePaths() {
//        for (DeferredItem<Item> bone : ALL_BONES) {
//            ResourceLocation id = bone.getId();
//            if (!id.getPath().contains("fresh")) { // Exclude fresh bones
//                System.out.println("\"" + id.getNamespace() + ":" + id.getPath() + "\",");
//            }
//        }
//    }



    public static void register(IEventBus bus) {
        PlantHandler.init();
        DinosaurHandler.doDinosInit();

        for(DeferredHolder<Block, ? extends Block> a : ModBlocks.MOD_BLOCKS.getEntries()){
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
                modBlocks.add(registerBlockItem(location.getPath(), a::get));
            }
        }

        for(Dinosaur a : Dinosaur.DINOSAUR_IDS.keySet()){

            if(a == Dinosaur.EMPTY)
                continue;

            String dinoName = a.getName();

            String formattedName = dinoName.toLowerCase(Locale.ROOT).replaceAll(" ", "_");

            DeferredItem<DinosaurSpawnEggItem> spawnEgg = registerDinosaurSpawnEgg(a, formattedName);
            if (spawnEgg != null) {
                DINO_SPAWN_EGGS.put(a, spawnEgg);
            }

            if(!a.givesDirectBirth()) {

                DeferredItem<DinosaurEggItem> egg = MOD_ITEMS.register("egg/egg_" + formattedName, () -> new DinosaurEggItem(new Item.Properties(), a));
                dinoEggs.put(a, egg);
                TabHandler.addToTab(TabHandler.DNA.getId(), () -> {
                    ItemStack stack = egg.get().getDefaultInstance();
                    CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
                    tag.putBoolean("isCreative", true);
                    ItemStackNbtUtil.setTag(stack, tag);
                    return stack;
                });

            }

            DeferredItem<HatchedEggItem> hatchedEgg = MOD_ITEMS.register("hatched_egg/egg_" + formattedName, () -> new HatchedEggItem(new Item.Properties(), a));
            TabHandler.addToTab(TabHandler.DNA.getId(), () -> {
                ItemStack stack = hatchedEgg.get().getDefaultInstance();
                CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
                tag.putBoolean("isCreative", true);
                ItemStackNbtUtil.setTag(stack, tag);
                return stack;
            });
            DeferredItem<DNAItem> dinoDna = register("dna/dna_" + formattedName, TabHandler.DNA, () -> new DNAItem(new Item.Properties(), a));
            DeferredItem<SoftTissueItem> softTissue = register("soft_tissue/soft_tissue_" + formattedName, TabHandler.DNA, () -> new SoftTissueItem(new Item.Properties(), a));
            DeferredItem<SyringeItem> dinoSyringe = register("syringe/syringe_" + formattedName, TabHandler.DNA, () -> new SyringeItem(new Item.Properties(), a));
            DeferredItem<ActionFigureItem> actionFigure = register("action_figure/action_figure_" + formattedName, TabHandler.DECORATIONS, () -> new ActionFigureItem(new Item.Properties(), a, false, true));
            DeferredItem<FreshSkeletonItem> freshSkeleton = register("skeleton/fresh/skeleton_fresh_" + formattedName, TabHandler.DECORATIONS, () -> new FreshSkeletonItem(new Item.Properties(), a));

            if (!a.isHybrid()) {
                DeferredItem<FossilSkeletonItem> fossilSkeleton = register("skeleton/fossil/skeleton_fossil_" + formattedName, TabHandler.DECORATIONS, () -> new FossilSkeletonItem(new Item.Properties(), a));
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
            PLANT_DNAS.put(p, register(dnaPath, TabHandler.DNA, () -> new PlantDNAItem(p, new Item.Properties())));

            String softTissuePath = "soft_tissue/plants/soft_tissue_" + formattedPlantName;
            register(softTissuePath,
                    TabHandler.DNA,
                    () -> new PlantSoftTissueItem(new Item.Properties(), p));

            TabHandler.addToTab(TabHandler.PLANTS.getId(), () -> {
                ItemStack stack = ModItems.PLANT_CALLUS.get().getPlantInstance(p, 100);
                CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
                tag.putBoolean("isCreative", true);
                ItemStackNbtUtil.setTag(stack, tag);
                return stack;
            });
        }

        for(DyeColor d : DyeColor.values()){
            String name = "cultivate/cultivate_bottom_" + d.getName();
            System.out.println(name);

            CULTIVATORS.put(d, register(name, TabHandler.BLOCKS, () -> new CultivatorItem(new Item.Properties(), d)));
        }

        MOD_ITEMS.register(bus);

    }


    private static String correct(String path) {//wtf - gamma
        return path;
    }

    private static boolean hasNoItem(ResourceLocation location) {
        return location.getPath().equals("display_block")
                || location.getPath().equals("gypsum_bricks")
                || location.getPath().equals("hologram_block")
                || location.getPath().equals("peat_moss")
                || location.getPath().equals("krill_swarm")
                || location.getPath().equals("encased_fauna_fossil")
                || location.getPath().startsWith("potted_")
                || location.getPath().equals("fauna_fossil")
                || location.getPath().equals("flora_fossil")
                || location.getPath().equals("deepslate_flora_fossil")
                || location.getPath().equals("plankton_swarm")
                || location.getPath().endsWith("_sapling")
                || location.getPath().startsWith("cultivator_")
                || MOD_ITEMS.getEntries().stream().anyMatch(obj -> obj.getId().equals(location));
    }



    @Nullable
    private static DeferredItem<DinosaurSpawnEggItem> registerDinosaurSpawnEgg(Dinosaur dinosaur, String formattedName) {
        DeferredHolder<EntityType<?>, ? extends EntityType<? extends DinosaurEntity>> entityType =
                DinosaurEntity.CLASS_TYPE_LIST.get(dinosaur.getDinosaurClass());
        if (entityType == null) {
            JurassicReborn.getLogger().warn("No entity type registered for dinosaur {}", dinosaur.getName());
            return null;
        }
        String id = "spawn_egg/" + formattedName + "_spawn_egg";
        DeferredItem<DinosaurSpawnEggItem> spawnEgg = MOD_ITEMS.register(id, () -> new DinosaurSpawnEggItem(dinosaur, entityType));
        TabHandler.addToTab(TabHandler.SPAWN_EGGS.getId(), spawnEgg::toStack);
        return spawnEgg;
    }

    @Nullable
    public static DeferredItem<Item> registerSingleBone(String boneName, DeferredHolder<CreativeModeTab, CreativeModeTab> tab, Supplier<Item> sup, Dinosaur dino, boolean fresh){

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
            DeferredItem<Item> item = register(id, tab, sup);
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



        LinkedHashMap<String, DeferredItem<Item>> DINO_BONES = new LinkedHashMap<>();
        for(String s : dinosaur.getBones()){
            DeferredItem<Item> item = registerSingleBone(s, TabHandler.FOSSILS, () -> new FossilItem(new Item.Properties(), s, false, dinosaur), dinosaur, false);
            if(item != null)
                DINO_BONES.put(s, item);
        }
        BONES.put(dinosaur, DINO_BONES);
    }

    public static void registerFreshBonesForDino(Dinosaur dino){

        LinkedHashMap<String, DeferredItem<Item>> fresh_bones = new LinkedHashMap<>();
        if(dino.getBones() == null){
            return;
        }
        for(String s : dino.getBones()){
            DeferredItem<Item> item = registerSingleBone(s, TabHandler.FOSSILS, () -> new FossilItem(new Item.Properties(), s, true, dino), dino, true);
            if(item != null)
                fresh_bones.put(s, item);
        }

        FRESH_BONES.put(dino, fresh_bones);
    }


    public static DeferredItem<Item> registerSingleRawMeat(Supplier<Item> sup, Dinosaur dino){
        return register("meat/meat_" + dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"), TabHandler.FOODS, sup);

    }

    public static DeferredItem<Item> registerSingleSteak(Supplier<Item> sup, Dinosaur dino){
        return register("meat/steak_" + dino.getName().toLowerCase(Locale.ROOT).replaceAll(" ", "_"), TabHandler.FOODS, sup);
    }

    public static void registerMeatsForDino(Dinosaur dino){
        List<MobEffectInstance> cookedEffects = dino.applyMeatEffect(new ArrayList<>(), true);
        FoodProperties.Builder cookedProperties = new FoodProperties.Builder().nutrition(8);
        for(MobEffectInstance i : cookedEffects){
            cookedProperties.effect(() -> i, 0.9f);
        }

        List<MobEffectInstance> rawEffects = dino.applyMeatEffect(new ArrayList<>(), false);
        FoodProperties.Builder rawProperties = new FoodProperties.Builder().nutrition(3);
        for(MobEffectInstance i : rawEffects){
            rawProperties.effect(() -> i, 0.9f);
        }
        DeferredItem<Item> rawMeat = registerSingleRawMeat(() -> new DinosaurMeatItem(new Item.Properties().food(rawProperties.build()), false, dino), dino);
        DeferredItem<Item> steak = registerSingleSteak(() -> new DinosaurMeatItem(new Item.Properties().food(cookedProperties.build()), true, dino), dino);
        MEATS.put(dino, rawMeat);
        STEAKS.put(dino, steak);
        ALL_MEATS.add(rawMeat);
        ALL_MEATS.add(steak);
    }

    public static DeferredItem<BlockItem> registerBlockItem(String name, Supplier<Block> blockSupplier) {
        return MOD_ITEMS.register(name, () -> {
            Block block = blockSupplier.get();
            BlockItem item = new BlockItem(block, new Item.Properties());
            ResourceLocation tabId = determineTab(block);
            if (tabId != null) {
                TabHandler.addToTab(tabId, () -> new ItemStack(item));
            }
            return item;
        });
    }


    private static DeferredItem<Item> registerBoat(String name, ModBoatType type, boolean hasChest) {
        DeferredItem<Item> registryObject = register(name, TabHandler.ITEMS, () -> new JurassicBoatItem(hasChest, type,
                new Item.Properties().stacksTo(1)));
        if (hasChest) {
            type.setChestBoatItem(() -> registryObject.get());
        } else {
            type.setBoatItem(() -> registryObject.get());
        }
        return registryObject;
    }

    private static ResourceLocation determineTab(Block block) {
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
            return TabHandler.PLANTS.getId();
        } else if (block instanceof FossilBlock) {
            return TabHandler.FOSSILS.getId();
        } else if (block instanceof ParkBenchBlock || block instanceof TrashCanBlock) {
            return TabHandler.DECORATIONS.getId();
        } else if (block instanceof SkullDisplayBlock || block instanceof CultivatorTopBlock || block instanceof CultivatorBottomBlock) {
            return null;
        } else {
            return TabHandler.BLOCKS.getId();
        }
    }



    private static Map<AttractionSignEntity.AttractionSignType, DeferredItem<Item>> registerAttractionSigns() {
        Map<AttractionSignEntity.AttractionSignType, DeferredItem<Item>> map = new EnumMap<>(AttractionSignEntity.AttractionSignType.class);
        for (AttractionSignEntity.AttractionSignType type : AttractionSignEntity.AttractionSignType.values()) {
            String name = "attraction_sign_" + type.name().toLowerCase(Locale.ROOT);
            map.put(
                    type,
                    register(
                            name,
                            TabHandler.DECORATIONS,
                            () -> new AttractionSignItem(type, new Item.Properties().stacksTo(1))
                    )
            );
        }
        return map;
    }
}
