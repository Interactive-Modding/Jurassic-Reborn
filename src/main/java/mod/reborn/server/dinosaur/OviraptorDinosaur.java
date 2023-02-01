package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.CompsognathusEntity;
import mod.reborn.server.entity.dinosaur.MicroraptorEntity;
import mod.reborn.server.entity.dinosaur.OviraptorEntity;
import mod.reborn.server.food.FoodType;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class OviraptorDinosaur extends Dinosaur
{
    public static final double SPEED = 0.40F;
    public OviraptorDinosaur(){
        super();

        this.setName("Oviraptor");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(OviraptorEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xA2A7AE, 0x666E81);
        this.setEggColorFemale(0xDEDAC4, 0x663341);
        this.setHealth(4, 16);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(9);
        this.setAttackSpeed(1.6);
        this.setStrength(2, 4);
        this.setAttackSpeed(2);
        this.setMaximumAge(fromDays(25));
        this.setEyeHeight(0.32F, 0.95F);
        this.setSizeX(0.25F, 0.6F);
        this.setSizeY(0.32F, 0.95F);
        this.setMaxHerdSize(8);
        this.setDiet(Diet.CARNIVORE.get().withModule(new Diet.DietModule(FoodType.PLANT)).withModule(new Diet.DietModule(FoodType.MEAT).withCondition(entity -> entity instanceof CompsognathusEntity || entity instanceof MicroraptorEntity)));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBones("arm_bones", "leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae");
        this.setHeadCubeName("Upper Head");
        this.setAttackBias(5);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setScale(0.4F, 0.09F);
        this.setBreeding(false, 2, 4, 30, false, true);
        String[][] recipe = {
                {"", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder"},
                {"", "leg_bones", "", "arm_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SANDY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
