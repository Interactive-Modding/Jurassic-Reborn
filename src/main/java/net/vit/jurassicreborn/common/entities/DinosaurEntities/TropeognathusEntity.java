package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.FlyingDinosaurEntity;import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class TropeognathusEntity extends FlyingDinosaurEntity
{
    public TropeognathusEntity(EntityType<TropeognathusEntity> type, Level world)
    {
        super(world, type, DinosaurHandler.TROPEOGNATHUS);
        this.target(AlligatorGarEntity.class, Squid.class, Cod.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class,    AlvarezsaurusEntity.class, BeelzebufoEntity.class, ChilesaurusEntity.class, KairukuEntity.class, CoelurusEntity.class, CompsognathusEntity.class, DimorphodonEntity.class, DiplocaulusEntity.class, DodoEntity.class, GuanlongEntity.class, HypsilophodonEntity.class, LeaellynasauraEntity.class, LeptictidiumEntity.class, LudodactylusEntity.class, MegapiranhaEntity.class, MicroceratusEntity.class, MicroraptorEntity.class, MoganopterusEntity.class, MussaurusEntity.class, OthnieliaEntity.class, OviraptorEntity.class, ProceratosaurusEntity.class, ProtoceratopsEntity.class, PteranodonEntity.class, SegisaurusEntity.class, TroodonEntity.class, ZhenyuanopterusEntity.class, Player.class
, Animal.class, Villager.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.TROPEOGNATHUS_LIVING;
            case DYING:
                return SoundHandler.TROPEOGNATHUS_DEATH;
            case INJURED:
                return SoundHandler.TROPEOGNATHUS_HURT;
            case ROARING:
                return SoundHandler.TROPEOGNATHUS_ROAR;
            case BEGGING:
                return SoundHandler.TROPEOGNATHUS_THREAT;
        }

        return null;
    }
}

