package mod.reborn.server.entity.animal;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.client.model.animation.PoseHandler;
import mod.reborn.server.api.Animatable;
import mod.reborn.server.entity.GrowthStage;
import mod.reborn.server.entity.ai.MateEntityAI;
import mod.reborn.server.entity.ai.SmartBodyHelper;
import mod.reborn.server.entity.animal.ai.MoveUnderwaterEntityAI;
import mod.reborn.server.item.ItemHandler;
import net.ilexiconn.llibrary.client.model.tools.ChainBuffer;
import net.ilexiconn.llibrary.server.animation.Animation;
import net.ilexiconn.llibrary.server.animation.AnimationHandler;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Biomes;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityShark extends EntityAnimal implements Animatable, IEntityAdditionalSpawnData, IMob {
    public static final PoseHandler<EntityShark> SHARK_POSE_HANDLER = new PoseHandler<>("shark", Lists.newArrayList(GrowthStage.ADULT));

    private static final DataParameter<Boolean> WATCHER_IS_RUNNING = EntityDataManager.createKey(EntityShark.class, DataSerializers.BOOLEAN);
    public ChainBuffer tailBuffer;

    private Animation animation;
    private int animationTick;
    private int animationLength;
    private boolean inLava;
    public boolean isSpawnedByEgg;

    public EntityShark(World world) {
        super(world);
        this.setSize(1.6F, 0.8F);
        this.stepHeight = 1.0F;
        this.animationTick = 0;
        this.preventEntitySpawning = false;
        this.setPathPriority(PathNodeType.WATER, 0);
        this.moveHelper = new SwimmingMoveHelper();
        this.navigator = new PathNavigateSwimmer(this, world);
        this.setAnimation(EntityAnimation.IDLE.get());
        this.isSpawnedByEgg = false;
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        compound.setBoolean("egg_spawned", this.isSpawnedByEgg);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        this.isSpawnedByEgg = compound.getBoolean("egg_spawned");
    }

    @Override
    public EntityAgeable createChild(EntityAgeable ageable) {
        return new EntityShark(world);
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
        int i = 0;

        if (entityIn instanceof EntityLivingBase)
        {
            f += EnchantmentHelper.getModifierForCreature(this.getHeldItemMainhand(), ((EntityLivingBase)entityIn).getCreatureAttribute());
            i += EnchantmentHelper.getKnockbackModifier(this);
        }

        boolean flag = entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), f);

        if (flag)
        {
            if (i > 0 && entityIn instanceof EntityLivingBase)
            {
                ((EntityLivingBase)entityIn).knockBack(this, (float)i * 0.5F, (double)MathHelper.sin(this.rotationYaw * 0.017453292F), (double)(-MathHelper.cos(this.rotationYaw * 0.017453292F)));
                this.motionX *= 0.6D;
                this.motionZ *= 0.6D;
            }

            int j = EnchantmentHelper.getFireAspectModifier(this);

            if (j > 0)
            {
                entityIn.setFire(j * 4);
            }

            if (entityIn instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)entityIn;
                ItemStack itemstack = this.getHeldItemMainhand();
                ItemStack itemstack1 = entityplayer.isHandActive() ? entityplayer.getActiveItemStack() : ItemStack.EMPTY;

                if (!itemstack.isEmpty() && !itemstack1.isEmpty() && itemstack.getItem().canDisableShield(itemstack, itemstack1, entityplayer, this) && itemstack1.getItem().isShield(itemstack1, entityplayer))
                {
                    float f1 = 0.25F + (float)EnchantmentHelper.getEfficiencyModifier(this) * 0.05F;

                    if (this.rand.nextFloat() < f1)
                    {
                        entityplayer.getCooldownTracker().setCooldown(itemstack1.getItem(), 100);
                        this.world.setEntityState(entityplayer, (byte)30);
                    }
                }
            }

            this.applyEnchantments(this, entityIn);
        }

        return flag;
    }

    @Override
    protected void handleJumpWater() {
    }

    @Override
    protected float getWaterSlowDown() {
        return 1F;
    }

    @Override
    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier) {
        this.dropItem(this.isBurning() ? ItemHandler.SHARK_MEAT_COOKED : ItemHandler.SHARK_MEAT_RAW, this.rand.nextInt(2) + 1);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(15.0);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
    }

    protected void applyEntityAI()
    {
        this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, EntityPlayer.class));
        this.targetTasks.addTask(2, new EntityAINearestAttackableTarget<>(this, GoatEntity.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntityCrab.class, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget<>(this, EntityPlayer.class, false));
        this.targetTasks.addTask(3, new EntityAINearestAttackableTarget<>(this, EntitySquid.class, false));
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
        return this.isInLava() || this.isInWater();
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
        if (!this.world.isRemote) {
            this.world.getDifficulty();
        }
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
    public int getMaxSpawnedInChunk()
    {
        return 1;
    }

    @Override
    public boolean getCanSpawnHere()
    {
        return this.posY > 45.0D && this.posY < (double)this.world.getSeaLevel();
    }

    public boolean isNotColliding()
    {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
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
        return SHARK_POSE_HANDLER;
    }

    public boolean canBreatheUnderwater()
    {
        return true;
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
    public boolean attackEntityFrom(DamageSource source, float amount) {
        return super.attackEntityFrom(source, amount);
    }

    @Override
    public void onEntityUpdate() {
        int i = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater())
        {
            --i;
            this.setAir(i);

            if (this.getAir() == -20)
            {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        }
        else
        {
            this.setAir(300);
        }
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater() && !this.isCarcass()) {
            this.moveRelative(strafe, vertical, forward, 0.1F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.7D;
            this.motionY *= 0.7D;
            this.motionZ *= 0.7D;
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    class SwimmingMoveHelper extends EntityMoveHelper {
        private final EntityShark swimmingEntity = EntityShark.this;

        public SwimmingMoveHelper() {
            super(EntityShark.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.swimmingEntity.getNavigator().noPath() && this.swimmingEntity.isInWater()) {
                double distanceX = this.posX - this.swimmingEntity.posX;
                double distanceY = this.posY - this.swimmingEntity.posY;
                double distanceZ = this.posZ - this.swimmingEntity.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distance = MathHelper.sqrt(distance);
                distanceY /= distance;
                float f = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.swimmingEntity.rotationYaw = this.limitAngle(this.swimmingEntity.rotationYaw, f, 30.0F);
                this.swimmingEntity.setAIMoveSpeed((float) (this.swimmingEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * this.speed));
                this.swimmingEntity.motionY += (double) this.swimmingEntity.getAIMoveSpeed() * distanceY * 0.05D;
            } else {
                this.swimmingEntity.setAIMoveSpeed(0.0F);
            }
        }
    }

    public boolean isPushedByWater() {
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return stack.getItem().equals(Items.FISH);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(1, new MoveUnderwaterEntityAI(this));
        this.tasks.addTask(0, new EntityAIAttackMelee(this, 1.6D, true));
        this.tasks.addTask(0, new EntityAIMate(this, 1));
        this.applyEntityAI();
    }
}

