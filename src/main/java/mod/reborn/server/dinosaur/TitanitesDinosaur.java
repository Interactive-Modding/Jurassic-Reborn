package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.TitanitesEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class TitanitesDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public TitanitesDinosaur() {
        super();
        this.setName("Titanites");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(TitanitesEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x0d5240, 0x52bc51);
        this.setEggColorFemale(0x59b46d, 0xb2e4ba);
        this.setHealth(4, 26);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStorage(27);
        this.setStrength(2, 4);
        this.setMaximumAge(fromDays(45));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.45F, 2.1F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.1F, 1.2F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setBones("beak", "shell_cover");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.3F);
        this.setBreeding(true, 2, 10, 20, false, false);
        this.setImprintable(false);
        this.setOffset(0, 0.5f, 0);
        String[][] recipe = {
                { "shell_cover","beak"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WATER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
