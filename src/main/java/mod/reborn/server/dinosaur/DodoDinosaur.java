package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DodoEntity;
import mod.reborn.server.period.TimePeriod;

public class DodoDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public DodoDinosaur()
    {
        super();

        this.setName("Dodo");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(DodoEntity.class);
        this.setTimePeriod(TimePeriod.QUATERNARY);
        this.setEggColorMale(0xA2996E, 0x545338);
        this.setEggColorFemale(0x908B80, 0x665C51);
        this.setHealth(5, 15);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.35F, 0.95F);
        this.setSizeX(0.25F, 0.5F);
        this.setSizeY(0.35F, 0.95F);
        this.setStorage(9);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.3F);
        this.setAttackBias(5);
        this.setImprintable(true);
        this.setStorage(6);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe =     {{"", "", "neck_vertebrae", "skull"},
                                {"pelvis", "ribcage", "shoulder"},
                                {"", "leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
