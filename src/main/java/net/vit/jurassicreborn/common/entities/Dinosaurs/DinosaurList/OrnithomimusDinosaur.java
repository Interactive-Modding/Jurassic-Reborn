package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;
import java.util.List;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.OrnithomimusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class OrnithomimusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public OrnithomimusDinosaur()
    {
        super();
        this.setName("Ornithomimus");
        this.setScientificName("Ornithomimus velox");
        this.setFamily("Ornithomimidae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(OrnithomimusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x92A8D5, 0x475F93);
        this.setEggColorFemale(0xBDC4A9, 0x7F91C1);
        this.setHealth(6, 26);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.24F, 1.45F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.25F, 1.55F);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head Base");
        this.setScale(0.9F, 0.15F);
        this.setFlockSpeed(1.4F);
        this.setAttackBias(-100);
        this.setFlee(true);
        this.setStorage(12);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setImprintable(true);
        this.setCanClimb(true);
        this.setJumpHeight(3);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                        {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                        {"", "leg_bones", "", "arm_bones", ""},
                        {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        this.enableSkeleton();
    }
}
