package mod.reborn.server.dinosaur;

import mod.reborn.server.api.Hybrid;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.RaphusrexEntity;
import mod.reborn.server.period.TimePeriod;

public class RaphusrexDinosaur extends Dinosaur implements Hybrid {
    public static final double SPEED = 0.42F;
    public RaphusrexDinosaur() {
        super();

        this.setName("RaphusRex");
        this.setDinosaurClass(RaphusrexEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.NONE);
        this.setEggColorMale(0x5d4625, 0x18283c);
        this.setEggColorFemale(0x5d4625, 0x382014);
        this.setHealth(10, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.5);
        this.setStrength(2, 25);
        this.setMaximumAge(this.fromDays(60));
        this.setEyeHeight(0.6F, 5.2F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.4F, 3.6F);
        this.setStorage(54);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.1F);
        this.setMaxHerdSize(4);
        this.setAttackBias(1000.0);
        this.setImprintable(true);
        this.setHybrid();
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 60, false, true);

        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();

    }
    public Class<? extends Dinosaur>[] getDinosaurs() {
        return new Class[]{TyrannosaurusDinosaur.class, DodoDinosaur.class};
    }
}
