package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.RugopsEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class RugopsDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public RugopsDinosaur()
    {
        super();

        this.setName("Rugops");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(RugopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xB5F75D, 0x202022);
        this.setEggColorFemale(0xE1A857, 0x5A2108);
        this.setHealth(10, 30);
        this.setStrength(5, 15);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.6F, 2.05F);
        this.setSizeX(0.20F, 1.5F);
        this.setSizeY(0.5F, 2.6F);
        this.setStorage(36);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "tooth", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(1.0F, 0.1F);
        this.setAttackBias(120);
        this.setAttackSpeed(1.6);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "", "leg_bones", "arm_bones", "claw"},
                {"", "", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.JUNGLE));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
