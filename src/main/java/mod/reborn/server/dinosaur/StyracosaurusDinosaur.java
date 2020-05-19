package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.StyracosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class StyracosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public StyracosaurusDinosaur() {
        super();

        this.setName("Styracosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(StyracosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x823B2A, 0xA14525);
        this.setEggColorFemale(0x785B43, 0x816149);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.3F, 1.4F);
        this.setSizeY(0.5F, 1.8F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.8F, 0.43F);
        this.setOffset(0, 0.75F, 0);
        this.setAttackBias(120);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setStorage(24);
        this.setBreeding(false, 2, 6, 20, false, true);
    }
}
