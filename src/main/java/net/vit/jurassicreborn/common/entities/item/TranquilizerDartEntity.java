package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.misc.Dart;

public class TranquilizerDartEntity extends ThrowableItemProjectile {

    private ItemStack dartStack = ItemStack.EMPTY;

    public TranquilizerDartEntity(EntityType<? extends TranquilizerDartEntity> type, Level level) {
        super(type, level);
    }

    public TranquilizerDartEntity(Level level, LivingEntity thrower, ItemStack stack) {
        super(ModEntities.TRANQUILIZER_DART.get(), thrower, level);
        this.dartStack = stack.copy();
    }
    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide && this.level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return dartStack.getItem();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level.isClientSide && result.getEntity() instanceof DinosaurEntity dino && this.getItem().getItem() instanceof Dart dartItem) {
            dartItem.getConsumer().accept(dino, this.getItem());
            discard();
        }
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!dartStack.isEmpty()) {
            tag.put("DartStack", dartStack.save(new CompoundTag()));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DartStack")) {
            dartStack = ItemStack.of(tag.getCompound("DartStack"));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
