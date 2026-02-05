package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.StegosaurusEntity;
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

public class StegosaurusDinosaur extends Dinosaur{
    public static final double SPEED = 0.35F;
    public StegosaurusDinosaur() {
        super();

        this.setName("Stegosaurus");
        this.setScientificName("Stegosaurus armatus");
        this.setFamily("Stegosauridae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(StegosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xBABF83, 0x75964E);
        this.setEggColorFemale(0xC8BC9A, 0x827D54);
        this.setHealth(10, 50);
        this.setStrength(12, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.26F, 3.5F);
        this.setSizeX(0.2F, 3.6F);
        this.setSizeY(0.3F, 3.6F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck", "pelvis", "ribcage", "shoulder", "skull", "tail", "thagomizer", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(0.65F, 0.05F);
        this.setAttackBias(350);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"tail", "pelvis", "ribcage","neck","skull"},
                {"thagomizer", "hind_leg_bones", "hind_leg_bones", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        TagKey<Biome>[] tags = (new TagKey[]{BiomeTags.HAS_VILLAGE_SAVANNA, Tags.Biomes.IS_CONIFEROUS, BiomeTags.IS_MOUNTAIN, Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_BADLANDS});
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
