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
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.4F, 2F);
        this.setSizeY(0.6F, 2.3F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.15F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setStorage(12);
        this.setBreeding(true, 0, 3, 45, false, true);
    }
}
