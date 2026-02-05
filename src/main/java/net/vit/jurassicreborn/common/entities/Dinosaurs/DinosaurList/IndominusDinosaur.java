package net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurList;

import net.vit.jurassicreborn.common.entities.DinosaurEntities.*;
import net.vit.jurassicreborn.common.entities.DinosaurEntities.IndominusEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.EntityUtils.Diet;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.entities.EntityUtils.Hybrid;
import net.vit.jurassicreborn.common.util.TimePeriod;
import net.minecraft.resources.ResourceLocation;

public class IndominusDinosaur extends Dinosaur implements Hybrid
{
    private ResourceLocation overlayTexture;

    public static final double SPEED = 0.4F;

    public IndominusDinosaur()
    {
        super();
        this.setName("Indominus");
        this.setScientificName("N/A");
        this.setFamily("Hybrid");
        this.setLocation("N/A");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(IndominusEntity.class);
        this.setTimePeriod(TimePeriod.NONE);
        this.setEggColorMale(0x252627, 0x293336);
        this.setEggColorFemale(0xBEBABB, 0x95949A);
        this.setHealth(16, 95);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(4, 40);
        this.setMaximumAge(fromDays(30));
        this.setAttackSpeed(1.6);
        this.setEyeHeight(0.35F, 3.4F);
        this.setSizeX(0.2F, 3.5F);
        this.setSizeY(0.4F, 3.6F);
        this.setStorage(54);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "claw", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(3.0F, 0.1F);
        this.setAttackBias(4000);
        this.setBreeding(false, 0, 0, 999, false, false);
        this.setHybrid();
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "leg_bones", "", "arm_bones", "claw"},
                {"", "foot_bones", "", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();

        this.overlayTexture = new ResourceLocation(getDinosaurTexture(""));

    }

    @Override
    public Class<? extends Dinosaur>[] getDinosaurs()
    {
        return new Class[] { TyrannosaurusDinosaur.class, VelociraptorDinosaur.class, GiganotosaurusDinosaur.class, RugopsDinosaur.class, MajungasaurusDinosaur.class, CarnotaurusDinosaur.class, TherizinosaurusDinosaur.class };
    }

    public ResourceLocation getCamoTexture(GrowthStage stage)
    {
        return overlayTexture;
    }//where this used - gamma_02
}
