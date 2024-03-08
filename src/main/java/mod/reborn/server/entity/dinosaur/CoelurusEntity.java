package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class CoelurusEntity extends DinosaurEntity
{
    public CoelurusEntity(World world)
    {
        super(world);
        this.target(AlligatorGarEntity.class, DimetrodonEntity.class, SmilodonEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CompsognathusEntity.class, DodoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, SegisaurusEntity.class, HypsilophodonEntity.class, OthnieliaEntity.class, MussaurusEntity.class, TroodonEntity.class, DimorphodonEntity.class, ChilesaurusEntity.class, MicroraptorEntity.class, LeptictidiumEntity.class, OviraptorEntity.class, ProtoceratopsEntity.class, GuanlongEntity.class, HyaenodonEntity.class, LeaellynasauraEntity.class, ProceratosaurusEntity.class, MicroceratusEntity.class, EntityPlayer.class, EntityAnimal.class, EntityVillager.class);
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

