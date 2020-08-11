package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.SuchomimusEntity;
import mod.reborn.server.period.TimePeriod;

public class SuchomimusDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public SuchomimusDinosaur() {
        super();

        this.setName("Suchomimus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(SuchomimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xCCB674, 0x4A5966);
        this.setEggColorFemale(0xB3BB6D, 0x45676B);
        this.setHealth(10, 45);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(5, 30);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.5F, 1.8F);
        this.setSizeY(0.75F, 3F);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("arm_bones", "claw", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "foot_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.3F);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(true);
        this.setStorage(12);
    }
}
