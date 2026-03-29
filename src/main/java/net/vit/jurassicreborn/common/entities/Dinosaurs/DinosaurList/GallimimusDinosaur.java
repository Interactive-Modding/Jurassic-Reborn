package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.GallimimusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

public class GallimimusDinosaur extends Dinosaur {
    public static final double SPEED = 0.41F;
    public GallimimusDinosaur() {
        super();
        this.setName("Gallimimus");
        this.setScientificName("Gallimimus bullatus");
        this.setFamily("Ornithomimidae");
        this.setLocation("Mongolia");
        this.setDinosaurClass(GallimimusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD5BA86, 0xD16918);
        this.setEggColorFemale(0xCCBA94, 0xAB733D);
        this.setHealth(6, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(this.fromDays(35));
        this.setEyeHeight(0.45F, 2.15F);
        this.setSizeX(0.3F, 1.2F);
        this.setSizeY(0.55F, 2.25F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get().withModule(new Diet.DietModule(FoodType.INSECT).withCondition(entity -> entity.getAgePercentage() < 25)));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head Base");
        this.setScale(1.05F, 0.1F);
        this.setImprintable(true);
        this.setFlee(true);
        this.setFlockSpeed(1.35F);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setJumpHeight(3);
        this.setCanClimb(true);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                        {"tail_vertebrae", "pelvis", "ribcage","shoulder",""},
                        {"", "leg_bones", "leg_bones", "arm_bones", ""},
                        {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, Tags.Biomes.IS_DRY_OVERWORLD});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        this.enableSkeleton();
    }
}
