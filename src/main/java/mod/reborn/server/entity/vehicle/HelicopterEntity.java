package mod.reborn.server.entity.vehicle;

import mod.reborn.client.particle.HelicopterEngineParticle;
import net.ilexiconn.llibrary.LLibrary;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import mod.reborn.RebornMod;
import mod.reborn.client.particle.HelicopterGroundParticle;
import mod.reborn.client.particle.WashingParticle;
import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.client.render.RenderingHandler;
import mod.reborn.server.entity.ai.util.InterpValue;
import mod.reborn.server.event.KeyBindingHandler;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.util.MutableVec3;

import java.util.List;

import javax.annotation.Nonnull;

public abstract class HelicopterEntity extends VehicleEntity {

    private static final byte UPWARD = 0b010000;
    private static final byte DOWNWARD = 0b100000;
    private int rotationControl = 0;
    public float gearLift;
    public boolean shouldGearLift = true;
    private final InterpValue rotationYawInterp = new InterpValue(this, 4f);
    public boolean isFlying;
    public final InterpValue interpRotationPitch = new InterpValue(this, 0.25D);
    public final InterpValue interpRotationRoll = new InterpValue(this, 0.25D);
    protected MutableVec3 direction;
    private final int MAX_MOVEMENT_ROTATION = 15;
    private boolean shouldFallDamage;
    public double rotAmount = 0D;
    private Vec3d prevInAirPos;
    private float damageAmount;
    private MutableBlockPos mb = new MutableBlockPos();
    protected boolean lockOn;
    protected int blastHeight = 6;
    @SideOnly(Side.CLIENT)

    private float currentEngineSpeed = 0;
    protected float torque;
    protected float yawRoationAcceleration = 0;

    private float shakingDirection = 0;
    protected ResourceLocation warnignSoundResource;
    private int warningDelay = 0;

    /*
     * Technical specifications
     */
    protected final int enginePower; // In PS
    protected final int engineSpeed; // In rotations per minute(recommended range: 1 - 300)
    protected final int rotorLength; // In blocks
    protected final int weight; // In kilograms
    private final float physicalWidth; // In blocks
    protected final float physicalHeight; // In blocks
    private final float physicalDepth; // In blocks
    protected final float qualityGrade = 0.75f;
    protected boolean simpleControle;

    /*
     * =============================================================================
     * Disclaimer: Do not take the formulas from this code for an physical project,
     * they do not fulfill the academical standard you need and are only working for
     * hobby helicopter constructors(as they came out of model helicopter
     * constructing)
     * =============================================================================
     */

    /**
     * widthIn, heightIn, depthIn in blocks; enginePower in PS; engineSpeedIn in
     * rotations per minute; weightIn in kilograms; rotorLengthIn in blocks
     */
    public HelicopterEntity(World worldIn, float widthIn, float heightIn, float depthIn, int enginePowerIn, int engineSpeedIn, int weightIn, int rotorLengthIn) {
        super(worldIn);

        this.physicalWidth = widthIn;
        this.physicalHeight = heightIn;
        this.physicalDepth = depthIn;
        this.setEntityBoundingBox(new AxisAlignedBB(0, 0, 0, this.physicalWidth, this.physicalHeight, this.physicalDepth));
        this.setSize((float) this.physicalDepth, (float) this.physicalHeight);
        this.enginePower = (int) ((float) enginePowerIn * 735.5f);
        this.engineSpeed = engineSpeedIn;
        this.weight = weightIn;
        this.rotorLength = rotorLengthIn;
        this.torque = this.computeTorque();
        this.speedModifier = 1.5f;
        this.isFlying = false;
        this.direction = new MutableVec3(0, 1, 0);
        this.simpleControle = true;
        this.lockOn = true;
        this.warnignSoundResource = new ResourceLocation(RebornMod.MODID, "helicopter_warning");
    }

    public boolean upward() {
        return this.getStateBit(UPWARD);
    }

    public boolean downward() {
        return this.getStateBit(DOWNWARD);
    }

    @Override
    public void startSound() {
        ClientProxy.playHelicopterSound(this);
    }

    public void upward(boolean upward) {
        this.setStateBit(UPWARD, upward);

    }

    public void downward(boolean downward) {
        this.setStateBit(DOWNWARD, downward);
    }

    @Override
    protected boolean shouldStopUpdates() {
        return false;
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
    }

    @Override
    protected void doBlockCollisions() {
        super.doBlockCollisions();
    }

    public boolean isController(EntityPlayer e) {
        if ((!this.getIfExists(0, false).equals("") && this.getIfExists(0, false).equals(Integer.toString(e.getEntityId())))) {
            return true;
        }
        return false;
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!world.isRemote) {
            if (!player.isSneaking() && !(player.getRidingEntity() == this)) {
                player.startRiding(this);
            }
        }
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected void handleControl() {
        if (this.isController(Minecraft.getMinecraft().player)) {
            if (this.isInWater()) {
                this.upward(false);
                this.downward(false);
            } else {
                this.upward(KeyBindingHandler.HELICOPTER_UP.isKeyDown());
                this.downward(KeyBindingHandler.HELICOPTER_DOWN.isKeyDown());
                this.handleKeyEnableAutoPilot(KeyBindingHandler.HELICOPTER_AUTOPILOT.isPressed());
                this.handleKeyLock(KeyBindingHandler.HELICOPTER_LOCK.isPressed());
                this.rotateLeft(KeyBindingHandler.HELICOPTER_ROTATE_LEFT.isKeyDown());
                this.rotateRight(KeyBindingHandler.HELICOPTER_ROTATE_RIGHT.isKeyDown());
            }
            super.handleControl();
        }
        if (this.isPassenger(Minecraft.getMinecraft().player)) {
            this.increaseThirdPersonViewDistance(KeyBindingHandler.HELICOPTER_THIRD_PERSON_VIEW_ZOOM_OUT.isKeyDown());
            this.decreaseThirdPersonViewDistance(KeyBindingHandler.HELICOPTER_THIRD_PERSON_VIEW_ZOOM_IN.isKeyDown());
        }
    }

