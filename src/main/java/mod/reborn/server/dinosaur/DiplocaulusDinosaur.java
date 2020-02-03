package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DiplocaulusEntity;
import mod.reborn.server.period.TimePeriod;

public class DiplocaulusDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public DiplocaulusDinosaur() {
        super();

        this.setName("Diplocaulus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DiplocaulusEntity.class);
        this.setTimePeriod(TimePeriod.PERMIAN);
        this.setEggColorMale(0xBDD9DE, 0x286A7F);
        this.setEggColorFemale(0xCDDEE7, 0x285880);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.3F, 1.4F);
        this.setSizeY(0.5F, 1.8F);
        this.setDiet(Diet.PISCIVORE.get());
        this.setBones("skull", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.2F);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setOffset(0,-4.5F,0);
        this.setAttackBias(10);
        this.setImprintable(true);
        this.setStorage(12);
        this.setBreeding(true, 2, 6, 20, false, true);
        this.setMarineAnimal(true);
    }
}
