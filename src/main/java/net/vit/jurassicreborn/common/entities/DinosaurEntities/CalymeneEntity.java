package net.vit.jurassicreborn.common.entities.DinosaurEntities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.frog.Frog;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.IHasVariants;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;

import java.util.EnumSet;
import java.util.Locale;

public class CalymeneEntity extends SwimmingDinosaurEntity implements IHasVariants {
    private static final EntityDataAccessor<Integer> VARIANT =
            SynchedEntityData.defineId(CalymeneEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ROLLED =
            SynchedEntityData.defineId(CalymeneEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> ON_BOTTOM =
            SynchedEntityData.defineId(CalymeneEntity.class, EntityDataSerializers.BOOLEAN);

    // --- Behavior tuning ---
    private static final double FLOOR_CLEARANCE   = 0.06D;   // ~6 cm above seafloor
    private static final double CONTACT_EPSILON   = 0.10D;   // consider "on bottom" within this distance
    private static final int    PANIC_ROLL_MIN    = 60;      // 3s
    private static final int    PANIC_ROLL_VAR    = 40;      // +0–2s
    private static final int    SWIM_BURST_MIN    = 60;      // 3s swim burst
    private static final int    SWIM_BURST_VAR    = 60;      // +0–3s
    private static final float  MAX_STEP_UP       = 1.0F;    // climb one block while crawling

    // Timers
    private int rollTicks;        // rolling into a ball
    private int panicTicks;       // general "spooked" timer (drives bursts)
    private int swimBurstTicks;   // temporarily ignore bottom-walk and actually swim

    public CalymeneEntity(Level world, EntityType<CalymeneEntity> type) {
        super(world, type, DinosaurHandler.CALYMENE);
        this.target(AlvarezsaurusEntity.class, BeelzebufoEntity.class, Squid.class, Cod.class, Dolphin.class,
                Salmon.class, TropicalFish.class, Turtle.class, Axolotl.class, GlowSquid.class, Frog.class,
                Tadpole.class, CompsognathusEntity.class, LeptictidiumEntity.class);

        this.setVariant(this.getRandom().nextInt(4));
        this.setMaxUpStep(MAX_STEP_UP);
        this.goalSelector.addGoal(0, new BottomCrawlGoal(this, 1.05D, 8, 6));
    }


    @Override
    public void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT, 0);
        this.entityData.define(ROLLED, Boolean.FALSE);
        this.entityData.define(ON_BOTTOM, Boolean.FALSE);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("Variant", this.entityData.get(VARIANT));
        nbt.putInt("RollTicks", this.rollTicks);
        nbt.putInt("PanicTicks", this.panicTicks);
        nbt.putInt("SwimBurstTicks", this.swimBurstTicks);
    }
    public boolean isOnBottom() { return this.entityData.get(ON_BOTTOM); }
    private void setOnBottom(boolean v) { this.entityData.set(ON_BOTTOM, v); }

