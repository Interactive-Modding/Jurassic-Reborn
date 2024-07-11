package mod.reborn.server.entity.ai;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.SwimmingDinosaurEntity;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class WaterLeapAI extends EntityAIBase {
    private static final int[] OFFSET_MULTIPLIERS = new int[]{0, 1, 4, 5, 6, 7};
    private final SwimmingDinosaurEntity entity;
    private final int chance;
    private final float yeetheight;
    private float jumpheight;
    private boolean launched = false;
    private int prevTick;
    private EntityAnimation animation;

    public WaterLeapAI(SwimmingDinosaurEntity entity, int chance, float yeetpower) {
        this.entity = entity;
        this.chance = chance;
        this.yeetheight = yeetpower;
        this.setMutexBits(Mutex.ATTACK | Mutex.ANIMATION);
    }

    @Override
    public boolean shouldExecute() {
        // Check if the chance allows for executing the leap
        if (entity.getRNG().nextInt(chance) != 0 && entity.isBusy() && (this.entity.getAgePercentage() < 50)) {
            return false;
        }

        // Check if entity is in water and has air blocks above in suitable directions
        EnumFacing direction = this.entity.getHorizontalFacing();
        int i = direction.getFrontOffsetX();
        int j = direction.getFrontOffsetZ();
        BlockPos blockPos = this.entity.getPosition();

        for (int k : OFFSET_MULTIPLIERS) {
            if (!this.entity.isInWater() || !this.isAirAbove(blockPos, i, j, k)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        // Continue executing if not in water or already launched
        return !this.entity.isInWater() || launched;
    }

    @Override
    public void startExecuting() {
        // Start preparing leap animation
        this.animation = EntityAnimation.PREPARE_LEAP;
    }

    @Override
    public void updateTask() {
        int tick = this.entity.getAnimationTick();

        if (this.animation == EntityAnimation.PREPARE_LEAP && tick < this.prevTick) {
            // Start the leap animation and set motion
            this.entity.setAnimation(EntityAnimation.LEAP.get());
            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.LEAP.get()), this.entity.getSoundVolume(), this.entity.getSoundPitch());
            this.entity.motionY = this.jumpheight;
        } else if (this.animation == EntityAnimation.LEAP && this.entity.motionY < 0) {
            // Transition to landing animation
            this.animation = EntityAnimation.LEAP_LAND;
            this.launched = true;
        } else if (this.animation == EntityAnimation.LEAP_LAND && (this.entity.onGround || this.entity.isSwimming())) {
            // Transition to idle state after landing
            this.animation = EntityAnimation.IDLE;
        }

        if (this.entity.getAnimation() != this.animation.get()) {
            // Set current animation state
            this.entity.setAnimation(this.animation.get());
            this.entity.setAnimationTick(this.prevTick + 1);
        }

        this.prevTick = tick;
    }

    private boolean isAirAbove(BlockPos pos, int xOffset, int zOffset, int multiplier) {
        // Check for air blocks above entity
        for (int i = 1; i <= 4; i++) { // Iterate from 1 to 4 (adjust as needed)
            BlockPos checkPos = pos.add(xOffset * multiplier, i, zOffset * multiplier);
            if (this.entity.world.isBlockLoaded(checkPos)) {
                if (this.entity.world.getBlockState(checkPos).getBlock() == Blocks.AIR) {
                    this.jumpheight = yeetheight + ((float) i / 2.0f - 0.5f); // Adjust jump height calculation
                    return true;
                }
            }
        }
        return false;
    }
}
