package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AsterocerasEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;

import net.minecraft.world.level.biome.Biome;


public class CamerocerasDinosaur extends Dinosaur {
    public static final double SPEED = 0.25F;
    public CamerocerasDinosaur() {
        super();
        this.setName("Cameroceras");
        this.setScientificName("Cameroceras trentonense");
        this.setFamily("Endoceratidae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CamerocerasEntity.class);
        this.setTimePeriod(TimePeriod.ORDOVICIAN);
        this.setEggColorMale(0x664F28, 0x7F7B74);
        this.setEggColorFemale(0xE6BE69, 0xFFFDF9);
        this.setOffset(0,0,-3);
        this.setSkeletonOffset(0,0,0);
        this.setHealth(4, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 8);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 0.8F);
        this.setSizeX(0.5F, 4.0F);
        this.setSizeY(0.5F, 1.0F);
        this.setMarineAnimal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setBones("beak", "shell_cover");
        this.setHeadCubeName("Head");
        this.setScale(1.9F, 0.3F);
        this.setBreeding(true, 2, 10, 20, false, false);
        this.setImprintable(false);
        String[][] recipe = {
                { "shell_cover"},
                {"beak"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
    }
}