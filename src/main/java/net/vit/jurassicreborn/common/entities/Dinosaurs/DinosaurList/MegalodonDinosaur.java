
package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.MegalodonEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;

import java.util.ArrayList;
import java.util.List;

public class MegalodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.45F;
    public MegalodonDinosaur()
    {
        super();
        this.setName("Megalodon");
        this.setScientificName("Otodus megalodon");
        this.setFamily("Otodontidae");
        this.setLocation("Worldwide");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MegalodonEntity.class);
        this.setTimePeriod(TimePeriod.NEOGENE);
        this.setEggColorMale(0x464845, 0xc6c1bd);
        this.setEggColorFemale(0x81807c, 0xb8b3af);
        this.setHealth(10, 90);
        this.setSpeed((SPEED - 0.1), SPEED);
        this.setStrength(10, 30);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.3F, 2.0F);
        this.setSizeX(0.8F, 4.0F);
        this.setSizeY(0.8F, 4.0F);
        this.setMarineAnimal(true);
        this.setStorage(27);
        this.setPaleoPadScale(2);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setBones("lower_jaw", "upper_jaw", "tooth" );
        this.setHeadCubeName("Main head");
        this.setScale(1.05F, 0.15F);
        this.setAttackBias(420);
        this.setImprintable(false);
        this.setBreeding(true, 2, 4, 28, false, false);
        String[][] recipe =     {
                {"tooth", "tooth", "tooth","tooth","tooth"},
                {"tooth", "", "upper_jaw","","tooth"},
                {"tooth", "", "lower_jaw","","tooth"},
                {"tooth", "", "","","tooth"},
                {"tooth", "tooth", "tooth","tooth","tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
