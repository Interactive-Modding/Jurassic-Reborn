package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class AlvarezsaurusEntity extends DinosaurEntity {

    public AlvarezsaurusEntity(EntityType<AlvarezsaurusEntity> type, Level world) {
        super(world, type, DinosaurHandler.ALVAREZSAURUS);
        this.target(DodoEntity.class, CompsognathusEntity.class, HypsilophodonEntity.class, Animal.class, LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, DimorphodonEntity.class, LeaellynasauraEntity.class, ProtoceratopsEntity.class, OviraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, TroodonEntity.class, SegisaurusEntity.class);
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

