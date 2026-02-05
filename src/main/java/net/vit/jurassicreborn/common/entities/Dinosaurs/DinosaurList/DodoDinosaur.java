package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DodoEntity;
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

public class DodoDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public DodoDinosaur()
    {
        super();
        this.setName("Dodo");
        this.setScientificName("Raphus cucullatus");
        this.setFamily("Raphinae");
        this.setLocation("Mauritius");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(DodoEntity.class);
        this.setTimePeriod(TimePeriod.NEOGENE);
        this.setEggColorMale(0xA2996E, 0x545338);
        this.setEggColorFemale(0x908B80, 0x665C51);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.3F, 0.9F);
        this.setSizeX(0.25F, 0.5F);
        this.setSizeY(0.35F, 0.95F);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.2F);
        this.setAttackBias(5);
        this.setImprintable(true);
        this.setStorage(6);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe =     {{ "", "", "neck_vertebrae", "skull"},
                { "leg_bones","pelvis", "ribcage", "shoulder"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{BiomeTags.IS_SAVANNA, BiomeTags.IS_JUNGLE, Tags.Biomes.IS_CONIFEROUS, Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_BADLANDS, Tags.Biomes.IS_SNOWY});
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