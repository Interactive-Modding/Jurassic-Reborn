package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.TyrannosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class TyrannosaurusDinosaur extends Dinosaur {
    public static final double SPEED = 0.42F;
    public TyrannosaurusDinosaur() {
        super();

        this.setName("Tyrannosaurus");
        this.setDinosaurClass(TyrannosaurusEntity.class);
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x4E502C, 0x353731);
        this.setEggColorFemale(0xBA997E, 0x7D5D48);
        this.setHealth(10, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(2);
        this.setStrength(2, 25);
        this.setMaximumAge(this.fromDays(60));
        this.setEyeHeight(0.35F, 3.5F);
        this.setSizeX(0.2F, 3.0F);
        this.setSizeY(0.4F, 3.6F);
        this.setStorage(54);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.4F, 0.1F);
        this.setMaxHerdSize(4);
        this.setAttackBias(1000.0);
        this.setBreeding(false, 2, 4, 60, false, true);

        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
