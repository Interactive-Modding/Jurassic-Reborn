package mod.reborn.server.food;

import com.google.common.collect.Sets;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.potion.EffectInstance;

import java.util.*;
import java.util.stream.Collectors;

public class FoodHelper {
    private static final Map<FoodType, List<FoodKey>> FOOD_TYPES = new EnumMap<>(FoodType.class);
    private static final List<FoodKey> FOODS = new LinkedList<>();
    private static final Map<FoodKey, Integer> HEAL_AMOUNTS = new HashMap<>();
    private static final Map<FoodKey, FoodEffect[]> FOOD_EFFECTS = new HashMap<>();

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
        public EffectInstance effect;
        public int chance;

        public FoodEffect(EffectInstance effect, int chance) {
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
