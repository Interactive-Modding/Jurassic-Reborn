package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.SinoceratopsEntity;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class SinoceratopsDinosaur extends Dinosaur {
    public static final double SPEED = 0.35F;
    public SinoceratopsDinosaur() {
        super();

        this.setName("Sinoceratops");
        this.setDinosaurClass(SinoceratopsEntity.class);
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0x76838c, 0xb5ae8b);
        this.setEggColorFemale(0x6e794c, 0x524f2d);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.3);
        this.setHealth(10, 70);
        this.setStrength(5, 20);
        this.setMaximumAge(this.fromDays(45));
        this.setEyeHeight(0.15F, 1.8F);
        this.setSizeX(0.15F, 2.5F);
        this.setSizeY(0.3F, 3.0F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "horn", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.35F, 0.125F);
        this.setOffset(0.0F, 0.45F, 0.0F);
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
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