    @Override
    public void readAdditionalSaveData(CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);
        this.entityData.set(VARIANT, nbt.getInt("Variant"));
        this.rollTicks      = nbt.getInt("RollTicks");
        this.panicTicks     = nbt.getInt("PanicTicks");
        this.swimBurstTicks = nbt.getInt("SwimBurstTicks");
        this.setRolled(this.rollTicks > 0);
    }

    public void setVariant(int value){ this.entityData.set(VARIANT, value); }
    public int getVariant(){ return this.entityData.get(VARIANT); }

    public boolean isRolled() { return this.entityData.get(ROLLED); }
    public void setRolled(boolean v){ this.entityData.set(ROLLED, v); }

    public boolean isPanicking() { return panicTicks > 0 || this.getTarget() != null; }
    private void startSwimBurst(RandomSource r) {
        this.swimBurstTicks = SWIM_BURST_MIN + r.nextInt(SWIM_BURST_VAR + 1);
    }

    /** Trigger defensive enrollment (rolling into a ball) for a short time. */
    public void triggerRoll(int durationTicks) {
        this.rollTicks = Math.max(this.rollTicks, durationTicks);
        this.setRolled(true);
        // Optional: small slowdown while rolled
        this.setSpeed((float)(this.getSpeed() * 0.5F));
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean res = super.hurt(source, amount);
        if (!this.level().isClientSide && this.isInWater() && !this.isCarcass()) {
            // Start/extend panic, maybe roll, and kick off a swim burst
            this.panicTicks = Math.max(this.panicTicks, 80 + this.getRandom().nextInt(80)); // 4–8s panic
            if (this.getHealth() / this.getMaxHealth() < 0.35F || this.getRandom().nextFloat() < 0.20F) {
                this.triggerRoll(PANIC_ROLL_MIN + this.getRandom().nextInt(PANIC_ROLL_VAR + 1)); // 3–5s
            }
            startSwimBurst(this.getRandom());
        }
        return res;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.rollTicks > 0) {
            if (--this.rollTicks <= 0) this.setRolled(false);
        }
        if (this.panicTicks > 0) this.panicTicks--;
        if (this.swimBurstTicks > 0) this.swimBurstTicks--;

        // Gentle bottom correction (server-side), only when not actively bursting
        if (!this.level().isClientSide && this.isInWater() && !this.isCarcass() && this.swimBurstTicks == 0) {
            double floorY = findSeafloorY(this.getX(), this.getY(), this.getZ());
            if (!Double.isNaN(floorY)) {
                final double targetY = floorY + FLOOR_CLEARANCE;
                final double dy = targetY - this.getY();

                // PD controller: pull toward the floor without bounce
                Vec3 vel = this.getDeltaMovement();
                double correction = dy * 0.18D - vel.y * 0.35D; // kP=0.18, kD=0.35
                this.setDeltaMovement(vel.x * 0.92D, vel.y + correction, vel.z * 0.92D);
            }
        }
    }

    /**
     * Replace base travel in water with a bottom-walk biased movement that can burst-swim.
     */
    @Override
    public void travel(Vec3 vec) {
        if (this.isInWater() && !this.isCarcass()) {

            // Burst-swim uses base behavior; flag off-bottom to play swim anim
            if (this.swimBurstTicks > 0) {
                this.setOnBottom(false);
                super.travel(vec);
                return;
            }

            final double floorY = findSeafloorY(this.getX(), this.getY(), this.getZ());
            final boolean hasFloor = !Double.isNaN(floorY);

            if (!hasFloor) {
                this.setOnBottom(false);
                super.travel(vec);
                return;
            }

            final double targetY = floorY + FLOOR_CLEARANCE;
            final double dy = targetY - this.getY();
            final boolean onBottom = Math.abs(dy) <= CONTACT_EPSILON && !this.isRolled();
            this.setOnBottom(onBottom); // <<< tell the animator

            if (onBottom) {
                // --- BENTHIC WALK MODE (unchanged math) ---
                this.moveRelative(0.10F, new Vec3((float)vec.x, 0.0F, (float)vec.z));
                this.move(MoverType.SELF, this.getDeltaMovement());
                Vec3 vel = this.getDeltaMovement();
                double correction = dy * 0.22D - vel.y * 0.45D;
                vel = new Vec3(vel.x * 0.86D, vel.y + correction, vel.z * 0.86D);
                if (dy > 0.0D && dy <= MAX_STEP_UP + 0.01D) {
                    double upBoost = Math.min(0.12D, dy * 0.55D);
                    vel = new Vec3(vel.x, Math.max(vel.y, upBoost), vel.z);
                }
                if (Math.abs(dy) < 0.05D) {
                    vel = new Vec3(vel.x, vel.y - 0.01D, vel.z);
                }
                this.setDeltaMovement(vel);
            } else {
                // Not quite on bottom yet — base swim while settling
                this.setOnBottom(false);
                super.travel(vec);
            }
        } else {
            this.setOnBottom(false);
            super.travel(vec);
        }
    }


    /**
     * Finds the Y of the first solid/non-water block BELOW, within a safe scan depth.
     * Returns NaN if no floor is found promptly.
     *
     */
    private double findSeafloorY(double x, double y, double z) {
        final Level lvl = this.level();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
        final int minY = lvl.getMinBuildHeight();
        final int maxScan = 24; // scan up to 24 blocks down

        int steps = 0;
        while (steps < maxScan && pos.getY() > minY) {
            FluidState fluidHere = lvl.getFluidState(pos);
            BlockPos below = pos.below();
            BlockState belowState = lvl.getBlockState(below);

            boolean waterHere = !fluidHere.isEmpty(); // any water (source or flowing)
            boolean solidTopBelow = belowState.getFluidState().isEmpty()
                    && belowState.isFaceSturdy(lvl, below, Direction.UP);

            if (waterHere && solidTopBelow) {
                // top surface of the solid block below
                return below.getY() + 1.0D;
            }

            pos.move(0, -1, 0);
            steps++;
        }
        return Double.NaN;
    }

    public ResourceLocation getTexture(){
        switch(getVariant()){
            case 0: default: return texture("brown");
            case 1: return texture("silver");
            case 2: return texture("purple");
            case 3: return texture("tan");
        }
    }
    private ResourceLocation texture(String variant){
        String formattedName = this.dinosaur.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
        String baseTextures = "textures/entities/" + formattedName + "/";
        String texture = baseTextures + formattedName;
        return isMale()
                ? new ResourceLocation(JurassicReborn.MODID, texture + "_male_"   + "adult" + "_" + variant + ".png")
                : new ResourceLocation(JurassicReborn.MODID, texture + "_female_" + "adult" + "_" + variant + ".png");
    }

    /**
     * Random strolling that picks points ON the seafloor.
     */
    static class BottomCrawlGoal extends Goal {
        private final CalymeneEntity mob;
        private final double speed;
        private final int radius;
        private final int tries;
        private int cooldown;

        public BottomCrawlGoal(CalymeneEntity mob, double speed, int radius, int tries) {
            this.mob = mob;
            this.speed = speed;
            this.radius = radius;
            this.tries = tries;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!mob.isInWater() || mob.isCarcass() || mob.isRolled()) return false;
            if (mob.swimBurstTicks > 0) return false; // <-- don't bottom-walk during bursts
            if (cooldown > 0) { cooldown--; return false; }
            return mob.getNavigation().isDone() && mob.getRandom().nextFloat() < 0.15F;
        }




        @Override
        public void start() {
            cooldown = 20 + mob.getRandom().nextInt(20);
            RandomSource r = mob.getRandom();
            BlockPos origin = mob.blockPosition();
            for (int i = 0; i < tries; i++) {
                int dx = Mth.nextInt(r, -radius, radius);
                int dz = Mth.nextInt(r, -radius, radius);
                double floorY = mob.findSeafloorY(origin.getX() + dx + 0.5D, origin.getY(), origin.getZ() + dz + 0.5D);
                if (!Double.isNaN(floorY)) {
                    double y = floorY + FLOOR_CLEARANCE;
                    mob.getNavigation().moveTo(origin.getX() + dx + 0.5D, y, origin.getZ() + dz + 0.5D, speed);
                    return;
                }
            }
        }

        @Override
        public boolean canContinueToUse() {
            return mob.isInWater() && !mob.isCarcass() && mob.swimBurstTicks == 0 && !mob.getNavigation().isDone();
        }
    }
}
