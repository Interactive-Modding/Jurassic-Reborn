package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.AllosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class AllosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.42F;
    public AllosaurusDinosaur() {
        super();

        this.setName("Allosaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(AllosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xbaa262, 0x753227);
        this.setEggColorFemale(0x3b4046, 0x877e73);
        this.setHealth(10, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.1);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 4.0F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.4F, 3.0F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("leg_bones", "neck_vertebrae", "arm_bones", "claw", "foot_bones", "leg_bones", "pelvis", "shoulder", "tooth", "ribcage", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setBreeding(false, 2, 4, 40, false, true);
        this.setScale(1.1F, 0.1F);
        this.shouldDefendOffspring();
        String[][] recipe = {{"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        doSkeletonCheck();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }

    protected void doSkeletonCheck(){
        this.enableSkeleton();
    }
}
