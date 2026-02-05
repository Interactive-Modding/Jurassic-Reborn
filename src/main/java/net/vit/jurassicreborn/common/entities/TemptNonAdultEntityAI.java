//package net.vit.jurassicreborn.common.entities;
//
//import net.minecraft.world.entity.ai.goal.TemptGoal;
//import net.minecraft.world.item.crafting.Ingredient;
//import net.minecraft.world.level.ItemLike;
//import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
//import net.vit.jurassicreborn.common.items.Food.FoodHelper;
//
///**
// * Like vanilla TemptGoal but only for juvexaxqniles and only when the dino is idle.
// */
//public class TemptNonAdultEntityAI extends TemptGoal {
//
//    private final DinosaurEntity dino;
//
//    public TemptNonAdultEntityAI(DinosaurEntity dino, double speed) {
//        super(
//                dino,
//                speed,
//                Ingredient.of((ItemLike) FoodHelper.getEdibleFoodItems(dino, dino.getDinosaur().getDiet())),
//                !dino.getDinosaur().getDiet().canEat(dino, FoodType.MEAT)   // canScare
//        );
//        this.dino = dino;
//    }
//
//    /* ------------------------------------------------------------------ */
//    /*  Juvenile-only gate                                                 */
//    /* ------------------------------------------------------------------ */
//
//    @Override
//    public boolean canUse() {
//        return super.canUse()
//                && !dino.isBusy()
//                && dino.getAgePercentage() < 50;
//    }
//}
