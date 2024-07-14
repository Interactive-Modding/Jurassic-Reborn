package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CrassigyrinusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class CrassigyrinusDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public CrassigyrinusDinosaur() {
        super();

        this.setName("Crassigyrinus");
        this.setDinosaurType(DinosaurType.SCARED); //??
        this.setDinosaurClass(CrassigyrinusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xE5C2AD, 0x6E320E);
        this.setEggColorFemale(0xE8D0B2, 0x74480F);
        this.setHealth(10, 20);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStorage(27);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setAttackBias(120);
        this.setEyeHeight(0.05F, 0.45F);
        this.setSizeX(0.1F, 1.0F);
        this.setSizeY(0.1F, 0.5F);
        this.setDiet(Diet.CARNIVORE.get());
        this.setBones("skull", "teeth","tail_vertebrae","ribcage","foot_bone","hind_leg_bones", "front_leg_bones");
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setHeadCubeName("Head");
        this.setAttackBias(5);
        this.setScale(0.65F, 0.1F);
        this.setOffset(0,-2,0);
        this.setMarineAnimal(true);
        this.setBreeding(true, 2, 4, 20, false, true);
        String[][] recipe =     {
                { "", "", "skull"},
                { "tail_vertebrae", "ribcage","teeth"},
                { "hind_leg_bones", "", "front_leg_bones"},
                { "", "", "foot_bone"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WATER));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
