package mod.reborn.server.entity.vehicle;

import com.google.common.collect.Lists;

import mod.reborn.RebornMod;
import mod.reborn.server.entity.ai.util.InterpValue;
import mod.reborn.server.event.KeyBindingHandler;
import mod.reborn.server.message.HelicopterDirectionMessage;
import mod.reborn.server.message.HelicopterEngineMessage;
import mod.reborn.server.util.MutableVec3;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Predicate;


public class HelicopterEntityNew extends EntityLivingBase {
    public static final float MAX_POWER = 80.0F;
    public static final float REQUIRED_POWER = MAX_POWER / 2.0F;
    private static final DataParameter<Boolean> DATA_WATCHER_ENGINE_RUNNING = EntityDataManager.createKey(HelicopterEntityNew.class, DataSerializers.BOOLEAN);
    private final Seat[] seats;
    public float rotAmount;
    //    private UUID heliID;
    private boolean engineRunning;
    private float enginePower;
    private float engineMovement;
    private double hoverMovement;
    private MutableVec3 direction;
    private float rotationDelta;

    public final InterpValue rotorRotationAmount = new InterpValue(this, 0.1D);
    public final InterpValue interpRotationPitch = new InterpValue(this, 0.25D);
    public final InterpValue interpRotationRoll = new InterpValue(this, 0.25D);
    public final InterpValue interpSpeed = new InterpValue(this, 0.01F);

    public HelicopterEntityNew(World worldIn) {
        super(worldIn);
        double w = 3f; // width in blocks
        double h = 3.1f; // height in blocks
        double d = 8f; // depth in blocks
        this.setBox(0, 0, 0, w, h, d);

        this.seats = new Seat[3];
        for (int i = 0; i < this.seats.length; i++) {
            float distance = i == 0 ? 1.5f : 0;
            this.seats[i] = new Seat(i, 0, 0F, distance, 0.5F, 0.25F);
        }

        this.direction = new MutableVec3(0, 0, 0);
    }

