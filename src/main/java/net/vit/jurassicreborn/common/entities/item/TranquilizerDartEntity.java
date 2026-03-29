package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.items.misc.Dart;

public class TranquilizerDartEntity extends ThrowableItemProjectile {

    private static final EntityDataAccessor<ItemStack> DART_STACK =
            SynchedEntityData.defineId(TranquilizerDartEntity.class, EntityDataSerializers.ITEM_STACK);

    public TranquilizerDartEntity(EntityType<? extends TranquilizerDartEntity> type, Level level) {
        super(type, level);
    }

    public TranquilizerDartEntity(Level level, LivingEntity thrower, ItemStack stack) {
        super(ModEntities.TRANQUILIZER_DART.get(), thrower, level); // <-- make sure this matches your entity registration
        ItemStack dartStack = stack.copy();
        setDartStack(dartStack);
        this.setItem(dartStack);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DART_STACK, ItemStack.EMPTY);
    }
    @Override
    public void tick() {
        super.tick();
        Level level = this.level();
        if (!this.level().isClientSide && level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY(), this.getZ(), 1, 0.0, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.DART_TRANQUILIZER.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!level().isClientSide && result.getEntity() instanceof DinosaurEntity dino) {
            ItemStack dartStack = getDartStack();
            if (dartStack.isEmpty()) {
                dartStack = this.getItem();
            }

            if (dartStack.getItem() instanceof Dart dartItem) {
                dartItem.getConsumer().accept(dino, dartStack);
            }
            discard();
        }
    }


    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (!getDartStack().isEmpty()) {
            HolderLookup.Provider provider = this.level().registryAccess();
            tag.put("DartStack", getDartStack().save(provider));
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("DartStack")) {
            HolderLookup.Provider provider = this.level().registryAccess();
            ItemStack dartStack = ItemStack.parseOptional(provider, tag.getCompound("DartStack"));
            setDartStack(dartStack);
            this.setItem(dartStack);
        }
    }

    private ItemStack getDartStack() {
        if (this.entityData == null) {
            return ItemStack.EMPTY;
        }
        return this.entityData.get(DART_STACK);
    }

    private void setDartStack(ItemStack stack) {
        this.entityData.set(DART_STACK, stack);
    }
}