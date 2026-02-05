package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.animal.goat.Goat;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class SegisaurusEntity extends DinosaurEntity
{
    public SegisaurusEntity(Level world, EntityType<SegisaurusEntity> type)
    {
        super(world, type, DinosaurHandler.SEGISAURUS);
        this.target(MicroraptorEntity.class, MicroceratusEntity.class, AlligatorGarEntity.class, DodoEntity.class, HypsilophodonEntity.class, CompsognathusEntity.class, AlvarezsaurusEntity.class, Player.class
, Animal.class, Villager.class, Goat.class);
        this.doesEatEggs(true);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.SEGISAURUS_LIVING;
            case DYING:
                return SoundHandler.SEGISAURUS_DEATH;
            case INJURED:
                return SoundHandler.SEGISAURUS_HURT;
            case BEGGING:
                return SoundHandler.SEGISAURUS_THREAT;
        }

        return null;
    }
}

