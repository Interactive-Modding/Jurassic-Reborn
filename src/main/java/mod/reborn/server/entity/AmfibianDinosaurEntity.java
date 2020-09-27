package mod.reborn.server.entity;

import mod.reborn.server.entity.ai.DinosaurWanderEntityAI;
import mod.reborn.server.entity.ai.Mutex;
import mod.reborn.server.entity.ai.navigation.DinosaurMoveHelper;
import mod.reborn.server.entity.animal.ai.EntityAIFindWater;
import mod.reborn.server.entity.animal.ai.EntityAIWanderNearWater;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public abstract class AmfibianDinosaurEntity extends DinosaurEntity {
    private boolean getOut = false;
    private boolean getInWater = false;
    private PathNavigate navigateSwimmer = new PathNavigateSwimmer(this,world);
    private PathNavigate navigateLand = new PathNavigateGround(this, world);
    private int waterTicks;
    private int landTicks;
    public AmfibianDinosaurEntity(World world) {
        super(world);
        this.tasks.removeTask(new DinosaurWanderEntityAI(this, 0.8D, 2, 10));
        this.tasks.addTask(10, new EntityAIFindWater(this, 1, 2, 30));
        this.tasks.addTask(10, new Wander(this,2, 10, 2));
        this.tasks.addTask(5, new MoveUnderwaterEntityAI(this));
        this.moveHelper = new AmfibianDinosaurEntity.SwimmingMoveHelper();
    }

    @Override
    public void onEntityUpdate() {
        int air = this.getAir();
        if(this.isEntityAlive()) {
            if (this.isInWater()) {
                waterTicks++;
                this.navigator = navigateSwimmer;
                if (waterTicks >= 120) {
                    this.getOut = true;
                }
                getInWater = false;
                this.setAir(300);
            } else {
                --air;
                this.setAir(air);
                if(this.getAir() <= 40) {
                    getInWater = true;
                }

                if (this.getAir() <= -20) {
                    this.setAir(0);
                    this.attackEntityFrom(DamageSource.DROWN, 2.0F);
                }
                waterTicks = 0;
                getOut = false;
            }

            if(this.getOut) {
                this.navigator = navigateLand;
            }

            if(!this.isInWater()) {
                landTicks++;
                if(landTicks > 50) {
                    this.getInWater = true;
                }
            } else {
                landTicks=0;
            }
        }



        super.onEntityUpdate();
    }

    @Override
    public void travel(float strafe, float vertical, float forward) {
        if (this.isServerWorld() && this.isInWater() && !this.isCarcass() && !this.getOut) {
            this.moveRelative(strafe, vertical, forward, 0.1F);
            this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
            this.motionX *= 0.7D;
            this.motionY *= 0.7D;
            this.motionZ *= 0.7D;
        } else {
            super.travel(strafe, vertical, forward);
        }
    }

    class SwimmingMoveHelper extends DinosaurMoveHelper {
        private final AmfibianDinosaurEntity swimmingEntity = AmfibianDinosaurEntity.this;

        public SwimmingMoveHelper() {
            super(AmfibianDinosaurEntity.this);
        }

        @Override
        public void onUpdateMoveHelper() {
            if (this.action == EntityMoveHelper.Action.MOVE_TO && !this.swimmingEntity.getNavigator().noPath() && this.swimmingEntity.isInWater() && this.swimmingEntity.getOut) {
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
                super.onUpdateMoveHelper();
            }
        }
    }

    class MoveUnderwaterEntityAI extends EntityAIBase {
        private AmfibianDinosaurEntity swimmingEntity;
        private double xPosition;
        private double yPosition;
        private double zPosition;

        public MoveUnderwaterEntityAI(AmfibianDinosaurEntity entity) {
            this.swimmingEntity = entity;
            this.setMutexBits(Mutex.MOVEMENT);
        }

        @Override
        public boolean shouldExecute() {
            if (this.swimmingEntity.getRNG().nextFloat() < 0.50 && this.swimmingEntity.isBusy()) {
                return false;
            }
            Vec3d target;
            if(swimmingEntity.getOut) {
                target = RandomPositionGenerator.getLandPos(this.swimmingEntity, 6, 6);
            } else {
                target = RandomPositionGenerator.findRandomTarget(this.swimmingEntity, 6, 6);
            }

            if (target == null) {
                return false;
            } else {
                this.xPosition = target.x;
                this.yPosition = target.y;
                this.zPosition = target.z;
                return true;
            }
        }

        @Override
        public boolean shouldContinueExecuting() {
            return !this.swimmingEntity.getNavigator().noPath();
        }

        @Override
        public void startExecuting() {
            this.swimmingEntity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, 1.0D);
        }

        @Override
        public boolean isInterruptible() {
            return false;
        }
    }

    class Wander extends EntityAIWanderNearWater {
        public final AmfibianDinosaurEntity creature;
        public Wander(AmfibianDinosaurEntity creatureIn, double speedIn, int chance, int walkradius) {
            super(creatureIn, speedIn, chance, walkradius);
            this.creature = creatureIn;
        }

        @Override
        public boolean shouldExecute() {
            if(creature.getInWater) {
                return false;
            }
            return super.shouldExecute();
        }
    }
}
