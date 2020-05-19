package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.CarnotaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class CarnotaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.42F;
    public CarnotaurusDinosaur()
    {
        super();

        this.setName("Carnotaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CarnotaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA2996E, 0x545338);
        this.setEggColorFemale(0x9C8E6A, 0x635639);
        this.setHealth(10, 35);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.4F, 2.4F);
        this.setSizeX(0.45F, 2.25F);
        this.setSizeY(0.6F, 2.8F);
        this.setStorage(36);
        this.setDiet(Diet.CARNIVORE.get());
        this.setSleepTime(SleepTime.CREPUSCULAR);
        this.setBones("skull", "tooth", "claw", "foot_bones", "neck_vertebrae", "pelvis", "shoulder", "arm_bones", "leg_bones", "ribcage", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.15F);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.shouldDefendOffspring();


        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder_bone","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", "claw"},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
    }
}
