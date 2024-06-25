package mod.reborn.server.entity.vehicle;


import com.google.common.collect.Lists;
import mod.reborn.RebornMod;
import mod.reborn.server.block.TourRailBlock;
import mod.reborn.server.entity.ai.util.InterpValue;
import mod.reborn.server.entity.ai.util.MathUtils;
import mod.reborn.server.entity.vehicle.util.WheelParticleData;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.message.MonorailChangeStateMessage;
import mod.reborn.server.message.MonorailUpdatePositionStateMessage;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;
import javax.vecmath.Vector2d;
import javax.vecmath.Vector4d;
import java.util.Arrays;
import java.util.List;

public class MonorailEntity extends VehicleEntity {

    public static final BlockPos INACTIVE = new BlockPos(-1, -1, -1);

    public boolean prevOnRails;
    public boolean onRails;
    private BlockPos prevRailTracks = INACTIVE;
    public BlockPos prevPos = INACTIVE;
    public BlockPos railTracks = INACTIVE;

    private boolean lastDirBackwards;

    public final MinecartLogic minecart = new MinecartLogic();

    private final InterpValue rotationYawInterp = new InterpValue(this, 4f);

    /* =================================== CAR START ===========================================*/

    public MonorailEntity(World world) {
        super(world);
        this.speedModifier = 0f;
    }

    @Override
    public void dropItems() {
        this.entityDropItem(new ItemStack(ItemHandler.VEHICLE_ITEM, 1, 10), 0);
    }

    @Override
    protected Seat[] createSeats() {
        Seat frontLeft = new Seat(0.563F, 0.45F, 0.4F, 0.5F, 0.25F);
        Seat frontRight = new Seat(-0.563F, 0.45F, 0.4F, 0.5F, 0.25F);
        Seat backLeft = new Seat( 0.563F, 0.45F, -1F, 0.5F, 0.25F);
        Seat backRight = new Seat( -0.563F, 0.45F, -1F, 0.5F, 0.25F);
        return new Seat[] { frontLeft, frontRight, backLeft, backRight };
    }

    @Override
    protected boolean shouldStopUpdates() {
        return onRails;
    }

    @Override
    public void onUpdate() {
        BlockPos startPos = this.getPosition();
        if(!world.isRemote) {
            BlockPos rail = getPosition();
            boolean isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
            if(!isRails) {
                rail = rail.down();
                isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
            }
            if(!isRails && world.getBlockState(rail.down()).getBlock() instanceof TourRailBlock && Arrays.asList(TourRailBlock.EnumRailDirection.ASCENDING_EAST, TourRailBlock.EnumRailDirection.ASCENDING_NORTH, TourRailBlock.EnumRailDirection.ASCENDING_SOUTH, TourRailBlock.EnumRailDirection.ASCENDING_WEST).contains(TourRailBlock.getRailDirection(world, rail.down()))) {
                rail = rail.down(1);
                isRails = world.getBlockState(rail).getBlock() instanceof TourRailBlock;
            }

            if(onRails != isRails) {
                if(isRails) {
                    minecart.isInReverse = lastDirBackwards;
                }
                onRails = isRails;
                RebornMod.NETWORK_WRAPPER.sendToDimension(new MonorailChangeStateMessage(this), world.provider.getDimension());
            }
            this.railTracks = isRails ? rail : INACTIVE;
            if(!this.railTracks.equals(prevRailTracks)) {
                RebornMod.NETWORK_WRAPPER.sendToDimension(new MonorailUpdatePositionStateMessage(this, rail), world.provider.getDimension());
            }
            this.prevRailTracks = railTracks;
        }
        if(onRails) {
            this.setSize(0.75F, 0.25F);
//			this.stepHeight = 0F;
        } else {
            this.setSize(3.0F, 2.5F);
//			this.stepHeight = 1.5F;
        }
        this.setPosition(this.posX, this.posY, this.posZ); //Make sure that the car is in the right position. Can cause issues when changing size of car
        super.onUpdate();
        if(onRails) {
            minecart.onUpdate();
            Vector4d vec = wheeldata.carVector;
            this.backValue.setTarget(this.calculateWheelHeight(vec.y, false));
            this.frontValue.setTarget(this.calculateWheelHeight(vec.w, false));
            this.leftValue.setTarget(posY);
            this.rightValue.setTarget(posY);
        }
        prevOnRails = onRails;
        if(!startPos.equals(this.getPosition())) {
            prevPos = this.getPosition();
        }
    }

