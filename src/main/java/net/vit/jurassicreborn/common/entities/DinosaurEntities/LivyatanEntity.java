package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import com.github.alexthe666.citadel.animation.Animation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.ai.WaterLeapAI;

public class LivyatanEntity extends SwimmingDinosaurEntity
{
    public LivyatanEntity(Level world, EntityType<LivyatanEntity> type)
    {
        super(world, type, DinosaurHandler.LIVYATAN);
        this.target(CoelacanthEntity.class,OrthocerasEntity.class,CamerocerasEntity.class, EndocerasEntity.class,MawsoniaEntity.class, CrassigyrinusEntity.class, AsterocerasEntity.class, ParapuzosiaEntity.class, TitanisEntity.class, AlligatorGarEntity.class, MegapiranhaEntity.class, Squid.class, Cod.class, Dolphin.class, Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class, Tadpole.class,  Player.class, Animal.class, Villager.class, Mob.class, Goat.class);
//        this.addTask(0, new WaterLeapAI/*WaterYeetAI*/(this, 12, 1.2F));
    }
    public SoundEvent getSoundForAnimation(Animation animation) {
        switch (EntityAnimation.getAnimation(animation)) {
            case SPEAK:
                return SoundHandler.LIVYATAN_LIVING;
            case CALLING:
                return SoundHandler.LIVYATAN_CALL;
            case DYING:
                return SoundHandler.LIVYATAN_DEATH;
            case INJURED:
                return SoundHandler.LIVYATAN_HURT;
            case BEGGING:
                return SoundHandler.LIVYATAN_CALL;
            default:
                return null;
        }
    }
}

