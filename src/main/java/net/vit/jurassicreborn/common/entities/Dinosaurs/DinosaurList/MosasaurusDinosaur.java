package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MosasaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;
public class MosasaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.45F;
    public MosasaurusDinosaur(){
        super();

        this.setName("Mosasaurus");
        this.setScientificName("Mosasaurus maximus");
        this.setFamily("Mosasauridae");
        this.setLocation("Netherlands");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MosasaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7e7257, 0x49412e);
        this.setEggColorFemale(0x5e8086, 0x4c6b70);
        this.setHealth(20, 150);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStrength(20, 50);
        this.setMaximumAge(fromDays(70));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.225F, 3.5F);
        this.setSizeX(1.8F, 3.6F);
        this.setSizeY(1.0F, 3.6F);
        this.setStorage(27);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("front_flipper", "hind_flipper", "inner_teeth", "ribcage", "skull", "spine", "tail_fluke", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Main head");
        this.setScale(4F, 0.20F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setAttackBias(1200);
        this.setAttackSpeed(2);
        this.setStorage(12);
        this.setPaleoPadScale(3);
        this.setImprintable(false);
        this.setBreeding(true, 1, 3, 80, false, true);
        String[][] recipe =     {{ "tail_fluke", "tail_vertebrae", "spine", "ribcage", "skull"},
                { "hind_flipper", "", "front_flipper", "inner_teeth", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        this.setOffset(0,1,0);
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{ BiomeTags.IS_OCEAN, Tags.Biomes.IS_WATER});
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