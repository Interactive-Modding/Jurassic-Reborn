package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.PachycephalosaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class PachycephalosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public PachycephalosaurusDinosaur()
    {
        super();

        this.setName("Pachycephalosaurus");
        this.setScientificName("Pachycephalosaurus wyomingensis");
        this.setFamily("Pachycephalosauridae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(PachycephalosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD9E1C0, 0x91501F);
        this.setEggColorFemale(0x8E805E, 0x4A5154);
        this.setHealth(10, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(4, 10);
        this.setMaximumAge(fromDays(40));
        this.setSizeX(0.15F, 1.25F);
        this.setSizeY(0.4F, 2.3F);
        this.setStorage(27);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "teeth", "ribcage", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.9F, 0.1F);
        this.setEyeHeight(0.35F, 2.2F);
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 6, 40, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder"},
                {"leg_bones", "leg_bones", "", "teeth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
