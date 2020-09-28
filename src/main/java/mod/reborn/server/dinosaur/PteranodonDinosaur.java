package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.PteranodonEntity;
import mod.reborn.server.period.TimePeriod;

public class PteranodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.40F;
    public PteranodonDinosaur()
    {
        super();

        this.setName("Pteranodon");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(PteranodonEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x57504C, 0x24383F);
        this.setEggColorFemale(0x535F65, 0x56312C);
        this.setHealth(10, 35);
        this.setStrength(5, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.8F, 2.0F);
        this.setSizeY(0.6F, 1.8F);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("leg_bones", "neck_vertebrae", "pelvis", "ribcage", "skull", "tail_vertebrae", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.2F, 0.3F);
        this.setAttackBias(800);
        this.shouldDefendOffspring();
        this.setImprintable(false);
        this.setBreeding(false, 2, 6, 80, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull"},
                {"", "leg_bones", "wing_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
