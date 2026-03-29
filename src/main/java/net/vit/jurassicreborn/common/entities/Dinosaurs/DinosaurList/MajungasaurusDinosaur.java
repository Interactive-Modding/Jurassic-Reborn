package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MajungasaurusEntity;
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

public class MajungasaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public MajungasaurusDinosaur()
    {
        super();
        this.setName("Majungasaurus");
        this.setScientificName("Majungasaurus crenatissimus");
        this.setFamily("Abelisauridae");
        this.setLocation("Madagascar");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MajungasaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6CC9B, 0x7C8A7D);
        this.setEggColorFemale(0xE8CF9C, 0xADAC7E);
        this.setHealth(10, 40);
        this.setStrength(2, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.35F, 2.45F);
        this.setSizeX(0.2F, 2.25F);
        this.setSizeY(0.4F, 2.5F);
        this.setStorage(36);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "tooth", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("head");
        this.shouldDefendOffspring();
        this.setScale(1.3F, 0.1F);
        this.setAttackBias(180);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 70, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
