package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.CorythosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;

public class CorythosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.41F;
    public CorythosaurusDinosaur()
    {
        super();

        this.setName("Corythosaurus");
        this.setDinosaurType(DinosaurType.PASSIVE);
        this.setDinosaurClass(CorythosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xBAA87E, 0x5E7201);
        this.setEggColorFemale(0xB3A27D, 0xE9BF47);
        this.setHealth(10, 56);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 15);
        this.setMaximumAge(fromDays(40));
        this.setAttackBias(50);
        this.setEyeHeight(0.45F, 3.45F);
        this.setSizeX(0.5F, 2.5F);
        this.setSizeY(0.8F, 3.5F);
        this.setStorage(36);
        this.setAttackBias(12);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("cheek_teeth", "front_leg_bones", "neck_vertebrae","hind_leg_bones", "pelvis", "shoulder", "skull", "tail_vertebrae", "ribcage");
        this.setHeadCubeName("Head");
        this.setScale(2.2F, 0.1F);
        this.setOffset(0.0F, 0.775F, 0.0F);
        this.setImprintable(true);
        this.setFlockSpeed(1.5F);
        this.setBreeding(false, 2, 6, 40, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "cheek_teeth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
