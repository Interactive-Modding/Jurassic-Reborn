package net.vit.jurassicreborn.common.blocks.parkBlocks;

import net.minecraft.world.level.block.Block;
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
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.Objects;

public class TourRailBlockEntity extends BlockEntity implements IAnimatable {
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
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    private final AnimationFactory animFactory = GeckoLibUtil.createFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 16, this::controller));
    }

    private final AnimationBuilder ANIMATION = new AnimationBuilder().addAnimation("animation.model.idle");//this is the default animation on all of the models--thus, switching them around shouldn't cause issues
    protected <E extends TourRailBlockEntity> PlayState controller(final AnimationEvent<E> event){
        event.getController().setAnimation(ANIMATION);
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return animFactory;
    }
}
