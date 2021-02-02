package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.SleepTime;
import mod.reborn.server.entity.dinosaur.DunkleosteusEntity;
import mod.reborn.server.period.TimePeriod;
import java.util.ArrayList;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;

public class DunkleosteusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.45F;
    public DunkleosteusDinosaur()
    {
        super();

        this.setName("Dunkleosteus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(DunkleosteusEntity.class);
        this.setTimePeriod(TimePeriod.DEVONIAN);
        this.setEggColorMale(0xA89B8C, 0x753A28);
        this.setEggColorFemale(0xA6A588, 0x785F2A);
        this.setHealth(16, 60);
        this.setSpeed((SPEED -0.4), SPEED);
        this.setStrength(10, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.3F, 1.2F);
        this.setSizeX(0.3F, 2.7F);
        this.setSizeY(0.2F, 2.0F);
        this.setMarineAnimal(true);
        this.setStorage(27);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setSleepTime(SleepTime.DIURNAL);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.setBones("mouth_plates", "skull", "dorsal_fin", "spine");
        this.setHeadCubeName("Main head");
        this.setScale(2.1F, 0.15F);
        this.setOffset(0.0F, 1.0F, -0.25F);
        this.shouldDefendOffspring();
        this.setAttackBias(420);
        this.setImprintable(false);
        this.setBreeding(true, 2, 6, 20, false, true);
        String[][] recipe =     {
                {"", "", "dorsal_fin","",""},
                {"spine", "spine", "spine", "skull", "mouth_plates"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.OCEAN));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.RIVER));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