    @Override
    public void fall(float distance, float damageMultiplier) {
        if (!world.isRemote && !isFlying) {
            float damage = MathHelper.ceil((distance - 3F) * damageMultiplier);
            if (damage > 0) {

                this.setHealth(this.getHealth() - (float) (damage * 1.25F));

                if (this.getHealth() <= 0) {
                    this.setDead();
                    if (this.world.getGameRules().getBoolean("doEntityDrops")) {
                        this.dropItems();
                    }
                }
            }
        }

        if (this.world.isRemote && !isFlying) {
            float damage = MathHelper.ceil((distance - 3F) * damageMultiplier);
            if (damage > 0) {
                float tmp = this.getHealth() - (float) (damage * 1.25F);
                if (tmp <= 0) {
                    this.playHelicopterExplosion();
                }
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        for (int i = 0; i < this.seats.length; i++) {
            if (passenger.equals(this.getEntityInSeat(i))) {
                passenger.noClip = false;
                break;
            }
        }
        if (this.world.isRemote) {
            resetThirdPersonViewDistance();
        }
    }

    @Override
    public void onEntityUpdate() {
        if (world.isRemote) {
            this.isFlying = this.hasNoGravity();
        }
        super.onEntityUpdate();

        if (!this.isInWater()) {
            final float dist = this.getDistanceToGround();

            if (!world.isRemote) {
                for (int i = 0; i < this.seats.length; i++) {
                    final Entity e = this.getEntityInSeat(i);
                    if (e != null) {
                        e.fallDistance = 0;
                    }
                }
            }
            if (forward() && this.isFlying) {
                this.pitch += this.computeThrottleUpDown() / 2;
            } else if (this.backward() && this.isFlying) {
                this.pitch -= this.computeThrottleUpDown() / 2;
            } else if (this.simpleControle && this.isFlying && this.lockOn && !this.isLowHealth()) {
                if (Math.abs(this.pitch) > 0 && Math.abs(this.pitch) < 1.0f) {
                    this.pitch = 0;
                } else if (this.pitch < 0f) {
                    this.pitch += this.computeThrottleUpDown() / 2;
                } else if (this.pitch > 0f) {
                    this.pitch -= this.computeThrottleUpDown() / 2;
                }
            }
            if (this.left() && !this.right() && this.isFlying) {
                this.roll += this.computeThrottleUpDown();
            } else if (this.right() && !this.left() && this.isFlying) {
                this.roll -= this.computeThrottleUpDown();
            } else if (this.simpleControle && this.isFlying && this.lockOn && !this.isLowHealth()) {
                if (Math.abs(this.roll) > 0 && Math.abs(this.roll) < 1.0f) {
                    this.roll = 0;
                } else if (this.roll < 0f) {
                    this.roll += this.computeThrottleUpDown();
                } else if (this.roll > 0f) {
                    this.roll -= this.computeThrottleUpDown();
                }
            }

            if (this.rotateLeft() && !this.left() && !this.right() && this.isFlying && dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
                this.yawRoationAcceleration -= 0.1f;
            } else if (this.rotateRight() && !this.left() && !this.right() && this.isFlying && dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
                this.yawRoationAcceleration += 0.1f;
            } else if (this.simpleControle && this.yawRoationAcceleration != 0 && dist > 0.1f && this.getCurrentEngineSpeed() > 10) {
                this.yawRoationAcceleration += (this.yawRoationAcceleration < 0 ? 0.05f : -0.05f);
            }
            if (this.pitch > 180) {
                this.pitch = -180 + (this.pitch - 180);
            } else if (this.pitch < -180) {
                this.pitch = this.pitch + 360;
            }
            if (this.pitch > this.computeMaxMovementRotation(dist)) {
                this.pitch -= this.computeThrottleUpDown() / 2;
            } else if (this.pitch < -this.computeMaxMovementRotation(dist)) {
                this.pitch += this.computeThrottleUpDown() / 2;
            }
            if (this.roll >= this.computeMaxMovementRotation(dist)) {
                this.roll = this.computeMaxMovementRotation(dist);
            } else if (this.roll <= -this.computeMaxMovementRotation(dist)) {
                this.roll = -this.computeMaxMovementRotation(dist);
            }
            if (this.yawRoationAcceleration > this.computeThrottleUpDown() * 2 && !this.isLowHealth()) {
                this.yawRoationAcceleration = this.computeThrottleUpDown() * 2;
            } else if (this.yawRoationAcceleration < -this.computeThrottleUpDown() * 2 && !this.isLowHealth()) {
                this.yawRoationAcceleration = -this.computeThrottleUpDown() * 2;
            } else if (dist <= 0.1 && this.yawRoationAcceleration != 0) {
                this.yawRoationAcceleration = 0;
            }
            this.updateHelicopterCrash(dist);
            this.rotationYaw += this.yawRoationAcceleration;

            final float requiredSpeedForHovering = this.computeRequiredEngineSpeedForHover();
            if ((this.shouldAdjustEngineSpeedByHorizontalControls(requiredSpeedForHovering) || this.shouldAdjustEngineSpeedWithoutHorizontalControls(requiredSpeedForHovering)) && this.simpleControle && this.isFlying
                    && this.getControllingPassenger() != null && Math.abs(this.getCurrentEngineSpeed() - requiredSpeedForHovering) <= 8f && !this.isLowHealth()) {
                if (Math.abs(this.getCurrentEngineSpeed() - requiredSpeedForHovering) <= 2) {
                    this.setCurrentEngineSpeed(requiredSpeedForHovering);
                } else if (this.getCurrentEngineSpeed() > requiredSpeedForHovering) {
                    this.changeCurrentEngineSpeed(this.computeRequiredEngineSpeedForHover() - this.getCurrentEngineSpeed());
                } else if (this.getCurrentEngineSpeed() < requiredSpeedForHovering) {
                    this.changeCurrentEngineSpeed(this.computeRequiredEngineSpeedForHover() - this.getCurrentEngineSpeed());
                }
            }

            rotationYawInterp.reset(this.rotationYaw - 180D);
            this.interpRotationPitch.setTarget(this.direction.zCoord * -30D);
            this.interpRotationRoll.setTarget(this.direction.xCoord * 20D);
            if ((this.getControllingPassenger() != null) && !this.isLowHealth()) {
                if (this.upward()) {
                    this.changeCurrentEngineSpeed(this.computeThrottleUpDown());
                    this.setFlying();
                } else if (this.downward() && this.isFlying) {
                    this.shouldFallDamage = false;
                    this.changeCurrentEngineSpeed(-this.computeThrottleUpDown());
                } else {
                    if (!this.isFlying) {
                        this.setNoGravity(false);
                        for (int i = 0; i < this.seats.length; i++) {
                            Entity e = this.getEntityInSeat(i);
                            if (e != null) {
                                e.setNoGravity(false);
                            }
                        }
                        if (this.simpleControle) {
                            if (this.getCurrentEngineSpeed() > 0) {
                                this.changeCurrentEngineSpeed(-1);
                            }
                        }
                    } else {
                        if (this.simpleControle) {
                            if (Math.abs(this.getCurrentEngineSpeed() - requiredSpeedForHovering) <= 2) {
                                this.setCurrentEngineSpeed(requiredSpeedForHovering);
                            } else if (this.getCurrentEngineSpeed() > requiredSpeedForHovering) {
                                this.changeCurrentEngineSpeed(-1.5f);
                            } else if (this.getCurrentEngineSpeed() < requiredSpeedForHovering) {
                                this.changeCurrentEngineSpeed(1.5f);
                            }
                        }
                    }
                }
                this.updateHelicopterTakeoffShaking(dist);
            } else if (this.getCurrentEngineSpeed() > 0) {
                this.changeCurrentEngineSpeed(-1);
            }

            if (this.onGround == true) {
                this.isFlying = false;
                if ((this.pitch > 30 || this.pitch < -30) || (this.roll > 30 || this.roll < -30) || (this.yawRoationAcceleration > 0.2 || this.yawRoationAcceleration < -0.2)) {
                    this.setHealth(-3);
                    this.setDead();
                    if (this.world.isRemote) {
                        this.playHelicopterExplosion();
                    }
                }
                this.lockOn = true;
                // DO NOT USE
                this.pitch = 0;
            }
            if (world.isRemote) {
                if (!this.shouldGearLift) {
                    this.gearLift += 0.02f;
                } else {
                    this.gearLift -= 0.02f;
                }

                if (dist < 10) {
                    this.shouldGearLift = false;
                } else {
                    this.shouldGearLift = true;
                }
            }
            if (this.getControllingPassenger() == null) {
                this.setNoGravity(false);
            }
            if (this.onGround && this.shouldFallDamage) {
                this.damageAmount = (float) this.prevInAirPos.y - (float) this.getPositionVector().y;
                this.setHealth(this.getHealth() - (float) Math.floor((double) (this.damageAmount / 3)));
                this.shouldFallDamage = false;
            }
            if (world.isRemote) {
                if (this.gearLift < -0.5f) {
                    this.gearLift = -0.5f;
                }
                if (this.gearLift > 0) {
                    this.gearLift = 0f;
                }
            }

            this.rotAmount += this.getCurrentEngineSpeed() * 0.00666666666 / 2d;

            if (this.getCurrentEngineSpeed() >= 1 && !this.isRotorAreaFree()) {
                this.setHealth(this.getHealth() - (this.getCurrentEngineSpeed() / this.engineSpeed * 2));
                if (this.getHealth() < 0 && this.world.isRemote) {
                    this.playHelicopterExplosion();
                }
            }
        } else {
            this.setNoGravity(false);
            this.wheelRotateAmount = 0;
            this.setCurrentEngineSpeed(this.getCurrentEngineSpeed() / 8);
        }

        if (this.world.isRemote) {
            this.spawnHoveringParticle();
            this.spawnEngineRunningParticle();
            this.spawnCrashingParticle();
            this.playWarningsound();
        }
        this.blastItems();
    }

    @Override
    protected void applyMovement() {
        float moveAmount = 0.0f;

        // A(side) = this.depth * this.height;
        float surfaceFront = this.physicalWidth * this.physicalHeight;
        float surfaceTop = this.physicalWidth * this.physicalDepth;
        final float horizontalSpeed = (float) Math.abs(Math.sqrt(Math.pow(this.motionX, 2) + Math.pow(this.motionZ, 2)) * 20);
        final float verticalSpeed = (float) Math.abs(this.motionY * 20);
        final float flowResistanceFront = (float) (2 * surfaceFront * 0.5f * 1.2f * Math.pow(horizontalSpeed, 2));
        final float flowResistanceTop = (float) (2 * surfaceTop * 0.5f * 1.2f * Math.pow(verticalSpeed, 2));

        moveAmount = ((this.computeHorizontalForceFrontBack() - flowResistanceFront) / this.weight) / 20;
        moveAmount *= Math.abs(((this.roll <= 45) ? this.roll / 45 : 2 - this.roll / 45) * 2 * ((this.pitch <= 90) ? this.pitch / 90 : 2 - this.pitch / 90));
        this.motionY += ((this.computeVerticalForce() - flowResistanceTop) / this.weight - 9.81) / 20;

        if ((this.roll > 0 && this.roll < 90 && this.pitch != 0)) {
            this.rotationDelta -= 20.0F * moveAmount;
        } else if ((this.roll < 0 && this.roll > -90 && this.pitch != 0)) {
            this.rotationDelta += 20.0F * moveAmount;
        }

        this.rotationDelta = MathHelper.clamp(this.rotationDelta, -30 * 0.1F, 30 * 0.1F);
        this.rotationYaw += this.rotationDelta;

        this.motionZ += Math.cos(this.rotationYaw * 0.017453292F) * (this.computeHorizontalForceFrontBack() - flowResistanceFront) / this.weight / 20;
        this.motionZ += Math.cos((this.rotationYaw - 90) * 0.017453292F) * (this.computeHorizontalForceLeftRight() - flowResistanceFront) / this.weight / 20;
        this.motionX += Math.sin(-this.rotationYaw * 0.017453292F) * (this.computeHorizontalForceFrontBack() - flowResistanceFront) / this.weight / 20;
        this.motionX += Math.sin(-(this.rotationYaw - 90) * 0.017453292F) * (this.computeHorizontalForceLeftRight() - flowResistanceFront) / this.weight / 20;
    }

    private void updateHelicopterCrash(float dist) {
        if (this.isLowHealth() && this.isFlying && dist > 1.5f) {
            this.yawRoationAcceleration += Math.random() * (this.getCurrentEngineSpeed() / this.engineSpeed) * 0.9f;
            if (this.yawRoationAcceleration > 7) {
                this.yawRoationAcceleration = 7;
            }
            this.motionX += Math.sin(Math.toRadians(this.rotationYaw)) * 0.05f;
            this.motionZ += Math.cos(Math.toRadians(this.rotationYaw)) * 0.05f;
            this.changeCurrentEngineSpeed(-1);
            if (this.pitch < (Math.random() * 10) + 20 && this.pitch >= 0) {
                this.pitch += 0.2f;
            } else if (this.pitch > -(Math.random() * 10 + 20) && this.pitch < 0) {
                this.pitch -= 0.2f;
            }
            if (this.roll < (Math.random() * 10 + 50) && this.roll >= 0) {
                this.roll -= 1f;
            } else if (this.roll > -(Math.random() * 10 + 50) && this.roll < 0) {
                this.roll -= 1f;
            }
        }
    }

    protected boolean isLowHealth() {
        return this.getHealth() <= 2;
    }

    private void updateHelicopterTakeoffShaking(float dist) {
        // int maxGroundDist = 3;
        // if (dist <= maxGroundDist && dist > 0.5f && this.isFlying && this.upward()) {
        // if (this.shakingDirection == 0) {
        // this.shakingDirection = (Math.random() < 0.5) ? 0.9f : -0.9f;
        // } else if (this.roll >= ((1.0f - dist / (maxGroundDist * 2)) * 2) ||
        // this.roll <= -((1.0f - dist / (maxGroundDist * 2)) * 2)) {
        // this.shakingDirection = this.shakingDirection * -1;
        // }
        // this.updateShakingRotation();
        // } else {
        // this.shakingDirection = 0;
        // }
    }

    protected void updateShakingRotation() {
        this.roll += this.shakingDirection;
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
    }

    @Override
    public float getSoundVolume() {
        return this.getCurrentEngineSpeed() > 0 ? (Math.abs(this.getCurrentEngineSpeed() / 150) + 0.001F) / (this.sound == null || this.sound.isDonePlaying() ? 2f : 4f)
                : (Math.abs(this.wheelRotateAmount) + 0.001F) / (this.sound == null || this.sound.isDonePlaying() ? 2f : 4f);
    }

    @Nonnull
    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return super.getAdjustedHorizontalFacing();
    }

    @Override
    protected boolean shouldTyresRender() {
        return false;
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Seat seat = null;
            if (getSeatForEntity(passenger) != -1)
                seat = this.seats[getSeatForEntity(passenger)];

            Vec3d pos;
            if (seat == null) {
                pos = new Vec3d(this.posX, this.posY + this.physicalHeight, this.posZ);
            } else {
                pos = seat.getPos();
            }
            passenger.setPosition(pos.x, pos.y + this.interpRotationPitch.getCurrent() / 75D, pos.z);
            passenger.rotationYaw += this.rotationDelta;
            passenger.setRotationYawHead(passenger.getRotationYawHead() + this.rotationDelta);
            if (passenger instanceof EntityLivingBase) {
                EntityLivingBase living = (EntityLivingBase) passenger;
                living.renderYawOffset += (living.rotationYaw - living.renderYawOffset) * 0.6F;
            }
        }
    }

