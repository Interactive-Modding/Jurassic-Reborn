package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.TroodonEntity;
import mod.reborn.server.period.TimePeriod;

public class TroodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public TroodonDinosaur()
    {
        super();

        this.setName("Troodon");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(TroodonEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9DAA7A, 0x636B67);
        this.setEggColorFemale(0xA2A67C, 0x646D66);
        this.setHealth(5, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.3F, 0.95F);
        this.setSizeX(0.3F, 0.7F);
        this.setSizeY(0.4F, 0.95F);
        this.setStorage(25);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.NOCTURNAL);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head UPPER");
        this.setAttackBias(600);
        this.setScale(0.75F, 0.25F);
        this.setOffset(0.0F, 0.0F, 0.5F);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(false);
        this.setStorage(6);
    }
}
