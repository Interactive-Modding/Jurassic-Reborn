package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.TropeognathusEntity;
import mod.reborn.server.period.TimePeriod;

public class TropeognathusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public TropeognathusDinosaur()
    {
        super();

        this.setName("Tropeognathus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(TropeognathusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E646B, 0x483141);
        this.setEggColorFemale(0x5C6C71, 0x4D3E4D);
        this.setHealth(10, 25);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.45F, 1.45F);
        this.setSizeX(0.35F, 1.5F);
        this.setSizeY(0.45F, 1.55F);
        this.setStorage(27);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "ribcage", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.15F, 0.3F);
        this.setAttackBias(650);
        this.shouldDefendOffspring();
        this.setBreeding(false, 0, 6, 20, false, true);
        this.setImprintable(false);
        this.setStorage(12);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull"},
                {"", "leg_bones", "wing_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
