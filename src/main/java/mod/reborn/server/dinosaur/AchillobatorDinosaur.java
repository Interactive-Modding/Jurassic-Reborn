package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.AchillobatorEntity;
import mod.reborn.server.period.TimePeriod;

public class AchillobatorDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public AchillobatorDinosaur() {
        super();

        this.setName("Achillobator");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(AchillobatorEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7A7268, 0x7E4941);
        this.setEggColorFemale(0xE1DFDC, 0x675C58);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setAttackBias(600.0);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.3F, 1.4F);
        this.setSizeY(0.5F, 1.8F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "skull", "claw", "pelvis", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head");
        this.setScale(1.1F, 0.325F);
        this.setBreeding(false,1, 7, 28, false, true);
        this.setMaxHerdSize(10);
        this.setCanClimb(true);
        this.setImprintable(true);
        this.setStorage(27);
        this.setJumpHeight(3);
        this.shouldDefendOffspring();
    }
}