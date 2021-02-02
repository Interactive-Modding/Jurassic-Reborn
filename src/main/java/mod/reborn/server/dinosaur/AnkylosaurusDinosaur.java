package mod.reborn.server.dinosaur;

import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.AnkylosaurusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import java.util.ArrayList;
import net.minecraftforge.common.BiomeDictionary;

public class AnkylosaurusDinosaur extends Dinosaur
{
    public static final double SPEED = 0.35F;
    public AnkylosaurusDinosaur()
    {
        super();
        this.setName("Ankylosaurus");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(AnkylosaurusEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xAB9B82, 0x7C6270);
        this.setEggColorFemale(0x554E45, 0x3F3935);
        this.setHealth(20, 120);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(6, 40);
        this.setMaximumAge(fromDays(50));
        this.setEyeHeight(0.4F, 2.0F);
        this.setSizeX(0.3F, 3.0F);
        this.setSizeY(0.3F, 3.0F);
        this.setStorage(27);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("armor_plating", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_club", "tail_vertebrae", "tooth");
        this.setHeadCubeName("head ");
        this.setScale(2.3F, 0.15F);
        this.setBreeding(false, 0, 6, 44, false, true);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setStorage(24);
        this.shouldDefendOffspring();
        String[][] recipe = {
                {"tail_club", "armor_plating", "","",""},
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
