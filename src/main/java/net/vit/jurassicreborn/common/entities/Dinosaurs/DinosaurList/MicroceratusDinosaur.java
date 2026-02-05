package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import java.util.ArrayList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroceratusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class MicroceratusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public MicroceratusDinosaur()
    {
        super();
        this.setName("Microceratus");
        this.setScientificName("Microceratus gobiensis");
        this.setFamily("Protoceratopsidae");
        this.setLocation("Mongolia");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(MicroceratusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xcf2c2b, 0x9DE172);
        this.setEggColorFemale(0xC7B94C, 0x943B2D);
        this.setHealth(2, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.14F, 0.5F);
        this.setSizeX(0.15F, 0.4F);
        this.setSizeY(0.18F, 0.55F);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.3F, 0.09F);
        this.setAttackBias(5);
        this.setImprintable(true);
        this.setFlee(true);
        this.setStorage(6);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"leg_bones", "arm_bones", "", "shoulder", "teeth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.HAS_VILLAGE_SAVANNA, Tags.Biomes.IS_CONIFEROUS, BiomeTags.IS_BADLANDS});
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