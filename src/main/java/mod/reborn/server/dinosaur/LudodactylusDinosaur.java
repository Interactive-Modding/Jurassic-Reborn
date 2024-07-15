package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.LudodactylusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class LudodactylusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public LudodactylusDinosaur()
    {
        super();

        this.setName("Ludodactylus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(LudodactylusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x565656, 0x1D1E20);
        this.setEggColorFemale(0x884D3E, 0x35302B);
        this.setHealth(4, 26);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(3, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.3F, 1.25F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.35F, 1.35F);
        this.setStorage(18);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "ribcage", "tail_vertebrae", "teeth", "wing_bones");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.1F);
        this.shouldDefendOffspring();
        this.setAttackBias(120);
        this.setImprintable(true);
        this.setAvianAnimal(true);
        this.setBreeding(false, 2, 6, 80, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage", "skull","teeth"},
                {"", "leg_bones", "wing_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
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
