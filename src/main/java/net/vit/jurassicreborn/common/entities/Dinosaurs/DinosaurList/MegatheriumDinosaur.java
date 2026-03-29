package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import java.util.ArrayList;
import java.util.List;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MegatheriumEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

public class MegatheriumDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public MegatheriumDinosaur() {
        super();

        this.setName("Megatherium");
        this.setScientificName("Megatherium americanum");
        this.setFamily("Megatheriidae");
        this.setLocation("South America");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MegatheriumEntity.class);
        this.setTimePeriod(TimePeriod.NEOGENE);
        this.setEggColorMale(0x5d420c, 0xac915b);
        this.setEggColorFemale(0xb7b7b7, 0x151515);
        this.setHealth(12, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setAttackSpeed(1.1);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 2.3F);
        this.setSizeX(0.35F, 2.1F);
        this.setSizeY(0.5F, 2.4F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "teeth","arm_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2.0F, 0.40F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(true, 2, 4, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","teeth"},
                {"", "", "hind_leg_bones", "arm_bones", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        TagKey<Biome>[] tags = (new TagKey[]{BiomeTags.IS_SAVANNA, BiomeTags.IS_MOUNTAIN, Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, Tags.Biomes.IS_SNOWY});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
