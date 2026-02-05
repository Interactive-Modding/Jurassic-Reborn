package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DiplocaulusEntity;
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

public class DiplocaulusDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public DiplocaulusDinosaur() {
        super();
        this.setName("Diplocaulus");
        this.setScientificName("Diplocaulus minimus");
        this.setFamily("Diplocaulidae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DiplocaulusEntity.class);
        this.setTimePeriod(TimePeriod.PERMIAN);
        this.setEggColorMale(0xBDD9DE, 0x286A7F);
        this.setEggColorFemale(0xCDDEE7, 0x285880);
        this.setHealth(2, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(18);
        this.setStrength(1, 4);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.25F, 0.55F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.3F, 0.6F);
        this.setDiet(Diet.PISCIVORE.get());
        this.setBones("skull", "teeth", "foot_bone","leg_bones", "ribcage", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.2F, 0.05F);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setOffset(0,-1.5F,0);
        this.setAttackBias(10);
        this.setImprintable(true);
        this.setBreeding(true, 2, 6, 20, false, true);
        this.setMarineAnimal(true);
        String[][] recipe =     {{ "", "", "skull"},
                {"tail_vertebrae", "ribcage","teeth"},
                { "leg_bones", "", "leg_bones"},
                { "", "", "foot_bone"}};
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