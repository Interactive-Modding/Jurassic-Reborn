package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MussaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class MussaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.32F;
    public MussaurusDinosaur() {
        super();
        this.setName("Mussaurus");
        this.setScientificName("Mussaurus patagonicus");
        this.setFamily("Mussauridae");
        this.setLocation("Argentina");
        this.setDinosaurClass(MussaurusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setFlee(true);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0x6F9845, 0x211F16);
        this.setEggColorFemale(0x526024, 0x222611);
        this.setHealth(2, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 2);
        this.setMaximumAge(this.fromDays(30));
        this.setEyeHeight(0.15F, 0.9F);
        this.setSizeX(0.25F, 1F);
        this.setSizeY(0.2F, 1.0F);
        this.setStorage(9);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head1");
        this.setScale(0.6F, 0.1F);
        this.setFlockSpeed(1.10F);
        this.setMaxHerdSize(20);
        this.setAttackBias(-500.0);
        this.setImprintable(true);
        this.setOffset(0.0F, 0.0F, 0.5F);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "pelvis", "","",""},
                {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                {"leg_bones", "leg_bones", "arm_bones", "arm_bones", "teeth"}};
        this.setRecipe(recipe);
        
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{ BiomeTags.IS_FOREST, Tags.Biomes.IS_PLAINS});
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
        this.enableSkeleton();
        
    }
}