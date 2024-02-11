package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.FlyingDinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CearadactylusEntity extends FlyingDinosaurEntity
{
    public CearadactylusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CompsognathusEntity.class, CoelacanthEntity.class, DimetrodonEntity.class, MicroceratusEntity.class, DimetrodonEntity.class, MicroraptorEntity.class, MegapiranhaEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, ZhenyuanopterusEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, DimorphodonEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
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
