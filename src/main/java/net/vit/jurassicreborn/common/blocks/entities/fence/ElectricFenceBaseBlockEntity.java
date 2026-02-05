package net.vit.jurassicreborn.common.blocks.entities.fence;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;

public class ElectricFenceBaseBlockEntity extends BlockEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public ElectricFenceBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BASE_FENCE_BLOCK_ENTITY.get(), pos, state);
    }

    // ─── GeckoLib boilerplate ─────────────────────────────────────────────
    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(
                new AnimationController<>(this, "idleController", 0, this::predicate)
        );
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


    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
