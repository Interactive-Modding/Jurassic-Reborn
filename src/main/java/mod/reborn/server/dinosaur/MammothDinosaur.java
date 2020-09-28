package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MammothEntity;
import mod.reborn.server.period.TimePeriod;

public class MammothDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public MammothDinosaur() {
        super();

        this.setName("Mammoth");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MammothEntity.class);
        this.setTimePeriod(TimePeriod.QUATERNARY);
        this.setEggColorMale(0x84441A, 0x614532);
        this.setEggColorFemale(0x684B2D, 0x6F4E39);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(5, 25);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.8F);
        this.setSizeX(0.4F, 2.2F);
        this.setSizeY(0.6F, 2.5F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "tooth","front_leg_bones", "hind_leg_bones", "tusks", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.7F, 0.30F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setStorage(12);
        this.setBreeding(true, 2, 4, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","tooth","tusks"},
                {"", "hind_leg_bones", "", "front_leg_bones", "shoulder"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
