package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;


import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.ParaceratheriumEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.util.TimePeriod;

public class ParaceratheriumDinosaur extends Dinosaur {
    public static final double SPEED = 0.3F;
    public ParaceratheriumDinosaur() {
        super();

        this.setName("Paraceratherium");
        this.setScientificName("Paraceratherium transouralicum");
        this.setFamily("Paraceratheriidae");
        this.setLocation("Pakistan");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(ParaceratheriumEntity.class);
        this.setTimePeriod(TimePeriod.PALEOGENE);
        this.setEggColorMale(0x4c402c, 0x423b3a);
        this.setEggColorFemale(0xaeafa9, 0x9d9d9b);
        this.setHealth(10, 60);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStorage(27);
        this.setStrength(3, 15);
        this.setMaximumAge(fromDays(45));
        this.setEyeHeight(1.6F, 3.3F);
        this.setSizeX(1.45F, 3F);
        this.setSizeY(1.6F, 3.4F);
        this.setDiet(Diet.HERBIVORE.get());
        this.setBones("skull", "tooth","front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "tail_vertebrae");
        this.setHeadCubeName("head");
        this.setScale(1.7F, 0.4F);
        this.setMammal(true);
        this.setBirthType(BirthType.LIVE_BIRTH);
        this.shouldDefendOffspring();
        this.setAttackBias(400);
        this.setImprintable(true);
        this.setDefendOwner(true);
        this.setBreeding(true, 2, 4, 40, false, true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","tooth",""},
                {"", "hind_leg_bones", "", "front_leg_bones", "shoulder"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
//        ArrayList<Biome> biomeList = new ArrayList<Biome>();
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SAVANNA));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.PLAINS));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.FOREST));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.MESA));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.SNOWY));
//        biomeList.addAll(BiomeDictionary.getBiomes(BiomeDictionary.Type.CONIFEROUS));
//        this.setSpawn(1, biomeList.toArray(new Biome[biomeList.size()]));
    }
}