    private void playHelicopterExplosion() {
        this.world.spawnParticle(EnumParticleTypes.EXPLOSION_HUGE, this.posX, this.posY, this.posZ, 0.1, 0.1, 0.1);
        this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_GENERIC_EXPLODE, SoundCategory.NEUTRAL, 4.0F, (1.0F + (this.world.rand.nextFloat() - this.world.rand.nextFloat()) * 0.2F) * 0.7F, false);
    }

    private float computeMaxMovementRotation(float dist) {
        return (dist <= 3) ? ((dist / 3.0f) * ((this.lockOn) ? MAX_MOVEMENT_ROTATION : 90)) : ((this.lockOn) ? MAX_MOVEMENT_ROTATION : 180f);
    }

    public float getDistanceToGround() {
        boolean found = false;
        float dist = -1;
        mb.setPos(this.getPosition());
        while (!found && this.posY >= 0 && mb.getY() >= 0) {
            if (world.isAirBlock(mb)) {
                mb = mb.setPos(mb.getX(), mb.getY() - 1, mb.getZ());
            } else {
                found = true;
                dist = (float) (this.posY - mb.getY() - 1);
            }
        }
        return dist;
    }

    protected IBlockState getGroundBlock() {
        boolean found = false;
        IBlockState groundBlock = null;
        mb.setPos(this.getPosition());
        while (!found && this.posY >= 0 && mb.getY() >= 0) {
            if (world.isAirBlock(mb)) {
                mb = mb.setPos(mb.getX(), mb.getY() - 1, mb.getZ());
            } else {
                found = true;
                groundBlock = this.world.getBlockState(mb);
            }
        }
        return groundBlock;
    }

    @Override
    protected void updateHeal() {
        // super.updateHeal();
    }

    @SideOnly(Side.CLIENT)
    private void increaseThirdPersonViewDistance(boolean shouldIncrease) {
        if (shouldIncrease) {
            RenderingHandler.INSTANCE.setThirdPersonViewDistance(RenderingHandler.INSTANCE.getThirdPersonViewDistance() + 1);
        }
    }

    @SideOnly(Side.CLIENT)
    private void decreaseThirdPersonViewDistance(boolean shouldDecrease) {
        if (shouldDecrease) {
            RenderingHandler.INSTANCE.setThirdPersonViewDistance(RenderingHandler.INSTANCE.getThirdPersonViewDistance() - 1);
        }
    }

    @SideOnly(Side.CLIENT)
    private void resetThirdPersonViewDistance() {
        RenderingHandler.INSTANCE.resetThirdPersonViewDistance();
    }

    @Override
    public void setDead() {
        if (!world.isRemote) {
            this.playHelicopterExplosion();
        }
        super.setDead();
    }

    @Override
    public float getCollisionBorderSize() {
        return 2.25f;
    }

    private boolean isRotorAreaFree() {
        boolean isFree = true;
        for (int x = -this.rotorLength; x < this.rotorLength && isFree; x++) {
            for (int z = -this.rotorLength; z < this.rotorLength && isFree; z++) {
                if (!(world.getBlockState(new BlockPos(this.posX + x, this.posY + this.physicalHeight, this.posZ + z)).getBlock() instanceof BlockAir)) {
                    isFree = false;
                }
            }
        }
        return isFree;
    }

    protected void setFlying() {
        this.isFlying = true;
        this.shouldFallDamage = true;
        this.prevInAirPos = this.getPositionVector();
        this.setNoGravity(true);
        for (int i = 0; i < this.seats.length; i++) {
            Entity e = this.getEntityInSeat(i);
            if (e != null) {
                e.setNoGravity(false);
            }
        }
    }

    public int getPositionLightFrequency() {
        return 30 - (int) ((this.getCurrentEngineSpeed() / this.engineSpeed) * 20);
    }


    protected void blastItems() {
        float dist = this.getDistanceToGround();
        if (dist >= 0) {
            List<Entity> items = this.world.getEntitiesWithinAABB(Entity.class,
                    new AxisAlignedBB(this.posX - this.rotorLength * 2, this.posY - dist - 1, this.posZ - this.rotorLength * 2, this.posX + this.rotorLength * 2, this.posY + 1, this.posZ + this.rotorLength * 2));
            for (Entity item : items) {
                float itemDist = item.getDistance(this);
                if (itemDist <= this.rotorLength * 2 && item.getEntityBoundingBox().getAverageEdgeLength() <= 0.55) {
                    float moveAmount = (float) ((1 - (item.getDistance(this) / (this.rotorLength * 2))) * (1 - (dist / this.blastHeight)) * ((1 / Math.pow(this.engineSpeed, 3)) * Math.pow(this.getCurrentEngineSpeed(), 3)));
                    float x = (float) ((item.posX - this.posX) / itemDist);
                    float z = (float) ((item.posZ - this.posZ) / itemDist);
                    item.addVelocity(x * moveAmount, 0, z * moveAmount);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnHoveringParticle() {
        float dist = this.getDistanceToGround();
        IBlockState groundBlock = this.getGroundBlock();
        if (dist <= blastHeight && dist >= 0) {
            for (int i = 0; i < 360; i++) {
                if (Math.random() * 100 < ((1 - (dist / blastHeight)) * ((groundBlock.getBlock().equals(Blocks.WATER)) ? 80 : 20)) * (((1 / Math.pow(this.engineSpeed, 3)) * Math.pow(this.getCurrentEngineSpeed(), 3)) * 1.0)) {
                    float x = (float) ((Math.cos(Math.toRadians(i)) * (this.rotorLength / 1.8f)) * ((Math.random() * 0.2) + 1));
                    float y = (float) (this.posY - dist);
                    float z = (float) ((Math.sin(Math.toRadians(i)) * (this.rotorLength / 1.8f)) * ((Math.random() * 0.2) + 1));
                    if (groundBlock.getBlock().equals(Blocks.WATER)) {
                        Minecraft.getMinecraft().effectRenderer.addEffect(new WashingParticle(this.world, this.posX + x, y + 0.5f, this.posZ + z, x / 5, 0.001f, z / 5, 0));
                    } else if (!groundBlock.getMaterial().equals(Material.LAVA)) {
                        if (this.isBlockDusty(groundBlock.getBlock()) && Math.random() < (this.world.isRaining() ? 0.1 : 0.4)) {
                            this.world.spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX + x, y + 0.1, this.posZ + z, x / 5, 0.001f + Math.random() * (this.world.isRaining() ? 0 : 0.5), z / 5, Block.getStateId(groundBlock));
                        }
                        Minecraft.getMinecraft().effectRenderer.addEffect(new HelicopterGroundParticle(this.world, this.posX + x, y, this.posZ + z, x / 5, 0.001f, z / 5));
                    }
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnEngineRunningParticle() {
        float[] offsetBack = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 3.0625f);
        float[] directionBack1 = this.computeEngineExhaustParticleDirection(15);
        float[] directionBack2 = this.computeEngineExhaustParticleDirection(-15);
        for (int i = 0; i < 5; i++) {
            if (Math.random() < this.getCurrentEngineSpeed() / this.engineSpeed) {
                Minecraft.getMinecraft().effectRenderer.addEffect(new HelicopterEngineParticle(this.world, this.posX + offsetBack[0] + Math.random() * 0.3, this.posY + offsetBack[2] + Math.random() * 0.3,
                        this.posZ - offsetBack[4] + Math.random() * 0.3, directionBack1[0] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed), 0.001f, directionBack1[2] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed), 1));
                Minecraft.getMinecraft().effectRenderer.addEffect(new HelicopterEngineParticle(this.world, this.posX + offsetBack[1] + Math.random() * 0.3, this.posY + offsetBack[3] + Math.random() * 0.3,
                        this.posZ - offsetBack[5] + Math.random() * 0.3, directionBack2[0] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed), 0.001f, directionBack2[2] * (this.getCurrentEngineSpeed() * 3 / this.engineSpeed), 1));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    protected void spawnCrashingParticle() {
        if (Math.random() < (-(this.getHealth() * 1.6f - MAX_HEALTH) / MAX_HEALTH)) {
            float[] offsetFront = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 0.9375f);
            float[] offsetBack = this.computeEngineOutletPosition(0.675f, -0.675f, 2.1f, 3.0625f);
            float[] directionFront1 = this.computeEngineFrontSmokeParticleDirection(-20);
            float[] directionFront2 = this.computeEngineFrontSmokeParticleDirection(20);
            float[] directionBack = this.computeEngineExhaustParticleDirection(0);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + offsetBack[0], this.posY + offsetBack[2], this.posZ - offsetBack[4], directionBack[0], directionBack[1], directionBack[2]);
            this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + offsetBack[1], this.posY + offsetBack[3], this.posZ - offsetBack[5], directionBack[0], directionBack[1], directionBack[2]);

            if (this.inWater && ((int) this.world.getChunkFromBlockCoords(this.getPosition()).getHeight(this.getPosition()) - this.posY) > 2) {
                if (Math.random() < (1 - (this.getHealth() * 2 - MAX_HEALTH) / MAX_HEALTH)) {
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + offsetBack[0], this.posY + offsetBack[2], this.posZ - offsetBack[4], 0, 0, 0);
                    this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + offsetBack[1], this.posY + offsetBack[3], this.posZ - offsetBack[5], 0, 0, 0);
                }

                if (Math.random() < (1.0f - (this.getCurrentEngineSpeed() * 1.6f / (float) (this.engineSpeed)))) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + offsetFront[0], this.posY + offsetFront[2], this.posZ - offsetFront[4], directionFront1[0] * 0.5, directionFront1[1] * 0.5, directionFront1[2] * 0.5);
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + offsetFront[1], this.posY + offsetFront[3], this.posZ - offsetFront[5], directionFront2[0] * 0.5, directionFront2[1] * 0.5, directionFront2[2] * 0.5);
                }
            } else {
                if (Math.random() < (1 - (this.getHealth() * 2 - MAX_HEALTH) / MAX_HEALTH)) {
                    this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + offsetBack[0], this.posY + offsetBack[2], this.posZ - offsetBack[4], directionBack[0], directionBack[1], directionBack[2]);
                    this.world.spawnParticle(EnumParticleTypes.FLAME, this.posX + offsetBack[1], this.posY + offsetBack[3], this.posZ - offsetBack[5], directionBack[0], directionBack[1], directionBack[2]);
                }

                if (Math.random() < (1.0f - (this.getCurrentEngineSpeed() * 1.6f / (float) (this.engineSpeed)))) {
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + offsetFront[0], this.posY + offsetFront[2], this.posZ - offsetFront[4], directionFront1[0], directionFront1[1], directionFront1[2]);
                    this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX + offsetFront[1], this.posY + offsetFront[3], this.posZ - offsetFront[5], directionFront2[0], directionFront2[1], directionFront2[2]);
                }
            }
        }
    }

    protected float[] computeEngineOutletPosition(float xPos1, float xPos2, float yPos, float zPos) {
        final float radiantRotationYaw = (float) Math.toRadians(this.rotationYaw);
        final float radiantPitch = (float) Math.toRadians(this.pitch);
        final float radiantRoll = (float) Math.toRadians(this.roll);
        float[] offsets = new float[6];
        float[] tmpOffsets = new float[6];
        // 0: Right; 1: Left;

        offsets[2] = yPos;
        offsets[3] = yPos;
        offsets[4] = zPos;
        offsets[5] = zPos;

        // x-Achsis
        offsets[0] = xPos1;
        offsets[1] = xPos2;
        offsets[2] = (float) (Math.cos(-radiantPitch) * yPos - Math.sin(-radiantPitch) * zPos);
        offsets[3] = (float) (Math.cos(-radiantPitch) * yPos - Math.sin(-radiantPitch) * zPos);
        offsets[4] = (float) (Math.sin(-radiantPitch) * yPos + Math.cos(-radiantPitch) * zPos);
        offsets[5] = (float) (Math.sin(-radiantPitch) * yPos + Math.cos(-radiantPitch) * zPos);

        // z-Achsis
        tmpOffsets = offsets.clone();
        offsets[0] = (float) (Math.cos(-radiantRoll) * tmpOffsets[0] - Math.sin(-radiantRoll) * tmpOffsets[2]);
        offsets[1] = (float) (Math.cos(-radiantRoll) * tmpOffsets[1] - Math.sin(-radiantRoll) * tmpOffsets[3]);
        offsets[2] = (float) (Math.sin(-radiantRoll) * tmpOffsets[0] + Math.cos(-radiantRoll) * tmpOffsets[2]);
        offsets[3] = (float) (Math.sin(-radiantRoll) * tmpOffsets[1] + Math.cos(-radiantRoll) * tmpOffsets[3]);

        // y-Achsis
        tmpOffsets = offsets.clone();
        offsets[0] = (float) (Math.cos(radiantRotationYaw) * tmpOffsets[0] + Math.sin(radiantRotationYaw) * tmpOffsets[4]);
        offsets[1] = (float) (Math.cos(radiantRotationYaw) * tmpOffsets[1] + Math.sin(radiantRotationYaw) * tmpOffsets[5]);
        offsets[4] = (float) ((-Math.sin(radiantRotationYaw)) * tmpOffsets[0] + Math.cos(radiantRotationYaw) * tmpOffsets[4]);
        offsets[5] = (float) ((-Math.sin(radiantRotationYaw)) * tmpOffsets[1] + Math.cos(radiantRotationYaw) * tmpOffsets[5]);
        return offsets;
    }

    protected float[] computeEngineExhaustParticleDirection(float rotOffset) {
        float[] directions = new float[3];
        float engineBlast = (this.getCurrentEngineSpeed() / this.engineSpeed) * 0.2f;
        directions[0] = (float) (Math.sin(-Math.toRadians(this.rotationYaw + rotOffset)) * (-0.05f - engineBlast) + ((Math.random() - 0.5) * 0.1));
        directions[1] = 0.01f;
        directions[2] = (float) (Math.cos(Math.toRadians(this.rotationYaw + rotOffset)) * (-0.05f - engineBlast) + ((Math.random() - 0.5) * 0.1));
        return directions;
    }

    protected float[] computeEngineFrontSmokeParticleDirection(float rotOffset) {
        float[] directions = new float[3];
        float engineBlast = (this.getCurrentEngineSpeed() / this.engineSpeed) * 0.2f;
        directions[0] = (float) (Math.sin(-Math.toRadians(this.rotationYaw + rotOffset)) * (0.05f + engineBlast) + ((Math.random() - 0.5) * 0.1));
        directions[1] = 0.000f;
        directions[2] = (float) (Math.cos(Math.toRadians(this.rotationYaw + rotOffset)) * (0.05f + engineBlast) + ((Math.random() - 0.5) * 0.1));
        return directions;
    }

    @SideOnly(Side.CLIENT)
    protected void playWarningsound() {
        if (this.getControllingPassenger() == Minecraft.getMinecraft().player) {
            if (this.getHealth() / this.MAX_HEALTH < 0.3 && this.warningDelay <= 0) {
                this.world.playSound(Minecraft.getMinecraft().player, Minecraft.getMinecraft().player.getPosition(), new SoundEvent(this.warnignSoundResource), SoundCategory.BLOCKS, (float) 0.1 * (1 - (this.getHealth() / this.MAX_HEALTH)),
                        1);
                this.warningDelay = 17;
            } else if (this.getHealth() / this.MAX_HEALTH < 0.3) {
                this.warningDelay--;
            }
        }
    }



    private boolean shouldAdjustEngineSpeedByHorizontalControls(float requiredSpeedForHovering) {
        return (this.forward() || this.backward() || this.left() || this.right()) && !(this.getCurrentEngineSpeed() > requiredSpeedForHovering && this.upward())
                && !(this.getCurrentEngineSpeed() < requiredSpeedForHovering && this.downward());
    }

    private boolean shouldAdjustEngineSpeedWithoutHorizontalControls(float requiredSpeedForHovering) {
        return !(this.forward() || this.backward() || this.left() || this.right()) && !(this.getCurrentEngineSpeed() > requiredSpeedForHovering && this.upward())
                && !(this.getCurrentEngineSpeed() < requiredSpeedForHovering && this.downward());
    }

    protected void handleKeyEnableAutoPilot(boolean shouldChange) {
        if (shouldChange) {
            if (this.simpleControle) {
                this.simpleControle = false;
            } else {
                this.simpleControle = true;
            }
        }
    }

    protected void handleKeyLock(boolean shouldChange) {
        if (shouldChange) {
            if (this.lockOn) {
                this.lockOn = false;
            } else {
                this.lockOn = true;
            }
        }
    }

    private void rotateLeft(boolean keyPressed) {
        if (keyPressed) {
            this.rotationControl = 1;
        } else if (this.rotationControl == 1) {
            if (!rotateRight()) {
                this.rotationControl = 0;
            }
        }
    }

    private void rotateRight(boolean keyPressed) {
        if (keyPressed) {
            if (!rotateLeft()) {
                this.rotationControl = 2;
            } else {
                this.rotationControl = 0;
            }
        } else if (this.rotationControl == 2) {
            if (!rotateLeft()) {
                this.rotationControl = 0;
            }
        }
    }

    protected boolean rotateLeft() {
        return this.rotationControl == 1;
    }

    protected boolean rotateRight() {
        return this.rotationControl == 2;
    }

    protected boolean isBlockDusty(Block block) {
        return block.equals(Blocks.SAND) || block.equals(Blocks.SOUL_SAND) || block.equals(Blocks.GRAVEL);
    }

    @Override
    public void dropItems() {
        this.entityDropItem(new ItemStack(ItemHandler.VEHICLE_ITEM, 1, 2), 0.1f);
    }

    // Physics

    protected void changeCurrentEngineSpeed(float changeSpeed) {
        this.setCurrentEngineSpeed(this.getCurrentEngineSpeed() + changeSpeed);
    }

    protected void setCurrentEngineSpeed(float speed) {
        this.currentEngineSpeed = speed;
        if (this.currentEngineSpeed > this.engineSpeed) {
            this.currentEngineSpeed = this.engineSpeed;
        } else if (this.currentEngineSpeed < 0) {
            this.currentEngineSpeed = 0;
        }
    }

    // F
    protected float computeRotorSweptArea() {
        return (float) (Math.PI * Math.pow(this.rotorLength, 2));
    }

    // P
    protected float computeShaftPower() {
        return (float) (this.torque * this.getCurrentEngineSpeed() * (Math.PI / 30));
    }

    protected float computeRotorForce() {
        // (2 * p * F * (ζ * P) ^ 2) ^ (1 / 3)
        return (float) Math.pow(2 * 1.2f * this.computeRotorSweptArea() * Math.pow(this.qualityGrade * this.computeShaftPower(), 2), (1.0 / 3.0));
    }

    protected float computeVerticalForce() {
        return ((1.0f - Math.abs(this.pitch) / 90) * (1 - Math.abs(this.roll) / 90)) * this.computeRotorForce();
    }

    protected float computeHorizontalForceFrontBack() {
        if (this.pitch <= 90.0f && this.pitch >= -90.0f) {
            return ((this.pitch / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * (((1.0f - Math.abs(this.pitch) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f) * this.computeRotorForce();
        } else {
            if (this.pitch < 0) {
                return ((1.0f + (this.pitch + 90.0f) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * (((Math.abs(this.pitch + 90) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f) * this.computeRotorForce();
            } else {
                return ((1.0f - (this.pitch - 90.0f) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * (((Math.abs(this.pitch - 90) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f)) * 5f) * this.computeRotorForce();
            }
        }
    }

    protected float computeHorizontalForceLeftRight() {
        return ((this.roll / 90.0f) * (1.0f - this.pitch / 90.0f)) * this.computeRotorForce();
    }

    public float getCurrentEngineSpeed() {
        return this.currentEngineSpeed;
    }

    protected float computeRequiredEngineSpeedForHover() {
        float S = (9.81f * this.weight) / ((1 - Math.abs(this.pitch) / 90.0f) * (1.0f - Math.abs(this.roll) / 90.0f));
        return (float) (((1 / this.qualityGrade) * Math.sqrt(Math.pow(S, 3) / (2 * 1.2f * this.computeRotorSweptArea()))) / (this.torque * (Math.PI / 30)));
    }

    protected float computeThrottleUpDown() {
        return ((float) this.enginePower / 735.5f) * 0.00029481132f;
    }

    protected float computeTorque() {
        return (float) (enginePower / (engineSpeed * (Math.PI / 30)));
    }

}