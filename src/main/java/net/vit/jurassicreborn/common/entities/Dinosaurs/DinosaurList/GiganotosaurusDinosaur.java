package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.GiganotosaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class GiganotosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public GiganotosaurusDinosaur() {
        super();
        this.setName("Giganotosaurus");
        this.setScientificName("Giganotosaurus carolinii");
        this.setFamily("Carcharodontosauridae");
        this.setLocation("Argentina");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(GiganotosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4F3F33, 0x4F3F33);
        this.setEggColorFemale(0x756E54, 0x4B474A);
        this.setHealth(20, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(4, 35);
        this.setEyeHeight(0.45F, 3.5F);
        this.setSizeX(0.2F, 3.6F);
        this.setSizeY(0.5F, 3.6F);
        this.setMaximumAge(fromDays(60));
        this.setStorage(54);
        this.setAttackSpeed(1.4);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "tooth", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "leg_bones", "arm_bones", "tail_vertebrae", "foot_bones", "claw");
        this.setHeadCubeName("Head");
        this.setScale(1.47F, 0.1F);
        this.shouldDefendOffspring();
        this.setAttackBias(280);
        this.setBreeding(false, 2, 6, 70, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
