package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CoelurusEntity;
import mod.reborn.server.entity.dinosaur.CompsognathusEntity;
import mod.reborn.server.period.TimePeriod;

public class CoelurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public CoelurusDinosaur()
    {
        super();
        this.setName("Coelurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CoelurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x7B8042, 0x454B3B);
        this.setEggColorFemale(0x7D734A, 0x484A3D);
        this.setHealth(2, 12);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(3, 6);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.2F, 0.5F);
        this.setSizeX(0.1F, 0.6F);
        this.setSizeY(0.25F, 0.95F);
        this.setStorage(9);
        this.setAttackBias(90);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("ribcage", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(0.85F, 0.25F);
        this.setImprintable(true);
        this.setStorage(8);
        this.setBreeding(false, 0, 4, 20, false, true);
        String[][] recipe =     {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"leg_bones", "leg_bones", "", "", "arm_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
