package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.LeptictidiumEntity;
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

public class LeptictidiumDinosaur extends Dinosaur
{
    public static final double SPEED = 0.39F;
    public LeptictidiumDinosaur()
    {
        super();
        this.setName("Leptictidium");
        this.setScientificName("Leptictidium auderiense");
        this.setFamily("Leptictidae");
        this.setLocation("Europe");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(LeptictidiumEntity.class);
        this.setTimePeriod(TimePeriod.PALEOGENE); // TODO EOCENE
        this.setEggColorMale(0x362410, 0x978A78);
        this.setEggColorFemale(0xAFA27E, 0x3E2D17);
        this.setHealth(4, 14);
        this.setStrength(2, 4);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(25));
        this.setEyeHeight(0.14F, 0.7F);
        this.setSizeX(0.1F, 0.5F);
        this.setSizeY(0.15F, 0.75F);
        this.setMammal(true);
        this.setDiet((Diet.INHERBIVORE.get()));
        this.setBones("arm_bones", "leg_bones", "pelvis", "ribcage", "skull", "tail_vertebrae", "teeth", "neck_vertebrae", "shoulder");
        this.setHeadCubeName("Head");
        this.setScale(0.3F, 0.15F);
        this.setAttackBias(-50);
        this.setImprintable(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setStorage(12);
        this.setBreeding(true, 2, 4, 20, false, true);
        String[][] recipe = {
                {"", "pelvis", "","",""},
                {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                {"leg_bones", "", "", "arm_bones", "teeth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<ResourceKey<Biome>> biomeList = new ArrayList<>();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.HAS_VILLAGE_SAVANNA, Tags.Biomes.IS_CONIFEROUS, BiomeTags.IS_BADLANDS, Tags.Biomes.IS_SNOWY});
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