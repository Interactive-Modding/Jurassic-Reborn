package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.MussaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class MussaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.32F;
    public MussaurusDinosaur() {
        super();
        this.setName("Mussaurus");
        this.setDinosaurClass(MussaurusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setFlee(true);
        this.setTimePeriod(TimePeriod.TRIASSIC);
        this.setEggColorMale(0x6F9845, 0x211F16);
        this.setEggColorFemale(0x526024, 0x222611);
        this.setHealth(2, 15);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 2);
        this.setMaximumAge(this.fromDays(30));
        this.setEyeHeight(0.25F, 1.2F);
        this.setSizeX(0.25F, 1F);
        this.setSizeY(0.2F, 0.9F);
        this.setStorage(9);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "teeth");
        this.setHeadCubeName("Head1");
        this.setScale(0.6F, 0.1F);
        this.setImprintable(true);
        this.setFlockSpeed(1.10F);
        this.setMaxHerdSize(20);
        this.setAttackBias(-500.0);
        this.setImprintable(true);
        this.setStorage(12);
        this.setOffset(0.0F, 0.0F, 0.5F);
        this.setBreeding(false, 2, 8, 15, false, true);
        String[][] recipe = {
                        {"", "pelvis", "","",""},
                        {"tail_vertebrae", "ribcage", "shoulder", "neck_vertebrae", "skull"},
                        {"leg_bones", "leg_bones", "arm_bones", "arm_bones", "teeth"}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(15, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
        
    }
}
