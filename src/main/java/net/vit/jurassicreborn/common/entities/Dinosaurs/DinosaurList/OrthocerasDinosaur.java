package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.minecraft.world.level.biome.Biome;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AsterocerasEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.OrthocerasEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.util.TimePeriod;

import java.util.ArrayList;


public class OrthocerasDinosaur extends Dinosaur {
    public static final double SPEED = 0.35F;
    public OrthocerasDinosaur() {
        super();
        this.setName("Orthoceras");
        this.setScientificName("Orthoceras regulare");
        this.setFamily("Orthoceratidae");
        this.setLocation("Morocco");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(OrthocerasEntity.class);
        this.setTimePeriod(TimePeriod.ORDOVICIAN);
        this.setEggColorMale(0x313131, 0x181E22);
        this.setEggColorFemale(0x313131, 0x347073);
        this.setHealth(2, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 3);
        this.setMaximumAge(fromDays(25));
        this.setMarineAnimal(true);
        this.setEyeHeight(0.10F, 0.3F);
        this.setSizeX(0.5F, 0.7F);
        this.setSizeY(0.2F, 0.4F);
        this.setMarineAnimal(true);
        this.setOffset(0,0,-1);
        this.setSkeletonOffset(0,0,0);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setBones("beak", "shell_cover");
        this.setHeadCubeName("Head");
        this.setScale(0.15F, 0.04F);
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