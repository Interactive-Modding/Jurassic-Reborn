package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CearadactylusEntity;
import mod.reborn.server.period.TimePeriod;

public class CearadactylusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public CearadactylusDinosaur()
    {
        super();

        this.setName("Cearadactylus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CearadactylusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x64A0B3, 0x3B3937);
        this.setEggColorFemale(0xB55252, 0x4C423A);
        this.setHealth(10, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.45F, 1.45F);
        this.setSizeX(0.35F, 1.5F);
        this.setSizeY(0.45F, 1.55F);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "neck_vertebrae", "tail_vertebrae", "teeth", "wing_bones", "ribcage");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.45F);
        this.setBreeding(false, 2, 6, 80, false, true);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","","teeth"},
                {"", "leg_bones", "", "wing_bones", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
