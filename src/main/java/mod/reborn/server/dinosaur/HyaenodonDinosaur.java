package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.HyaenodonEntity;
import mod.reborn.server.period.TimePeriod;

public class HyaenodonDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public HyaenodonDinosaur() {
        super();

        this.setName("Hyaenodon");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(HyaenodonEntity.class);
        this.setTimePeriod(TimePeriod.PALEOGENE);
        this.setEggColorMale(0xA3A3A3, 0x191919);
        this.setEggColorFemale(0x6F6652, 0x100E0B);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.3F, 1.4F);
        this.setSizeY(0.5F, 1.8F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tail_vertebrae", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.5F);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setBreeding(true, 0, 2, 20, false, true);
        this.setAttackBias(50);
        this.setImprintable(true);
        this.setStorage(12);
        this.setMammal(true);
    }
}
