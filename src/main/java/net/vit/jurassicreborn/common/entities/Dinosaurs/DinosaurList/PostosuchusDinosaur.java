package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.PostosuchusEntity;
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

public class PostosuchusDinosaur extends Dinosaur {
    public static final double SPEED = 0.35F;
    public PostosuchusDinosaur() {
        super();

        this.setName("Postosuchus");
        this.setScientificName("Postosuchus kirkpatricki");
        this.setFamily("Rauisuchidae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(PostosuchusEntity.class);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0xAA9575, 0x744942 );
        this.setEggColorFemale(0xAC9574, 0x985D10);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(2, 16);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.25F, 1.7F);
        this.setSizeX(0.15F, 1.4F);
        this.setSizeY(0.3F, 1.8F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tooth", "femur", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.1F);
        this.setAttackBias(120);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "femur", "ribcage","shoulder"},
                {"leg_bones", "leg_bones", "", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();

        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{ BiomeTags.IS_RIVER, Tags.Biomes.IS_SWAMP, BiomeTags.HAS_VILLAGE_SAVANNA, BiomeTags.IS_BEACH});
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