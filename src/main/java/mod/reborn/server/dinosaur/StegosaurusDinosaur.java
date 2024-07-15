package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.StegosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class StegosaurusDinosaur extends Dinosaur{
    public static final double SPEED = 0.35F;
    public StegosaurusDinosaur() {
        super();

        this.setName("Stegosaurus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(StegosaurusEntity.class);
        this.setTimePeriod(TimePeriod.JURASSIC);
        this.setEggColorMale(0xBABF83, 0x75964E);
        this.setEggColorFemale(0xC8BC9A, 0x827D54);
        this.setHealth(10, 50);
        this.setStrength(12, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.26F, 3.5F);
        this.setSizeX(0.2F, 3.6F);
        this.setSizeY(0.3F, 3.6F);
        this.setStorage(36);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("front_leg_bones", "hind_leg_bones", "neck", "pelvis", "ribcage", "shoulder", "skull", "tail", "thagomizer", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.55F, 0.15F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.setAttackBias(350);
        this.shouldDefendOffspring();
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 6, 20, false, true);
        String[][] recipe = {
                {"tail", "pelvis", "ribcage","neck","skull"},
                {"thagomizer", "hind_leg_bones", "hind_leg_bones", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}

