package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MosasaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class MosasaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public MosasaurusDinosaur(){
        super();

        this.setName("Mosasaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MosasaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB5ACB6, 0x5B4F5F);
        this.setEggColorFemale(0xA0AEB2, 0x4F595D);
        this.setHealth(75, 150);
        this.setSpeed((SPEED -0.15), SPEED);
        this.setStrength(25, 200);
        this.setMaximumAge(fromDays(40));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.225F, 1.3F);
        this.setSizeX(1.2F, 3F);
        this.setSizeY(1.0F, 2.8F);
        this.setStorage(27);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(3F, 0.25F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setAttackBias(1200);
        this.setStorage(12);
        this.setImprintable(false);
        this.setBreeding(true, 0, 6, 20, false, true);
    }
}
