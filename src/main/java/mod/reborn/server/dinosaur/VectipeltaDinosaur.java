package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.VectipeltaEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class VectipeltaDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public VectipeltaDinosaur()
    {
        super();
        this.setName("Vectipelta");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(VectipeltaEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x5C311C, 0xAA7336);
        this.setEggColorFemale(0x682C21, 0xC36B39);
        this.setHealth(20, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(6, 10);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.2F, 1.8F);
        this.setSizeX(0.15F, 1.4F);
        this.setSizeY(0.3F, 1.8F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("armor_plating", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.15F);
        this.setBreeding(false, 2, 6, 44, false, true);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setStorage(24);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "armor_plating", "","",""},
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
