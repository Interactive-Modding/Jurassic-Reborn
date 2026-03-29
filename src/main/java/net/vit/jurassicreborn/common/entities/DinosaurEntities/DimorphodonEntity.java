package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DimorphodonEntity extends FlyingDinosaurEntity
{
    public DimorphodonEntity(Level world, EntityType<DimorphodonEntity> type)
    {
        super(world, type, DinosaurHandler.DIMORPHODON);
        this.target(AlvarezsaurusEntity.class, Player.class
, Animal.class, Villager.class, CompsognathusEntity.class, DodoEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, OthnieliaEntity.class, SegisaurusEntity.class);
    }

//    @Override
//    protected void doTarget(){
//        this.target(LeptictidiumEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
//    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DIMORPHODON_LIVING;
            case DYING:
                return SoundHandler.DIMORPHODON_DEATH;
            case INJURED:
                return SoundHandler.DIMORPHODON_HURT;
            case CALLING:
                return SoundHandler.DIMORPHODON_CALL;
            case BEGGING:
                return SoundHandler.DIMORPHODON_THREAT;
        }

        return null;
    }
}

