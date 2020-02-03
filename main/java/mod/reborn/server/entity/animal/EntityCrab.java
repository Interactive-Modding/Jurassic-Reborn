package mod.reborn.server.entity.animal;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.PoseHandler;
import mod.reborn.server.api.Animatable;
import mod.reborn.server.entity.GrowthStage;
import mod.reborn.server.entity.ai.*;
import mod.reborn.server.entity.animal.ai.EntityAIFindWater;
import mod.reborn.server.entity.animal.ai.EntityAIWanderNearWater;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;

public class EntityCrab extends EntityAnimal implements Animatable, IEntityAdditionalSpawnData {
    public static final PoseHandler<EntityCrab> CRAB_POSE_HANDLER = new PoseHandler<>("crab", Lists.newArrayList(GrowthStage.ADULT));

    private static final DataParameter<Boolean> WATCHER_IS_RUNNING = EntityDataManager.createKey(EntityCrab.class, DataSerializers.BOOLEAN);

    private Animation animation;
    private int animationTick;
    private int animationLength;
    private boolean inLava;
    private boolean alternative;

    public EntityCrab(World world) {
        super(world);
        this.setSize(0.4F, 0.3F);
        this.stepHeight = 1.0F;
        this.animationTick = 0;
        this.setAnimation(EntityAnimation.IDLE.get());
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return null;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(10, new EntityAIFindWater(this, 1, 10, 10));
        this.tasks.addTask(10, new EntityAIWanderNearWater(this, 1, 5, 2));
        this.tasks.addTask(8, new EntityAIAvoidEntity<>(this, EntityShark.class, 9.0F, 1.0F, 1.45F));
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(7.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(WATCHER_IS_RUNNING, false);
    }

    @Override
    protected EntityBodyHelper createBodyHelper() {
        return new SmartBodyHelper(this);
    }

    @Override
    public boolean isCarcass() {
        return false;
    }

    @Override
    public boolean isMoving() {
        float deltaX = (float) (this.posX - this.prevPosX);
        float deltaZ = (float) (this.posZ - this.prevPosZ);
        return deltaX * deltaX + deltaZ * deltaZ > 0.001F;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    public boolean isSwimming() {
        return this.isInLava();
    }

    @Override
    public boolean isRunning() {
        return this.dataManager.get(WATCHER_IS_RUNNING);
    }

    @Override
    public boolean inWater() {
        return this.isInWater();
    }

    @Override
    public boolean inLava() {
        return this.inLava;
    }

    @Override
    public boolean canUseGrowthStage(GrowthStage growthStage) {
        return growthStage == GrowthStage.ADULT;
    }

    @Override
    public boolean isMarineCreature() {
        return true;
    }

    @Override
    public boolean shouldUseInertia() {
        return true;
    }

    @Override
    public boolean isSleeping() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.ticksExisted % 10 == 0) {
            this.inLava = this.isInLava();
        }
        if (this.animation != null && this.animation != EntityAnimation.IDLE.get()) {
            boolean shouldHold = EntityAnimation.getAnimation(this.animation).shouldHold();
            if (this.animationTick < this.animationLength) {
                this.animationTick++;
            } else if (!shouldHold) {
                this.animationTick = 0;
                this.setAnimation(EntityAnimation.IDLE.get());
            } else {
                this.animationTick = this.animationLength - 1;
            }
        }
        if (!this.world.isRemote) {
            this.dataManager.set(WATCHER_IS_RUNNING, this.getAIMoveSpeed() > this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
        }
    }

    @Override
    public Animation[] getAnimations() {
        return EntityAnimation.getAnimations();
    }

    @Override
    public Animation getAnimation() {
        return this.animation;
    }

    @Override
    public GrowthStage getGrowthStage() {
        return GrowthStage.ADULT;
    }

    @Override
    public PoseHandler getPoseHandler() {
        return CRAB_POSE_HANDLER;
    }

    @Override
    public int getAnimationTick() {
        return this.animationTick;
    }

    @Override
    public void setAnimationTick(int tick) {
        this.animationTick = tick;
    }


    @Override
    public void setAnimation(Animation newAnimation) {
        Animation oldAnimation = this.animation;
        this.animation = newAnimation;
        if (oldAnimation != newAnimation) {
            this.animationTick = 0;
            this.animationLength = (int) this.getPoseHandler().getAnimationLength(this.animation, this.getGrowthStage());
            AnimationHandler.INSTANCE.sendAnimationMessage(this, newAnimation);
        }
    }


    @Override
    public void writeSpawnData(ByteBuf buffer) {

    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {

    }

    @Override
    public void onEntityUpdate() {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater()) {
            --i;
            this.setAir(i);
        } else {
            this.setAir(300);
        }
    }

    @Override
    public void moveRelative(float strafe, float up, float forward, float friction)
    {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt(f);
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.motionX += (double)(strafe * f2 - forward * f1);
            this.motionY += (double)up;
            this.motionZ += (double)(forward * f2 + strafe * f1);
        }
    }

    @Override
    protected void handleJumpWater() {
    }

    public boolean isPushedByWater() {
        return false;
    }
    public Type getType() {
        return this.alternative ? Type.ALTERNATIVE : Type.CRAB;
    }

    public enum Type {
        CRAB,
        ALTERNATIVE
    }
}

