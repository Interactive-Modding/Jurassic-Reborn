package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DimetrodonEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class DimetrodonDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public DimetrodonDinosaur()
    {
        super();

        this.setName("Dimetrodon");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DimetrodonEntity.class);
        this.setTimePeriod(TimePeriod.PERMIAN);
        this.setEggColorMale(0xa5a578, 0xff4c00);
        this.setEggColorFemale(0xa97b22, 0x441933);
        this.setHealth(10, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(55));
        this.setEyeHeight(0.3F, 1.0F);
        this.setSizeX(0.2F, 1.4F);
        this.setSizeY(0.3F, 1.8F);
        this.setStorage(20);
        this.setDiet((Diet.PCARNIVORE.get()));
        this.setBones("skull", "tooth", "foot_bones", "femur", "neck_vertebrae", "ribcage", "tail_vertebrae", "pelvis");
        this.setHeadCubeName("headsupport");
        this.setScale(0.95F, 0.1F);
        this.setAttackBias(900);
        this.shouldDefendOffspring();
        this.setBreeding(false, 2, 6, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","","tooth"},
                {"", "femur", "", "femur", ""},
                {"", "foot_bones", "", "foot_bones", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SWAMP));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
