package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TyrannosaurusEntity;
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

public class TyrannosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.42F;
    public TyrannosaurusDinosaur() {
        super();

        this.setName("Tyrannosaurus");
        this.setScientificName("Tyrannosaurus rex");
        this.setFamily("Tyrannosauridae");
        this.setLocation("United States");
        this.setDinosaurClass(TyrannosaurusEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E502C, 0x353731);
        this.setEggColorFemale(0xBA997E, 0x7D5D48);
        this.setHealth(10, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.3);
        this.setStrength(2, 25);
        this.setMaximumAge(this.fromDays(60));
        this.setEyeHeight(0.35F, 3.5F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.4F, 3.6F);
        this.setStorage(54);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.1F);
        this.setMaxHerdSize(4);
        this.setAttackBias(1000.0);
        this.setBreeding(false, 2, 4, 60, false, true);

        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
