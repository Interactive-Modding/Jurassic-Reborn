package net.vit.jurassicreborn.common.items.Food;

import com.google.common.collect.Sets;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.crafting.Ingredient;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.blocks.ModBlocks;
import net.vit.jurassicreborn.common.blocks.ModWoodTypes;
import net.vit.jurassicreborn.common.blocks.wood.AncientLeavesBlock;
import net.vit.jurassicreborn.common.blocks.wood.WoodBlocks;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.items.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.vit.jurassicreborn.common.plants.Plant;
import net.vit.jurassicreborn.common.plants.PlantHandler;
import org.apache.logging.log4j.Level;

import java.util.*;
import java.util.stream.Collectors;

public class FoodHelper {
        private static final Map<FoodType, List<FoodKey>> FOOD_TYPES = new EnumMap<>(FoodType.class);
        private static final List<FoodKey> FOODS = new LinkedList<>();
        private static final Map<FoodKey, Integer> HEAL_AMOUNTS = new HashMap<>();
        private static final Map<FoodKey, FoodEffect[]> FOOD_EFFECTS = new HashMap<>();

        static {
            for (FoodType type : FoodType.values()) {
                FOOD_TYPES.put(type, new ArrayList<>());
            }
        }

    public static void init() {
        registerFood(LeavesBlock.class, FoodType.PLANT, 2000);
        registerFood(TallGrassBlock.class, FoodType.PLANT, 1000);
        registerFood(Blocks.WHEAT, FoodType.PLANT, 2000);
        registerFood(Blocks.MELON, FoodType.PLANT, 3000);
        registerFood(Blocks.SUGAR_CANE, FoodType.PLANT, 1000);
        registerFood(SaplingBlock.class, FoodType.PLANT, 1000);
        registerFood(Blocks.PUMPKIN, FoodType.PLANT, 3000);
        registerFood(Blocks.CARROTS, FoodType.PLANT, 2000);
        registerFood(Blocks.POTATOES, FoodType.PLANT, 2000);
        registerFood(Blocks.HAY_BLOCK, FoodType.PLANT, 5000);
        registerFood(Blocks.LILY_PAD, FoodType.PLANT, 500);
        registerFood(FlowerBlock.class, FoodType.PLANT, 500);
        registerFood(SeagrassBlock.class, FoodType.PLANT, 500);
        registerFood(TallFlowerBlock.class, FoodType.PLANT, 2000);
        registerFood(TallSeagrassBlock.class, FoodType.PLANT, 2000);
        registerFood(Blocks.BROWN_MUSHROOM, FoodType.PLANT, 250);
        registerFood(Blocks.RED_MUSHROOM, FoodType.PLANT, 250);
        registerFood(ModItems.COCKROACHES.get(), FoodType.INSECT, 250);
        registerFood(ModItems.CRICKETS.get(), FoodType.INSECT, 250);
        registerFood(ModItems.MEALWORM_BEETLES.get(), FoodType.INSECT, 250);
        registerFood(ModItems.RHAMNUS_BERRIES.get(), FoodType.PLANT, 250);
        registerFood(ModItems.RHAMNUS_SEEDS.get(), FoodType.PLANT, 250);
        registerFood(ModItems.WILD_ONION.get(), FoodType.PLANT, 250);
        registerFood(ModItems.WILD_POTATO.get(), FoodType.PLANT, 250);
        registerFood(ModItems.WILD_POTATO_COOKED.get(), FoodType.PLANT, 250);
        registerFood(ModItems.WILD_POTATO_SEEDS.get(), FoodType.PLANT, 250);
        registerFood(ModItems.KRILL.get(), FoodType.FILTER, 250);
        registerFood(ModItems.PLANKTON.get(), FoodType.FILTER, 250);
        registerFood(ModBlocks.PALEO_BALE_CYCADEOIDEA.get(), FoodType.PLANT, 5000);
        registerFood(ModBlocks.PALEO_BALE_CYCAD.get(), FoodType.PLANT, 5000);
        registerFood(ModBlocks.PALEO_BALE_FERN.get(), FoodType.PLANT, 5000);
        registerFood(ModBlocks.PALEO_BALE_LEAVES.get(), FoodType.PLANT, 5000);
        registerFood(ModBlocks.PALEO_BALE_OTHER.get(), FoodType.PLANT, 5000);
        registerFood(WoodBlocks.PHOENIX_LEAVES.get(), FoodType.PLANT, 2000);
        registerFood(WoodBlocks.MAGNOLIA_LEAVES.get(), FoodType.PLANT, 2000);
        registerFood(WoodBlocks.ARAUCARIA_LEAVES.get(), FoodType.PLANT, 2000);
        registerFood(WoodBlocks.PSARONIUS_LEAVES.get(), FoodType.PLANT, 2000);
        registerFood(WoodBlocks.CALAMITES_LEAVES.get(), FoodType.PLANT, 2000);
        registerFood(WoodBlocks.GINKGO_LEAVES.get(), FoodType.PLANT, 2000);


        for (Plant plant : PlantHandler.getPlants()) {
            registerFood(plant.getBlock(), FoodType.PLANT, plant.getHealAmount(), plant.getEffects());
        }

        for (WoodType type : ModWoodTypes.modWoodTypes) {
            registerFood(AncientLeavesBlock.class, FoodType.PLANT, 2000);
            registerFood(WoodBlocks.getSaplingForType(type), FoodType.PLANT, 1000);
        }

        registerFood(Items.WHEAT, FoodType.PLANT, 1000);
        registerFood(Items.WHEAT_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.MELON_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.PUMPKIN_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.BEETROOT_SEEDS, FoodType.PLANT, 100);
        registerFood(Items.BEETROOT, FoodType.PLANT, 100);
        registerFood(Items.BEETROOT_SOUP, FoodType.PLANT, 600);
        registerFood(Items.CACTUS, FoodType.PLANT, 50);

        registerFoodAuto(Items.APPLE, FoodType.PLANT);
        registerFoodAuto(Items.TALL_GRASS, FoodType.PLANT);
        registerFoodAuto(Items.MELON_SLICE, FoodType.PLANT);
        registerFoodAuto(Items.BREAD, FoodType.PLANT);
        registerFoodAuto(Items.SWEET_BERRIES, FoodType.PLANT);
        registerFoodAuto(Items.BEEF, FoodType.MEAT);
        registerFoodAuto(Items.COOKED_BEEF, FoodType.MEAT);
        registerFoodAuto(Items.CHICKEN, FoodType.MEAT);
        registerFoodAuto(Items.COOKED_CHICKEN, FoodType.MEAT);
        registerFoodAuto(Items.RABBIT, FoodType.MEAT);
        registerFoodAuto(Items.COOKED_RABBIT, FoodType.MEAT);
        registerFoodAuto(Items.PORKCHOP, FoodType.MEAT);
        registerFoodAuto(Items.COOKED_PORKCHOP, FoodType.MEAT);
        registerFoodAuto(Items.MUTTON, FoodType.MEAT);
        registerFoodAuto(Items.COOKED_MUTTON, FoodType.MEAT);
        registerFoodAuto(Items.SALMON, FoodType.FISH);
        registerFoodAuto(Items.COOKED_SALMON, FoodType.FISH);
        registerFoodAuto(Items.COD, FoodType.FISH);
        registerFoodAuto(Items.COOKED_COD, FoodType.FISH);

        registerFoodAuto( Items.TROPICAL_FISH, FoodType.FISH);

        for(var item : ModItems.MEATS.values()){
            registerFoodAuto(item.get(), FoodType.MEAT);
        }
        for(var item : ModItems.STEAKS.values()){
            registerFoodAuto(item.get(), FoodType.MEAT);
        }


        registerFoodAuto(ModItems.GOAT_RAW.get(), FoodType.MEAT);
        registerFoodAuto(ModItems.GOAT_COOKED.get(), FoodType.MEAT);
        registerFoodAuto(ModItems.SHARK_MEAT_RAW.get(), FoodType.FISH);
        registerFoodAuto(ModItems.SHARK_MEAT_COOKED.get(), FoodType.FISH);
        registerFoodAuto(ModItems.CRAB_MEAT_COOKED.get(), FoodType.FISH);
        registerFoodAuto(ModItems.CRAB_MEAT_RAW.get(), FoodType.FISH);
        registerFoodAuto(ModItems.PHOENIX_FRUIT.get(), FoodType.PLANT);

    //TODO: listen to item registration to do this on the fly instead of by looping through the entire registry

//        for (Item item : Item.REGISTRY) {
//
//            if (item instanceof ItemFood) {
//                ItemFood food = (ItemFood) item;
//                registerFoodAuto(food, food.isWolfsFavoriteMeat() ? FoodType.MEAT : FoodType.PLANT);
//            }
//        }
    }
    public static Ingredient getEdibleFoodIngredient(DinosaurEntity dino, Diet diet) {
        HashSet<Item> edibleItems = getEdibleFoodItems(dino, diet);
        if (edibleItems.isEmpty()) return Ingredient.EMPTY;
        return Ingredient.of(edibleItems.toArray(new Item[0]));
    }
    private static void registerFood(Class<? extends Block> blockClass, FoodType plant, int i) {
        registerFood(new FoodKey(blockClass), plant, i);
    }

