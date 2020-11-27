package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.OrnithomimusEntity;
import mod.reborn.server.period.TimePeriod;

public class OrnithomimusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public OrnithomimusDinosaur()
    {
        super();

        this.setName("Ornithomimus");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(OrnithomimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x92A8D5, 0x475F93);
        this.setEggColorFemale(0xBDC4A9, 0x7F91C1);
        this.setHealth(6, 26);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.58F, 1.95F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.25F, 1.55F);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head Base");
        this.setScale(0.9F, 0.15F);
        this.setFlockSpeed(1.4F);
        this.setAttackBias(-100);
        this.setFlee(true);
        this.setImprintable(true);
        this.setStorage(12);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(true);
        this.setJumpHeight(3);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                        {"tail_vertebrae", "pelvis", "ribcage","shoulder",""},
                        {"", "leg_bones", "", "arm_bones", ""},
                        {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
