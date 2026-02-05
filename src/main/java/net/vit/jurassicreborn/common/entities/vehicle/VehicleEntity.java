package net.vit.jurassicreborn.common.entities.vehicle;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.entity.Entity.MoveFunction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.client.sounds.CarLoopSound;
import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.network.CarEntityPlayRecord;
import net.vit.jurassicreborn.common.network.Network;
import org.jetbrains.annotations.Nullable;
import net.minecraft.world.phys.Vec2;
import com.mojang.math.Vector4f;
import java.util.List;

public abstract class VehicleEntity extends Entity {
    // Synched data
    public static final EntityDataAccessor<Byte> WATCHER_STATE = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Float> WATCHER_HEALTH = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> WATCHER_SPEED = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<ItemStack> RECORD_ITEM = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<CompoundTag> WATCHER_SEATS = SynchedEntityData.defineId(VehicleEntity.class, EntityDataSerializers.COMPOUND_TAG);

    public static final float MAX_HEALTH = 40;
    private static final byte LEFT = 0b000001;
    private static final byte RIGHT = 0b000010;
    private static final byte FORWARD = 0b000100;
    private static final byte BACKWARD = 0b001000;
    private float wheelFrameProgress;
    protected final Seat[] seats = createSeats();
    protected final WheelData wheeldata = createWheels();

    public float wheelRotation;
    public float wheelRotateAmount;
    public float prevWheelRotateAmount;
    public InterpValue steerAmount;
    public float pitch;
    public float roll;
    protected float rotationDelta;
    private boolean noiseInstance;
    public int interpProgress;
    double interpTargetX;
    public double interpTargetY;
    public double interpTargetZ;
    public double interpTargetYaw;
    public float speedModifier = 0f;
    private static final double INTERP_AMOUNT = 0.15D;
    public boolean wasOnGroundLastTick;
    private Vec3 prevUnairbornPos;

    public final InterpValue backValue = new InterpValue(this, INTERP_AMOUNT);
    public final InterpValue frontValue = new InterpValue(this, INTERP_AMOUNT);
    public final InterpValue leftValue = new InterpValue(this, INTERP_AMOUNT);
    public final InterpValue rightValue = new InterpValue(this, INTERP_AMOUNT);

    public final CarWheel backLeftWheel = new CarWheel(0, wheeldata.bl);
    public final CarWheel backRightWheel = new CarWheel(1, wheeldata.br);
    public final CarWheel frontLeftWheel = new CarWheel(2, wheeldata.fl);
    public final CarWheel frontRightWheel = new CarWheel(3, wheeldata.fr);

    @SuppressWarnings("unchecked")
    public final List<WheelParticleData>[] wheelDataList = new List[4];

    public List<CarWheel> allWheels = Lists.newArrayList(backLeftWheel, frontLeftWheel, backRightWheel, frontRightWheel);

    @OnlyIn(Dist.CLIENT)
    public CarLoopSound engineSound;
    private static final Item[] STATIONS = new Item[] {
            Items.MUSIC_DISC_13,
            Items.MUSIC_DISC_CAT,
            Items.MUSIC_DISC_11,
            Items.MUSIC_DISC_13,
            Items.MUSIC_DISC_BLOCKS,
            Items.MUSIC_DISC_CHIRP,
            Items.MUSIC_DISC_MALL,
            Items.MUSIC_DISC_MELLOHI,
            Items.MUSIC_DISC_OTHERSIDE,
            Items.MUSIC_DISC_PIGSTEP,
            Items.MUSIC_DISC_STAL,
            Items.MUSIC_DISC_STRAD,
            Items.MUSIC_DISC_WAIT,
            Items.MUSIC_DISC_WARD,
            Items.MUSIC_DISC_FAR
    };
    private float healAmount;
    private int healCooldown = 40;

    private Vec3 previousPosition = null;
    private long prevWorldTime = -1;
    public double estimatedSpeed = 0D;

    private byte prevState = 0;

