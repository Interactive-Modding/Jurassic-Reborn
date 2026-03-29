package net.vit.jurassicreborn.common.entities.vehicle.boat;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vit.jurassicreborn.common.entities.ModEntities;

/**
 * Base boat entity used for the Jurassic Reborn wood types.
 */
public class JurassicBoat extends Boat {
    private static final EntityDataAccessor<Integer> DATA_TYPE = SynchedEntityData.defineId(JurassicBoat.class, EntityDataSerializers.INT);

    public JurassicBoat(EntityType<? extends Boat> type, Level level) {
        super(type, level);
    }

    public JurassicBoat(Level level, double x, double y, double z) {
        this(ModEntities.JURASSIC_BOAT.get(), level);
        this.setPos(x, y, z);
        this.setDeltaMovement(0.0D, 0.0D, 0.0D);
        this.xo = x;
        this.yo = y;
        this.zo = z;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(DATA_TYPE, ModBoatType.ARAUCARIA.ordinal());
    }

    public void setVariant(ModBoatType type) {
        this.entityData.set(DATA_TYPE, type.ordinal());
    }

    public ModBoatType getJurassicVariant() {
        return ModBoatType.byId(this.entityData.get(DATA_TYPE));
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putString("JurassicType", this.getJurassicVariant().getSerializedName());
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("JurassicType", Tag.TAG_STRING)) {
            this.setVariant(ModBoatType.byName(tag.getString("JurassicType")));
        }
    }

    @Override
    public ItemStack getPickResult() {
        return new ItemStack(this.getDropItem());
    }

    @Override
    public Item getDropItem() {
        return this.getJurassicVariant().getBoatItem(false);
    }
}
