package mod.reborn.server.entity.dinosaur;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.sound.SoundHandler;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.animal.GoatEntity;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.World;

public class HyaenodonEntity extends DinosaurEntity {


    public HyaenodonEntity(World world) {
        super(world);
        this.target(GoatEntity.class, SmilodonEntity.class, TitanisEntity.class, EntityPlayer.class, ArsinoitheriumEntity.class, EntityAnimal.class, EntityVillager.class, AchillobatorEntity.class, AlligatorGarEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, VelociraptorBlueEntity.class, VelociraptorCharlieEntity.class, ChilesaurusEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, VelociraptorDeltaEntity.class, DodoEntity.class, DiplocaulusEntity.class, VelociraptorEchoEntity.class, GallimimusEntity.class, GuanlongEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, SegisaurusEntity.class, TroodonEntity.class, VelociraptorEntity.class);
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class, TyrannosaurusEntity.class, GiganotosaurusEntity.class, SpinosaurusEntity.class));
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.HYAENODON_LIVING;
            case CALLING:
                return SoundHandler.HYAENODON_CALL;
            case DYING:
                return SoundHandler.HYAENODON_DEATH;
            case INJURED:
                return SoundHandler.HYAENODON_HURT;
        }

        return null;
    }
}
