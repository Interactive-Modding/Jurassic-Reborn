package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DreadnoughtusEntity;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DreadnoughtusDinosaur extends Dinosaur {
    public static final double SPEED = 0.22F;
    public DreadnoughtusDinosaur() {
        super();
        this.setName("Dreadnoughtus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DreadnoughtusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x587175, 0x4e5050);
        this.setEggColorFemale(0x836d4b, 0xb2a07b);
        this.setHealth(10, 160);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 15);
        this.setMaximumAge(this.fromDays(85));
        this.setEyeHeight(2.2F, 18.4F);
        this.setSizeX(0.4F, 6.1F);
        this.setSizeY(0.5F, 6.6F);
        this.setStorage(54);
        this.setPaleoPadScale(1);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.1F);
        this.setOffset(0.0F, 0.0F, 0.0F);
        this.setAttackBias(1200.0);
        this.setMaxHerdSize(4);
        this.setBreeding(false, 4, 8, 70, true, false);
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
