package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;
import org.jetbrains.annotations.Nullable;

public class PaddockSignEntity extends HangingEntity {
    private static final EntityDataAccessor<Integer> SIGN_DINOSAUR =
            SynchedEntityData.defineId(PaddockSignEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Byte> SIGN_FACING =
            SynchedEntityData.defineId(PaddockSignEntity.class, EntityDataSerializers.BYTE);
    private static final int SIGN_WIDTH = 16;
    private static final int SIGN_HEIGHT = 16;
    private int dinosaur;
    public ResourceLocation getTextureLocation(PaddockSignEntity sign) {
        String name = DinosaurHandler.getName(sign.getDinosaur());
        String textureName = name.replace(' ', '_');
        // this will look for assets/jurassicreborn/textures/paddock/<name>_sign.png
        return ResourceLocation.parse(JurassicReborn.MODID + ":" + "textures/paddock/" + textureName + ".png");
    }
    // Normal (type + world) constructor
    public PaddockSignEntity(EntityType<? extends PaddockSignEntity> type, Level world) {
        super(type, world);
    }

    public PaddockSignEntity(Level world, BlockPos clickedPos, Direction facing, int dinosaur) {
        super(ModEntities.PADDOCK_SIGN.get(), world, clickedPos.relative(facing));
        this.setDirection(facing);
        this.dinosaur = dinosaur;
        setSyncedFacing(facing);
        setSyncedDinosaur(dinosaur);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(SIGN_DINOSAUR, 0);
        builder.define(SIGN_FACING, (byte) 0); // Just use 0 as default, will be set properly in constructor
    }
    public int getWidth()  { return 16; }
    public int getHeight() { return 16; }
    // -----------------------------------
    // Saving to disk
    // -----------------------------------
    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Dinosaur", this.dinosaur);
        tag.putByte("Facing", (byte) this.direction.get2DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.dinosaur = tag.getInt("Dinosaur");
        setSyncedDinosaur(this.dinosaur);
        // RESTORE FACING!
        if (tag.contains("Facing")) {
            Direction facing = Direction.from2DDataValue(tag.getByte("Facing"));
            this.setDirection(facing);
            setSyncedFacing(facing);
        }
    }

    @Override
    protected AABB calculateBoundingBox(BlockPos pos, Direction dir) {
        double cx = pos.getX() + 0.5D;
        double cy = pos.getY() + 0.5D;
        double cz = pos.getZ() + 0.5D;
        double halfWidth  = SIGN_WIDTH  / 32.0D; // 0.5
        double halfHeight = SIGN_HEIGHT / 32.0D; // 0.5
        double halfDepth = SIGN_WIDTH / 32.0D;
        // Bias the box OUTWARD, not the center
        switch (dir) {
            case NORTH -> {
                return new AABB(
                        cx - halfWidth, cy - halfHeight, cz,
                        cx + halfWidth, cy + halfHeight, cz + halfDepth
                );
            }
            case SOUTH -> {
                return new AABB(
                        cx - halfWidth, cy - halfHeight, cz - halfDepth,
                        cx + halfWidth, cy + halfHeight, cz
                );
            }
            case WEST -> {
                return new AABB(
                        cx, cy - halfHeight, cz - halfWidth,
                        cx + halfDepth, cy + halfHeight, cz + halfWidth
                );
            }
            case EAST -> {
                return new AABB(
                        cx - halfDepth, cy - halfHeight, cz - halfWidth,
                        cx, cy + halfHeight, cz + halfWidth
                );
            }
            default -> {
                return new AABB(
                        cx - halfWidth, cy - halfHeight, cz + halfDepth,
                        cx + halfWidth, cy + halfHeight, cz - halfDepth
                );
            }
        }
    }

    @Override
    protected AABB calculateSupportBox() {
        // Use the parent HangingEntity's implementation
        return this.getBoundingBox().move(this.direction.step().mul(-0.5F)).deflate(1.0E-7);
    }



    // -----------------------------------
    // HangingEntity behavior
    // -----------------------------------
    @Override
    public void playPlacementSound() {
        // no sound
    }

    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        if (!level().isClientSide()
                && level().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            if (brokenEntity instanceof Player p && p.getAbilities().instabuild) {
                return;
            }

            // drop the sign, preserving the dino tag in the ItemStack if you want
            ItemStack stack = new ItemStack(ModItems.PADDOCK_SIGN.get());
            CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
            tag.putInt("Dinosaur", this.dinosaur);
            ItemStackNbtUtil.setTag(stack, tag);
            spawnAtLocation(stack, 0f);
        }
    }

    public int getDinosaur() {
        return dinosaur;
    }

    public void setDinosaur(int dinosaur) {
        this.dinosaur = dinosaur;
        setSyncedDinosaur(dinosaur);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        if (SIGN_DINOSAUR.equals(key)) {
            this.dinosaur = this.entityData.get(SIGN_DINOSAUR);
        } else if (SIGN_FACING.equals(key)) {
            this.setDirection(Direction.from2DDataValue(this.entityData.get(SIGN_FACING)));
        }
    }

    private void setSyncedDinosaur(int dinosaur) {
        if (!this.level().isClientSide) {
            this.entityData.set(SIGN_DINOSAUR, dinosaur);
        }
    }

    private void setSyncedFacing(Direction facing) {
        if (!this.level().isClientSide) {
            this.entityData.set(SIGN_FACING, (byte) facing.get2DDataValue());
        }
    }
}