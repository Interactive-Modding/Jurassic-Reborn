package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.BaryonyxEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;

public class BaryonyxDinosaur extends Dinosaur
{
    public static final double SPEED = 0.40F;
    public BaryonyxDinosaur()
    {
        super();
        this.setName("Baryonyx");
        this.setScientificName("Baryonyx walkeri");
        this.setFamily("Spinosauridae");
        this.setLocation("United Kingdom");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(BaryonyxEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x796e20, 0x077d73);
        this.setEggColorFemale(0x8a7f31, 0x463e15);
        this.setHealth(6, 35);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.6);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.45F, 2.85F);
        this.setSizeX(0.3F, 1.5F);
        this.setSizeY(0.5F, 2.95F);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("arm_bones", "claw", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "foot_bones");
        this.setHeadCubeName("Head");
        this.setBreeding(false, 2, 6, 60, false, true);
        this.setScale(1.38F, 0.1F);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setDefendOwner(true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", ""},
                {"", "foot_bones", "", "claw", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));

        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, Tags.Biomes.IS_SWAMP, BiomeTags.IS_RIVER});
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