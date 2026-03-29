package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.BrachiosaurusEntity;
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

public class BrachiosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.22F;
    public BrachiosaurusDinosaur() {
        super();
        this.setName("Brachiosaurus");
        this.setScientificName("Brachiosaurus altithorax");
        this.setFamily("Brachiosauridae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(BrachiosaurusEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x919256, 0x75281c);
        this.setEggColorFemale(0x624e43, 0x7c6b5e);
        this.setHealth(10, 150);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 30);
        this.setMaximumAge(this.fromDays(85));
        this.setEyeHeight(1.0F, 3.4F);
        this.setSizeX(0.5F, 6.15F);
        this.setSizeY(1.1F, 6.65F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head");
        this.setScale(1.75F, 0.1F);
        this.setOffset(0.0F, 0.0F, 1.0F);
        this.setAttackBias(1200.0);
        this.setPaleoPadScale(5);
        this.setMaxHerdSize(15);
        this.setBreeding(false, 4, 8, 70, true, false);
        String[][] recipe =     {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae","tooth"},
                {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        this.enableSkeleton();
    }
}
