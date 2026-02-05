package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.GuanlongEntity;
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

public class GuanlongDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public GuanlongDinosaur() {
        super();
        this.setName("Guanlong");
        this.setScientificName("Guanlong wucaii");
        this.setFamily("Proceratosauridae");
        this.setLocation("China");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(GuanlongEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xEFE3B9, 0xEFE3B9);
        this.setEggColorFemale(0xEFE2B4, 0x4E4D4B);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(2, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.35F, 1.3F);
        this.setSizeX(0.2F, 1.2F);
        this.setSizeY(0.4F, 1.4F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae", "tooth", "claw", "foot_bones", "skull");
        this.setHeadCubeName("Head");
        this.setScale(0.7F, 0.1F);
        this.shouldDefendOffspring();
        this.setAttackBias(1200);
        this.setAttackSpeed(1.2F);
        this.setDefendOwner(true);
        this.setImprintable(true);
        this.setBreeding(false, 2, 4, 24, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae","pelvis", "ribcage","shoulder"},
                {"leg_bones", "arm_bones", "claw", "tooth"},
                {"foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{BiomeTags.IS_SAVANNA, Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST});
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