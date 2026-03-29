package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.CoelurusEntity;
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

public class CoelurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public CoelurusDinosaur()
    {
        super();
        this.setName("Coelurus");
        this.setScientificName("Coelurus fragilis");
        this.setFamily("Coeluridae");
        this.setLocation("United States");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CoelurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x9c7219, 0x382508);
        this.setEggColorFemale(0x7D734A, 0x484A3D);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 6);
        this.setMaximumAge(fromDays(20));
        this.setEyeHeight(0.2F, 0.5F);
        this.setSizeX(0.1F, 0.6F);
        this.setSizeY(0.25F, 0.95F);
        this.setStorage(9);
        this.setAttackSpeed(1.2);
        this.setAttackBias(90);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("ribcage", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(0.85F, 0.1F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 20, false, true);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                        {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                        {"leg_bones", "leg_bones", "", "arm_bones", "claw"},
                        {"foot_bones", "foot_bones", "", "",""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();

    }
}
