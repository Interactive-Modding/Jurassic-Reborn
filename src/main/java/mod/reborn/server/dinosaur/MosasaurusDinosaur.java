package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MosasaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class MosasaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public MosasaurusDinosaur(){
        super();

        this.setName("Mosasaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MosasaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7e7257, 0x49412e);
        this.setEggColorFemale(0x5e8086, 0x4c6b70);
        this.setHealth(20, 150);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStrength(20, 50);
        this.setMaximumAge(fromDays(70));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.225F, 1.3F);
        this.setSizeX(1.8F, 4F);
        this.setSizeY(1.6F, 3.6F);
        this.setStorage(27);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Main head");
        this.setScale(4F, 0.20F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setAttackBias(1200);
        this.setAttackSpeed(2);
        this.setRotationAngle(50, 20);
        this.setStorage(12);
        this.setPaleoPadScale(3);
        this.setImprintable(false);
        this.setBreeding(true, 1, 3, 80, false, true);
        String[][] recipe =     {{ "tail_fluke", "tail_vertebrae", "spine", "ribcage", "skull"},
                                { "hind_flipper", "", "front_flipper", "inner_teeth", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        this.setOffset(0,1,0);
    }
}
