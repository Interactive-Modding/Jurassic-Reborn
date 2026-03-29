package net.vit.jurassicreborn.common.blocks.entities.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;

public class ElectricFenceBaseBlockEntity extends BlockEntity implements GeoBlockEntity {
    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("idle");
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    public ElectricFenceBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASE_FENCE_BLOCK_ENTITY.get(), pos, state);
    }

    // ─── GeckoLib boilerplate ─────────────────────────────────────────────
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "idleController", 0,
                state -> state.setAndContinue(IDLE_ANIMATION)));
    }
    @Override
    public void onLoad() {
        if (level != null && !level.isClientSide) {
            // For all four horizontal sides, notify adjacent blocks (wires) to update
            for (var dir : net.minecraft.core.Direction.Plane.HORIZONTAL) {
                BlockPos neighbor = worldPosition.relative(dir);
                level.updateNeighborsAt(neighbor, getBlockState().getBlock());
            }
        }
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }
}
