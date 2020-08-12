package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.LambeosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class LambeosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.41F;
    public LambeosaurusDinosaur()
    {
        super();

        this.setName("Lambeosaurus");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(LambeosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x82947A, 0x2F3129);
        this.setEggColorFemale(0x898969, 0x464C3A);
        this.setHealth(10, 60);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.5F, 3.45F);
        this.setSizeX(0.5F, 2.8F);
        this.setSizeY(0.8F, 4.25F);
        this.setStorage(45);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("cheek_teeth", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "shoulder", "pelvis", "ribcage", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2.5F, 0.45F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.setAttackBias(-50);
        this.setImprintable(true);
        this.setStorage(6);
        this.setBreeding(false, 2, 6, 40, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "", "", "shoulder", "cheek_teeth"},
                {"", "", "", "", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}