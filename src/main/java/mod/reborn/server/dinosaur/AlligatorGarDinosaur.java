package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.ai.util.MovementType;
import mod.reborn.server.entity.dinosaur.AlligatorGarEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class AlligatorGarDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public AlligatorGarDinosaur()
    {
        super();
        this.setName("Alligator Gar");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(AlligatorGarEntity.class);
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x707B94, 0x3B4963);
        this.setEggColorFemale(0x7C775E, 0x4D4A3B);
        this.setHealth(3, 10);
        this.setFlee(true);
        this.setSpeed((SPEED -0.15), SPEED);
        this.setAttackSpeed(1.5);
        this.setStrength(0.5, 3);
        this.setMaximumAge(this.fromDays(30));
        this.setEyeHeight(0.35F, 1.2F);
        this.setSizeX(0.1F, 1.1F);
        this.setSizeY(0.02F, .4F);
        this.setStorage(9);
        this.setDiet(Diet.PISCIVORE.get().withModule(new Diet.DietModule(FoodType.FILTER)));
        this.setSleepTime(SleepTime.NO_SLEEP);
        this.setBones("anal_fin", "dorsal_fin", "pectoral_fin_bones", "pelvic_fin_bones", "scales", "skull", "spine", "tail_fin", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(.65F, 0.15F);
        this.setMaxHerdSize(30);
        this.setOffset(0.0F, .7F, 0F);
        this.setAttackBias(100.0);
        this.setBreeding(true, 0, 2, 20, false, true);
        this.setMarineAnimal(true);
        this.setMovementType(MovementType.NEAR_SURFACE);
        this.setBreeding(true, 1, 3, 15, true, false);
        this.setRandomFlock(false);
        this.setImprintable(false);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setStorage(4);
        String[][] recipe = {{"", "dorsal_fin", "", ""},
                {"tail_fin", "spine", "pectoral_fin_bones", "skull"},
                {"anal_fin", "", "pelvic_fin_bones", "teeth"}};
        this.setRecipe(recipe);

        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        this.setSpawn(5, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }

    @Override
    public void applyMeatEffect(EntityPlayer player, boolean cooked)
    {
        if (!cooked)
        {
            player.addPotionEffect(new PotionEffect(MobEffects.POISON, 400, 1));
        }
        player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 1));
    }
}
