package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.GuanlongEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class GuanlongDinosaur extends Dinosaur {
    public static final double SPEED = 0.4F;
    public GuanlongDinosaur() {
        super();

        this.setName("Guanlong");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(GuanlongEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xEFE3B9, 0xEFE3B9);
        this.setEggColorFemale(0xEFE2B4, 0x4E4D4B);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(2, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.35F, 1.3F);
        this.setSizeX(0.2F, 1.2F);
        this.setSizeY(0.4F, 1.4F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae", "tooth", "claw", "foot_bones", "skull");
        this.setHeadCubeName("Head");
        this.setScale(0.7F, 0.1F);
        this.shouldDefendOffspring();
        this.setAttackBias(1200.0);
        this.setAttackSpeed(1.2F);
        this.setDefendOwner(true);
        this.setImprintable(true);
        this.setBreeding(false, 2, 4, 24, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae","pelvis", "ribcage","shoulder"},
                {"leg_bones", "arm_bones", "claw", "tooth"},
                {"foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
