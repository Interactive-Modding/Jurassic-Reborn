package net.vit.jurassicreborn.common.entities.EntityUtils;


import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import net.vit.jurassicreborn.client.render.entity.animation.PoseHandler;
import net.minecraft.world.entity.LivingEntity;

public interface Animatable extends IAnimatedEntity {
    default boolean isCarcass(){
        return false;
    }
    default boolean isMoving(){
        return false;
    }
    default boolean isClimbing(){
        return false;
    }
    default boolean inWater(){return false;}
    default boolean isSwimming(){
        return false;
    }
    default boolean isRunning(){
        return false;
    }
    default boolean inLava(){
        return false;
    }
    default boolean canUseGrowthStage(GrowthStage growthStage){
        return false;
    }
    default boolean isMarineCreature(){
        return false;
    }
    default boolean shouldUseInertia(){
        return true;
    }
    default boolean isSleeping(){
        return false;
    }
    GrowthStage getGrowthStage();
    <ENTITY extends LivingEntity & Animatable> PoseHandler<ENTITY> getPoseHandler();
}