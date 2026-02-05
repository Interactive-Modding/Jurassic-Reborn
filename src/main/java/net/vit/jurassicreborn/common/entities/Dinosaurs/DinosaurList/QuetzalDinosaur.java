package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.QuetzalEntity;
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

public class QuetzalDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public QuetzalDinosaur()
    {
        super();

        this.setName("Quetzalcoatlus");
        this.setScientificName("Quetzalcoatlus northropi");
        this.setFamily("Azhdarchidae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(QuetzalEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x8d8f8b, 0x1c2d40);
        this.setEggColorFemale(0x8d8f8b, 0x2e201a);
        this.setHealth(10, 30);
        this.setStrength(2, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.55F, 2.9F);
        this.setSizeX(0.8F, 2.4F);
        this.setSizeY(0.6F, 3.0F);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "ribcage", "leg_bones", "neck_vertebrae", "pelvis", "tail_vertebrae", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.15F);
        this.setAttackBias(1200);
        this.shouldDefendOffspring();
        this.setBreeding(false, 1, 4, 80, false, true);
        this.enableSkeleton();
        this.setAvianAnimal(true);
        String[][] recipe = {
                {"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","",""},
                {"", "leg_bones", "", "wing_bones", ""}};
        this.setRecipe(recipe);
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
    }
}