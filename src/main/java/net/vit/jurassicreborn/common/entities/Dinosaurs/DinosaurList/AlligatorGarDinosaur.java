package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.AlligatorGarEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.FoodType;
import net.vit.jurassicreborn.common.entities.EntityUtils.MovementType;
import net.vit.jurassicreborn.common.entities.EntityUtils.SleepTime;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.neoforge.common.Tags;

import java.util.ArrayList;
import java.util.List;

public class AlligatorGarDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public AlligatorGarDinosaur()
    {
        super();
        this.setName("Alligator Gar");
        this.setScientificName("Atractosteus spatula");
        this.setFamily("Lepisosteidae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(AlligatorGarEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x707B94, 0x3B4963);
        this.setEggColorFemale(0x7C775E, 0x4D4A3B);
        this.setHealth(2, 10);
        this.setFlee(true);
        this.setSpeed((SPEED -0.15), SPEED);
        this.setAttackSpeed(1.5);
        this.setStrength(0.5, 3);
        this.setMaximumAge(this.fromDays(30));
        this.setEyeHeight(0.18F, 0.9F);
        this.setSizeX(0.2F, 1.0F);
        this.setSizeY(0.2F, 1.0F);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setSleepTime(SleepTime.NO_SLEEP);
        this.setBones("anal_fin", "dorsal_fin", "pectoral_fin_bones", "pelvic_fin_bones", "scales", "skull", "spine", "tail_fin", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(.95F, 0.15F);
        this.setMaxHerdSize(30);
        this.setOffset(0.0F, .5F, 0F);
        this.setAttackBias(100.0);
        this.setBreeding(true, 2, 12, 40, false, true);
        this.setMarineAnimal(true);
        this.setMovementType(MovementType.NEAR_SURFACE);
        this.setRandomFlock(false);
        this.setImprintable(false);
        this.setBirthType(BirthType.LIVE_BIRTH);
        String[][] recipe = {{"", "dorsal_fin", "scales", ""},
                {"tail_fin", "spine", "pectoral_fin_bones", "skull"},
                {"anal_fin", "", "pelvic_fin_bones", "teeth"}};
        this.setRecipe(recipe);
//      List<ResourceKey<Biome>> biomeList = biomeKeysForTags(tags);
//       this.setSpawn(1, biomeList);
this.init();
        this.enableSkeleton();
    }

    @Override
    public List<MobEffectInstance> applyMeatEffect(List<MobEffectInstance> player, boolean cooked){
        if (!cooked){
                player.add(new MobEffectInstance(MobEffects.POISON, 400, 1));
        }
        player.add(new MobEffectInstance(MobEffects.CONFUSION, 200, 1));
        return player;
    }
}
