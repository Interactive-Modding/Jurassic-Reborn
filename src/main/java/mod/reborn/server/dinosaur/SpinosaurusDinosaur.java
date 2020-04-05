package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.SpinosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class SpinosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.6F;
    public SpinosaurusDinosaur()
    {
        super();

        this.setName("Spinosaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(SpinosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x48403D, 0xC5CFDA);
        this.setEggColorFemale(0x756862, 0x91594D);
        this.setHealth(10, 90);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 40);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.6F, 3.8F);
        this.setSizeX(0.6F, 3.0F);
        this.setSizeY(0.8F, 4.8F);
        this.setStorage(54);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("skull", "tooth", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2.37F, 0.3F);
        this.setAttackBias(900);
        this.shouldDefendOffspring();
        this.setStorage(12);
        this.setBreeding(false, 0, 4, 20, false, true);
    }
}
