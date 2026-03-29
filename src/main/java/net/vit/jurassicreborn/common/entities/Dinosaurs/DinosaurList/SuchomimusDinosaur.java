package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;
import java.util.List;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.SuchomimusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class SuchomimusDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public SuchomimusDinosaur() {
        super();
        this.setName("Suchomimus");
        this.setScientificName("Suchomimus tenerensis");
        this.setFamily("Spinosauridae");
        this.setLocation("Niger");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(SuchomimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xCCB674, 0x4A5966);
        this.setEggColorFemale(0xB3BB6D, 0x45676B);
        this.setHealth(10, 45);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(5, 30);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.35F, 3.1F);
        this.setSizeX(0.2F, 2.0F);
        this.setSizeY(0.4F, 3.2F);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("arm_bones", "claw", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "foot_bones");
        this.setHeadCubeName("Head");
        this.setScale(1.5F, 0.1F);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "", "leg_bones", "arm_bones", "claw"},
                {"", "", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
