package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ApatosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class ApatosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.25F;
    public ApatosaurusDinosaur()
    {
        super();
        this.setName("Apatosaurus");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(ApatosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xA79F93, 0x987664);
        this.setEggColorFemale(0x7E7D70, 0x30343E);
        this.setHealth(10, 80);
        this.setStrength(5, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(80));
        this.setEyeHeight(0.9F, 6.8F);
        this.setSizeX(0.9F, 6.5F);
        this.setSizeY(1.0F, 6.8F);
        this.setStorage(54);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.0F, 0.25F);
        this.setBreeding(false, 4, 8, 70, false, true);
        this.setImprintable(true);
        this.setMaxHerdSize(6);
        this.setOffset(0.0F, 0.0F, 0.1F);
        String[][] recipe =     {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae","tooth"},
                {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
