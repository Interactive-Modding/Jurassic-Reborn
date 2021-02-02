package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ArsinoitheriumEntity;
import mod.reborn.server.entity.dinosaur.MegatheriumEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class MegatheriumDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public MegatheriumDinosaur() {
        super();

        this.setName("Megatherium");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(MegatheriumEntity.class);
        this.setTimePeriod(TimePeriod.NEOGENE);
        this.setEggColorMale(0x5d420c, 0xac915b);
        this.setEggColorFemale(0xb7b7b7, 0x151515);
        this.setHealth(12, 50);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setAttackSpeed(1.1);
        this.setStrength(2, 10);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.7F);
        this.setSizeX(0.35F, 2.1F);
        this.setSizeY(0.5F, 2.4F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "teeth","arm_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(2.0F, 0.40F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(true, 2, 4, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","teeth"},
                {"", "", "hind_leg_bones", "arm_bones", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
