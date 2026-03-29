package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.DunkleosteusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class DunkleosteusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.45F;
    public DunkleosteusDinosaur()
    {
        super();
        this.setName("Dunkleosteus");
        this.setScientificName("Dunkleosteus terrelli");
        this.setFamily("Dunkleosteidae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DunkleosteusEntity.class);
        this.setTimePeriod(TimePeriod.DEVONIAN);
        this.setEggColorMale(0xA89B8C, 0x753A28);
        this.setEggColorFemale(0xA6A588, 0x785F2A);
        this.setHealth(16, 60);
        this.setSpeed((SPEED -0.1), SPEED);
        this.setStrength(10, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.15F, 1.9F);
        this.setSizeX(0.3F, 2.7F);
        this.setSizeY(0.2F, 2.0F);
        this.setMarineAnimal(true);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setBones("mouth_plates", "skull", "dorsal_fin", "spine");
        this.setHeadCubeName("Main head");
        this.setScale(1.2F, 0.15F);
        this.shouldDefendOffspring();
        this.setAttackBias(420);
        this.setImprintable(false);
        this.setBreeding(true, 2, 6, 20, false, true);
        String[][] recipe =     {
                {"", "", "dorsal_fin","",""},
                {"spine", "spine", "spine", "skull", "mouth_plates"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
