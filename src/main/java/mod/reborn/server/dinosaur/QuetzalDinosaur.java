package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.QuetzalEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class QuetzalDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public QuetzalDinosaur()
    {
        super();

        this.setName("Quetzalcoatlus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(QuetzalEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x8d8f8b, 0x1c2d40);
        this.setEggColorFemale(0x8d8f8b, 0x2e201a);
        this.setHealth(10, 30);
        this.setStrength(2, 10);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.55F, 2.9F);
        this.setSizeX(0.8F, 2.4F);
        this.setSizeY(0.6F, 3.0F);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "ribcage", "leg_bones", "neck_vertebrae", "pelvis", "tail_vertebrae", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.15F);
        this.setAttackBias(1200.0);
        this.shouldDefendOffspring();
        this.setBreeding(false, 1, 4, 80, false, true);
        this.enableSkeleton();
        this.setAvianAnimal(true);
        String[][] recipe = {
                {"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","",""},
                {"", "leg_bones", "", "wing_bones", ""}};
        this.setRecipe(recipe);
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MOUNTAIN));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
