package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CompsognathusEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MicroraptorEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.OviraptorEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraftforge.common.Tags;
import net.minecraftforge.registries.ForgeRegistries;

public class OviraptorDinosaur extends Dinosaur
{
    public static final double SPEED = 0.40F;
    public OviraptorDinosaur(){
        super();

        this.setName("Oviraptor");
        this.setScientificName("Oviraptor philoceratops");
        this.setFamily("Oviraptoridae");
        this.setLocation("Mongolia");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(OviraptorEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x8f1528, 0x4c5565);
        this.setEggColorFemale(0x02621a, 0xd2cd03);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(9);
        this.setAttackSpeed(1.11);
        this.setStrength(2, 4);
        this.setMaximumAge(fromDays(25));
        this.setEyeHeight(0.25F, 0.9F);
        this.setSizeX(0.25F, 0.6F);
        this.setSizeY(0.32F, 0.95F);
        this.setMaxHerdSize(8);
        this.setDiet(Diet.CARNIVORE.get().withModule(new Diet.DietModule(FoodType.PLANT)).withModule(new Diet.DietModule(FoodType.MEAT).withCondition(entity -> entity instanceof CompsognathusEntity || entity instanceof MicroraptorEntity)));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Upper Head");
        this.setAttackBias(5);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setScale(0.4F, 0.09F);
        this.setBreeding(false, 2, 4, 30, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder"},
                {"", "leg_bones", "", "arm_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{ Tags.Biomes.IS_SANDY, Tags.Biomes.IS_DRY, BiomeTags.IS_SAVANNA});
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