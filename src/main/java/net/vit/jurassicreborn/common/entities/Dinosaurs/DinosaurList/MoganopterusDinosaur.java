package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;



import net.vit.jurassicreborn.common.entities.DinosaurEntities.MoganopterusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class MoganopterusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public MoganopterusDinosaur()
    {
        super();
        this.setName("Moganopterus");
        this.setScientificName("Moganopterus zhuiana");
        this.setFamily("Tapejaridae");
        this.setLocation("China");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MoganopterusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6E2D8, 0xD67F5C);
        this.setEggColorFemale(0xE0DED3, 0xD37B58);
        this.setHealth(4, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 1.25F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.35F, 1.35F);
        this.setStorage(27);
        this.setDiet((Diet.PISCIVORE.get()));
        this.setBones("leg_bones", "pelvis", "ribcage", "skull", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.725F, 0.1F);
        this.setAttackBias(200);
        this.setAvianAnimal(true);
        this.setBreeding(false, 2, 6, 80, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull","teeth"},
                {"", "leg_bones", "wing_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//        List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//
//
//
//        this.setSpawn(1, biomeList);
this.init();
    }
}
