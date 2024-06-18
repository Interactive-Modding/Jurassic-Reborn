package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CearadactylusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class CearadactylusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public CearadactylusDinosaur()
    {
        super();

        this.setName("Cearadactylus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CearadactylusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x64A0B3, 0x3B3937);
        this.setEggColorFemale(0xB55252, 0x4C423A);
        this.setHealth(10, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 10);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.45F, 1.45F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.35F, 1.35F);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("leg_bones", "pelvis", "skull", "neck_vertebrae", "tail_vertebrae", "teeth", "wing_bones", "ribcage");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.1F);
        this.setAvianAnimal(true);
        this.setBreeding(false, 2, 6, 80, false, true);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","","teeth"},
                {"", "leg_bones", "", "wing_bones", ""}};
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
