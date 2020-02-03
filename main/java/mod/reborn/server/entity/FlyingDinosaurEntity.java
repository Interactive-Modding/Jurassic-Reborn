package mod.reborn.server.entity;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.block.entity.FeederBlockEntity;
import mod.reborn.server.block.machine.FeederBlock;
import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.entity.ai.navigation.DinosaurMoveHelper;
import mod.reborn.server.entity.ai.util.OnionTraverser;
import mod.reborn.server.entity.dinosaur.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.passive.EntityFlying;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.Path;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import mod.reborn.server.entity.ai.DinosaurWanderEntityAI;
import mod.reborn.server.entity.ai.util.MathUtils;

import java.util.Random;

public abstract class FlyingDinosaurEntity extends DinosaurEntity implements EntityFlying, IAnimals {

    private int ticksOnFloor;
    private int ticksInAir;
    private boolean takingOff;
    private int feederSearchTick;
    private BlockPos closestFeeder;
    public boolean shouldLand;

    public FlyingDinosaurEntity(World world) {
        super(world);
        this.moveHelper = new FlyingDinosaurEntity.FlyingMoveHelper();
        this.tasks.addTask(1, new FlyingDinosaurEntity.AIFlyLand());
        this.tasks.addTask(2, new FlyingDinosaurEntity.AIStartFlying());
        this.tasks.addTask(2, new FlyingDinosaurEntity.AIRandomFly());
        this.tasks.addTask(0, new FlyingDinosaurEntity.AIWander());
        this.tasks.addTask(2, new AILookAround());
        this.doesEatEggs(true);
        this.doTarget();
    }

    protected void doTarget(){
        this.target(LeptictidiumEntity.class, HypsilophodonEntity.class, MicroraptorEntity.class, MicroceratusEntity.class, CompsognathusEntity.class);
    }

    @Override
    public void onEntityUpdate() {

        setNoGravity(!isOnGround());
        if(this.getMetabolism().isStarving() || this.getMetabolism().isThirsty()) {
            this.shouldLand = true;
        }
        if(isOnGround()) {
            ticksInAir = 0;
            ticksOnFloor++;
        } else {
            ticksInAir++;
            ticksOnFloor = 0;
        }

        if(ticksInAir > 150) {
            this.takingOff = false;
        }

        rotationPitch = prevRotationPitch + (rotationPitch - prevRotationPitch);
        super.onEntityUpdate();
    }

    public boolean isOnGround() {
        return !this.world.getCollisionBoxes(this, this.getEntityBoundingBox().grow(0.24d)).isEmpty() && !takingOff || this.isDead || this.isCarcass();
    }

    public void startTakeOff() {
        takingOff = true;
    }

