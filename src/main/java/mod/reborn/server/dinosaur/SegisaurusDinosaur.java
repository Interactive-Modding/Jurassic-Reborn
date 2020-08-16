package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.SegisaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class SegisaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public SegisaurusDinosaur()
    {
        super();

        this.setName("Segisaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(SegisaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x834B4C, 0x4B8FB5);
        this.setEggColorFemale(0xCEEE99, 0x776343);
        this.setHealth(4, 14);
        this.setStrength(3, 7);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.3F, 0.85F);
        this.setSizeX(0.28F, 0.43F);
        this.setSizeY(0.35F, 0.8F);
        this.setStorage(9);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.NOCTURNAL);
        this.setBones("skull", "tail_vertebrae", "teeth", "leg_bones", "foot_bones", "neck_vertebrae", "shoulder", "ribcage", "pelvis", "arm_bones", "claw");
        this.setHeadCubeName("head");
        this.setAttackBias(90);
        this.setScale(0.45F, 0.13F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setStorage(12);
        this.setBreeding(false, 0, 6, 20, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","teeth"},
                {"", "", "leg_bones", "arm_bones", "claw"},
                {"", "", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
