package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DeinotheriumEntity;
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

public class DeinotheriumDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public DeinotheriumDinosaur() {
        super();

        this.setName("Deinotherium");
        this.setScientificName("Deinotherium giganteum");
        this.setFamily("Deinotheriidae");
        this.setLocation("Africa");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DeinotheriumEntity.class);
        this.setTimePeriod(TimePeriod.PALEOGENE);
        this.setEggColorMale(0x625b54, 0x997969);
        this.setEggColorFemale(0x868275, 0xc9bcae);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(3, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(1.2F, 3.2F);
        this.setSizeX(1.25F, 3F);
        this.setSizeY(1.4F, 3.3F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "tooth","front_leg_bones", "hind_leg_bones", "tusk", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2F, 0.45F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(true, 2, 4, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","tooth","tusk"},
                {"", "hind_leg_bones", "", "front_leg_bones", "shoulder"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
