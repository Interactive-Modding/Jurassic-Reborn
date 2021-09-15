package mod.reborn.server.food;

import mod.reborn.server.items.ItemHandler;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.Map;

public class FoodNutrients {
    public static final Map<Item, FoodNutrients> NUTRIENTS = new HashMap<>();

    private final double proximate;
    private final double minerals;
    private final double vitamins;
    private final double lipids;
    private final Item food;

    public FoodNutrients(Item food, double foodProximates, double foodMinerals, double foodVitamins, double foodLipids) {
        this.food = food;
        this.proximate = foodProximates;
        this.minerals = foodMinerals;
        this.vitamins = foodVitamins;
        this.lipids = foodLipids;
    }

    public static void register() {
        register(Items.APPLE, 0.060, 0.065, 0.100, 0.010);
        register(Items.POTATO, 0.100, 0.200, 0.160, 0.020);
        register(Items.BREAD, 0.300, 0.400, 0.430, 0.180);
        register(Items.CHICKEN, 0.390, 0.350, 0.280, 0.450);
        register(Items.COOKED_CHICKEN, 0.490, 0.425, 0.335, 0.555);
        register(Items.PORKCHOP, 0.460, 0.310, 0.390, 0.380);
        register(Items.COOKED_PORKCHOP, 0.580, 0.390, 0.490, 0.470);
        register(Items.BEEF, 0.460, 0.310, 0.390, 0.380);
        register(Items.COOKED_BEEF, 0.520, 0.330, 0.410, 0.400);
        register(Items.TROPICAL_FISH, 0.480, 0.430, 0.140, 0.240);
        register(Items.SALMON, 0.480, 0.430, 0.140, 0.240);
        register(Items.COD, 0.480, 0.430, 0.140, 0.240);
        register(Items.COOKED_SALMON, 0.500, 0.450, 0.200, 0.280);
        register(Items.COOKED_COD, 0.500, 0.450, 0.200, 0.280);
        register(Items.MILK_BUCKET, 0.180, 0.260, 0.220, 0.600);
        register(Items.EGG, 0.050, 0.030, 0.050, 0.250);
        register(Items.CARROT, 0.070, 0.170, 0.350, 0.010);
        register(Items.SUGAR, 0.200, 0.010, 0.010, 0.010);
        register(Items.MELON, 0.060, 0.060, 0.060, 0.010);
        register(Items.WHEAT, 0.100, 0.220, 0.100, 0.030);
        register(Items.MUTTON, 0.460, 0.310, 0.390, 0.380);
        register(Items.COOKED_MUTTON, 0.580, 0.390, 0.490, 0.470);
        //register(ItemHandler.GOAT_RAW, 0.460, 0.310, 0.390, 0.380);
        //register(ItemHandler.GOAT_COOKED, 0.580, 0.390, 0.490, 0.470);
        //register(ItemHandler.DINOSAUR_MEAT, 0.460, 0.310, 0.390, 0.380);
        //register(ItemHandler.DINOSAUR_STEAK, 0.580, 0.390, 0.490, 0.470);
    }

    public static void register(Item item, double foodProximates, double foodMinerals, double foodVitamins, double foodLipids) {
        NUTRIENTS.put(item, new FoodNutrients(item, foodProximates, foodMinerals, foodVitamins, foodLipids));
    }

    public Item getFoodItem() {
        return this.food;
    }

    public double getProximate() {
        return this.proximate;
    }

    public double getMinerals() {
        return this.minerals;
    }

    public double getVitamins() {
        return this.vitamins;
    }

    public double getLipids() {
        return this.lipids;
    }

    public static FoodNutrients get(Item item) {
        return NUTRIENTS.get(item);
    }
}
