package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MegapiranhaEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;

public class MegapiranhaDinosaur extends Dinosaur
{
    public static final double SPEED = 0.5F;
    public MegapiranhaDinosaur()
    {
        super();

        this.setName("Megapiranha");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MegapiranhaEntity.class);
        this.setTimePeriod(TimePeriod.NEOGENE);
        this.setEggColorMale(0x17100B, 0x645C54);
        this.setEggColorFemale(0x7D735D, 0x322922);
        this.setHealth(2, 12);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(4, 8);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.35F, 0.35F);//TODO uh?
        this.setSizeX(0.15F, 0.5F);
        this.setSizeY(0.15F, 0.7F);
        this.setMarineAnimal(true);
        this.setStorage(18);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setBones("anal_fin", "body_fins", "caudal_fin", "dorsal_fin", "skull", "spine", "teeth");
        this.setHeadCubeName("Neck ");
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setScale(1.0F, 0.15F);
        this.setOffset(0.0F, 0.65F, -0.25F);
        this.setMarineAnimal(true);
        this.setAttackBias(400);
        this.setImprintable(false);
        this.setBreeding(true, 0, 6, 20, false, true);
        String[][] recipe =     {{ "", "dorsal_fin", "", ""},
                                { "caudal_fin", "spine", "body_fins", "skull"},
                                { "anal_fin","","", "teeth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
    }
}
