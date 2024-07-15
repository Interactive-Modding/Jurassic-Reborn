package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.HyaenodonEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class HyaenodonDinosaur extends Dinosaur {
    public static final double SPEED = 0.40F;
    public HyaenodonDinosaur() {
        super();

        this.setName("Hyaenodon");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(HyaenodonEntity.class);
        this.setTimePeriod(TimePeriod.PALEOGENE);
        this.setEggColorMale(0xA3A3A3, 0x191919);
        this.setEggColorFemale(0x6F6652, 0x100E0B);
        this.setHealth(10, 40);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(1, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.35F, 1.7F);
        this.setSizeX(0.2F, 1.4F);
        this.setSizeY(0.4F, 1.8F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "tail_vertebrae", "front_leg_bones", "ribcage", "hind_leg_bones", "neck_vertebrae", "pelvis", "shoulder", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.3F, 0.15F);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setBreeding(true, 2, 4, 40, false, true);
        this.setAttackBias(600);
        this.setAttackSpeed(1.9);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setMammal(true);
        String[][] recipe =     {
                { "tail_vertebrae", "pelvis", "ribcage","neck_vertebrae", "skull"},
                { "hind_leg_bones", "", "front_leg_bones", "shoulder", "tooth"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
