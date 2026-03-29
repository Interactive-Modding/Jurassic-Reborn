package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.TroodonEntity;
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

public class TroodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public TroodonDinosaur()
    {
        super();
        this.setName("Troodon");
        this.setScientificName("Troodon formosus");
        this.setFamily("Troodontidae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(TroodonEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x9DAA7A, 0x636B67);
        this.setEggColorFemale(0xA2A67C, 0x646D66);
        this.setHealth(4, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.15F, 0.9F);
        this.setSizeX(0.15F, 0.7F);
        this.setSizeY(0.2F, 0.95F);
        this.setStorage(12);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.NOCTURNAL);
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head UPPER");
        this.setAttackBias(600);
        this.setScale(0.25F, 0.05F);
        this.setOffset(0.0F, 0.0F, 0.5F);
        this.setAttackSpeed(1.2F);
        this.shouldDefendOffspring();
        this.setCanClimb(true);
        this.setDefendOwner(true);
        this.setBreeding(false,1, 7, 28, false, true);
        this.setJumpHeight(2);
        this.setImprintable(true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae","pelvis", "ribcage","shoulder"},
                {"", "leg_bones", "arm_bones", "tooth"},
                {"", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
