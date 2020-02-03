package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class HerrerasaurusEntity extends DinosaurEntity
{
    public HerrerasaurusEntity(World world)
    {
        super(world);
        this.target(CompsognathusEntity.class, AnkylosaurusEntity.class, EntityPlayer.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LudodactylusEntity.class, HypsilophodonEntity.class, GallimimusEntity.class, SegisaurusEntity.class, ProtoceratopsEntity.class, ParasaurolophusEntity.class, OthnieliaEntity.class, MicroceratusEntity.class, TriceratopsEntity.class, StegosaurusEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));

    }

    @Override
    public float getSoundVolume()
    {
        return (float) interpolate(1.3F, 2.0F);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HERRERASAURUS_LIVING;
            case DYING:
                return SoundHandler.HERRERASAURUS_DEATH;
            case INJURED:
                return SoundHandler.HERRERASAURUS_DEATH;
        }

        return null;
    }
}
