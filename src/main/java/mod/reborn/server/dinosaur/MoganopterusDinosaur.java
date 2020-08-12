package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MoganopterusEntity;
import mod.reborn.server.period.TimePeriod;

public class MoganopterusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public MoganopterusDinosaur()
    {
        super();

        this.setName("Moganopterus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MoganopterusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6E2D8, 0xD67F5C);
        this.setEggColorFemale(0xE0DED3, 0xD37B58);
        this.setHealth(4, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.225F, 1.3F);
        this.setSizeX(0.3F, 1.0F);
        this.setSizeY(0.3F, 1.3F);
        this.setStorage(27);
        this.setDiet((Diet.PISCIVORE.get()));
        this.setBones("leg_bones", "pelvis", "ribcage", "skull", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.725F, 0.2F);
        this.setAttackBias(200);
        this.setBreeding(false, 2, 6, 80, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull","teeth"},
                {"", "leg_bones", "wing_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
