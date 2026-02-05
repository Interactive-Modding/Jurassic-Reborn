package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.AmphibianDinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.PenguinDinosaurEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.damagesource.DamageSource;

public class KairukuEntity extends PenguinDinosaurEntity {

    public KairukuEntity (Level world, EntityType<KairukuEntity> type)
    {
        super(world, type, DinosaurHandler.KAIRUKU);
        this.target(Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class);
    }
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.KAIRUKU_LIVING;
            case CALLING:
                return SoundHandler.KAIRUKU_CALL;
            case DYING:
                return SoundHandler.KAIRUKU_DEATH;
            case INJURED:
                return SoundHandler.KAIRUKU_HURT;
            case BEGGING:
                return SoundHandler.KAIRUKU_CALL;
            default:
                return null;
        }
    }
}

