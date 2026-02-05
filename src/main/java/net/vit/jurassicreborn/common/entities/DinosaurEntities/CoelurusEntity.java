package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
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

public class CoelurusEntity extends DinosaurEntity
{
    public CoelurusEntity(Level world, EntityType<CoelurusEntity> type)
    {
        super(world, type, DinosaurHandler.COELURUS);
        this.target(AlligatorGarEntity.class, SmilodonEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CompsognathusEntity.class, DodoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, SegisaurusEntity.class, HypsilophodonEntity.class, OthnieliaEntity.class, MussaurusEntity.class, TroodonEntity.class, DimorphodonEntity.class, ChilesaurusEntity.class, KairukuEntity.class, MicroraptorEntity.class, LeptictidiumEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, GuanlongEntity.class, HyaenodonEntity.class, LeaellynasauraEntity.class, ProceratosaurusEntity.class, MicroceratusEntity.class, Player.class
, Animal.class, Villager.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.COELURUS_LIVING;
            case DYING:
                return SoundHandler.COELURUS_DEATH;
            case INJURED:
                return SoundHandler.COELURUS_HURT;
            case BEGGING:
                return SoundHandler.COELURUS_THREAT;
        }

        return null;
    }
}


