package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CeratosaurusEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;

public class CeratosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public CeratosaurusDinosaur(){
        super();

        this.setName("Ceratosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(CeratosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xAB9573, 0xB05453);
        this.setEggColorFemale(0xB4B793, 0x764A30);
        this.setHealth(10, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.4F, 2.8F);
        this.setSizeY(0.3F, 4F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tooth", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2.2F, 0.25F);
        this.setBreeding(false, 2, 6, 40, false, true);
        this.setImprintable(true);
        this.setStorage(26);
        this.shouldDefendOffspring();
        String[][] recipe = {{"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
