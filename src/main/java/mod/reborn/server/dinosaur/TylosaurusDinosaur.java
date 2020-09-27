package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.TylosaurusEntity;
import mod.reborn.server.period.TimePeriod;

public class TylosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.75F;
    public TylosaurusDinosaur()
    {
        super();

        this.setName("Tylosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(TylosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x187D75, 0x15544F);
        this.setEggColorFemale(0x798A8F, 0x101517);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.65), SPEED);
        this.setStrength(5, 30);
        this.setMaximumAge(fromDays(60));
        this.setEyeHeight(0.35F, 2.35F);
        this.setSizeX(0.85F, 4.5F);
        this.setSizeY(0.55F, 2.95F);
        this.setStorage(54);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Main head");
        this.setScale(2.2F, 0.45F);
        this.setOffset(0.0F, 0.0F, 1.0F);
        this.shouldDefendOffspring();
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setAttackBias(500);
        this.setMarineAnimal(true);
        this.givesDirectBirth();
        this.setBreeding(true, 1, 4, 60, false, true);
        this.setImprintable(false);
        this.setStorage(54);
        String[][] recipe =     {
                {"tail_fluke", "tail_vertebrae", "spine", "ribcage", "skull"},
                {"", "", "", "inner_teeth", "tooth"},
                {"hind_flipper", "hind_flipper", "", "front_flipper", "front_flipper"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
