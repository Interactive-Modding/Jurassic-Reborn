package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.StegosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class StegosaurusDinosaur extends Dinosaur{
    public static final double SPEED = 0.32F;
    public StegosaurusDinosaur() {
        super();

        this.setName("Stegosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(StegosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xBABF83, 0x75964E);
        this.setEggColorFemale(0xC8BC9A, 0x827D54);
        this.setHealth(10, 50);
        this.setStrength(12, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.26F, 2.2F);
        this.setSizeX(0.5F, 4.0F);
        this.setSizeY(0.7F, 4.8F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck", "pelvis", "ribcage", "shoulder", "skull", "tail", "thagomizer", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.55F, 0.35F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.setAttackBias(350);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setStorage(24);
        this.setBreeding(false, 2, 6, 20, false, true);
    }
}

