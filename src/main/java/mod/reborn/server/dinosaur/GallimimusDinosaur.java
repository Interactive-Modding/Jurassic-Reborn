package mod.reborn.server.dinosaur;

import java.util.ArrayList;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.GallimimusEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class GallimimusDinosaur extends Dinosaur {
    public static final double SPEED = 0.41F;
    public GallimimusDinosaur() {
        super();

        this.setName("Gallimimus");
        this.setDinosaurClass(GallimimusEntity.class);
        this.setDinosaurType(DinosaurType.SCARED);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xD5BA86, 0xD16918);
        this.setEggColorFemale(0xCCBA94, 0xAB733D);
        this.setHealth(6, 30);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(1, 5);
        this.setMaximumAge(this.fromDays(35));
        this.setEyeHeight(0.58F, 2.7F);
        this.setSizeX(0.2F, 1.2F);
        this.setSizeY(0.35F, 2.45F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get().withModule(new Diet.DietModule(FoodType.INSECT).withCondition(entity -> entity.getAgePercentage() < 25)));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Head Base");
        this.setScale(1.05F, 0.1F);
        this.setImprintable(true);
        this.setFlee(true);
        this.setFlockSpeed(1.35F);
        this.setBreeding(false, 2, 6, 20, false, true);
        this.setJumpHeight(3);
        String[][] recipe =
                {{"", "", "", "neck_vertebrae", "skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder",""},
                {"", "leg_bones", "leg_bones", "arm_bones", ""},
                {"", "foot_bones", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
        this.enableSkeleton();
    }
}
