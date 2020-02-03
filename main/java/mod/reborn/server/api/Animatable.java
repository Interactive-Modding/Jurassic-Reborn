package mod.reborn.server.api;

import mod.reborn.client.model.animation.PoseHandler;
import net.ilexiconn.llibrary.server.animation.IAnimatedEntity;
import net.minecraft.entity.EntityLivingBase;
import mod.reborn.server.entity.GrowthStage;

public interface Animatable extends IAnimatedEntity {
    boolean isCarcass();
    boolean isMoving();
    boolean isClimbing();
    boolean inWater();
    boolean isSwimming();
    boolean isRunning();
    boolean inLava();
    boolean canUseGrowthStage(GrowthStage growthStage);
    boolean isMarineCreature();
    boolean shouldUseInertia();
    boolean isSleeping();
    GrowthStage getGrowthStage();
    <ENTITY extends EntityLivingBase & Animatable> PoseHandler<ENTITY> getPoseHandler();
}
