package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DiplodocusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class DiplodocusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.25F;
    public DiplodocusDinosaur()
    {
        super();

        this.setName("Diplodocus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DiplodocusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x30484c, 0xc8eced);
        this.setEggColorFemale(0x1e3235, 0xb4b4b4);
        this.setHealth(10, 90);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(52);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setStrength(10, 60);
        this.setMaximumAge(fromDays(95));
        this.setEyeHeight(1.55F, 8.95F);
        this.setRotationAngle(50, 10);
        this.setSizeX(0.4F, 5.4F);
        this.setSizeY(0.5F, 5.8F);
        this.setBones("skull", "tooth", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.8F, 0.1F);
        this.setPaleoPadScale(5);
        this.shouldDefendOffspring();
        this.setAttackBias(1500);
        this.setBreeding(false, 4, 8, 80, false, true);
        String[][] recipe =     {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae","tooth"},
                {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
