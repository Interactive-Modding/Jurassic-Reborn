package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TitanitesEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.TimePeriod;


public class TitanitesDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public TitanitesDinosaur() {
        super();
        this.setName("Titanites");
        this.setScientificName("Titanites giganteus");
        this.setFamily("Titanitidae");
        this.setLocation("England");
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
        this.setEyeHeight(0.05F, 1.1F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.1F, 1.8F);
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
//        ArrayList<Biome> biomeList = new ArrayList<Biome>();
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WATER));
//        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}