    /**
     * Sets entity size
     *
     * @param offsetX The offset of the box in blocks on the X axis
     * @param offsetY The offset of the box in blocks on the Y axis
     * @param offsetZ The offset of the box in blocks on the Z axis
     * @param w The width of the entity
     * @param h The height of the entity
     * @param d The depth of the entity
     */
    private void setBox(double offsetX, double offsetY, double offsetZ, double w, double h, double d) {
        double minX = this.getEntityBoundingBox().minX + offsetX;
        double minY = this.getEntityBoundingBox().minY + offsetY;
        double minZ = this.getEntityBoundingBox().minZ + offsetZ;
        double maxX = this.getEntityBoundingBox().minX + w + offsetX;
        double maxY = this.getEntityBoundingBox().minY + h + offsetY;
        double maxZ = this.getEntityBoundingBox().minZ + d + offsetZ;
        this.setEntityBoundingBox(new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ));
        this.width = (float) (maxX - minX);
        this.height = (float) (maxY - minY);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        this.dataManager.register(DATA_WATCHER_ENGINE_RUNNING, false);
    }

    @Override
    public Iterable<ItemStack> getArmorInventoryList() {
        return Lists.newArrayList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EntityEquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EntityEquipmentSlot slot, ItemStack stack) {

    }

    // apparently up to the entity to update its position given the motion

    @Override
    public void onLivingUpdate() {
        this.interpSpeed.setTarget(2F);
        super.onLivingUpdate();
        if (this.motionX * this.motionX + this.motionZ * this.motionZ > 1.1 * 1.1 && this.collidedHorizontally) {
            world.createExplosion(this.getRidingEntity(), posX, posY, posZ, 12, true);
        }

        // update rotor angle
        float time = this.enginePower / MAX_POWER; //Why is this called time ?
//        this.rotorRotation += Easings.easeInCubic(time, this.rotorRotation, this.enginePower, 1f);

        this.rotorRotationAmount.setTarget(time);

//        this.rotorRotation += this.rotorRotationAmount.getCurrent();
//        this.rotorRotation %= 360f;



//        System.out.println(time);

        this.fallDistance = 0f;
        this.ignoreFrustumCheck = true; // always draws the entity
        // Update seats positions
//        for (HelicopterSeatEntity seat : this.seats) {
//            if (seat != null) {
//                seat.setParentID(this.heliID);
//                seat.parent = this;
//                //     seat.update();
//            }
//        }

        Seat seat = this.seats[0];

//        float REQUIRED_POWER = 0f;

        Entity controller = seat.getOccupant();
        boolean runEngine;
        if (controller != null) // There is a pilot!
        {
            EntityPlayer rider = (EntityPlayer) controller;
            if (this.world.isRemote) // We are on client
            {
                runEngine = this.handleClientRunning(rider);
                if (this.isPilotThisClient(rider)) {
                    this.updateEngine(runEngine);
                    this.engineRunning = runEngine;
                    if (this.enginePower >= REQUIRED_POWER) {
                        this.direction = this.drive(this.direction);
                    } else {
                        this.direction.set(0, 1, 0);
                    }
                }
            }
        } else {
            this.updateEngine(false);
            this.direction.set(0, 1, 0);
        }
        this.rotationDelta = - (float) (this.direction.xCoord * 1.25f);
        this.rotationYaw += rotationDelta;;
        this.interpRotationPitch.setTarget(this.direction.zCoord * -30D);
        this.interpRotationRoll.setTarget(this.direction.xCoord * 20D);

        this.updateDirection(this.direction);
        if (this.engineRunning) {
            this.enginePower+= 1f;
        } else {
            if((controller == null || this.engineMovement == -1) && this.motionY == 0) {
                this.enginePower-= 0.3f;
            }
            if (this.enginePower < 0f) {
                this.enginePower = 0f;
            }
        }
        if (this.enginePower >= REQUIRED_POWER) {
            // We can fly \o/
            // ♪ Fly on the wings of code! ♪
            MutableVec3 localDir = new MutableVec3(this.direction.xCoord, this.direction.yCoord, this.direction.zCoord * 8f);
            localDir = localDir.rotateYaw((float) Math.toRadians(-this.rotationYaw));
            final float gravityCancellation = 0.08f;
            final float speedY = gravityCancellation + 0.005f;
            double my = speedY * localDir.yCoord;
            if (my < gravityCancellation) {
                my = gravityCancellation;
            }

            if(this.engineMovement == -1) {
                this.hoverMovement = this.motionY += my * (this.enginePower / MAX_POWER) * 0.75;
            } else if(this.engineMovement == 0) {
                this.motionY = (this.hoverMovement *= 0.95);
            } else {
                this.hoverMovement = this.motionY += my * (this.enginePower / MAX_POWER);
            }
            this.motionX += localDir.xCoord * this.interpSpeed.getCurrent() * 0.005;
            this.motionZ += localDir.zCoord * this.interpSpeed.getCurrent() * 0.005;
        }
        if (this.enginePower >= MAX_POWER) {
            this.enginePower = MAX_POWER;
        }
    }

    private void updateDirection(MutableVec3 direction) {
        if (this.world.isRemote) {
            RebornMod.NETWORK_WRAPPER.sendToServer(new HelicopterDirectionMessage(this.getEntityId(), direction));
        } else {
            RebornMod.NETWORK_WRAPPER.sendToAll(new HelicopterDirectionMessage(this.getEntityId(), direction));
        }
    }


    @SideOnly(Side.CLIENT)
    private MutableVec3 drive(MutableVec3 direction) {
        direction.addVector(0, 1, 0);

        if (Minecraft.getMinecraft().gameSettings.keyBindForward.isKeyDown()) {
            direction.addVector(0, 0, 1f);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindBack.isKeyDown()) {
            direction.addVector(0, 0, -1f);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindLeft.isKeyDown()) {
            direction.addVector(1f, 0, 0);
        }

        if (Minecraft.getMinecraft().gameSettings.keyBindRight.isKeyDown()) {
            direction.addVector(-1f, 0, 0);
        }

        this.engineMovement = 0;
        if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
            this.engineMovement++;
        }

        if (KeyBindingHandler.HELICOPTER_DOWN.isKeyDown()) {
            this.engineMovement--;
        }

        return direction.normalize();
    }

    @Override
    protected void collideWithNearbyEntities() {
    }

    public void updateEngine(boolean engineState) {
        if (this.world.isRemote) {
            RebornMod.NETWORK_WRAPPER.sendToServer(new HelicopterEngineMessage(this.getEntityId(), engineState, this.engineMovement));
        } else {
            RebornMod.NETWORK_WRAPPER.sendToAll(new HelicopterEngineMessage(this.getEntityId(), engineState, this.engineMovement));
        }
    }

    /**
     * Checks if the current pilot is the player using this client
     *
     * @param pilot The pilot
     * @return True if Client's player's UUID is equal to pilot
     */
    @SideOnly(Side.CLIENT)
    private boolean isPilotThisClient(EntityPlayer pilot) {
        return pilot.getUniqueID().equals(Minecraft.getMinecraft().player.getUniqueID());
    }

    @SideOnly(Side.CLIENT)
    private boolean handleClientRunning(EntityPlayer rider) {
        if (this.isPilotThisClient(rider)) {
            if (Minecraft.getMinecraft().gameSettings.keyBindJump.isKeyDown()) {
                return true;
            }
        }
        return false;
    }

