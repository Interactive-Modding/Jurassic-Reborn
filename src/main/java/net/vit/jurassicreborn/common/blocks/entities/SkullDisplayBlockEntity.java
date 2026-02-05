package net.vit.jurassicreborn.common.blocks.entities;

import com.github.alexthe666.citadel.client.model.TabulaModel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import org.jetbrains.annotations.NotNull;

public class SkullDisplayBlockEntity extends BlockEntity {
    private short angle = 0;
    private int dinosaur = -1;
    private boolean isFossilized;
    private boolean hasStand;
    public float[] modelScale = {1.0F, 1.0F, 1.0F};
    @OnlyIn(Dist.CLIENT)
    public TabulaModel model;

    /**
     * Texture used to render the skull. Only present on the client.
     */
    @OnlyIn(Dist.CLIENT)
    public ResourceLocation texture;
    public SkullDisplayBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SKULL_DISPLAY_BLOCK_ENTITY.get(), pos, state);
    }

    public void setModel(int dinosaurID, boolean fossilized, boolean stand) {
        this.dinosaur = dinosaurID;
        this.isFossilized = fossilized;
        this.hasStand = stand;
        setChanged();
        if (this.level != null && !this.level.isClientSide) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public short getAngle() {
        return this.angle;
    }

    public void setAngle(short angle) {
        this.angle = angle;
    }

    public boolean hasStand() {
        return this.hasStand;
    }

    public Dinosaur getDinosaur() {
        return DinosaurHandler.getById(this.dinosaur);
    }

    public boolean hasData() {
        return this.dinosaur != -1;
    }

    public boolean isFossilized() {
        return this.isFossilized;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("dinosaur", this.dinosaur);
        tag.putBoolean("isFossilized", this.isFossilized);
        tag.putShort("angle", this.angle);
        tag.putBoolean("type", this.hasStand);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.dinosaur = tag.getInt("dinosaur");
        this.isFossilized = tag.getBoolean("isFossilized");
        this.angle = tag.getShort("angle");
        this.hasStand = tag.getBoolean("type");
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this, BlockEntity::saveWithoutMetadata);
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public @NotNull AABB getRenderBoundingBox() {
        return super.getRenderBoundingBox().inflate(2);
    }
}
