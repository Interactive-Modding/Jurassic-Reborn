package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.HerrerasaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class HerrerasaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public HerrerasaurusDinosaur()
    {
        super();

        this.setName("Herrerasaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(HerrerasaurusEntity.class);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0x2B1919, 0x932C23);
        this.setEggColorFemale(0x7C6F44, 0x2B2721);
        this.setHealth(10, 35);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 2.25F);
        this.setSizeX(0.4F, 1.8F);
        this.setSizeY(0.55F, 2.55F);
        this.setStorage(36);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "claw");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.25F);
        this.setBreeding(false, 1, 4, 66, false, true);
        this.setAttackBias(300);
        this.setImprintable(true);
        this.setStorage(12);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
