package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.PachycephalosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class PachycephalosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public PachycephalosaurusDinosaur()
    {
        super();

        this.setName("Pachycephalosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(PachycephalosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD9E1C0, 0x91501F);
        this.setEggColorFemale(0x8E805E, 0x4A5154);
        this.setHealth(10, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(4, 10);
        this.setMaximumAge(fromDays(40));
        this.setSizeX(0.15F, 1.25F);
        this.setSizeY(0.4F, 2.3F);
        this.setStorage(27);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "teeth", "ribcage", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.9F, 0.1F);
        this.setEyeHeight(0.35F, 2.2F);
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 6, 40, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder"},
                {"leg_bones", "leg_bones", "", "teeth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
