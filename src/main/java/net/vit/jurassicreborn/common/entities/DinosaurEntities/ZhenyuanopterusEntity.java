package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class ZhenyuanopterusEntity extends FlyingDinosaurEntity
{
    public ZhenyuanopterusEntity(EntityType<ZhenyuanopterusEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.ZHENYUANOPTERUS);
        this.target(AlligatorGarEntity.class, Squid.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CoelacanthEntity.class, GuanlongEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DodoEntity.class, DiplocaulusEntity.class, MoganopterusEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, Player.class
, Animal.class, Villager.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.ZHENYUANOPTERUS_LIVING;
            case DYING:
                return SoundHandler.ZHENYUANOPTERUS_DEATH;
            case INJURED:
                return SoundHandler.ZHENYUANOPTERUS_HURT;
            case ROARING:
                return SoundHandler.ZHENYUANOPTERUS_ROAR;
            case BEGGING:
                return SoundHandler.ZHENYUANOPTERUS_THREAT;
        }

        return null;
    }
}

