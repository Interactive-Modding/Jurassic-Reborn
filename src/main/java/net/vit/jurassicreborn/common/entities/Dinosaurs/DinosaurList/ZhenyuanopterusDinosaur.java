package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ZhenyuanopterusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class ZhenyuanopterusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public ZhenyuanopterusDinosaur()
    {
        super();
        this.setName("Zhenyuanopterus");
        this.setScientificName("Zhenyuanopterus longirostris");
        this.setFamily("Boreopteridae");
        this.setLocation("China");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(ZhenyuanopterusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x434F4E, 0x0F1010);
        this.setEggColorFemale(0x4A5957, 0xB9B7A3);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setHealth(6, 25);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.225F, 1.25F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.35F, 1.35F);
        this.setStorage(27);
        this.setDiet((Diet.PISCIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "tail_vertebrae", "wing_bones", "teeth", "ribcage");
        this.setHeadCubeName("Head");
        this.setScale(0.7F, 0.15F);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 80, false, true);
        this.setImprintable(false);
        this.setStorage(12);
        this.setAvianAnimal(true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull"},
                {"", "leg_bones", "wing_bones", "teeth", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();


        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{BiomeTags.HAS_VILLAGE_SAVANNA, BiomeTags.IS_JUNGLE, BiomeTags.IS_MOUNTAIN, Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_BADLANDS});
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

        this.setSpawn(1, biomeList);
this.init();
    }
}