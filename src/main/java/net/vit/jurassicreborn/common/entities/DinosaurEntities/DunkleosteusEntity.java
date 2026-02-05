package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;

public class DunkleosteusEntity extends SwimmingDinosaurEntity
{
    public DunkleosteusEntity(Level world, EntityType<DunkleosteusEntity> type)
    {
        super(world, type, DinosaurHandler.DUNKLEOSTEUS);
        this.target(AlligatorGarEntity.class, MawsoniaEntity.class, EndocerasEntity.class, CamerocerasEntity.class, Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class,    BeelzebufoEntity.class, CrassigyrinusEntity.class, DiplocaulusEntity.class, Player.class
, MegapiranhaEntity.class, Villager.class, Animal.class, Mob.class);
    }
    @Override
    public SoundEvent getSoundForAnimation(Animation animation)
    {
        switch (EntityAnimation.getAnimation(animation))
        {
            case SPEAK:
                return SoundHandler.DUNKLEOSTEUS_LIVING;
            case DYING:
                return SoundHandler.DUNKLEOSTEUS_DEATH;
            case INJURED:
                return SoundHandler.DUNKLEOSTEUS_HURT;
            case BEGGING:
                return SoundHandler.DUNKLEOSTEUS_THREAT;
        }

        return null;
    }
}

