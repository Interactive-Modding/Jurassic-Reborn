package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.DilophosaurusEntity;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DilophosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public DilophosaurusDinosaur() {
        super();

        this.setName("Dilophosaurus");
        this.setDinosaurClass(DilophosaurusEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x6B7834, 0x2A2E30);
        this.setEggColorFemale(0x62712E, 0x30241C);
        this.setHealth(10, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 6);
        this.setMaximumAge(this.fromDays(40));
        this.setEyeHeight(0.35F, 1.8F);
        this.setSizeX(0.4F, 1.2F);
        this.setSizeY(0.5F, 1.9F);
        this.setStorage(27);
        this.setDiet(Diet.CARNIVORE.get());
        this.setSleepTime(SleepTime.CREPUSCULAR);
        this.setBones("arm_bones", "leg_bones", "neck", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(0.95F, 0.22F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(10);
        this.setAttackBias(1200.0);
        this.setBreeding(false, 2, 4, 24, false, true);
        String[][] recipe =     {{"", "", "", "neck", "skull"},
                                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                                 {"leg_bones", "leg_bones", "", "", "arm_bones"}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(10, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
