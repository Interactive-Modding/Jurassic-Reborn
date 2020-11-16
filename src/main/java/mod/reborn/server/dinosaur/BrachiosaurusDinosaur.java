package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.BrachiosaurusEntity;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class BrachiosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.22F;
    public BrachiosaurusDinosaur() {
        super();
        this.setName("Brachiosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(BrachiosaurusEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0x919256, 0x75281c);
        this.setEggColorFemale(0x624e43, 0x7c6b5e);
        this.setHealth(10, 150);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 30);
        this.setMaximumAge(this.fromDays(85));
        this.setEyeHeight(2.2F, 18.4F);
        this.setSizeX(0.5F, 6.3F);
        this.setSizeY(1.1F, 6.8F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head");
        this.setScale(2.3F, 0.1F);
        this.setOffset(0.0F, 0.0F, 1.0F);
        this.setAttackBias(1200.0);
        this.setMaxHerdSize(4);
        this.setBreeding(false, 4, 8, 70, true, false);
        String[][] recipe =     {{"", "", "", "", "skull"},
                                 {"", "", "", "neck_vertebrae","tooth"},
                                 {"tail_vertebrae","pelvis","ribcage","shoulder",""},
                                 {"","hind_leg_bones","hind_leg_bones","front_leg_bones","front_leg_bones"}};
        this.setRecipe(recipe);
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(5, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
