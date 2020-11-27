package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.LeaellynasauraEntity;
import mod.reborn.server.period.TimePeriod;

public class LeaellynasauraDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public LeaellynasauraDinosaur()
    {
        super();

        this.setName("Leaellynasaura");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(LeaellynasauraEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE1D0A6, 0x262B27);
        this.setEggColorFemale(0xC8B50C, 0x926045);
        this.setHealth(4, 20);
        this.setStrength(1, 5);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.35F, 0.95F);
        this.setSizeX(0.15F, 0.6F);
        this.setSizeY(0.25F, 0.95F);
        this.setStorage(9);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("shoulder", "skull", "tail_vertebrae", "tooth", "leg_bones", "neck_vertebrae", "pelvis", "ribcage");
        this.setHeadCubeName("Head ");
        this.setScale(0.7F, 0.15F);
        this.setAttackBias(-50);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "pelvis", "","",""},
                {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                {"leg_bones", "", "", "", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
