package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class GiganotosaurusEntity extends DinosaurEntity
{
    private int stepCount = 0;

    public GiganotosaurusEntity(World world)
    {
        super(world);
        this.target(AchillobatorEntity.class, ArsinoitheriumEntity.class, SinoceratopsEntity.class, AlligatorGarEntity.class, AllosaurusEntity.class, AnkylosaurusEntity.class, AlvarezsaurusEntity.class, GuanlongEntity.class, BaryonyxEntity.class, BeelzebufoEntity.class, CarcharodontosaurusEntity.class, CarnotaurusEntity.class, CeratosaurusEntity.class, VelociraptorEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, VelociraptorDeltaEntity.class, VelociraptorEchoEntity.class, ChasmosaurusEntity.class, CoelurusEntity.class, CorythosaurusEntity.class, CompsognathusEntity.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, EdmontosaurusEntity.class, GallimimusEntity.class, HerrerasaurusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, IndominusEntity.class, LambeosaurusEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MajungasaurusEntity.class, MammothEntity.class, MetriacanthosaurusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, PachycephalosaurusEntity.class, ParasaurolophusEntity.class, PostosuchusEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, QuetzalEntity.class, RugopsEntity.class, SegisaurusEntity.class, SpinosaurusEntity.class, StegosaurusEntity.class, StyracosaurusEntity.class, SuchomimusEntity.class, TherizinosaurusEntity.class, TriceratopsEntity.class, TroodonEntity.class, TropeognathusEntity.class, TyrannosaurusEntity.class, ZhenyuanopterusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class,  GoatEntity.class);
    }

    @Override
    public void onUpdate()
    {
        super.onUpdate();

        if (this.moveForward > 0 && this.stepCount <= 0)
        {
            this.playSound(SoundHandler.STOMP, (float) interpolate(0.1F, 1.0F), this.getSoundPitch());
            stepCount = 65;
        }

        this.stepCount -= this.moveForward * 9.5;
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.GIGANOTOSAURUS_LIVING;
            case DYING:
                return SoundHandler.GIGANOTOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.GIGANOTOSAURUS_HURT;
            case CALLING:
                return SoundHandler.GIGANOTOSAURUS_CALL;
            case ROARING:
                return SoundHandler.GIGANOTOSAURUS_ROAR;
            case BEGGING:
                return SoundHandler.GIGANOTOSAURUS_THREAT;
        }

        return null;
    }
}
