package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.AnkylosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class AnkylosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public AnkylosaurusDinosaur()
    {
        super();
        this.setName("Ankylosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(AnkylosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xAB9B82, 0x7C6270);
        this.setEggColorFemale(0x554E45, 0x3F3935);
        this.setHealth(20, 120);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 10);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.4F, 2.0F);
        this.setSizeX(0.8F, 3.0F);
        this.setSizeY(0.6F, 3.0F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("armor_plating", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_club", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head ");
        this.setScale(2.3F, 0.45F);
        this.setBreeding(false, 0, 6, 44, false, true);
        this.setImprintable(true);
        this.setStorage(24);
        this.shouldDefendOffspring();
    }
}
