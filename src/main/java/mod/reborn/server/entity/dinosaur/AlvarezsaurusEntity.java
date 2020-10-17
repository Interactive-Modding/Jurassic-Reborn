package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class AlvarezsaurusEntity extends DinosaurEntity {

    public AlvarezsaurusEntity(World world) {
        super(world);
        this.target(DodoEntity.class, CompsognathusEntity.class, HypsilophodonEntity.class, EntityAnimal.class, LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, DimorphodonEntity.class, LeaellynasauraEntity.class, ProtoceratopsEntity.class, OviraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, TroodonEntity.class, SegisaurusEntity.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.ALVAREZSAURUS_LIVING;
            case DYING:
                return SoundHandler.ALVAREZSAURUS_DEATH;
            case INJURED:
                return SoundHandler.ALVAREZSAURUS_HURT;
            case CALLING:
                return SoundHandler.ALVAREZSAURUS_CALL;
            case BEGGING:
                return SoundHandler.ALVAREZSAURUS_THREAT;
        }

        return null;
    }
}
