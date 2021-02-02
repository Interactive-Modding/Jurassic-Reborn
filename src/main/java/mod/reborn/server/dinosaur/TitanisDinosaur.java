package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import java.util.ArrayList;
import mod.reborn.server.entity.dinosaur.TitanisEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class TitanisDinosaur extends Dinosaur {
    public static final double SPEED = 0.40F;
    public TitanisDinosaur() {
        super();

        this.setName("Titanis");
        this.setDinosaurClass(TitanisEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.PALEOGENE);
        this.setEggColorMale(0xe5e1d3, 0x58598d);
        this.setEggColorFemale(0xe5e1d3, 0x7b7262);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.95);
        this.setHealth(6, 30);
        this.setStrength(4, 15);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.45F, 1.7F);
        this.setSizeX(0.5F, 2.3F);
        this.setSizeY(0.5F, 2.5F);
        this.setStorage(20);
        this.setDiet(Diet.CARNIVORE.get());
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBones("skull", "leg_bones", "ribcage", "neck_vertebrae", "pelvis", "shoulder");
        this.setHeadCubeName("Head");
        this.setScale(1.6F, 0.2F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMaxHerdSize(40);
        this.setAttackBias(600.0);
        this.setCanClimb(true);
        this.setBreeding(false,2, 4, 30, false, true);
        this.setJumpHeight(2);
        String[][] recipe =     {
                { "", "pelvis", "ribcage","neck_vertebrae", "skull"},
                { "leg_bones", "", "leg_bones", "shoulder", ""}};
        this.setRecipe(recipe);
        doSkeletonCheck();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.COLD));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }

    protected void doSkeletonCheck(){
        this.enableSkeleton();
    }
}
