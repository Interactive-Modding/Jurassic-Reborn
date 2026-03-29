package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.AllosaurusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class AllosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.42F;
    public AllosaurusDinosaur() {
        super();

        this.setName("Allosaurus");
        this.setScientificName("Allosaurus fragilis");
        this.setFamily("Allosauridae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(AllosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xbaa262, 0x753227);
        this.setEggColorFemale(0x3b4046, 0x877e73);
        this.setHealth(10, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.1);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.39F, 2.9F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.4F, 3.0F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("leg_bones", "neck_vertebrae", "arm_bones", "claw", "foot_bones", "leg_bones", "pelvis", "shoulder", "tooth", "ribcage", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setBreeding(false, 2, 4, 40, false, true);
        this.setScale(1.1F, 0.1F);
        this.shouldDefendOffspring();
        String[][] recipe = {{"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        doSkeletonCheck();
        TagKey<Biome>[] tags = (new TagKey[]{ Tags.Biomes.IS_PLAINS, BiomeTags.IS_SAVANNA, BiomeTags.IS_FOREST});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }

    protected void doSkeletonCheck(){
        this.enableSkeleton();
    }
}
