package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ProtoceratopsEntity;
import mod.reborn.server.period.TimePeriod;

public class ProtoceratopsDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public ProtoceratopsDinosaur()
    {
        super();

        this.setName("Protoceratops");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(ProtoceratopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(10, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 0.85F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.4F, 1.25F);
        this.setStorage(27);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "teeth", "tail_vertebrae", "pelvis", "ribcage", "front_leg_bones", "hind_leg_bones", "shoulder");
        this.setHeadCubeName("Head");
        this.setAttackBias(1);
        this.setScale(1.2F, 0.35F);
        this.setImprintable(true);
        this.setStorage(12);
        this.setBreeding(false, 2, 6, 20, false, true);
    }
}
