package mod.reborn.server.dinosaur;

import mod.reborn.server.api.Hybrid;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.GrowthStage;
import mod.reborn.server.entity.dinosaur.IndominusEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.util.ResourceLocation;

public class IndominusDinosaur extends Dinosaur implements Hybrid
{
    private ResourceLocation texture;

    private ResourceLocation overlayTexture;

    public static final double SPEED = 0.4F;

    public IndominusDinosaur()
    {
        super();

        this.setName("Indominus");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(IndominusEntity.class);
        this.setTimePeriod(TimePeriod.NONE); //TODO, it's a hybrid, what do you do here?
        this.setEggColorMale(0xBEBABB, 0x95949A);
        this.setEggColorFemale(0xBEBABB, 0x95949A);
        this.setHealth(15, 110);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setStrength(5, 40);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.55F, 5.4F);
        this.setSizeX(0.4F, 3.5F);
        this.setSizeY(0.7F, 6.0F);
        this.setStorage(54);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("arm_bones", "foot_bones", "leg_bones", "neck_vertebrae", "claw", "pelvis", "ribcage", "shoulder", "skull", "tail_vertebrae", "tooth");
        this.setHeadCubeName("Head");
        this.setScale(2.8F, 0.4F);
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

        this.texture = new ResourceLocation(getDinosaurTexture("camouflage"));
        this.overlayTexture = new ResourceLocation(getDinosaurTexture(""));

    }

    @Override
    public Class<? extends Dinosaur>[] getDinosaurs()
    {
        return new Class[] { TyrannosaurusDinosaur.class, VelociraptorDinosaur.class, GiganotosaurusDinosaur.class, RugopsDinosaur.class, MajungasaurusDinosaur.class, CarnotaurusDinosaur.class, TherizinosaurusDinosaur.class };
    }

    @Override
    public ResourceLocation getMaleTexture(GrowthStage stage)
    {
        return texture;
    }

    @Override
    public ResourceLocation getFemaleTexture(GrowthStage stage)
    {
        return texture;
    }

    public ResourceLocation getCamoTexture(GrowthStage stage)
    {
        return overlayTexture;
    }
}
