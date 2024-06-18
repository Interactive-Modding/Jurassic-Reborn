package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ParapuzosiaEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;
import java.util.ArrayList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class ParapuzosiaDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public ParapuzosiaDinosaur() {
        super();
        this.setName("Parapuzosia");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(ParapuzosiaEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xf7e9da, 0xfbd5d9);
        this.setEggColorFemale(0xf7e9da, 0xf39db0);
        this.setHealth(4, 36);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStorage(27);
        this.setStrength(2, 4);
        this.setMaximumAge(fromDays(50));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.90F, 4.2F);
        this.setSizeX(0.6F, 3.8F);
        this.setSizeY(1.0F, 4.6F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setBones("beak", "shell_cover","radula");
        this.setHeadCubeName("Head");
        this.setScale(3.0F, 0.6F);
        this.setBreeding(true, 2, 10, 20, false, false);
        this.setImprintable(false);
        this.setOffset(0, 0.6f, 0);
        String[][] recipe = {
                { "shell_cover","radula","beak"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WATER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