    @Override
    public int getHorizontalFaceSpeed() {
        return getVerticalFaceSpeed();
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if(isOnGround()) {
            super.travel(strafe, vertical, forward);
            return;
        }
        if (this.inWater()) {
            this.moveRelative(strafe, forward, 0.02F, 0F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.800000011920929D;
            this.motionY *= 0.800000011920929D;
            this.motionZ *= 0.800000011920929D;
        } else if (this.inLava()) {
            this.moveRelative(strafe, forward, 0.02F, 0F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.5D;
            this.motionY *= 0.5D;
            this.motionZ *= 0.5D;
        } else {
            float friction = 0.91F;

            if (this.onGround) {
                friction = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            float f3 = 0.16277136F / (friction * friction * friction);
            this.moveRelative(strafe, forward, this.onGround ? f3 * 0.1F : 0.02F, 0F);
            friction = 0.91F;

            if (this.onGround) {
                friction = this.world.getBlockState(new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.getEntityBoundingBox().minY) - 1, MathHelper.floor(this.posZ))).getBlock().slipperiness * 0.91F;
            }

            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= (double) friction;
            this.motionY *= (double) friction;
            this.motionZ *= (double) friction;
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double moveX = this.posX - this.prevPosX;
        double moveZ = this.posZ - this.prevPosZ;
        float dist = MathHelper.sqrt(moveX * moveX + moveZ * moveZ) * 4.0F;

        if (dist > 1.0F) {
            dist = 1.0F;
        }

        this.limbSwingAmount += (dist - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    @Override
    public boolean isOnLadder() {
        return false;
    }

    private boolean isCourseTraversable(Vec3d loc) {
        double distance = this.getPositionVector().distanceTo(loc);
        distance++;
        double d4 = (loc.x - this.posX) / distance;
        double d5 = (loc.y - this.posY) / distance;
        double d6 = (loc.z - this.posZ) / distance;
        AxisAlignedBB axisalignedbb = this.getCollisionBoundingBox();
        if(axisalignedbb == null) {
            axisalignedbb = this.getEntityBoundingBox();
        }
        for(int i = 1; (double)i < distance; ++i) {
            axisalignedbb.offset(d4, d5, d6);
            if(!this.world.getCollisionBoxes(this, axisalignedbb).isEmpty()) {
                return false;
            }
        }

        return true;
    }

    class AIStartFlying extends EntityAIBase {
        private final FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;


        public AIStartFlying() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            return dino.ticksOnFloor >= 220/*TODO: config this ?*/ && dino.isOnGround() && this.dino.rand.nextFloat() < 0.03F; //TODO: config this value
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            this.dino.startTakeOff();
            this.dino.setAnimation(EntityAnimation.FLYING.get());
        }


    }

    class AIRandomFly extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIRandomFly() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if(dino.isOnGround()) {
                return false;
            }
            EntityMoveHelper moveHelper = this.dino.getMoveHelper();
            if (!moveHelper.isUpdating()) {
                return true;
            } else {
                double moveX = moveHelper.getX() - this.dino.posX;
                double moveY = moveHelper.getY() - this.dino.posY;
                double moveZ = moveHelper.getZ() - this.dino.posZ;
                double distance = moveX * moveX + moveY * moveY + moveZ * moveZ;
                return distance < 3.0D || distance > 3600.0D;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {//TODO: Fix cosine issues. Not sure why they're happening. Maybe clash with diffrent ai or the task being run multiple times??
            Vec3d lookVec = new Vec3d(dino.getLookVec().x * 10D, dino.getLookVec().y * 10D, dino.getLookVec().z * 10D).add(new Vec3d(getPosition()));
            Random random = this.dino.getRNG();
            for(int i = 0; i < 100; i++) {
                double destinationX = this.dino.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                double destinationY = this.dino.posY + (double) ((random.nextFloat() * 2.0F - 1.0F) * 4.0F);
                double destinationZ = this.dino.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
                Vec3d vecPos = new Vec3d(destinationX, destinationY, destinationZ);
                if(dino.isCourseTraversable(vecPos) && Math.abs(MathUtils.cosineFromPoints(vecPos, lookVec, new Vec3d(getPosition()))) < 45D)
                {
                    this.dino.getLookHelper().setLookPosition(destinationX, destinationY, destinationZ, this.dino.getHorizontalFaceSpeed(), this.dino.getVerticalFaceSpeed());
                    this.dino.setAnimation(EntityAnimation.FLYING.get());
                    this.dino.getMoveHelper().setMoveTo(destinationX, destinationY, destinationZ, 2D);
                    return;
                }
            }
        }
    }

    class AIFlyLand extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIFlyLand() {
            this.setMutexBits(1);
        }

        @Override
        public boolean shouldExecute() {
            if(dino.ticksOnFloor <= 150 && dino.isOnGround() && this.dino.isInWater() && this.dino.isOverWater()) {
                return false;
            }
            EntityMoveHelper moveHelper = this.dino.getMoveHelper();
            if (!moveHelper.isUpdating() && dino.rand.nextFloat() < 0.1f || !moveHelper.isUpdating() && this.dino.shouldLand) {
                return true;
            } else {
                double moveX = moveHelper.getX() - this.dino.posX;
                double moveY = moveHelper.getY() - this.dino.posY;
                double moveZ = moveHelper.getZ() - this.dino.posZ;
                double distance = moveX * moveX + moveY * moveY + moveZ * moveZ;
                if(distance < 1.0D || distance > 3600.0D) {
                    return this.dino.world.getBlockState(this.dino.getPosition().down()).getMaterial() == Material.AIR && this.dino.getRNG().nextFloat() < 0.01f;//TODO: change float value
                }
            }
            return false;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return false;
        }

        @Override
        public void startExecuting() {
            Random random = this.dino.getRNG();
            double destinationX = this.dino.posX + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double destinationZ = this.dino.posZ + (double) ((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double destinationY = this.dino.world.getTopSolidOrLiquidBlock(new BlockPos(destinationX, 100, destinationZ)).getY();
            if(world.getBlockState(new BlockPos(destinationX, destinationY - 1, destinationZ)).getMaterial() != Material.AIR) {
                this.dino.getMoveHelper().setMoveTo(destinationX, destinationY - 1.8D, destinationZ, 2.0D);
                this.dino.setAnimation(EntityAnimation.ON_LAND.get());
            }
        }
    }

    class FlyingMoveHelper extends DinosaurMoveHelper {
        private FlyingDinosaurEntity parentEntity = FlyingDinosaurEntity.this;
        private int timer;

        public FlyingMoveHelper() {
            super(FlyingDinosaurEntity.this);
        }


        @Override
        public void onUpdateMoveHelper() {
            if(parentEntity.isOnGround()) {
                super.onUpdateMoveHelper();
                return;
            }
            if (this.action == EntityMoveHelper.Action.MOVE_TO) {
                double distanceX = this.posX - this.parentEntity.posX;
                double distanceY = this.posY - this.parentEntity.posY;
                double distanceZ = this.posZ - this.parentEntity.posZ;
                double distance = distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ;

                if (this.timer-- <= 0) {
                    this.timer += this.parentEntity.getRNG().nextInt(5) + 2;
                    distance = (double) MathHelper.sqrt(distance);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, distance)) {
                        this.parentEntity.motionX += distanceX / distance * this.speed * 0.1D;
                        this.parentEntity.motionY += distanceY / distance * this.speed * 0.1D;
                        this.parentEntity.motionZ += distanceZ / distance * this.speed * 0.1D;
                    } else {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
                if (distance < 2.5E-07) {
                    this.entity.setMoveForward(0.0F);
                    return;
                }
            }

        }

        private boolean isNotColliding(double x, double y, double z, double distance) {
            double d0 = (x - this.parentEntity.posX) / distance;
            double d1 = (y - this.parentEntity.posY) / distance;
            double d2 = (z - this.parentEntity.posZ) / distance;
            AxisAlignedBB bounds = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double) i < distance; ++i) {
                bounds = bounds.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, bounds).isEmpty()) {
                    return false;
                }
            }

            return true;
        }
    }

