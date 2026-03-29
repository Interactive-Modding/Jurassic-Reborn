package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;
import java.util.List;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MetriacanthosaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

public class MetriacanthosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public MetriacanthosaurusDinosaur()
    {
        super();

        this.setName("Metriacanthosaurus");
        this.setScientificName("Metriacanthosaurus parkeri");
        this.setFamily("Metriacanthosauridae");
        this.setLocation("England");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MetriacanthosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xB05E1C, 0xE7DB27);
        this.setEggColorFemale(0xB5985E, 0x60451C);
        this.setHealth(10, 40);
        this.setStrength(2, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.25F, 1.85F);
        this.setSizeX(0.15F, 1.15F);
        this.setSizeY(0.25F, 1.95F);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.2F, 0.15F);
        this.shouldDefendOffspring();
        this.setAttackBias(120);
        this.setBreeding(false, 2, 4, 70, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "", "leg_bones", "arm_bones", ""},
                {"", "", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
