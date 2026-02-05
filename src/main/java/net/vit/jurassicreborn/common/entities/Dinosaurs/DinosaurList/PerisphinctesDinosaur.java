package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.PerisphinctesEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import java.util.ArrayList;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class PerisphinctesDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public PerisphinctesDinosaur() {
        super();
        this.setName("Perisphinctes");
        this.setScientificName("Perisphinctes Waagen");
        this.setFamily("Ammonoidea");
        this.setLocation("Global");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(PerisphinctesEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xE4936B, 0xBC5312);
        this.setEggColorFemale(0xE4936B, 0xB86D1B);
        this.setHealth(4, 26);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(2, 4);
        this.setMaximumAge(fromDays(45));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.25F, 0.78F);
        this.setSizeX(0.3F, 0.8F);
        this.setSizeY(0.5F, 0.8F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        // Bone names should not include the species prefix or the fossil system
        // will duplicate the prefix when generating item IDs, preventing the
        // skeleton and action figure from recognizing the bones.
        this.setBones("beak", "shell_cover");
        this.setHeadCubeName("Head");
        this.setScale(0.4F, 0.2F);
        this.setBreeding(true, 2, 10, 20, false, false);
        this.setImprintable(false);
        this.setOffset(0, 0.2f, 0);
        String[][] recipe = {
                { "shell_cover", "beak" }};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<ResourceKey<Biome>>();
        TagKey<Biome>[] tags = (new TagKey[]{ Tags.Biomes.IS_WATER, BiomeTags.IS_OCEAN});
        ArrayList<Biome> allBiomes = new ArrayList<>(ForgeRegistries.BIOMES.getValues());

        biomeList = new ArrayList<>(allBiomes.stream().filter((biome ->{
            boolean accept = false;
            
            for(var tag : tags){
                if(ForgeRegistries.BIOMES.tags().getTag(tag).contains(biome)){
                    accept = true;
                }
            }
            return accept;
            
        })).map((biome) -> ForgeRegistries.BIOMES.getResourceKey(biome).get()).toList());
        this.setSpawn(1, biomeList);
this.init();
    }
}