package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.VelociraptorEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;


public class VelociraptorDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public VelociraptorDinosaur() {
        super();

        this.setName("Velociraptor");
        this.setScientificName("Velociraptor mongoliensis");
        this.setFamily("Dromaeosauridae");
        this.setLocation("Mongolia");
        this.setDinosaurClass(VelociraptorEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA06238, 0x632D11);
        this.setEggColorFemale(0x91765D, 0x5A4739);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.9);
        this.setHealth(6, 35);
        this.setStrength(2, 10);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.45F, 1.7F);
        this.setSizeX(0.5F, 1.0F);
        this.setSizeY(0.5F, 1.8F);
        this.setStorage(27);
        this.setDiet(Diet.CARNIVORE.get());
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBones("claw", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "pelvis");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.1F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(7);
        this.setAttackBias(600.0);
        this.setCanClimb(true);
        this.setBreeding(false,1, 7, 28, false, true);
        this.setJumpHeight(3);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae","pelvis", "ribcage","shoulder"},
                {"", "leg_bones", "arm_bones", "tooth"},
                {"", "foot_bones", "claw", ""}};
        this.setRecipe(recipe);
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        doSkeletonCheck();
    }

    protected void doSkeletonCheck(){
        this.enableSkeleton();
    }
}
