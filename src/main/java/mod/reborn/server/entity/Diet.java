package mod.reborn.server.entity;

import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.food.FoodHelper;
import mod.reborn.server.food.FoodType;
import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class Diet {
    public static String getDietTypeName = "Carnivore";

    public static final Supplier<Diet> CARNIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.MEAT))
            .withModule(new DietModule(FoodType.INSECT)
                    .withCondition(entity -> entity.getAgePercentage() < 25));
    public static final Supplier<Diet> HERBIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.PLANT));
    public static final Supplier<Diet> INSECTIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.INSECT));
    public static final Supplier<Diet> PISCIVORE = () -> new Diet()
            .withModule(new DietModule(FoodType.FISH));
    public static final Supplier<Diet> PCARNIVORE =() -> new Diet()
            .withModule(new DietModule(FoodType.FISH))
            .withModule(new DietModule(FoodType.MEAT));
    public static final Supplier<Diet> INHERBIVORE =() -> new Diet()
            .withModule(new DietModule(FoodType.PLANT))
            .withModule(new DietModule(FoodType.INSECT));
    public static final Supplier<Diet> INCARNIVORE =() -> new Diet()
            .withModule(new DietModule(FoodType.MEAT))
            .withModule(new DietModule(FoodType.INSECT));

    private List<DietModule> modules = new ArrayList<>();

    public Diet withModule(DietModule module) {
        this.modules.add(module);
        return this;
    }

    public List<DietModule> getModules() {
        return this.modules;
    }

    public boolean canEat(DinosaurEntity entity, FoodType foodType) {
        for (DietModule module : this.modules) {
            if (module.applies(entity) && module.getFoodType() == foodType) {
                return true;
            }
        }
        return false;
    }

    public static class DietModule {
        private Function<DinosaurEntity, Boolean> condition = dinosaur -> true;
        private FoodType type;

        public DietModule(FoodType type) {
            this.type = type;
        }

        public DietModule withCondition(Function<DinosaurEntity, Boolean> condition) {
            this.condition = condition;
            return this;
        }

        public boolean canEat(DinosaurEntity entity, Item item) {
            return this.condition.apply(entity) && FoodHelper.getFoodType(item) == type;
        }

        public FoodType getFoodType() {
            return this.type;
        }

        public boolean applies(DinosaurEntity entity) {
            return this.condition.apply(entity);
        }
    }
}