package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.BaryonyxEntity;
import mod.reborn.server.period.TimePeriod;

public class BaryonyxDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public BaryonyxDinosaur()
    {
        super();
        this.setName("Baryonyx");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(BaryonyxEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x567F4F, 0x13270F);
        this.setEggColorFemale(0x9D9442, 0x2A2405);
        this.setHealth(5, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.55F, 2.95F);
        this.setSizeX(0.35F, 1.5F);
        this.setSizeY(0.55F, 2.95F);
        this.setStorage(36);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "claw", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "foot_bones");
        this.setHeadCubeName("Head");
        this.setBreeding(false, 0, 2, 61, false, true);
        this.setScale(1.3F, 0.25F);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder_bone","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
