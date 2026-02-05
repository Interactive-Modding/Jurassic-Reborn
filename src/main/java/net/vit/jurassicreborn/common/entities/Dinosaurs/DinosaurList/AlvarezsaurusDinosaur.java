package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AlvarezsaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;

import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class AlvarezsaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public AlvarezsaurusDinosaur(){
        super();
        this.setName("Alvarezsaurus");
        this.setScientificName("Alvarezsaurus calvoi");
        this.setFamily("Alvarezsauridae");
        this.setLocation("Argentina");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(AlvarezsaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xC1946C, 0xE39B48);
        this.setEggColorFemale(0xBD8D62, 0xACACAC);
        this.setHealth(4, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 4);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.225F, 0.59F);
        this.setSizeX(0.3F, 0.5F);
        this.setSizeY(0.3F, 0.6F);
        this.setStorage(27);
        this.setDiet((Diet.INCARNIVORE.get()));
        this.setBones("claw", "arm_bones", "foot_bones","leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Upper Head");
        this.setScale(0.5F, 0.125F);
        this.setBreeding(false, 2, 6, 20, false, false);
        this.setImprintable(true);
        this.setFlee(true);
        this.setMaxHerdSize(12);
        this.setDefendOwner(true);
        String[][] recipe = {
                {"", "pelvis", "","",""},
                {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{ Tags.Biomes.IS_SANDY, Tags.Biomes.IS_DRY, BiomeTags.HAS_VILLAGE_SAVANNA});
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

    }
}