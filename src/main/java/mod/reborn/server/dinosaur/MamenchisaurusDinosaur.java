package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MamenchisaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class MamenchisaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.25F;
    public MamenchisaurusDinosaur()
    {
        super();

        this.setName("Mamenchisaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MamenchisaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xD1BA49, 0x909B1D);
        this.setEggColorFemale(0x98764E, 0x545028);
        this.setHealth(10, 135);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(52);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setStrength(20, 60);
        this.setMaximumAge(fromDays(95));
        this.setEyeHeight(1.55F, 13.95F);
        this.setRotationAngle(50, 10);
        this.setSizeX(0.4F, 5.4F);
        this.setSizeY(0.5F, 5.8F);
        this.setBones("skull", "tooth", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.6F, 0.1F);
        this.setPaleoPadScale(5);
        this.setOffset(0.0F, 0.0F, -0.5F);
        this.shouldDefendOffspring();
        this.setAttackBias(1500);
        this.setBreeding(false, 4, 8, 80, false, true);
        String[][] recipe =     {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae","tooth"},
                {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
