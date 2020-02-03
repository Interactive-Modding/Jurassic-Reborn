package mod.reborn.server.world.loot;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;

public class LootItems {
    public static final Object[] BASIC_STORAGE = new Object[] {
            BlockHandler.LOW_SECURITY_FENCE_WIRE, 0, 64,
            BlockHandler.LOW_SECURITY_FENCE_POLE, 0, 20,
            BlockHandler.LOW_SECURITY_FENCE_BASE, 0, 24,
            ItemHandler.PLANT_FOSSIL, 0, 48,
            ItemHandler.TWIG_FOSSIL, 0, 32,
            //ItemHandler.PLASTER_AND_BANDAGE, 0, 64,
            Items.IRON_INGOT, 0, 16,
            Items.GOLD_INGOT, 0, 4,
            Items.REDSTONE, 0, 64,
            Items.COAL, 0, 64,
            Items.DIAMOND, 0, 2,
            ItemHandler.CAR_TIRE, 0, 12,
            ItemHandler.CAR_CHASSIS, 0, 2,
            ItemHandler.ENGINE_SYSTEM, 0, 3,
            ItemHandler.BASIC_CIRCUIT, 0, 32,
            ItemHandler.EMPTY_TEST_TUBE, 0, 64,
            ItemHandler.EMPTY_SYRINGE, 0, 64,
            ItemHandler.MURAL, 0, 10,
            ItemHandler.ATTRACTION_SIGN, 0, 3,
            Items.SIGN, 0, 16,
            Items.BOOK, 0, 20,
            Items.STRING, 0, 16,
            Blocks.BROWN_MUSHROOM, 0, 8,
            Blocks.RED_MUSHROOM, 0, 8,
            Items.WHEAT_SEEDS, 0, 20,
            Items.MELON_SEEDS, 0, 20,
            Items.PUMPKIN_SEEDS, 0, 20,
            ItemHandler.STORAGE_DISC, 0, 64,
            Items.COMPARATOR, 0, 3,
            Items.REPEATER, 0, 4,
            Blocks.REDSTONE_LAMP, 0, 24,
            Blocks.SEA_LANTERN, 0, 8,
            Items.GLOWSTONE_DUST, 0, 24,
            Blocks.GLOWSTONE, 0, 8,
            Blocks.QUARTZ_BLOCK, 0, 64,
            Blocks.COBBLESTONE, 0, 64,
            BlockHandler.REINFORCED_STONE, 0, 64,
            ItemHandler.GYPSUM_POWDER, 0, 64,
            BlockHandler.FEEDER, 0, 5,
            Items.PAPER, 0, 64
    };

    public static Object[] BASIC_CONTROL = new Object[] {
            Items.PAPER, 0, 64,
            Items.BOOK, 0, 32,
            ItemHandler.BASIC_CIRCUIT, 0, 64,
            ItemHandler.ADVANCED_CIRCUIT, 0, 32,
            ItemHandler.LASER, 0, 5,
            ItemHandler.DISC_DRIVE, 0, 3,
            ItemHandler.COMPUTER_SCREEN, 0, 1,
            ItemHandler.KEYBOARD, 0, 4,
            Blocks.STONE_BUTTON, 0, 12,
            Items.IRON_INGOT, 0, 7,
            ItemHandler.STORAGE_DISC, 0, 32,
            Items.REDSTONE, 0, 32,
            Items.REPEATER, 0, 5,
            Items.COMPARATOR, 0, 2
    };

    public static Object[] BASIC_LABORATORY = new Object[] {
            ItemHandler.STORAGE_DISC, 0, 64,
            ItemHandler.EMPTY_TEST_TUBE, 0, 64,
            ItemHandler.LIQUID_AGAR, 0, 32,
            ItemHandler.EMPTY_SYRINGE, 0, 64,
            ItemHandler.PETRI_DISH, 0, 64,
            ItemHandler.DNA_NUCLEOTIDES, 0, 64,
            Items.PAPER, 0, 32,
            Items.BOOK, 0, 12
    };

