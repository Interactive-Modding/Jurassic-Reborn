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

public class QuetzalEntity extends FlyingDinosaurEntity
{
    public QuetzalEntity(EntityType<QuetzalEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.QUETZAL);
        this.target(AlligatorGarEntity.class, SmilodonEntity.class, TitanisEntity.class, MegatheriumEntity.class, ElasmotheriumEntity.class, DeinotheriumEntity.class, ArsinoitheriumEntity.class, MawsoniaEntity.class, EndocerasEntity.class, CamerocerasEntity.class, AlvarezsaurusEntity.class, BeelzebufoEntity.class, CearadactylusEntity.class, CoelacanthEntity.class, CoelurusEntity.class, CompsognathusEntity.class, CrassigyrinusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GuanlongEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, TroodonEntity.class, SegisaurusEntity.class, ZhenyuanopterusEntity.class, PteranodonEntity.class, TropeognathusEntity.class, Player.class
, Animal.class, Villager.class);
    }

    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.QUETZALCOATLUS_LIVING;
            case CALLING:
                return SoundHandler.QUETZALCOATLUS_LIVING;
            case DYING:
                return SoundHandler.QUETZALCOATLUS_DEATH;
            case INJURED:
                return SoundHandler.QUETZALCOATLUS_HURT;
            case BEGGING:
                return SoundHandler.QUETZALCOATLUS_THREAT;
        }

        return null;
    }
}

