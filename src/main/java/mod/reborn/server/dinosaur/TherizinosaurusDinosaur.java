package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.TherizinosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class TherizinosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public TherizinosaurusDinosaur()
    {
        super();

        this.setName("Therizinosaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(TherizinosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x787878, 0x2B2B2B);
        this.setEggColorFemale(0x7F7F7F, 0x272727);
        this.setHealth(10, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(10, 35);
        this.setMaximumAge(fromDays(65));
        this.setEyeHeight(0.95F, 5.85F);
        this.setSizeX(0.65F, 2.25F);
        this.setSizeY(1.0F, 5.95F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBones("skull", "teeth", "ribcage", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(3.5F, 0.55F);
        this.setOffset(0.0F, 1.0F, 0.0F);
        this.setAttackSpeed(4);
        this.setAttackBias(800);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(true);
        this.setDefendOwner(true);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                        {"tail_vertebrae", "pelvis", "ribcage","shoulder",""},
                        {"", "leg_bones", "", "arm_bones", ""},
                        {"", "foot_bones", "", "claw", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
