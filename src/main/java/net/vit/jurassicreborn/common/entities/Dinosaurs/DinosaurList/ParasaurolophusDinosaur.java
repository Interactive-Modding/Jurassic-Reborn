package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ParasaurolophusEntity;
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

public class ParasaurolophusDinosaur extends Dinosaur {
    public static final double SPEED = 0.41F;
    public ParasaurolophusDinosaur() {
        super();

        this.setName("Parasaurolophus");
        this.setScientificName("Parasaurolophus walkeri");
        this.setFamily("Hadrosauridae");
        this.setLocation("North America");
        this.setDinosaurClass(ParasaurolophusEntity.class);
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9f846c, 0x7cc780);
        this.setEggColorFemale(0x977e69, 0x87411a);
        this.setHealth(10, 55);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 8);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.35F, 3.4F);
        this.setSizeX(0.2F, 2.7F);
        this.setSizeY(0.4F, 3.5F);
        this.setOffset(0.0F, 0.0F, 0.6F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("cheek_teeth", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder","skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.55F, 0.1F);
        this.setImprintable(true);
        this.setFlockSpeed(1.5F);
        this.setAttackBias(-100.0);
        this.setBreeding(false, 4, 6, 40, false, true);

        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "cheek_teeth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        this.enableSkeleton();
    }
}
