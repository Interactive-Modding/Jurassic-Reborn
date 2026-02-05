package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CompsognathusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class CompsognathusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public CompsognathusDinosaur()
    {
        super();
        this.setName("Compsognathus");
        this.setScientificName("Compsognathus longipes");
        this.setFamily("Compsognathidae");
        this.setLocation("Germany");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(CompsognathusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x7B8042, 0x454B3B);
        this.setEggColorFemale(0x7D734A, 0x484A3D);
        this.setHealth(2, 4);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 3);
        this.setMaximumAge(fromDays(20));
        this.setAttackBias(50);
        this.setEyeHeight(0.20F, 0.5F);
        this.setSizeX(0.1F, 0.3F);
        this.setSizeY(0.25F, 0.55F);
        this.setStorage(9);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.2F, 0.04F);
        this.setOffset(0.0F, -3.0F, -0.8F);
        this.setBreeding(false, 2, 6, 15, false, true);
        this.setJumpHeight(2);
        this.setCanClimb(true);
        this.setImprintable(true);
        String[][] recipe =     {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","teeth"},
                {"leg_bones", "leg_bones", "", "", "arm_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.HAS_VILLAGE_SAVANNA, BiomeTags.IS_BADLANDS, Tags.Biomes.IS_SANDY});
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