//    @Override
//    public EnumActionResult applyPlayerInteraction(EntityPlayer player, Vec3d vec, EnumHand hand) {
//        // Transforms the vector in local coordinates (cancels possible rotations to simplify 'seat detection')
//        Vec3d localVec = vec.rotateYaw((float) Math.toRadians(this.rotationYaw));
//
//        if (!this.attachModule(player, localVec, activeItemStack)) {
//
//            if (localVec.z > 0.6) {
//                player.startRiding(this.seats[0]);
//                return EnumActionResult.SUCCESS;
//            } else if (localVec.z < 0.6 && localVec.x > 0) {
//                player.startRiding(this.seats[1]);
//                return EnumActionResult.SUCCESS;
//            } else if (localVec.z < 0.6 && localVec.x < 0) {
//                player.startRiding(this.seats[2]);
//                return EnumActionResult.SUCCESS;
//            }
//            for (HelicopterModuleSpot spot : this.moduleSpots) {
//                if (spot != null && spot.isClicked(localVec)) {
//                    System.out.println(spot);
//                    spot.onClicked(player, vec);
//                    return EnumActionResult.SUCCESS;
//                }
//            }
//        }
//        return EnumActionResult.PASS;
//    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote && !player.isSneaking()) {
            player.startRiding(this);
        }
        return true;
    }

    @Override
    protected void addPassenger(Entity passenger) {
        super.addPassenger(passenger);
        if (!this.world.isRemote && passenger instanceof EntityPlayer && !(this.getControllingPassenger() instanceof EntityPlayer)) {
            Entity existing = this.seats[0].occupant;
            this.seats[0].occupant = passenger;
            this.usherPassenger(existing, 1);
        } else {
            this.usherPassenger(passenger, 0);
        }
    }

    private void usherPassenger(Entity passenger, int start) {
        for (int i = start; i < this.seats.length; i++) {
            Seat seat = this.seats[i];
            if (seat.occupant == null && seat.predicate.test(passenger)) {
                seat.occupant = passenger;
                return;
            }
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        for (Seat seat : this.seats) {
            if (passenger.equals(seat.occupant)) {
                seat.occupant = null;
                break;
            }
        }
    }

    @Override
    public void updatePassenger(Entity passenger) {
        if (this.isPassenger(passenger)) {
            Seat seat = null;
            for (Seat s : this.seats) {
                if (passenger.equals(s.occupant)) {
                    seat = s;
                    break;
                }
            }
            Vec3d pos;
            if (seat == null) {
                pos = new Vec3d(this.posX, this.posY + this.height, this.posZ);
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

    public Seat getSeat(int id) {
        if(id < seats.length) {
            return seats[id];
        }
        return null;
    }

    /**
     * Returns a boundingBox used to collide the entity with other entities and blocks. This enables the entity to be pushable on contact, like boats or minecarts.
     */
    @Override
    public AxisAlignedBB getCollisionBox(Entity entityIn) {
        return this.getBoundingBox();
    }

    /**
     * returns the bounding box for this entity
     */
    public AxisAlignedBB getBoundingBox() {
        return this.getEntityBoundingBox();
    }

    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public EnumHandSide getPrimaryHand() {
        return EnumHandSide.RIGHT;
    }

    public boolean isEngineRunning() {
        return this.engineRunning;
    }

    public void setEngineRunning(boolean engineRunning) {
        this.engineRunning = engineRunning;
    }

    public void setEngineMovement(float engineMovement) {
        this.engineMovement = engineMovement;
    }

    public float getEnginePower() {
        return this.enginePower;
    }

    public void setDirection(MutableVec3 direction) {
        this.direction.set(direction);
    }


//    public HelicopterSeatEntity getSeat(int index) {
//        if (index < 0 || index >= this.seats.length) {
//            throw new ArrayIndexOutOfBoundsException(index + ", size is " + this.seats.length);
//        }
//        return this.seats[index];
//    }
//
//    public void setSeat(int index, HelicopterSeatEntity seat) {
//        this.seats[index] = seat;
//    }

    public final class Seat {
        private final int index;

        private float offsetX;
        private float offsetY;
        private float offsetZ;

        private final float radius;
        private final float height;
        private final Predicate<Entity> predicate;

        private Entity occupant;

        public Seat(int index, float offsetX, float offsetY, float offsetZ, float radius, float height) {
            this(index, offsetX, offsetY, offsetZ, radius, height, entity -> true);
        }

        public Seat(int index, float offsetX, float offsetY, float offsetZ, float radius, float height, Predicate<Entity> predicate) {
            this.index = index;
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.radius = radius;
            this.height = height;
            this.predicate = predicate;
        }

        protected void so(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
        }

        public Entity getOccupant() {
            return this.occupant;
        }

        public Vec3d getPos() {
            double theta = Math.toRadians(HelicopterEntityNew.this.rotationYaw);
            double sideX = Math.cos(theta);
            double sideZ = Math.sin(theta);
            double forwardTheta = theta + Math.PI / 2;
            double forwardX = Math.cos(forwardTheta);
            double forwardZ = Math.sin(forwardTheta);
            double x = HelicopterEntityNew.this.posX + sideX * this.offsetX + forwardX * this.offsetZ;
            double y = HelicopterEntityNew.this.posY + this.offsetY;
            double z = HelicopterEntityNew.this.posZ + sideZ * this.offsetX + forwardZ * this.offsetZ;
            return new Vec3d(x, y, z);
        }

        public AxisAlignedBB getBounds() {
            Vec3d pos = this.getPos();
            double x = pos.x;
            double y = pos.y;
            double z = pos.z;
            return new AxisAlignedBB(x - this.radius, y, z - this.radius, x + this.radius, y + this.offsetY + this.height, z + this.radius);
        }
    }
}