    @Override
    protected void doBlockCollisions() {
        if(!onRails) {
            super.doBlockCollisions();
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
    }

    @Override
    public void onEntityUpdate() {
        super.onEntityUpdate();
        if(onRails) {
            if(this.canPassengerSteer()) {
                if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof EntityPlayer)) {
                    this.setControlState((byte) 0);
                }
                if(this.world.isRemote) {
                    this.handleControl(); //+Z-X
                }
            }
        } else {
            rotationYawInterp.reset(this.rotationYaw - 180D);
        }
        if(forward()) {
            lastDirBackwards = false;
        } else if(backward()) {
            lastDirBackwards = true;
        }
    }

    @Override
    protected void writeEntityToNBT(NBTTagCompound compound) {
        super.writeEntityToNBT(compound);
        compound.setBoolean("OnRails", onRails);
        compound.setLong("BlockPosition", railTracks.toLong());
        compound.setLong("PrevBlockPosition", this.prevPos.toLong());
    }

    @Override
    protected void readEntityFromNBT(NBTTagCompound compound) {
        super.readEntityFromNBT(compound);
        onRails = compound.getBoolean("OnRails");
        railTracks = BlockPos.fromLong(compound.getLong("BlockPosition"));
        this.prevPos = BlockPos.fromLong(compound.getLong("PrevBlockPosition"));
    }

    @Override
    public float getSoundVolume() {
        return onRails ? this.getControllingPassenger() != null ? this.getSpeed().modifier / 2f : 0f : super.getSoundVolume();
    }

    @Nonnull
    @Override
    public EnumFacing getAdjustedHorizontalFacing() {
        return onRails ? minecart.getAdjustedHorizontalFacing() : super.getAdjustedHorizontalFacing();
    }

    @Override
    protected WheelData createWheels() {
        return new WheelData(1, 2, -1, -2.2);
    }

    @Override
    protected boolean shouldTyresRender() {
        return super.shouldTyresRender() && !onRails;
    }

    @Override
    public Vector2d getBackWheelRotationPoint() {
        Vector2d point = super.getBackWheelRotationPoint();
        return new Vector2d(point.x, onRails ? 0 : point.y);
    }

    @Override
    public float getCollisionBorderSize() {
        return 2.25f;
    }

    /* =================================== CAR END ===========================================*/
    /* ================================ MINECART START =======================================*/


    public class MinecartLogic {
        private boolean isInReverse;
        private boolean prevKeyDown;
        private double adjustedRotationYaw;


        public EnumFacing getAdjustedHorizontalFacing() {
            return this.isInReverse ? getHorizontalFacing().getOpposite().rotateY() : getHorizontalFacing().rotateY();
        }

        public void onUpdate() {
            //CAR STUFF START
            rotationDelta *= 0.8f;
            allWheels.forEach(MonorailEntity.this::processWheel);

            for(int i = 0; i < 4; i++) {
                List<WheelParticleData> markedRemoved = Lists.newArrayList();
                wheelDataList[i].forEach(wheel -> wheel.onUpdate(markedRemoved));
                markedRemoved.forEach(wheelDataList[i]::remove);
            }
            //CAR STUFF END

            if (posY < -64.0D) {
                outOfWorld();
            }
            MinecraftServer minecraftserver = world.getMinecraftServer();
            if (!world.isRemote && world instanceof WorldServer && minecraftserver != null) {
                world.profiler.startSection("portal");
                int i = getMaxInPortalTime();
                if (inPortal) {
                    if (minecraftserver.getAllowNether()) {
                        if (!isRiding() && portalCounter++ >= i) {
                            portalCounter = i;
                            timeUntilPortal = getPortalCooldown();
                            int j;
                            if (world.provider.getDimensionType().getId() == -1) {
                                j = 0;
                            } else {
                                j = -1;
                            }

                            changeDimension(j);
                        }

                        inPortal = false;
                    }
                } else {
                    if (portalCounter > 0) {
                        portalCounter -= 4;
                    }

                    if (portalCounter < 0) {
                        portalCounter = 0;
                    }
                }

                if (timeUntilPortal > 0) {
                    --timeUntilPortal;
                }

                world.profiler.endSection();
            }

            if (!hasNoGravity()) {
                motionY -= 0.03999999910593033D;
            }
            if(railTracks.equals(INACTIVE)) { //Shouldn't occur
                return;
            }
            if(getPassengers().isEmpty()) {
                return;
            }
            moveAlongTrack();

            if(!world.isRemote) {
                doBlockCollisions();
                rotationPitch = 0.0F;

                handleWaterMovement();
            }

        }

        protected void moveAlongTrack() {
            fallDistance = 0.0F;
            Vec3d vec3d = getPos();

            posY = (double)railTracks.getY();

            double slopeAdjustment = 0.0078125D;
            TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(world, railTracks);

            EnumFacing facing = getFacingDir();
            //TODO: Working?
            switch (dir) {
                case ASCENDING_EAST:
                    motionX -= slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_WEST:
                    motionX += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_NORTH:
                    motionZ += slopeAdjustment;
                    ++posY;
                    break;
                case ASCENDING_SOUTH:
                    motionZ -= slopeAdjustment;
                    ++posY;
                default:
            }
            double d1 = (double)(dir.getBackwardsX(facing) - dir.getForwardX(facing));
            double d2 = (double)(dir.getBackwardsZ(facing) - dir.getForwardZ(facing));
            double d3 = Math.sqrt(d1 * d1 + d2 * d2);
            double d4 = motionX * d1 + motionZ * d2;
            if (d4 < 0.0D) {
                d1 = -d1;
                d2 = -d2;
            }

            double d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

            if (d5 > 2.0D) {
                d5 = 2.0D;
            }
            double d = 1;
            if(forward()) {
                if(!prevKeyDown && isInReverse) {
                    d = -1;
                }
                isInReverse = false;
                prevKeyDown = true;
            } else if(backward()) {
                if(!prevKeyDown && !isInReverse) {
                    d = -1;
                }
                isInReverse = true;
                prevKeyDown = true;
            } else {
                prevKeyDown = false;
            }
            if(!world.isRemote) {
                d5 *= d;
            }


            motionX = d5 * d1 / d3;
            motionZ = d5 * d2 / d3;




            Vec3d vec = getPositionVector();
            Vec3d dirVec = new Vec3d(-d1, 0, d2).add(vec);
            double target = MathUtils.cosineFromPoints(vec.addVector(0, 0, 1), dirVec, vec);

            if(dirVec.x < vec.x) {
                target = -target;
            }

            this.adjustedRotationYaw = target;

            if(isInReverse) {
                target += 180F;
            }

            double d22;
            do {
                d22 = Math.abs(rotationYawInterp.getCurrent() - target);
                double d23 = Math.abs(rotationYawInterp.getCurrent() - (target + 360f));
                double d24 = Math.abs(rotationYawInterp.getCurrent() - (target - 360f));

                if(d23 < d22) {
                    target += 360f;
                } else if(d24 < d22) {
                    target -= 360f;
                }
            } while(d22 > 180);

            target = Math.round(target * 100D) / 100D;




            rotationYawInterp.setSpeed(this.getSpeedType().modifier * 4f);

            if(!prevOnRails) {
                rotationYawInterp.reset(target);
            } else if(d != -1) {
                rotationYawInterp.setTarget(target);
            }

            setRotation((float) rotationYawInterp.getCurrent(), rotationPitch);

            double d18 = (double)railTracks.getX() + 0.5D + (double)dir.getForwardX(facing) * 0.5D;
            double d19 = (double)railTracks.getZ() + 0.5D + (double)dir.getForwardZ(facing) * 0.5D;
            double d20 = (double)railTracks.getX() + 0.5D + (double)dir.getBackwardsX(facing) * 0.5D;
            double d21 = (double)railTracks.getZ() + 0.5D + (double)dir.getBackwardsZ(facing) * 0.5D;
            d1 = d20 - d18;
            d2 = d21 - d19;
            double d10;

            if (d1 == 0.0D) {
                posX = (double)railTracks.getX() + 0.5D;
                d10 = posZ - (double)railTracks.getZ();
            } else if (d2 == 0.0D) {
                posZ = (double)railTracks.getZ() + 0.5D;
                d10 = posX - (double)railTracks.getX();
            } else {
                double d11 = posX - d18;
                double d12 = posZ - d19;
                d10 = (d11 * d1 + d12 * d2) * 2.0D;
            }

            posX = d18 + d1 * d10;
            posZ = d19 + d2 * d10;
            setPosition(posX, posY, posZ);
            moveMinecartOnRail();

            double drag = isBeingRidden() ? 0.9D : 0.75D;

            motionX *= drag;
            motionZ *= drag;

            Vec3d vec3d1 = getPos();

            if (vec3d1 != null && vec3d != null) {
                double d14 = (vec3d.y - vec3d1.y) * 0.05D;
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);

                if (d5 > 0.0D) {
                    motionX = motionX / d5 * (d5 + d14);
                    motionZ = motionZ / d5 * (d5 + d14);
                }
            }

            int j = MathHelper.floor(posX);
            int i = MathHelper.floor(posZ);

            if (j != railTracks.getX() || i != railTracks.getZ()) {
                d5 = Math.sqrt(motionX * motionX + motionZ * motionZ);
                motionX = d5 * (double)(j - railTracks.getX());
                motionZ = d5 * (double)(i - railTracks.getZ());
            }
            double d15 = Math.sqrt(motionX * motionX + motionZ * motionZ);
            if(d15 == 0) {
                d15 = 1;
            }
            double d16 = 0.06D;
            motionX += motionX / d15 * d16;
            motionZ += motionZ / d15 * d16;
        }

        private Vec3d getPos() {
            double x = posX;
            double y = posY;
            double z = posZ;

            IBlockState iblockstate = world.getBlockState(new BlockPos(railTracks));

            if (iblockstate.getBlock() instanceof TourRailBlock)
            {
                TourRailBlock.EnumRailDirection dir = TourRailBlock.getRailDirection(world, railTracks);

                EnumFacing facing = getFacingDir();

                double d0 = x + 0.5D + (double)dir.getForwardX(facing) * 0.5D;
                double d1 = y + 0.0625D + (double)dir.getForwardY(facing) * 0.5D;
                double d2 = z + 0.5D + (double)dir.getForwardZ(facing) * 0.5D;
                double d3 = x + 0.5D + (double)dir.getBackwardsX(facing) * 0.5D;
                double d4 = y + 0.0625D + (double)dir.getBackwardsY(facing) * 0.5D;
                double d5 = z + 0.5D + (double)dir.getBackwardsZ(facing) * 0.5D;
                double d6 = d3 - d0;
                double d7 = (d4 - d1) * 2.0D;
                double d8 = d5 - d2;
                double d9;

                if (d6 == 0.0D) {
                    d9 = z - z;
                } else if (d8 == 0.0D) {
                    d9 = x - x;
                } else {
                    double d10 = x - d0;
                    double d11 = z - d2;
                    d9 = (d10 * d6 + d11 * d8) * 2.0D;
                }

                x = d0 + d6 * d9;
                y = d1 + d7 * d9;
                z = d2 + d8 * d9;

                if (d7 < 0.0D) {
                    ++y;
                }

                if (d7 > 0.0D) {
                    y += 0.5D;
                }

                return new Vec3d(x, y, z);
            } else {
                return null;
            }
        }

        private void moveMinecartOnRail() {
            double mX = motionX;
            double mZ = motionZ;
            if(mX == 0 && mZ == 0 && !getPassengers().isEmpty()) { //Should only happen when re-logging. //TODO: make a more elegant solution
                mX = getLook(1f).x;
                mZ = getLook(1f).z;
            }

            IBlockState state = world.getBlockState(railTracks);
            double max = (((TourRailBlock)state.getBlock()).getSpeedType().getSpeed(getSpeed())).modifier / 4f;
            mX = MathHelper.clamp(mX, -max, max);
            mZ = MathHelper.clamp(mZ, -max, max);
            MonorailEntity.this.move(MoverType.SELF, mX, 0D, mZ);
        }

        private Speed getSpeedType() {
            return ((TourRailBlock)world.getBlockState(railTracks).getBlock()).getSpeedType().getSpeed(getSpeed());
        }

        private EnumFacing getFacingDir() {
            EnumFacing facing = EnumFacing.getHorizontal(MathHelper.floor(this.adjustedRotationYaw * 4.0D / 360.0D + 0.5D) & 3);
            if(this.isInReverse) {
                facing = facing.getOpposite();
            }
            return facing;
        }
    }
    /* ================================= MINECART END ========================================*/
}