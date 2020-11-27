package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.PostosuchusEntity;
import mod.reborn.server.period.TimePeriod;

public class PostosuchusDinosaur extends Dinosaur {
    public static final double SPEED = 0.35F;
    public PostosuchusDinosaur() {
        super();

        this.setName("Postosuchus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(PostosuchusEntity.class);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0xAA9575, 0x744942 );
        this.setEggColorFemale(0xAC9574, 0x985D10);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(2, 16);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.15F, 1.4F);
        this.setSizeY(0.3F, 1.8F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tooth", "femur", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.1F);
        this.setAttackBias(120);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
        {"", "","neck_vertebrae","skull"},
        {"tail_vertebrae", "femur", "ribcage","shoulder"},
        {"leg_bones", "leg_bones", "", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
