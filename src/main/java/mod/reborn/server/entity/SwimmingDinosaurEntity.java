package mod.reborn.server.entity;

import net.minecraft.block.material.Material;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import mod.reborn.server.entity.ai.MoveUnderwaterEntityAI;


public abstract class SwimmingDinosaurEntity extends DinosaurEntity {
    private boolean blocked;

    public SwimmingDinosaurEntity(World world) {
        super(world);
        setSize(1, 1);
        blocked = false;
        this.moveHelper = new SwimmingDinosaurEntity.SwimmingMoveHelper();
        this.tasks.addTask(1, new MoveUnderwaterEntityAI(this));
        this.navigator = new PathNavigateSwimmer(this, world);
    }

    @Override
    public void onEntityUpdate() {
        int air = this.getAir();
        super.onEntityUpdate();

        if (this.isEntityAlive() && !this.isInWater()) {
            --air;
            this.setAir(air);

            if (this.getAir() == -20) {
                this.setAir(0);
                this.attackEntityFrom(DamageSource.DROWN, 2.0F);
            }
        } else {
            this.setAir(300);
        }
    }

    public boolean isNotColliding()
    {
        return this.world.checkNoEntityCollision(this.getEntityBoundingBox(), this);
    }

    @Override
    protected boolean canTriggerWalking() {
        return false;
    }

    @Override
    public boolean isMovementBlocked() {
        return this.isCarcass() || this.isSleeping() || blocked;
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.5F;
    }

    /*TODO: make sure this works */
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

    @Override
    public boolean isInWater() {
        return this.inWater || this.world.handleMaterialAcceleration(this.getEntityBoundingBox().grow(0.0D, -0.5D, 0.0D).shrink(0.001D), Material.WATER, this);
    }

    class SwimmingMoveHelper extends EntityMoveHelper {
        private final SwimmingDinosaurEntity swimmingEntity = SwimmingDinosaurEntity.this;

        public SwimmingMoveHelper() {
            super(SwimmingDinosaurEntity.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.swimmingEntity.getNavigator().noPath()) {
                double distanceX = this.posX - this.swimmingEntity.posX;
                double distanceY = this.posY - this.swimmingEntity.posY;
                double distanceZ = this.posZ - this.swimmingEntity.posZ;
                double distance = Math.abs(distanceX * distanceX + distanceY * distanceY + distanceZ * distanceZ);
                distance = MathHelper.sqrt(distance);
                distanceY /= distance;
                float f = (float) (Math.atan2(distanceZ, distanceX) * 180.0D / Math.PI) - 90.0F;
                this.swimmingEntity.rotationYaw = this.limitAngle(this.swimmingEntity.rotationYaw, f, 30);
                this.swimmingEntity.setAIMoveSpeed((float) (this.swimmingEntity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue() * this.speed));
                this.swimmingEntity.motionY += (double) this.swimmingEntity.getAIMoveSpeed() * distanceY * 0.05D;
            } else {
                this.swimmingEntity.setAIMoveSpeed(0.0F);
            }
        }
    }
}