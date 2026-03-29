package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TropeognathusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

public class TropeognathusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.3F;
    public TropeognathusDinosaur()
    {
        super();
        this.setName("Tropeognathus");
        this.setScientificName("Tropeognathus mesembrinus");
        this.setFamily("Anhangueridae");
        this.setLocation("Brazil");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(TropeognathusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E646B, 0x483141);
        this.setEggColorFemale(0x5C6C71, 0x4D3E4D);
        this.setHealth(10, 25);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 20);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.25F, 1.25F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.35F, 1.35F);
        this.setStorage(27);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "ribcage", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.15F, 0.1F);
        this.setAttackBias(650);
        this.shouldDefendOffspring();
        this.setBreeding(false, 0, 6, 20, false, true);
        this.setImprintable(false);
        this.setAvianAnimal(true);
        this.setStorage(12);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull"},
                {"", "leg_bones", "wing_bones", "teeth", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, BiomeTags.IS_BADLANDS, BiomeTags.IS_JUNGLE});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
