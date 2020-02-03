package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CorythosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class CorythosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.41F;
    public CorythosaurusDinosaur()
    {
        super();

        this.setName("Corythosaurus");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(CorythosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xBAA87E, 0x5E7201);
        this.setEggColorFemale(0xB3A27D, 0xE9BF47);
        this.setHealth(10, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 15);
        this.setMaximumAge(fromDays(40));
        this.setAttackBias(50);
        this.setEyeHeight(0.35F, 1.9F);
        this.setSizeX(0.4F, 1.8F);
        this.setSizeY(0.6F, 2.8F);
        this.setStorage(36);
        this.setAttackBias(12);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("cheek_teeth", "front_leg_bones", "hind_leg_bones", "pelvis", "shoulder", "skull", "tail_vertebrae", "ribcage");
        this.setHeadCubeName("Head");
        this.setScale(1.75F, 0.35F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.setImprintable(true);
        this.setStorage(12);
        this.setBreeding(false, 1, 4, 20, false, true);

    }
}