    public static void registerFoodAuto(Item food, FoodType foodType, FoodEffect... effects) {
        ItemStack testStack = new ItemStack(food);
        if (food.getFoodProperties(testStack, null) != null) {
            int nutrition = food.getFoodProperties(testStack, null).nutrition();
            registerFood(new FoodKey(food), foodType, nutrition * 650, effects);
        } else {
            registerFood(new FoodKey(food), foodType, 1300, effects);
        }
    }


    public static void registerFood(Item food, FoodType foodType, int healAmount, FoodEffect... effects) {
        registerFood(new FoodKey(food), foodType, healAmount, effects);
    }

    private static void registerFood(FoodKey food, FoodType foodType, int healAmount, FoodEffect... effects) {
        if (!FOODS.contains(food)) {
            if (food == null || food.hashCode() == 0) {
                return;
            }

            FOOD_TYPES.get(foodType).add(food);   // list is guaranteed non‑null by static block
            FOODS.add(food);
            HEAL_AMOUNTS.put(food, healAmount);
            FOOD_EFFECTS.put(food, effects);
        }
    }

    public static void registerFood(Block food, FoodType foodType, int foodAmount, FoodEffect... effects) {
        registerFood(new FoodKey(food), foodType, foodAmount, effects);
    }

    public static List<FoodKey> getFoodType(FoodType type) {
        // never returns null
        return FOOD_TYPES.getOrDefault(type, Collections.emptyList());
    }