    public VehicleEntity(EntityType<? extends VehicleEntity> type, Level world) {
        super(type, world);
        this.setBoundingBox(new AABB(this.getX(), this.getY(), this.getZ(), this.getX() + 3.0F, this.getY() + 2.5F, this.getZ() + 3.0F));
        this.maxUpStep = 1.5F;
        if (world.isClientSide) {
            this.steerAmount = new InterpValue(this, 0.1D);
        }
        for (int i = 0; i < 4; i++) {
            this.wheelDataList[i] = Lists.newArrayList();
        }
        backLeftWheel.setPair(backRightWheel);
        frontLeftWheel.setPair(frontRightWheel);
    }

    protected abstract void dropFromLootTable(boolean causedByPlayer);

    /**
     * Drops the item stack that spawned this vehicle if one was recorded, otherwise falls back to
     * the loot table implementation. Ensures we only ever drop a single vehicle item.
     */
    protected void dropRecordedItemOrLoot(boolean causedByPlayer) {
        if (this.level.isClientSide) return;

        ItemStack record = this.getItem();
        if (!record.isEmpty()) {
            this.spawnAtLocation(record);
        } else {
            this.dropFromLootTable(causedByPlayer);
        }
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(WATCHER_STATE, (byte) 0);
        this.entityData.define(WATCHER_HEALTH, MAX_HEALTH);
        this.entityData.define(WATCHER_SPEED, 1);
        this.entityData.define(RECORD_ITEM, ItemStack.EMPTY);
        CompoundTag s = new CompoundTag();
        for (int i = 0; i < createSeats().length; i++) {
            s.putString(str(i), "");
        }
        this.entityData.define(WATCHER_SEATS, s);
    }

    public boolean left() { return getStateBit(LEFT); }
    public boolean right() { return getStateBit(RIGHT); }
    public boolean forward() { return getStateBit(FORWARD); }
    public boolean backward() { return getStateBit(BACKWARD); }
    public void left(boolean b) { setStateBit(LEFT, b); }
    public void right(boolean b) { setStateBit(RIGHT, b); }
    public void forward(boolean b) { setStateBit(FORWARD, b); }
    public void backward(boolean b) { setStateBit(BACKWARD, b); }
    protected boolean getStateBit(byte mask) { return (getControlState() & mask) != 0; }
    protected void setStateBit(byte mask, boolean newState) {
        byte state = getControlState();
        setControlState((byte) (newState ? state | mask : state & ~mask));
    }

    public byte getControlState() { return this.entityData.get(WATCHER_STATE); }
    public byte getPreviousState() { return this.prevState; }
    public void setControlState(byte state) { this.entityData.set(WATCHER_STATE, state); }
    public void setPreviousState(byte state) { this.prevState = state; }

    public void setSpeed(Speed speed) { this.entityData.set(WATCHER_SPEED, speed.ordinal()); }
    public Speed getSpeed() { return Speed.values()[this.entityData.get(WATCHER_SPEED)]; }
    public void setHealth(float health) { this.entityData.set(WATCHER_HEALTH, health); }
    public float getHealth() { return this.entityData.get(WATCHER_HEALTH); }
    public ItemStack getItem() { return this.entityData.get(RECORD_ITEM); }

    /** Stores a copy of the item stack that spawned this vehicle for later drops. */
    public void setItem(ItemStack stack) {
        this.entityData.set(RECORD_ITEM, stack.copy());
    }

    @Override
    public boolean shouldRenderAtSqrDistance(double dist) { return true; }

    @Override
    protected boolean canAddPassenger(Entity passenger) { return this.getPassengers().size() < this.seats.length; }

    @Nullable
    @Override
    public Entity getControllingPassenger() {
        String id = getIfExists(0, false);
        if (id.equals("")) return null;
        try {
            int entityId = Integer.parseInt(id);
            return level.getEntity(entityId);
        } catch (Exception ignored) {}
        return null;
    }

