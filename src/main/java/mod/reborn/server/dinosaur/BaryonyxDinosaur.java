package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.BaryonyxEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class BaryonyxDinosaur extends Dinosaur
{
    public static final double SPEED = 0.40F;
    public BaryonyxDinosaur()
    {
        super();
        this.setName("Baryonyx");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(BaryonyxEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x796e20, 0x077d73);
        this.setEggColorFemale(0x8a7f31, 0x463e15);
        this.setHealth(6, 35);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.6);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.45F, 2.85F);
        this.setSizeX(0.3F, 1.5F);
        this.setSizeY(0.5F, 2.95F);
        this.setDiet(Diet.PCARNIVORE.get());
        this.setBones("arm_bones", "claw", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth", "foot_bones");
        this.setHeadCubeName("Head");
        this.setBreeding(false, 2, 6, 60, false, true);
        this.setScale(1.38F, 0.1F);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setDefendOwner(true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", ""},
                {"", "foot_bones", "", "claw", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
