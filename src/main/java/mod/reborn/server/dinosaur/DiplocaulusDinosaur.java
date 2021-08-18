package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.DiplocaulusEntity;
import mod.reborn.server.period.TimePeriod;
import java.util.ArrayList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DiplocaulusDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public DiplocaulusDinosaur() {
        super();

        this.setName("Diplocaulus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(DiplocaulusEntity.class);
        this.setTimePeriod(TimePeriod.PERMIAN);
        this.setEggColorMale(0xBDD9DE, 0x286A7F);
        this.setEggColorFemale(0xCDDEE7, 0x285880);
        this.setHealth(10, 20);
        this.setSpeed((SPEED -0.25), SPEED);
        this.setStorage(18);
        this.setStrength(1, 20);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(0.45F, 1.6F);
        this.setSizeX(0.15F, 1.4F);
        this.setSizeY(0.35F, 1.8F);
        this.setDiet(Diet.PISCIVORE.get());
        this.setBones("skull", "teeth", "foot_bone","leg_bones", "ribcage", "tail_vertebrae");
        this.setHeadCubeName("Head");
        this.setScale(0.8F, 0.1F);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setOffset(0,-4.5F,0);
        this.setAttackBias(10);
        this.setImprintable(true);
        this.setBreeding(true, 2, 6, 20, false, true);
        this.setMarineAnimal(true);
        String[][] recipe =     {{ "", "", "skull"},
                                 {"tail_vertebrae", "ribcage","teeth"},
                                 { "leg_bones", "", "leg_bones"},
                                 { "", "", "foot_bone"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.WATER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
