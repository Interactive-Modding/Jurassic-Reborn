package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.vit.jurassicreborn.common.blocks.entities.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Objects;

public class TourRailBlockEntity extends BlockEntity implements GeoBlockEntity {
    private TourRailBlock.EnumRailDirection direction;

    public TourRailBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TOUR_RAIL_BLOCK_ENTITY.get(), pos, state);//TODO!!!!!!!

    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
//        int metadata = getBlockMetadata();
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(Objects.requireNonNull(pkt.getTag()));
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }


    @Override
    public void saveAdditional(CompoundTag compound) {
        checkNonNull();
        compound.putInt("RailDirection", direction.ordinal());
        super.saveAdditional(compound);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        direction = TourRailBlock.EnumRailDirection.values()[compound.getInt("RailDirection")];
    }

    public TourRailBlock.EnumRailDirection getDirection() {
        checkNonNull();
        return direction;
    }

    private void checkNonNull() {
        if(direction == null) {
            direction = TourRailBlock.EnumRailDirection.NORTH_SOUTH;
        }
    }

    public void setDirection(TourRailBlock.EnumRailDirection direction) {
        this.direction = direction;
        checkNonNull();
        setChanged();
        if(level != null && !level.isClientSide) {
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    private static final RawAnimation IDLE_ANIMATION = RawAnimation.begin().thenLoop("animation.model.idle");
    private final AnimatableInstanceCache animationCache = GeckoLibUtil.createInstanceCache(this);

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 16,
                state -> state.setAndContinue(IDLE_ANIMATION)));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return animationCache;
    }
}
