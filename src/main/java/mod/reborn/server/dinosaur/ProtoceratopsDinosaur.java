package mod.reborn.server.dinosaur;


import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.ProtoceratopsEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import java.util.ArrayList;

public class ProtoceratopsDinosaur extends Dinosaur
{
    public static final double SPEED = 0.4F;
    public ProtoceratopsDinosaur()
    {
        super();

        this.setName("Protoceratops");
        this.setDinosaurType(DinosaurType.SCARED);
        this.setDinosaurClass(ProtoceratopsEntity.class);
        this.setTimePeriod(TimePeriod.CRETACEOUS);
        this.setEggColorMale(0xFDCEB5, 0xFBC073);
        this.setEggColorFemale(0xEBCC98, 0xAA804E);
        this.setHealth(4, 20);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 10);
        this.setMaximumAge(fromDays(40));
        this.setEyeHeight(0.35F, 0.85F);
        this.setSizeX(0.15F, 1.0F);
        this.setSizeY(0.2F, 1.25F);
        this.setStorage(9);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("skull", "teeth", "tail_vertebrae", "shoulder", "pelvis", "ribcage", "front_leg_bones", "hind_leg_bones", "shoulder");
        this.setHeadCubeName("Head");
        this.setAttackBias(1);
        this.setScale(1.2F, 0.15F);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(false, 2, 4, 40, false, true);
        String[][] recipe = {
                {"tail_vertebrae", "pelvis", "ribcage","","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "teeth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        ArrayList<Biome> biomeList = new ArrayList<Biome>();
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.DRY));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}
