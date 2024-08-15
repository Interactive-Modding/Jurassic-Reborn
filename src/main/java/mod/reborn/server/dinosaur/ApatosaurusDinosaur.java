package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ApatosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class ApatosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.25F;
    public ApatosaurusDinosaur()
    {
        super();
        this.setName("Apatosaurus");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(ApatosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x565b4b, 0x7a925c);
        this.setEggColorFemale(0x31313f, 0x222238);
        this.setHealth(10, 80);
        this.setStrength(5, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(80));
        this.setEyeHeight(0.5F, 3.59F);
        this.setSizeX(0.5F, 6.3F);
        this.setSizeY(0.6F, 6.6F);
        this.setStorage(54);
        this.shouldDefendOffspring();
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.32F, 0.1F);
        this.setBreeding(false, 4, 8, 70, false, true);
        this.setImprintable(true);
        this.setMaxHerdSize(6);
        this.setOffset(0.0F, 0.0F, 0.1F);
        String[][] recipe =     {{"", "", "", "", "skull"},
                {"", "", "", "neck_vertebrae","tooth"},
                {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
