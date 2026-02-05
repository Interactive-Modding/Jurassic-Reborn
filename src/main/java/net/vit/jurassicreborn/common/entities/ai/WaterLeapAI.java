package net.vit.jurassicreborn.common.entities.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.state.BlockState;
import net.vit.jurassicreborn.client.render.entity.animation.EntityAnimation;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;

import java.util.EnumSet;

public class WaterLeapAI extends Goal {
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};

    private final SwimmingDinosaurEntity entity;
    private final int chance;
    private final float yeetHeight;

    private float jumpHeight;
    private boolean launched = false;

    private int prevTick;
    private EntityAnimation animation;

    public WaterLeapAI(SwimmingDinosaurEntity entity, int chance, float yeetPower) {
        this.entity = entity;
        this.chance = chance;
        this.yeetHeight = yeetPower;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
    }

    @Override
    public boolean canUse() {
        // Must pass the random roll AND not be busy
        if (entity.getRandom().nextInt(chance) != 0) return false;
        if (entity.isBusy()) return false;

        // Must be in water and have a clear arc out in the facing direction
        if (!entity.isInWater()) return false;

        this.launched = false;
        Direction dir = this.entity.getDirection();
        int dx = dir.getStepX();
        int dz = dir.getStepZ();
        BlockPos base = this.entity.getOnPos();

        for (int mult : OFFSET_MULTIPLIERS) {
            if (!this.entity.isInWater() || !isAirAbove(base, dx, dz, mult)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        // Continue while we’re airborne (launched) and haven’t landed back in water/ground with IDLE anim
        // Once launched, keep going until we either land or we’ve switched to idle.
        return launched && this.entity.getAnimation() != EntityAnimation.IDLE.get();
    }

    @Override
    public void start() {
        this.animation = EntityAnimation.PREPARE_LEAP;
        this.prevTick  = this.entity.getAnimationTick();
    }

    @Override
    public void tick() {
        int tick = this.entity.getAnimationTick();

        if (this.animation == EntityAnimation.PREPARE_LEAP && tick < this.prevTick) {
            // Launch!
            this.animation = EntityAnimation.LEAP;

            // Horizontal impulse along look direction, vertical from computed jumpHeight
            var look = this.entity.getLookAngle().normalize();
            double hSpeed = 1.8; // tweak as desired
            double motionX = look.x * hSpeed;
            double motionZ = look.z * hSpeed;
            double motionY = this.jumpHeight;

            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.LEAP.get()),
                    this.entity.getSoundVolume(), this.entity.getVoicePitch());
            this.entity.setDeltaMovement(motionX, motionY, motionZ);
        } else if (this.animation == EntityAnimation.LEAP && this.entity.getDeltaMovement().y < 0) {
            this.animation = EntityAnimation.LEAP_LAND;
            this.launched  = true;
        } else if (this.animation == EntityAnimation.LEAP_LAND && (this.entity.isOnGround() || this.entity.isSwimming())) {
            this.animation = EntityAnimation.IDLE;
        }

        if (this.entity.getAnimation() != this.animation.get()) {
            this.entity.setAnimation(this.animation.get());
            this.entity.setAnimationTick(this.prevTick + 1);
        }

        this.prevTick = tick;
    }

    @Override
    public void stop() {
        this.entity.setAnimation(EntityAnimation.IDLE.get());
        this.launched = false;
    }

    private boolean isAirAbove(BlockPos pos, int xOffset, int zOffset, int multiplier) {
        // Check a small vertical column at the projected exit point;
        // pick jumpHeight based on first free layer we find.
        for (int y = 1; y <= 4; y++) {
            BlockPos check = pos.offset(xOffset * multiplier, y, zOffset * multiplier);
            BlockState state = this.entity.level.getBlockState(check);
            if (state.isAir()) {
                // Slight scaling so higher clearances let us yeet a bit more
                this.jumpHeight = yeetHeight + ((y / 2.0f) - 0.5f);
                return true;
            }
        }
        return false;
    }
}
