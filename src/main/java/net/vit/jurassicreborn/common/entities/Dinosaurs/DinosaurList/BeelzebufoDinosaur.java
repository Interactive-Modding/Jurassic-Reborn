package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import java.util.ArrayList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.BeelzebufoEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.MovementType;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class BeelzebufoDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public BeelzebufoDinosaur(){
        super();
        this.setName("Beelzebufo");
        this.setScientificName("Beelzebufo ampinga");
        this.setFamily("Ceratophryidae");
        this.setLocation("Madagascar");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(BeelzebufoEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xECC35C, 0xCDC605);
        this.setEggColorFemale(0xE4CC92, 0xCEC704);
        this.setHealth(4, 10);
        this.setSpeed((SPEED -0.35), SPEED);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.15F, 0.4F);
        this.setSizeX(0.2F, 0.5F);
        this.setSizeY(0.2F, 0.6F);
        this.setMovementType(MovementType.NEAR_SURFACE);
        this.setStorage(5);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("leg_bones", "skull", "ribcage", "teeth");
        this.setHeadCubeName("Head");
        this.setMarineAnimal(true);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setScale(1.05F, 0.065F);
        this.shouldDefendOffspring();
        String[][] recipe =     {
                { "teeth"},
                { "skull"},
                { "ribcage"},
                { "leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<ResourceKey<Biome>>();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_SANDY, BiomeTags.IS_BEACH, BiomeTags.IS_RIVER, Tags.Biomes.IS_SNOWY});
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