    @Nullable
    public Entity getEntityInSeat(int seatID) {
        String id = getIfExists(seatID, false);
        if (id.equals("")) return null;
        try {
            int entityId = Integer.parseInt(id);
            return level.getEntity(entityId);
        } catch (Exception ignored) {}
        return null;
    }
    @OnlyIn(Dist.CLIENT)
    public float pitch(float partialTicks) {
        return this.pitch;
    }

    @OnlyIn(Dist.CLIENT)
    public float roll(float partialTicks) {
        return this.roll;
    }
    public String getIfExists(int seatID, boolean reset) {
        String string = this.entityData.get(WATCHER_SEATS).getString(str(seatID));
        if (!string.equals("")) {
            Entity entity = null;
            try { entity = level.getEntity(Integer.parseInt(string)); } catch (Exception ignored) {}
            if (!(entity != null && entity.getVehicle() == this)) {
                if (reset) setSeat(str(seatID), "");
                return "";
            } else {
                return string;
            }
        }
        return "";
    }

    @Override
    public boolean isPickable() { return true; }

    public Vector4f
 getCarDimensions() { return this.wheeldata.carVector; }
    public Vec2 getBackWheelRotationPoint() { return new Vec2(-0.5F, 1.4F); }

    // Interp logic for remote move (handle on client only!)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int duration) {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = yaw;
        this.interpProgress = duration;
    }

    /* --------------------------------------------------------------------- */
    /*  PASSENGERS                                                           */
    /* --------------------------------------------------------------------- */
    protected void doBlockCollisions() {
    }

    // Gravity/damage/fall
    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        if (!level.isClientSide) {
            float damage = Mth.ceil((fallDistance - 3F) * damageMultiplier);
            if (damage > 0) {
                this.setHealth(this.getHealth() - (damage * 1.25F));
                this.checkAndHandleDeath();
            }
        }
        return false;
    }

    protected double calculateWheelHeight(double distance, boolean rotate90) {
        final Level lvl = this.level;                 // new naming in 1.19
        float localYaw = this.yRotO + (this.getYRot() - this.yRotO);   // prevRotationYaw → yRotO
        double bestY = this.getY() - 4;               // start below vehicle, will climb up

        Vector4f carVec = wheeldata.carVector;
        double side = Math.abs(rotate90 ? carVec.x() - carVec.z() : carVec.z() - carVec.w());
        for (double d = -side; d <= side; d += 0.25D) {
            // rotate wheel sample point into world space
            double xRot =  Math.sin(Math.toRadians(localYaw)) * (rotate90 ? d : distance)
                    - Math.cos(Math.toRadians(localYaw)) * (rotate90 ? distance : d);
            double zRot = -Math.cos(Math.toRadians(localYaw)) * (rotate90 ? d : distance)
                    - Math.sin(Math.toRadians(localYaw)) * (rotate90 ? distance : d);

            Vec3  sample   = new Vec3(getX() + xRot, getY(), getZ() + zRot);
            BlockPos pos = new BlockPos(Mth.floor(sample.x), Mth.floor(sample.y), Mth.floor(sample.z));
            boolean found  = false;
            double  topY   = Double.NEGATIVE_INFINITY;

            // walk downward until we hit collision
            while (!found && pos.getY() >= lvl.getMinBuildHeight()) {
                BlockState state = lvl.getBlockState(pos);
                VoxelShape shape = state.getCollisionShape(lvl, pos);
                if (state.isAir() || shape.isEmpty()) {
                    pos = pos.below();
                    continue;
                }
                // found solid; record highest box of shape
                topY = pos.getY() + shape.max(Direction.Axis.Y);
                found = true;
            }

            if (!found) {
                // nothing solid beneath this X/Z column → keep vehicle height
                bestY = Math.max(bestY, getY());
                continue;
            }

            // obstructed?  make sure there’s head-room for wheels
            BlockPos above  = pos.above();
            BlockPos above2 = pos.above(2);
            if (!lvl.getBlockState(above).isAir() || !lvl.getBlockState(above2).isAir()) {
                bestY = Math.max(bestY, getY());
                continue;
            }

            bestY = Math.max(bestY, topY);
        }

        return bestY;
    }


    protected void handleControl() {
        // default implementation does nothing
    }
    // Core tick
    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide && !noiseInstance) {
            noiseInstance = true;
            startSound();
        }
        // Dead check
        this.checkAndHandleDeath();


        // Heal
        if (this.healCooldown > 0) this.healCooldown--;
        else if (this.healAmount > 0) {
            this.setHealth(this.getHealth() + 1);
            this.healAmount--;
            if (this.getHealth() > MAX_HEALTH) {
                this.setHealth(MAX_HEALTH);
                this.healAmount = 0;
            }
        }

        // Speed estimation
        if (previousPosition == null) previousPosition = this.position();
        estimatedSpeed = previousPosition.distanceTo(this.position()) / (level.getGameTime() - prevWorldTime + 1);
        previousPosition = this.position();
        prevWorldTime = level.getGameTime();

        // Wheels and particles
        for (int i = 0; i < 4; i++) {
            List<WheelParticleData> markedRemoved = Lists.newArrayList();
            wheelDataList[i].forEach(wheel -> wheel.onUpdate(markedRemoved));
            markedRemoved.forEach(wheelDataList[i]::remove);
        }

        // Process wheels
        this.allWheels.forEach(this::processWheel);
        this.spawnWheelParticles();

        // Suspension math
        Vector4f vec = wheeldata.carVector;
        this.backValue.setTarget(this.calculateWheelHeight(vec.y(), false));
        this.frontValue.setTarget(this.calculateWheelHeight(vec.w(), false));
        this.leftValue.setTarget(this.calculateWheelHeight(vec.z(), true));
        this.rightValue.setTarget(this.calculateWheelHeight(vec.x(), true));
        // Interp and motion
        this.tickInterp();
        this.updateMotion();

        // Control state
        if (this.getPassengers().isEmpty() || !(this.getPassengers().get(0) instanceof Player)) {
            this.setControlState((byte) 0);
        }
        if (level.isClientSide) {
            handleControl();
        }
        // Client input -- needs to be hooked with KeyMappings, use events not direct MovementInput in 1.19.2
        updateWheelSpin();           // we’ll fix this next
        advanceInterpolations();
        this.applyMovement();
        this.move(MoverType.SELF, getDeltaMovement());
        updateWheelSpin();
        if (level.isClientSide) {
            updateSeatAnimations();
        }
        clientAnimate();
    }

    protected void updateMotion() {
        final double resist = 0.8F;
        setDeltaMovement(getDeltaMovement().scale(resist));
        this.rotationDelta *= resist;
        if (!this.isNoGravity()) setDeltaMovement(getDeltaMovement().add(0, -0.15F, 0));
    }
    /** converts travelled distance this tick → wheel rotation values */
    private void updateWheelSpin() {
        // keep last tick’s value for partial-tick lerp in the renderer
        prevWheelRotateAmount = wheelRotateAmount;

        /* distance travelled (horizontal only) */
        double dx = getX() - xOld;      // xOld / zOld are set by Entity each tick
        double dz = getZ() - zOld;
        double dist = Math.sqrt(dx*dx + dz*dz);

        /* how much to rotate this frame – tune the multiplier to taste            *
         *  0.35 gives roughly one full revolution per block for 1-block wheels.   */
        float delta = (float)(dist * 0.35 * 360.0);   // blocks → degrees

        wheelRotateAmount = Mth.lerp(0.4F, wheelRotateAmount, delta); // smooth
        wheelRotation += wheelRotateAmount;                           // phase
    }
    private void updateSeatAnimations() {
        for (Seat seat : seats) {
            seat.getInterpValue().tick();  // advances toward its target
        }
    }

    protected void applyMovement() {
        Speed speed = this.getSpeed();
        float moveAmount = 0.0f;
        if ((this.left() || this.right()) && !(this.forward() || this.backward())) moveAmount += 0.05F;
        if (this.forward()) moveAmount += 0.1F;
        else if (this.backward()) moveAmount -= 0.05F;
        moveAmount *= (speed.modifier + this.speedModifier);
        if (this.isInWater()) {
            moveAmount -= 0.1f;
            if (moveAmount < 0f) moveAmount = 0f;
        }
        if (this.left()) this.rotationDelta -= 20.0F * moveAmount;
        else if (this.right()) this.rotationDelta += 20.0F * moveAmount;
        this.rotationDelta = Mth.clamp(this.rotationDelta, -3F, 3F);
        this.setYRot(getYRot() + this.rotationDelta);
        setDeltaMovement(getDeltaMovement().add(
                Mth.sin(-getYRot() * 0.017453292F) * moveAmount,
                0,
                Mth.cos(getYRot() * 0.017453292F) * moveAmount
        ));
    }
    // Put near other private helpers
    private boolean didDieOnce = false;

    private void handleDeath(@Nullable Entity killer) {
        if (this.level.isClientSide || this.didDieOnce || this.isRemoved()) return;
        this.didDieOnce = true;

        boolean causedByPlayer = killer instanceof Player;
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
            this.dropRecordedItemOrLoot(causedByPlayer);
        }
        this.discard();
    }

    private void checkAndHandleDeath() {
        if (!this.level.isClientSide && this.getHealth() <= 0f) {
            handleDeath(null);
        }
    }

    private void tickInterp() {
        if (this.interpProgress > 0 && !this.isControlledByLocalInstance()) {
            double interpolatedX = this.getX() + (this.interpTargetX - this.getX()) / this.interpProgress;
            double interpolatedY = this.getY() + (this.interpTargetY - this.getY()) / this.interpProgress;
            double interpolatedZ = this.getZ() + (this.interpTargetZ - this.getZ()) / this.interpProgress;
            double deltaYaw = Mth.wrapDegrees(this.interpTargetYaw - this.getYRot());
            this.setYRot((float) (this.getYRot() + deltaYaw / this.interpProgress));
            this.interpProgress--;
            this.setPos(interpolatedX, interpolatedY, interpolatedZ);
        }
    }
    protected boolean canPassengerSteer() {
        Entity entity = getControllingPassenger();
        return entity instanceof Player;
    }
    /** call this at the very end of tick(), after move() */
    private void clientAnimate() {
        if (!level.isClientSide) return;          // server never touches visuals

        /* -------- wheel spin -------- */

        /* -------- doors / seat easing -------- */
        for (Seat seat : seats) {
            seat.getInterpValue().update();   // advances toward its current target
        }
    }


    @Override
    public void positionRider(Entity passenger) {
        if (!this.hasPassenger(passenger)) return;

        int seatId = getSeatForEntity(passenger);
        Seat seat  = seatId >= 0 ? seats[seatId] : null;

        double baseY = getY() + getPassengersRidingOffset()/5;

        double px = getX();
        double py = baseY + passenger.getMyRidingOffset();     // always apply rider offset
        double pz = getZ();

        if (seat != null) {
            Vec3 seatPos = seat.getPos(this);
            px = seatPos.x;
            py = baseY + seat.getOffsetY() + passenger.getMyRidingOffset();  // seat lift
            pz = seatPos.z;
        }

        passenger.setPos(px, py, pz);

        // keep orientation in sync with the vehicle’s steering
        passenger.setYRot(passenger.getYRot() + rotationDelta);
        passenger.setYHeadRot(passenger.getYHeadRot() + rotationDelta);
        if (passenger instanceof LivingEntity living) {
            living.yBodyRot += rotationDelta;
        }
    }

    @Override
    protected void removePassenger(Entity passenger) {
        // Ensure the rider is positioned at the vehicle's latest location
        // before the association is broken, otherwise the player can
        // momentarily snap back to a previous tick when dismounting.
        positionRider(passenger);

        // When the vehicle stops being locally controlled and becomes a
        // regular server-driven entity, its previous position still points
        // to an outdated location. Updating the "old" position here keeps
        // the client from rendering the vehicle a few blocks behind where
        // it actually is once the rider dismounts.
        this.setOldPosAndRot();

        // Cancel any interpolation that might still be in progress from
        // when the vehicle was client-controlled. Otherwise, the vehicle
        // will slowly lerp back to the server position over many ticks
        // after the rider dismounts.
        this.interpTargetX = this.getX();
        this.interpTargetY = this.getY();
        this.interpTargetZ = this.getZ();
        this.interpTargetYaw = this.getYRot();
        this.interpProgress = 0;

        // Force a teleport update so all tracking clients immediately
        // receive the vehicle's true location instead of slowly interpolating
        // back to it. Without this, the server-correct position may only be
        // reached after many ticks, making the car appear several blocks
        // behind the dismount point.
        if (!this.level.isClientSide) {
            ((ServerLevel) this.level).getChunkSource().broadcast(this, new ClientboundTeleportEntityPacket(this));
        }

        // Determine seat before dismounting so we can clear the slot
        int seat = getSeatForEntity(passenger);
        super.removePassenger(passenger);
        if (seat != -1) {
            setSeat(Integer.toString(seat), "");
        }
        passenger.noPhysics = false;
    }
    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) return false;
        if (!this.level.isClientSide) {
            if (source.getEntity() instanceof Player) {
                amount *= 10f;
                this.healAmount += amount;
                this.healCooldown = 40;
            }
            this.setHealth(this.getHealth() - amount);
            if (this.getHealth() <= 0f) {
                handleDeath(source.getEntity());
            }
        }
        return true;
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.setHealth(compound.getFloat("Health"));
        this.healAmount = compound.getFloat("HealAmount");
        this.setSpeed(Speed.values()[compound.getInt("Speed")]);
        CompoundTag tag = compound.getCompound("InterpValues");
        this.backValue.deserializeNBT(tag.getCompound("Back"));
        this.frontValue.deserializeNBT(tag.getCompound("Front"));
        this.leftValue.deserializeNBT(tag.getCompound("Left"));
        this.rightValue.deserializeNBT(tag.getCompound("Right"));
        this.entityData.set(RECORD_ITEM, ItemStack.of(compound.getCompound("RecordItem")));
    }
    private void advanceInterpolations() {
        if (!level.isClientSide) return;

        if (steerAmount != null) steerAmount.tick(); // steering wheel
        for (Seat s : seats) s.getInterpValue().tick();  // every door/seat
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putFloat("Health", this.getHealth());
        compound.putFloat("HealAmount", this.healAmount);
        compound.putInt("Speed", this.getSpeed().ordinal());
        CompoundTag tag = new CompoundTag();
        tag.put("Back", this.backValue.serializeNBT());
        tag.put("Front", this.frontValue.serializeNBT());
        tag.put("Left", this.leftValue.serializeNBT());
        tag.put("Right", this.rightValue.serializeNBT());
        compound.put("InterpValues", tag);
        compound.put("RecordItem", this.entityData.get(RECORD_ITEM).save(new CompoundTag()));
    }

    // Utility methods
    private String str(int input) { return Integer.toString(input); }

    // --------- SEATS, PASSENGERS ---------
    public void setSeat(String seatID, String uuid) {
        CompoundTag comp = this.entityData.get(WATCHER_SEATS).copy();
        comp.putString(seatID, uuid);
        this.entityData.set(WATCHER_SEATS, comp);
        // TODO: handle control state update/network sync if needed
    }

    public boolean tryPutInSeat(Entity passenger, int seatID, boolean isPacket) {
        if (seatID < this.seats.length && seatID >= 0) {
            int seatNumber = getSeatForEntity(passenger);
            if ((seatNumber == 0 && seatID == 0 && !isPacket) || getIfExists(seatID, false).equals("")) {
                if (seatNumber != -1) {
                    setSeat(str(seatNumber), "");
                }
                setSeat(str(seatID), str(passenger.getId()));
                return true;
            }
        }
        return false;
    }

    @Nullable
    public int getSeatForEntity(Entity entity) {
        for (int i = 0; i < this.seats.length; i++) {
            if (getIfExists(i, true).equals(str(entity.getId()))) {
                return i;
            }
        }
        return -1;
    }

    public Seat getSeat(int id) {
        if (id < seats.length) return seats[id];
        return null;
    }
    public int getSeatCount() {
        return seats.length;
    }

    public void cycleSeat(Entity passenger) {
        int current = getSeatForEntity(passenger);
        if (current == -1) return;
        int next = (current + 1) % seats.length;
        if (tryPutInSeat(passenger, next, false)) {
            passenger.startRiding(this, true);
        }
    }


    public void cycleStation() {
        if (level.isClientSide) return;
        ItemStack current = getItem();
        int idx = -1;
        for (int i = 0; i < STATIONS.length; i++) {
            if (current.is(STATIONS[i])) {
                idx = i;
                break;
            }
        }
        int next = (idx + 1) % STATIONS.length;
        ItemStack record = new ItemStack(STATIONS[next]);
        this.entityData.set(RECORD_ITEM, record);
        Network.sendToAllNear(level, blockPosition(), new CarEntityPlayRecord(getId(), record));
    }
    // --- WHEELS, PARTICLES ---
    /**
     * Calculates the world-space position of a wheel and stores it on the wheel.
     * Tyre track creation happens later once all wheels have been updated so
     * that each wheel has access to its opposite's position from the same tick.
     */
    protected void processWheel(CarWheel wheel) {
        float localYaw = this.getYRot(); // No lerp here for now
        Vec2 relPos = wheel.getRelativeWheelPosition();
        double xRot = Math.sin(Math.toRadians(localYaw)) * relPos.y - Math.cos(Math.toRadians(localYaw)) * relPos.x;
        double zRot = -Math.cos(Math.toRadians(localYaw)) * relPos.y - Math.sin(Math.toRadians(localYaw)) * relPos.x;
        Vec3 vec = new Vec3(getX() + xRot, this.getY(), getZ() + zRot);
        int groundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mth.floor(vec.x), Mth.floor(vec.z)) - 1;
        Vec3 groundPos = new Vec3(vec.x, groundY, vec.z);
        wheel.setCurrentWheelPos(groundPos);
    }

    /**
     * Creates tyre track particle data for each wheel. By doing this after all
     * wheels have updated their positions we ensure that both sides of an axle
     * use matching positions, fixing misaligned tracks.
     */
    protected void spawnWheelParticles() {
        if (!level.isClientSide) return;

        for (CarWheel wheel : allWheels) {
            CarWheel opposite = wheel.getOppositeWheel();
            if (opposite == null) continue;

            Vec3 groundPos = wheel.getCurrentWheelPos();
            BlockPos pos = new BlockPos(groundPos.x, groundPos.y, groundPos.z);
            BlockState ground = level.getBlockState(pos);
            boolean allowed = (ground.getMaterial() == net.minecraft.world.level.material.Material.GRASS
                    || ground.getMaterial() == net.minecraft.world.level.material.Material.DIRT
                    || ground.getMaterial() == net.minecraft.world.level.material.Material.SAND)
                    && ground.isFaceSturdy(level, pos, Direction.UP)
                    && level.getBlockState(pos.above()).getMaterial() != net.minecraft.world.level.material.Material.WATER;
            if (!allowed) continue;

            Vec3 opp = opposite.getCurrentWheelPos();
            int oppGroundY = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                    Mth.floor(opp.x), Mth.floor(opp.z)) - 1;
            Vec3 oppGroundPos = new Vec3(opp.x, oppGroundY, opp.z);
            wheelDataList[wheel.getID()].add(new WheelParticleData(groundPos, oppGroundPos, level.getGameTime()));
        }
    }
    // --------- Inner classes ---------
    /* ------------------------------------------------------ */
    /*  Seat – keeps its own easing value & geometry helpers   */
    /* ------------------------------------------------------ */
    public static final class Seat {
        /** Per-seat easing (used by doors, seat-belts, etc.) */
        private final InterpValue interpValue = new InterpValue(() -> true, 0.10D);

        private float offsetX, offsetY, offsetZ;
        private final float radius;
        private final float height;

        public Seat(float offsetX, float offsetY, float offsetZ, float radius, float height) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            this.offsetZ = offsetZ;
            this.radius  = radius;
            this.height  = height;
        }

        /** legacy setter (`so` in the 1.12 code) */
        public void setOffsets(float x, float y, float z) {
            this.offsetX = x;
            this.offsetY = y;
            this.offsetZ = z;
        }

        /* ----------------  spatial helpers  ---------------- */
        public Vec3 getPos(VehicleEntity car) {
            double theta = Math.toRadians(car.getYRot());
            double sideX = Math.cos(theta);
            double sideZ = Math.sin(theta);
            double fTheta = theta + Math.PI / 2;
            double fX = Math.cos(fTheta);
            double fZ = Math.sin(fTheta);

            double x = car.getX() + sideX * offsetX + fX * offsetZ;
            double y = car.getY() + offsetY;
            double z = car.getZ() + sideZ * offsetX + fZ * offsetZ;
            return new Vec3(x, y, z);
        }

        public AABB getBounds(VehicleEntity car) {
            Vec3 p = getPos(car);
            return new AABB(
                    p.x - radius, p.y,                       p.z - radius,
                    p.x + radius, p.y + offsetY + height,    p.z + radius);
        }

        public float getOffsetY() { return offsetY; }
        public float getOffsetZ() { return offsetZ; }

        /* ------------- animation / door helper ------------- */
        public InterpValue getInterpValue() { return interpValue; }
    }


    protected static class WheelData {
        public final Vec2 bl, br, fl, fr;
        public final Vector4f carVector;
        public WheelData(double backLeftX, double backLeftZ, double frontRightX, double frontRightZ) {
            bl = new Vec2((float) backLeftX, (float) backLeftZ);
            br = new Vec2((float) frontRightX, (float) backLeftZ);
            fl = new Vec2((float) backLeftX, (float) frontRightZ);
            fr = new Vec2((float) frontRightX, (float) frontRightZ);
            carVector = new Vector4f((float) backLeftX, (float) backLeftZ, (float) frontRightX, (float) frontRightZ);
        }
    }

    public enum Speed {
        SLOW(0.5f), MEDIUM(1f), FAST(2f);
        public final float modifier;
        Speed(float modifier) { this.modifier = modifier; }
    }
    @Override
    public InteractionResult interact(Player player, InteractionHand hand) {
        if (hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;

        if (!level.isClientSide) {
            // If the player is already riding this vehicle, do nothing
            if (player.getVehicle() == this) return InteractionResult.CONSUME;

            // Find an empty seat
            int seat = getSeatForEntity(player);
            if (seat == -1) {
                for (int i = 0; i < seats.length; i++) {
                    if (getIfExists(i, false).equals("")) { seat = i; break; }
                }
            }

            if (seat != -1 && tryPutInSeat(player, seat, false)) {
                player.startRiding(this, true);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }


    @OnlyIn(Dist.CLIENT)
    public float getSoundVolume() {
        return (Math.abs(this.wheelRotateAmount) + 0.001F)
                / (this.engineSound == null || this.engineSound.isStopped() ? 2f : 4f);
    }

    @OnlyIn(Dist.CLIENT)
    public void startSound() {
        if (this.engineSound == null) {
            this.engineSound = new CarLoopSound(this, SoundHandler.CAR_MOVE, SoundSource.RECORDS,
                    v -> !v.isRemoved());
            Minecraft.getInstance().getSoundManager().play(this.engineSound);
        }
    }
    // -------- Abstracts to implement --------
    protected abstract Seat[] createSeats();
    protected abstract WheelData createWheels();
    public abstract void dropItems();
}
