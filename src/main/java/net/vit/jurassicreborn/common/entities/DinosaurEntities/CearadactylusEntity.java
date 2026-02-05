package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class CearadactylusEntity extends FlyingDinosaurEntity
{
    public CearadactylusEntity(Level world, EntityType<CearadactylusEntity> type)
    {
        super(world, type, DinosaurHandler.CEARADACTYLUS);
        this.target(AlligatorGarEntity.class,
                AlvarezsaurusEntity.class,
                BeelzebufoEntity.class,
                CompsognathusEntity.class,
                CoelacanthEntity.class,
                MicroceratusEntity.class,
                MicroraptorEntity.class,
                MegapiranhaEntity.class,
                LeptictidiumEntity.class,
                LudodactylusEntity.class,
                OthnieliaEntity.class,
                OviraptorEntity.class,
                ProtoceratopsEntity.class,
                SegisaurusEntity.class,
                TroodonEntity.class,
                ZhenyuanopterusEntity.class,
                CrassigyrinusEntity.class,
                DiplocaulusEntity.class,
                DimorphodonEntity.class,
                Player.class
                , Animal.class,
                Villager.class);
    }
        @Override
        public SoundEvent getSoundForAnimation(Animation animation)
        {
            switch (EntityAnimation.getAnimation(animation))
            {
                case SPEAK:
                    return SoundHandler.CEARADACTYLUS_LIVING;
                case DYING:
                    return SoundHandler.CEARADACTYLUS_DEATH;
                case INJURED:
                    return SoundHandler.CEARADACTYLUS_HURT;
                case CALLING:
                    return SoundHandler.CEARADACTYLUS_CALL;
                case BEGGING:
                    return SoundHandler.CEARADACTYLUS_THREAT;
            }

            return null;
        }
    }

