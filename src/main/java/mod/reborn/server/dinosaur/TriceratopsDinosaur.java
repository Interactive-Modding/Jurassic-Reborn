package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.TriceratopsEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class TriceratopsDinosaur extends Dinosaur {
    public static final double SPEED = 0.35F;
    public TriceratopsDinosaur() {
        super();

        this.setName("Triceratops");
        this.setDinosaurClass(TriceratopsEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x404138, 0x1C1C1C);
        this.setEggColorFemale(0x8F7B76, 0x73676A);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.3);
        this.setHealth(10, 70);
        this.setStrength(5, 20);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.25F, 2.9F);
        this.setSizeX(0.15F, 2.5F);
        this.setSizeY(0.3F, 3.0F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.15F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setAttackBias(400.0);
        this.setBreeding(false, 2, 6, 48, false, true);
        String[][] recipe = {
                {"", "", "","","horn"},
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SPARSE));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
