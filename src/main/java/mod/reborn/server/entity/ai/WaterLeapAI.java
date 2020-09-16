package mod.reborn.server.entity.ai;

import mod.reborn.client.model.animation.EntityAnimation;
import mod.reborn.server.entity.DinosaurEntity;
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
        if(entity.getRNG().nextInt(chance) == 0 && this.entity.isBusy()) {
            return false;
        } else {
            this.launched = false;
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
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.entity.isInWater() || launched;
    }

    @Override
    public void startExecuting() {
        this.animation = EntityAnimation.PREPARE_LEAP;
    }

    @Override
    public void updateTask() {
        int tick = this.entity.getAnimationTick();

        if (this.animation == EntityAnimation.PREPARE_LEAP && tick < this.prevTick) {
            this.entity.setAnimation(EntityAnimation.LEAP.get());
            this.entity.playSound(this.entity.getSoundForAnimation(EntityAnimation.LEAP.get()), this.entity.getSoundVolume(), this.entity.getSoundPitch());
            this.entity.motionX = 2F;
            this.entity.motionY = this.jumpheight;
            this.entity.motionZ = 2F;
        } else if (this.animation == EntityAnimation.LEAP && this.entity.motionY < 0) {
            this.animation = EntityAnimation.LEAP_LAND;
            this.launched = true;
        } else if (this.animation == EntityAnimation.LEAP_LAND && (this.entity.onGround || this.entity.isSwimming())) {
            this.animation = EntityAnimation.IDLE;
        }

        if (this.entity.getAnimation() != this.animation.get()) {
            this.entity.setAnimation(this.animation.get());
            this.entity.setAnimationTick(this.prevTick + 1);
        }

        this.prevTick = tick;
    }

    private boolean isAirAbove(BlockPos pos, int xOffset, int zOffset, int multiplier) {
        for(float i = 1; i != 5; i++) {
            if (this.entity.world.getBlockState(pos.add(xOffset * multiplier, i, zOffset * multiplier)).getBlock() == Blocks.AIR) {
                this.jumpheight = yeetheight + ((i/2)-0.5f);
                return true;
            }
        }
        return false;
    }
}
