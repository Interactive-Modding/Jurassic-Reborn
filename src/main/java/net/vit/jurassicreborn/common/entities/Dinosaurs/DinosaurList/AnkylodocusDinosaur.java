package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.AnkylodocusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.util.TimePeriod;

import net.minecraft.resources.ResourceLocation;


public class AnkylodocusDinosaur extends Dinosaur implements Hybrid
{
    private ResourceLocation texture;
    public static final double SPEED = 0.25F;

    public AnkylodocusDinosaur()
    {
        super();
        this.setName("Ankylodocus");
        this.setFamily("Diplodocidae");
        this.setLocation("North America");
        this.setDinosaurType(DinosaurType.NEUTRAL);
        this.setDinosaurClass(AnkylodocusEntity.class);
        this.setTimePeriod(TimePeriod.NONE); //TODO, it's a hybrid, what do you do here?
        this.setEggColorMale(0x9d5353, 0xe67a13);
        this.setEggColorFemale(0x9d5353, 0x727272);
        this.setHealth(10, 80);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(10, 50);
        this.setMaximumAge(fromDays(95));
        this.setEyeHeight(0.45F, 3.75F);
        this.setSizeX(0.4F, 5.4F);
        this.setSizeY(0.5F, 5.8F);
        this.setStorage(54);
        this.setDiet((Diet.HERBIVORE.get()));
        this.setBones("armor_plating", "front_leg_bones", "hind_leg_bones", "neck_vertebrae", "pelvis", "ribcage", "shoulder", "skull", "tail_club", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(1.2F, 0.1F);
        this.setPaleoPadScale(5);
        this.shouldDefendOffspring();
        this.setHybrid();
        this.setAttackBias(1500);
        this.setDefendOwner(true);
        this.setImprintable(true);
        this.setBreeding(false, 4, 8, 80, false, true);
        String[][] recipe = {
                {"tail_club", "armor_plating", "","",""},
                {"tail_vertebrae", "pelvis", "ribcage","neck_vertebrae","skull"},
                {"hind_leg_bones", "hind_leg_bones", "", "shoulder", "tooth"},
                {"", "", "", "front_leg_bones", "front_leg_bones"}};
        this.setRecipe(recipe);
        this.enableSkeleton();
        this.setHybrid();
    }

    @Override
    public Class<? extends Dinosaur>[] getDinosaurs() {
        return new Class[]{AnkylosaurusDinosaur.class, DiplodocusDinosaur.class};
    }
}