package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ZhenyuanopterusEntity;
import mod.reborn.server.period.TimePeriod;

public class ZhenyuanopterusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public ZhenyuanopterusDinosaur()
    {
        super();
        this.setName("Zhenyuanopterus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(ZhenyuanopterusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x434F4E, 0x0F1010);
        this.setEggColorFemale(0x4A5957, 0xB9B7A3);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setHealth(6, 25);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.225F, 1.3F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.3F, 1.3F);
        this.setStorage(27);
        this.setDiet((Diet.PISCIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.7F, 0.25F);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 80, false, true);
        this.setImprintable(false);
        this.setStorage(12);
        this.setAvianAnimal(true);
    }
}
