package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ProtoceratopsEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.common.Tags;

public class ProtoceratopsDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public ProtoceratopsDinosaur()
    {
        super();

        this.setName("Protoceratops");
        this.setScientificName("Protoceratops andrewsi");
        this.setFamily("Protoceratopsidae");
        this.setLocation("Mongolia");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(ProtoceratopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(4, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.15F, 0.9F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.2F, 1.0F);
        this.setStorage(9);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "teeth", "tail_vertebrae", "shoulder", "pelvis", "ribcage", "front_leg_bones", "hind_leg_bones", "shoulder");
        this.setHeadCubeName("Head");
        this.setAttackBias(1);
        this.setScale(0.6F, 0.13F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 40, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "teeth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
    }
}
