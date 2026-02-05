package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DunkleosteusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class DunkleosteusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.45F;
    public DunkleosteusDinosaur()
    {
        super();
        this.setName("Dunkleosteus");
        this.setScientificName("Dunkleosteus terrelli");
        this.setFamily("Dunkleosteidae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DunkleosteusEntity.class);
        this.setTimePeriod(TimePeriod.DEVONIAN);
        this.setEggColorMale(0xA89B8C, 0x753A28);
        this.setEggColorFemale(0xA6A588, 0x785F2A);
        this.setHealth(16, 60);
        this.setSpeed((SPEED -0.1), SPEED);
        this.setStrength(10, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.15F, 1.9F);
        this.setSizeX(0.3F, 2.7F);
        this.setSizeY(0.2F, 2.0F);
        this.setMarineAnimal(true);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setBones("mouth_plates", "skull", "dorsal_fin", "spine");
        this.setHeadCubeName("Main head");
        this.setScale(1.2F, 0.15F);
        this.shouldDefendOffspring();
        this.setAttackBias(420);
        this.setImprintable(false);
        this.setBreeding(true, 2, 6, 20, false, true);
        String[][] recipe =     {
                {"", "", "dorsal_fin","",""},
                {"spine", "spine", "spine", "skull", "mouth_plates"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_WATER, BiomeTags.IS_OCEAN, BiomeTags.IS_RIVER});
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