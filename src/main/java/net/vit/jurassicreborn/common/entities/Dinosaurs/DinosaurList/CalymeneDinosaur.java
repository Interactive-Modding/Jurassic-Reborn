package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CalymeneEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DiplocaulusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public class CalymeneDinosaur extends Dinosaur {
    public static final double SPEED = 0.15F;
    public CalymeneDinosaur() {
        super();
        this.setName("Calymene");
        this.setScientificName("Calymene blumenbachii");
        this.setFamily("Calymenidae");
        this.setLocation("Europe");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(CalymeneEntity.class);
        this.setTimePeriod(TimePeriod.SILURIAN);
        this.setEggColorMale(0x7C7C7A, 0x262626);
        this.setEggColorFemale(0x7C7C7B, 0x262629);
        this.setHealth(2, 5);
        this.setSpeed((SPEED), SPEED);
        this.setStrength(0.5, 1);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.02F, 0.05F);
        this.setSizeX(0.1F, 0.3F);
        this.setSizeY(0.2F, 0.3F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("cephalon", "thorax", "pygidium");
        this.setHeadCubeName("Head");
        this.setScale(0.1F, 0.05F);
        this.setBirthType(BirthType.EGG_LAYING);
        this.setAttackBias(1);
        this.setImprintable(false);
        this.setBreeding(false, 6, 16, 20, false, true); // more eggs than a vertebrate
        this.setMarineAnimal(true);
        String[][] recipe = {
                {  "cephalon" },
                {  "thorax"},
                { "pygidium" }};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
