package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.entity.animal.EntityShark;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class MosasaurusEntity extends SwimmingDinosaurEntity {

    public MosasaurusEntity(World world) {
        super(world);
        this.target(CompsognathusEntity.class, EntityPlayer.class, EntityShark.class, DilophosaurusEntity.class, DimorphodonEntity.class, DodoEntity.class, LeaellynasauraEntity.class, HypsilophodonEntity.class, StegosaurusEntity.class, ProtoceratopsEntity.class, OthnieliaEntity.class, MicroceratusEntity.class);
        this.target(EntityLivingBase.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.MOSASAURUS_LIVING;
            case CALLING:
                return SoundHandler.MOSASAURUS_MATE_CALL;
            case INJURED:
                return SoundHandler.MOSASAURUS_HURT;
            case DYING:
                return SoundHandler.MOSASAURUS_DEATH;
            case ROARING:
                return SoundHandler.MOSASAURUS_ROAR;
            case ATTACKING:
                return SoundHandler.MOSASAURUS_ATTACK;
            default:
                return null;
        }
    }
}
