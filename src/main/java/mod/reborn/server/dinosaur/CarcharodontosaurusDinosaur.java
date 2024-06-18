package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CarcharodontosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class CarcharodontosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.44F;
    public CarcharodontosaurusDinosaur() {
        super();
        this.setName("Carcharodontosaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(CarcharodontosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xd2b994, 0x694d3b);
        this.setEggColorFemale(0x656462, 0x8d734f);
        this.setHealth(10, 70);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.5);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.15F, 4.2F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tooth", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tail_vertebrae", "ribcage");
        this.setHeadCubeName("head");
        this.setScale(2.0F, 0.1F);
        this.setBreeding(false, 2, 6, 60, false, true);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", "claw"},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        this.setRecipe(recipe);
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
