package mod.reborn.server.dinosaur;

import mod.reborn.server.api.Hybrid;
import mod.reborn.server.entity.Diet;
import mod.reborn.server.entity.dinosaur.SpinoraptorEntity;
import mod.reborn.server.period.TimePeriod;
import net.minecraft.util.ResourceLocation;


public class SpinoraptorDinosaur extends Dinosaur implements Hybrid
{
    private ResourceLocation texture;
    public static final double SPEED = 0.45F;

    public SpinoraptorDinosaur()
    {
        super();

        this.setName("Spinoraptor");
        this.setDinosaurType(DinosaurType.AGGRESSIVE);
        this.setDinosaurClass(SpinoraptorEntity.class);
        this.setTimePeriod(TimePeriod.NONE); //TODO, it's a hybrid, what do you do here?
        this.setEggColorMale(0xa98b2b, 0x191919);
        this.setEggColorFemale(0xbc972e, 0x6f5b25);
        this.setHealth(8, 60);
        this.setSpeed((SPEED -0.05), SPEED);
        this.setAttackSpeed(1.5);
        this.setStrength(2, 25);
        this.setMaximumAge(fromDays(30));
        this.setEyeHeight(0.15F, 3.4F);
        this.setSizeX(0.2F, 2.8F);
        this.setSizeY(0.2F, 3.6F);
        this.setStorage(54);
        this.setDiet((Diet.CARNIVORE.get()));
        this.setBones("skull", "tooth", "arm_bones", "claw", "foot_bones", "leg_bones", "neck_vertebrae", "ribcage", "shoulder", "tail_vertebrae", "pelvis");
        this.setHeadCubeName("Head");
        this.setScale(1.4F, 0.15F);
        this.setAttackBias(4000);
        this.setBreeding(false, 0, 0, 999, false, false);
        this.setHybrid();
        this.setMaxHerdSize(4);
        this.setJumpHeight(3);
        this.setCanClimb(true);
        this.setDefendOwner(true);
        this.setImprintable(true);
        String[][] recipe = {
                {"", "", "","neck_vertebrae","skull"},
                {"tail_vertebrae", "pelvis", "ribcage","shoulder","tooth"},
                {"", "", "leg_bones", "arm_bones", "claw"},
                {"", "", "foot_bones", "", ""}};
        this.setRecipe(recipe);
        this.enableSkeleton();

    }

    @Override
    public Class<? extends Dinosaur>[] getDinosaurs() {
        return new Class[]{SpinosaurusDinosaur.class, VelociraptorDinosaur.class};
    }
}
