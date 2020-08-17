package mod.reborn.server.food;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import org.apache.logging.log4j.Level;
import mod.reborn.RebornMod;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.tree.TreeType;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.plant.Plant;
import mod.reborn.server.plant.PlantHandler;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class FoodHelper {
    private static final Map<FoodType, List<FoodKey>> FOOD_TYPES = new EnumMap<>(FoodType.class);
    private static final List<FoodKey> FOODS = new LinkedList<>();
    private static final Map<FoodKey, Integer> HEAL_AMOUNTS = new HashMap<>();
    private static final Map<FoodKey, FoodEffect[]> FOOD_EFFECTS = new HashMap<>();

    public static void init() {
        registerFood(Blocks.LEAVES, FoodType.PLANT, 2000);
        registerFood(Blocks.LEAVES2, FoodType.PLANT, 2000);
        registerFood(Blocks.TALLGRASS, FoodType.PLANT, 1000);
        registerFood(Blocks.WHEAT, FoodType.PLANT, 2000);
        registerFood(Blocks.MELON_BLOCK, FoodType.PLANT, 3000);
        registerFood(Blocks.REEDS, FoodType.PLANT, 1000);
        registerFood(Blocks.SAPLING, FoodType.PLANT, 1000);
        registerFood(Blocks.PUMPKIN, FoodType.PLANT, 3000);
        registerFood(Blocks.CARROTS, FoodType.PLANT, 2000);
        registerFood(Blocks.POTATOES, FoodType.PLANT, 2000);
        registerFood(Blocks.HAY_BLOCK, FoodType.PLANT, 5000);
        registerFood(Blocks.WATERLILY, FoodType.PLANT, 500);
        registerFood(Blocks.YELLOW_FLOWER, FoodType.PLANT, 500);
        registerFood(Blocks.RED_FLOWER, FoodType.PLANT, 500);
        registerFood(Blocks.DOUBLE_PLANT, FoodType.PLANT, 2000);
        registerFood(Blocks.BROWN_MUSHROOM, FoodType.PLANT, 250);
        registerFood(Blocks.RED_MUSHROOM, FoodType.PLANT, 250);

        registerFood(ItemHandler.COCKROACHES, FoodType.INSECT, 250);
        registerFood(ItemHandler.CRICKETS, FoodType.INSECT, 250);
        registerFood(ItemHandler.MEALWORM_BEETLES, FoodType.INSECT, 250);

        registerFood(ItemHandler.KRILL, FoodType.FILTER, 250);
        registerFood(ItemHandler.PLANKTON, FoodType.FILTER, 250);

        registerFood(BlockHandler.PALEO_BALE_CYCADEOIDEA, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_CYCAD, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_FERN, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_LEAVES, FoodType.PLANT, 5000);
        registerFood(BlockHandler.PALEO_BALE_OTHER, FoodType.PLANT, 5000);

        for (Plant plant : PlantHandler.getPlants()) {
            registerFood(plant.getBlock(), FoodType.PLANT, plant.getHealAmount(), plant.getEffects());
        }

        for (TreeType type : TreeType.values()) {
            registerFood(BlockHandler.ANCIENT_LEAVES.get(type), FoodType.PLANT, 2000);
            registerFood(BlockHandler.ANCIENT_SAPLINGS.get(type), FoodType.PLANT, 1000);
        }

        registerFood(Items.WHEAT, FoodType.PLANT, 1000);
        registerFood(Items.WHEAT_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.MELON_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.PUMPKIN_SEEDS, FoodType.PLANT, 100);

        registerFoodAuto((ItemFood) Items.FISH, FoodType.FISH);
        registerFoodAuto((ItemFood) Items.COOKED_FISH, FoodType.FISH);

        registerFoodAuto(ItemHandler.DINOSAUR_MEAT, FoodType.MEAT);
        registerFoodAuto(ItemHandler.DINOSAUR_STEAK, FoodType.MEAT);

        registerFoodAuto(ItemHandler.GOAT_RAW, FoodType.MEAT);
        registerFoodAuto(ItemHandler.GOAT_COOKED, FoodType.MEAT);
        registerFoodAuto(ItemHandler.SHARK_MEAT_RAW, FoodType.FISH);
        registerFoodAuto(ItemHandler.SHARK_MEAT_COOKED, FoodType.FISH);
        registerFoodAuto(ItemHandler.SHARK_RAW, FoodType.FISH);
        registerFoodAuto(ItemHandler.SHARK_COOKED, FoodType.FISH);
        registerFoodAuto(ItemHandler.CRAB_COOKED, FoodType.FISH);
        registerFoodAuto(ItemHandler.CRAB_RAW, FoodType.FISH);
        registerFoodAuto(ItemHandler.CRAB_MEAT_COOKED, FoodType.FISH);
        registerFoodAuto(ItemHandler.CRAB_MEAT_RAW, FoodType.FISH);


        for (Item item : Item.REGISTRY) {
            if (item instanceof ItemFood) {
                ItemFood food = (ItemFood) item;
                registerFoodAuto(food, food.isWolfsFavoriteMeat() ? FoodType.MEAT : FoodType.PLANT);
            }
        }
    }

    public static void registerFoodAuto(ItemFood food, FoodType foodType, FoodEffect... effects) {
        registerFood(new FoodKey(food), foodType, food.getHealAmount(new ItemStack(food)) * 650, effects);
    }

    public static void registerFood(Item food, FoodType foodType, int healAmount, FoodEffect... effects) {
        registerFood(new FoodKey(food), foodType, healAmount, effects);
    }

    private static void registerFood(FoodKey food, FoodType foodType, int healAmount, FoodEffect... effects) {
        if (!FOODS.contains(food)) {
            if( food == null || food.hashCode() == 0 ) {
                RebornMod.getLogger().log(Level.ERROR, "Something tried to register an invalid food!");
                return;
            }

            List<FoodKey> foodsForType = FOOD_TYPES.get(foodType);

            if (foodsForType == null) {
                foodsForType = new ArrayList<>();
            }

            foodsForType.add(food);

            FOODS.add(food);
            FOOD_TYPES.put(foodType, foodsForType);
            HEAL_AMOUNTS.put(food, healAmount);
            FOOD_EFFECTS.put(food, effects);
        }
    }

    public static void registerFood(Block food, FoodType foodType, int foodAmount, FoodEffect... effects) {
        registerFood(new FoodKey(food), foodType, foodAmount, effects);
    }

    public static List<FoodKey> getFoodType(FoodType type) {
        return FOOD_TYPES.get(type);
    }

    public static List<Item> getFoodItems(FoodType type) {
        return getValidItemList(FOOD_TYPES.get(type));
    }

    private static List<Item> getValidItemList(List<FoodKey> keys) {
        return keys.stream().map(key -> {
            if( key.item != null ) {
                return key.item;
            } else if( key.block != null ) {
                Item itm = Item.getItemFromBlock(key.block);
                if( itm != Items.AIR ) {
                    return itm;
                }
            }

            return null;
        } ).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private static FoodType getFoodType(FoodKey key) {
        for (FoodType foodType : FoodType.values()) {
            if (getFoodType(foodType).contains(key)) {
                return foodType;
            }
        }

        return null;
    }

    public static FoodType getFoodType(Item item) {
        return getFoodType(new FoodKey(item));
    }

    public static FoodType getFoodType(Block block) {
        return getFoodType(new FoodKey(block));
    }

    public static boolean isFoodType(Item item, FoodType foodType) {
        return getFoodType(foodType).contains(new FoodKey(item));
    }

    public static boolean isEdible(DinosaurEntity entity, Diet diet, Item item) {
        return item != null && getEdibleFoods(entity, diet).contains(new FoodKey(item));
    }

    public static boolean isEdible(DinosaurEntity entity, Diet diet, Block block) {
        return block != null && getEdibleFoods(entity, diet).contains(new FoodKey(block));
    }

    public static HashSet<Item> getEdibleFoodItems(DinosaurEntity entity, Diet diet) {
        return Sets.newHashSet(getValidItemList(getEdibleFoods(entity, diet)));
    }

    public static List<FoodKey> getEdibleFoods(DinosaurEntity entity, Diet diet) {
        List<FoodKey> possibleItems = new ArrayList<>();
        for (Diet.DietModule module : diet.getModules()) {
            if (module.applies(entity)) {
                possibleItems.addAll(getFoodType(module.getFoodType()));
            }
        }
        return possibleItems;
    }

    public static int getHealAmount(Item item) {
        return HEAL_AMOUNTS.getOrDefault(new FoodKey(item), 0);
    }

    public static void applyEatEffects(DinosaurEntity entity, Item item) {
        FoodEffect[] effects = FOOD_EFFECTS.get(new FoodKey(item));

        if (effects != null) {
            for (FoodEffect effect : effects) {
                if (entity.getRNG().nextInt(100) <= effect.chance) {
                    entity.addPotionEffect(effect.effect);
                }
            }
        }
    }

    public static boolean isFood(Item item) {
        return FOODS.contains(new FoodKey(item));
    }

    public static class FoodEffect {
        public PotionEffect effect;
        public int chance;

        public FoodEffect(PotionEffect effect, int chance) {
            this.effect = effect;
            this.chance = chance;
        }
    }

    static class FoodKey {
        final Item item;
        final Block block;

        FoodKey(Item item) {
            this.item = item;
            this.block = null;
        }

        FoodKey(Block block) {
            Item blkItm = Item.getItemFromBlock(block);
            this.item = blkItm != Items.AIR ? blkItm : null;
            this.block = blkItm == Items.AIR ? block : null;
        }

        @Override
        public boolean equals(Object obj) {
            return this.hashCode() == obj.hashCode();
        }

        @Override
        public int hashCode() {
            return this.item != null && this.item != Items.AIR ? this.item.hashCode() : this.block != null && this.block != Blocks.AIR ? this.block.hashCode() : 0;
        }
    }
}