    public static Object[] BASIC_KITCHEN = new Object[] {
            Items.WHEAT_SEEDS, 0, 64,
            Items.WHEAT, 0, 64,
            Items.MELON_SEEDS, 0, 64,
            Items.PUMPKIN_SEEDS, 0, 64,
            Items.BOWL, 0, 16,
            Items.BOOK, 0, 10,
            Items.POTIONITEM, 0, 16,
            Blocks.BROWN_MUSHROOM, 0, 5,
            Blocks.RED_MUSHROOM, 0, 5,
            ItemHandler.CHILEAN_SEA_BASS, 0, 5,
            Items.BEEF, 0, 32,
            Items.PORKCHOP, 0, 48,
            Items.CHICKEN, 0, 64,
            Items.FISH, 0, 64,
            Blocks.ICE, 0, 24,
            Blocks.PACKED_ICE, 0, 12,
            Items.BREAD, 0, 64,
            Items.MELON, 0, 64,
            Items.POTATO, 0, 64,
            Items.CARROT, 0, 64,
            ItemHandler.FUN_FRIES, 0, 10
    };

    public static final Object[] BASIC_DORM_TOWER = new Object[] {
            Items.GOLD_NUGGET, 0, 5,
            Items.EMERALD, 0, 2,
            Items.STRING, 0, 10,
            Items.FLINT, 0, 5,
            Items.PAPER, 0, 24,
            Items.FEATHER, 0, 2,
            Items.STICK, 0, 5,
            Items.DYE, 0, 5,
            Items.IRON_HELMET, 0, 2,
            Items.LEATHER_CHESTPLATE, 0, 2,
            Items.LEATHER_LEGGINGS, 0, 2,
            Items.IRON_BOOTS, 0, 2,
            Items.BOOK, 0, 16
    };

    public static final Object[] BASIC_INFIRMARY = new Object[] {
            ItemHandler.EMPTY_SYRINGE, 0, 48,
            ItemHandler.EMPTY_TEST_TUBE, 0, 16,
            ItemHandler.PLASTER_AND_BANDAGE, 0, 24,
            Items.PAPER, 0, 12,
            Items.BOOK, 0, 8
    };

    public static final Object[] BASIC_GARAGE = new Object[] {
            ItemHandler.CAR_TIRE, 0, 8,
            ItemHandler.CAR_SEATS, 0, 2,
            ItemHandler.ENGINE_SYSTEM, 0, 5,
            ItemHandler.BASIC_CIRCUIT, 0, 16,
            ItemHandler.CAR_CHASSIS, 0, 2,
            ItemHandler.UNFINISHED_CAR, 0, 1,
            ItemHandler.ADVANCED_CIRCUIT, 0, 4,
            Items.IRON_INGOT, 0, 10,
            Blocks.IRON_BARS, 0, 32,
            Items.DYE, 0, 10,
            ItemHandler.IRON_ROD, 0, 16,
            Items.SADDLE, 0, 1,
            Items.LEATHER, 0, 5
    };

    public static final Object[] BASIC_STAFF_DORMS = new Object[] {
            Items.GOLD_NUGGET, 0, 5,
            Items.EMERALD, 0, 2,
            Items.STRING, 0, 10,
            Items.FLINT, 0, 5,
            Items.PAPER, 0, 24,
            Items.FEATHER, 0, 2,
            Items.STICK, 0, 5,
            Items.DYE, 0, 5,
            Items.IRON_HELMET, 0, 2,
            Items.LEATHER_CHESTPLATE, 0, 2,
            Items.LEATHER_LEGGINGS, 0, 2,
            Items.IRON_BOOTS, 0, 2,
            Items.BOOK, 0, 16
    };

    public static final Object[] BASIC_DINING_HALL = new Object[] {
            ItemHandler.MR_DNA_KEYCHAIN, 0, 5,
            ItemHandler.AMBER_KEYCHAIN, 0, 5,
            ItemHandler.AMBER_CANE, 0, 5,
            ItemHandler.STAMP_SET, 0, 5
    };
}