    public BlockPos getClosestFeeder() {
        if (this.ticksExisted - this.feederSearchTick > 200) {
            this.feederSearchTick = this.ticksExisted;
            OnionTraverser traverser = new OnionTraverser(this.getPosition(), 32);
            for (BlockPos pos : traverser) {
                IBlockState state = this.world.getBlockState(pos);
                if (state.getBlock() instanceof FeederBlock) {
                    TileEntity tile = this.world.getTileEntity(pos);
                    if (tile instanceof FeederBlockEntity) {
                        FeederBlockEntity feeder = (FeederBlockEntity) tile;
                        if (feeder.canFeedDinosaur(this) && feeder.getFeeding() == null && feeder.openAnimation == 0 && this.isOnGround()) {
                            Path path = this.getNavigator().getPathToPos(pos);
                            if (path != null && path.getCurrentPathLength() != 0) {
                                return this.closestFeeder = pos;
                            }
                        }
                    }
                }
            }
        }
        return this.closestFeeder;
    }

    class AILookAround extends EntityAIBase {
        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AILookAround() {
            this.setMutexBits(2);
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void updateTask() {
            if (this.dino.getAttackTarget() == null) {
                this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(this.dino.motionX, this.dino.motionZ)) * 180.0F / (float) Math.PI;
            } else {
                EntityLivingBase attackTarget = this.dino.getAttackTarget();
                double maxDistance = 64.0D;

                if (attackTarget.getDistance(this.dino) < maxDistance * maxDistance) {
                    double diffX = attackTarget.posX - this.dino.posX;
                    double diffZ = attackTarget.posZ - this.dino.posZ;
                    this.dino.renderYawOffset = this.dino.rotationYaw = -((float) Math.atan2(diffX, diffZ)) * 180.0F / (float) Math.PI;
                }
            }
        }
    }

    class AIWander extends DinosaurWanderEntityAI {

        private FlyingDinosaurEntity dino = FlyingDinosaurEntity.this;

        public AIWander() {
            super(FlyingDinosaurEntity.this, 0.8D, 10, RebornConfig.ENTITIES.dinosaurWalkingRadius);
        }

        @Override
        public boolean shouldExecute()
        {
            if(!this.dino.isOnGround()) {
                return false;
            }
            return super.shouldExecute();
        }

    }
}
