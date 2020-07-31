package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DimorphodonEntity;
import mod.reborn.server.period.TimePeriod;

public class DimorphodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public DimorphodonDinosaur()
    {
        super();

        this.setName("Dimorphodon");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DimorphodonEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xB2AC94, 0x636644);
        this.setEggColorFemale(0xBDB4A9, 0x726B57);
        this.setHealth(6, 12);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setAttackBias(80);
        this.setEyeHeight(0.25F, 0.7F);
        this.setSizeX(0.25F, 0.5F);
        this.setSizeY(0.25F, 0.75F);
        this.setStorage(9);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("leg_bones", "neck", "ribs_and_spine", "shoulder_blade", "skull", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setImprintable(true);
        this.setStorage(12);
        this.setScale(0.5F, 0.15F);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "", "","neck","skull"},
                {"tail_vertebrae", "ribs_and_spine", "shoulder_blade","","teeth"},
                {"", "leg_bones", "wing_bones", "", ""},
                {"", "", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