    public static List<Item> getFoodItems(FoodType type) {
        return getValidItemList(getFoodType(type));
    }

    private static List<Item> getValidItemList(List<FoodKey> keys) {
        if (keys == null || keys.isEmpty()) return Collections.emptyList();

        List<Item> items = keys.stream()
                .filter(k -> !k.isGeneral)
                .map(k -> {
                    if (k.item != null) return k.item;
                    if (k.block != null) {
                        Item itm = Item.byBlock(k.block);
                        return itm != Items.AIR ? itm : null;
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // general (class‑based) keys
        for (FoodKey k : keys) {
            if (k.isGeneral && k.BlockClass != null) {
                Item.BY_BLOCK.keySet().stream()
                        .filter(b -> k.BlockClass.isAssignableFrom(b.getClass()))
                        .map(Item.BY_BLOCK::get)
                        .filter(itm -> itm != null && itm != Items.AIR)
                        .forEach(items::add);
            }
        }
        return items;
    }

    private static FoodType getFoodType(FoodKey key) {
        for (FoodType foodType : FoodType.values()) {
            if (getFoodType(foodType).contains(key)) return foodType;
        }
        return null;
    }

    public static FoodType getFoodType(Item item) { return getFoodType(new FoodKey(item)); }
    public static FoodType getFoodType(Block block) { return getFoodType(new FoodKey(block)); }
    public static boolean isFoodType(Item item, FoodType foodType) {
        FoodKey key = new FoodKey(item);
        if (getFoodType(foodType).contains(key)) {
            return true;
        }

        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            for (FoodKey foodKey : getFoodType(foodType)) {
                if (foodKey.isGeneral && foodKey.BlockClass != null) {
                    if (foodKey.BlockClass.isAssignableFrom(block.getClass())) {
                        return true;
                    }
                }
            }
        }

        return false;
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
        List<FoodKey> possible = new ArrayList<>();
        for (Diet.DietModule module : diet.getModules()) {
            if (module.applies(entity)) {
                possible.addAll(getFoodType(module.getFoodType()));
            }
        }
        return possible;
    }

    public static int getHealAmount(Item item) { return HEAL_AMOUNTS.getOrDefault(new FoodKey(item), 0); }

    public static void applyEatEffects(DinosaurEntity entity, Item item) {
        FoodEffect[] effects = FOOD_EFFECTS.get(new FoodKey(item));
        if (effects != null) {
            for (FoodEffect effect : effects) {
                if (entity.getRandom().nextInt(100) <= effect.chance) {
                    entity.addEffect(new MobEffectInstance(effect.effect));
                }
            }
        }
    }

    public static boolean isFood(Item item) { return FOODS.contains(new FoodKey(item)); }
    public static boolean isFood(ItemStack stack) { return isFood(stack.getItem()); }
    public static boolean isFood(ItemEntity ent) { return isFood(ent.getItem().getItem()); }

    public static class FoodEffect {
        public Holder<MobEffect> effect;
        public int chance;
        public FoodEffect(Holder<MobEffect> effect, int chance) { this.effect = effect; this.chance = chance; }
    }

    /* unchanged inner FoodKey class */
    static class FoodKey {
        public boolean isGeneral = false;
        final Item item;
        final Block block;
        final Class<? extends Block> BlockClass;
        FoodKey(Item item) { this.item = item; this.block = null; this.BlockClass = null; }
        FoodKey(Block block) {
            Item blkItm = Item.byBlock(block);
            this.item = blkItm != Items.AIR ? blkItm : null;
            this.block = blkItm == Items.AIR ? block : null;
            this.BlockClass = null;
        }
        FoodKey(Class<? extends Block> cls) { this.BlockClass = cls; this.isGeneral = true; this.block = null; this.item = null; }
        @Override public boolean equals(Object o) { return o instanceof FoodKey && this.hashCode() == o.hashCode(); }
        @Override public int hashCode() {
            if (!isGeneral) {
                return item != null && item != Items.AIR ? item.hashCode() : block != null && block != Blocks.AIR ? block.hashCode() : 0;
            }
            return BlockClass.hashCode() + 1;
        }
    }
}
