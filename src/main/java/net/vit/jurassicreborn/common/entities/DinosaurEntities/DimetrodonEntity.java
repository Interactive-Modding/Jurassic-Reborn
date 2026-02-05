package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class DimetrodonEntity extends DinosaurEntity
{
    public DimetrodonEntity(Level world, EntityType<DimetrodonEntity> type)
    {
        super(world, type, DinosaurHandler.DIMETRODON);
        this.target(AlvarezsaurusEntity.class, GuanlongEntity.class, TitanisEntity.class, SmilodonEntity.class, ArsinoitheriumEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DodoEntity.class, GallimimusEntity.class, HyaenodonEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OrnithomimusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, SegisaurusEntity.class, TroodonEntity.class, Player.class, ProtoceratopsEntity.class, Animal.class, Villager.class);
        this.doesEatEggs(true);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DIMETRODON_LIVING;
            case CALLING:
                return SoundHandler.DIMETRODON_LIVING;
            case DYING:
                return SoundHandler.DIMETRODON_ROAR;
            case INJURED:
                return SoundHandler.DIMETRODON_ROAR;
            case BEGGING:
                return SoundHandler.DIMETRODON_LIVING;
        }

        return null;
    }
}
