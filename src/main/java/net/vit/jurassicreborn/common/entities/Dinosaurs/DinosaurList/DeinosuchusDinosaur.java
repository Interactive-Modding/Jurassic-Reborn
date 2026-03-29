package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DeinosuchusEntity;
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

public class DeinosuchusDinosaur extends Dinosaur {

    public static final double SPEED = 0.28F;

    public DeinosuchusDinosaur() {
        super();

        this.setName("Deinosuchus");
        this.setScientificName("Deinosuchus rugosus");
        this.setFamily("Alligatoridae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DeinosuchusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x272520, 0xbfa07a);
        this.setEggColorFemale(0x3c3426, 0xc5a885);
        this.setHealth(20, 80);
        this.setSpeed((SPEED - 0.05), SPEED);
        this.setStorage(27);
        this.setPaleoPadScale(2);
        this.setStrength(4, 22);
        this.setMaximumAge(fromDays(60));
        this.setEyeHeight(0.20F, 0.9F);
        this.setSizeX(0.30F, 2.5F);
        this.setSizeY(0.25F, 2.0F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "teeth", "femur", "leg_bones", "neck_vertebrae",
                "ribcage", "shoulder_blade", "tail_vertebrae", "arm_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.1F);
        this.setAttackBias(90);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "", "neck_vertebrae", "skull","teeth"},
                {"tail_vertebrae", "femur", "ribcage", "shoulder_blade",""},
                {"leg_bones", "", "", "arm_bones",""}
        };
        this.setRecipe(recipe);
        this.enableSkeleton();
        TagKey<Biome>[] tags = (new TagKey[]{ BiomeTags.IS_RIVER, Tags.Biomes.IS_SWAMP, BiomeTags.IS_SAVANNA, BiomeTags.IS_BEACH});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
        this.init();
    }
}
