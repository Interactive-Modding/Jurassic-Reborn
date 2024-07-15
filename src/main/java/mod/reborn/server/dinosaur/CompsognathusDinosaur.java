package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CompsognathusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class CompsognathusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public CompsognathusDinosaur()
    {
        super();

        this.setName("Compsognathus");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(CompsognathusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x7B8042, 0x454B3B);
        this.setEggColorFemale(0x7D734A, 0x484A3D);
        this.setHealth(2, 4);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 3);
        this.setMaximumAge(fromDays(20));
        this.setAttackBias(50);
        this.setEyeHeight(0.20F, 0.5F);
        this.setSizeX(0.1F, 0.3F);
        this.setSizeY(0.25F, 0.55F);
        this.setStorage(9);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head");
        this.setScale(0.1F, 0.04F);
        this.setOffset(0.0F, -12.0F, -0.8F);
        this.setBreeding(false, 2, 6, 15, false, true);
        this.setJumpHeight(2);
        this.setCanClimb(true);
        this.setImprintable(true);
        String[][] recipe =     {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","teeth"},
                {"leg_bones", "leg_bones", "", "", "arm_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
