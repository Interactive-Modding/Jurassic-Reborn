package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MajungasaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class MajungasaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public MajungasaurusDinosaur()
    {
        super();

        this.setName("Majungasaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(MajungasaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE6CC9B, 0x7C8A7D);
        this.setEggColorFemale(0xE8CF9C, 0xADAC7E);
        this.setHealth(10, 40);
        this.setStrength(5, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.6F, 2.6F);
        this.setSizeX(0.2F, 2.25F);
        this.setSizeY(0.4F, 3.0F);
        this.setStorage(36);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "tooth", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.shouldDefendOffspring();
        this.setScale(1.6F, 0.1F);
        this.setAttackBias(180);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 70, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}