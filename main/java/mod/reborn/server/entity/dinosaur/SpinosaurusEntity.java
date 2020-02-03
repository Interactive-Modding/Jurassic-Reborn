package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class SpinosaurusEntity extends DinosaurEntity
{
    private int stepCount = 0;

    public SpinosaurusEntity(World world)
    {
        super(world);
        this.target(TyrannosaurusEntity.class, CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class, BrachiosaurusEntity.class, ApatosaurusEntity.class, RugopsEntity.class, HerrerasaurusEntity.class, VelociraptorEntity.class, AchillobatorEntity.class, CarnotaurusEntity.class);
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
                return SoundHandler.SPINOSAURUS_LIVING;
            case DYING:
                return SoundHandler.SPINOSAURUS_DEATH;
            case INJURED:
                return SoundHandler.SPINOSAURUS_HURT;
        }

        return null;
    }
}
