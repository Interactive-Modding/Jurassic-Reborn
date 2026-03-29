package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.HypsilophodonEntity;
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

public class HypsilophodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public HypsilophodonDinosaur()
    {
        super();
        this.setName("Hypsilophodon");
        this.setScientificName("Hypsilophodon foxii");
        this.setFamily("Hypsilophodontidae");
        this.setLocation("England");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(HypsilophodonEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x7DAC78, 0x3E6226);
        this.setEggColorFemale(0x799073, 0x33432F);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.10), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(fromDays(35));
        this.setEyeHeight(0.2F, 0.8F);
        this.setSizeX(0.2F, 0.6F);
        this.setSizeY(0.25F, 0.85F);
        this.setStorage(9);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "tail_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tooth", "arm_bones");
        this.setHeadCubeName("Head ");
        this.setBreeding(false, 2, 6, 22, false, true);
        this.setAttackBias(-50);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setScale(0.65F, 0.1F);
        String[][] recipe = {
                {"", "pelvis", "","",""},
                {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                {"leg_bones", "", "", "arm_bones", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        TagKey<Biome>[] tags = (new TagKey[]{Tags.Biomes.IS_PLAINS, BiomeTags.IS_FOREST, BiomeTags.IS_SAVANNA, BiomeTags.IS_BADLANDS, Tags.Biomes.IS_SANDY});
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
