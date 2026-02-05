package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;

import javax.annotation.Nullable;
import java.util.Locale;

public class MuralEntity extends WallHangingEntity implements IEntityAdditionalSpawnData {

    private Type type;

    public MuralEntity(EntityType<? extends MuralEntity> entityType, Level level) {
        super(entityType, level);
        this.type = Type.CREATION_LAB;
        this.noCulling = true;
    }

    public MuralEntity(Level level, BlockPos clickedPos, Direction facing, Type type) {
        super(ModEntities.MURAL.get(), level, clickedPos.relative(facing));
        this.type = type;
        this.noCulling = true;
        this.setDirection(facing);
    }

    /** Accessor renamed to avoid clashing with Entity#getType(). */
    public Type getMuralType() {
        return type;
    }

    public void setMuralType(Type type) {
        this.type = type;
        if (this.direction != null) {
            this.setDirection(this.direction);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Type", this.type.ordinal());
        tag.putByte("Facing", (byte) this.direction.get2DDataValue());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.type = Type.byIndex(tag.getInt("Type"));
        if (tag.contains("Facing")) {
            this.setDirection(Direction.from2DDataValue(tag.getByte("Facing")));
        } else if (this.direction != null) {
            this.setDirection(this.direction);
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        buf.writeInt(this.type.ordinal());
        buf.writeLong(this.getPos().asLong());
        buf.writeByte(this.direction.get2DDataValue());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        this.type = Type.byIndex(buf.readInt());
        this.pos = BlockPos.of(buf.readLong());
        this.setDirection(Direction.from2DDataValue(buf.readUnsignedByte()));
    }

    @Override
    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override public int getWidth()  { return 16; }
    @Override public int getHeight() { return 16; }


    @Override
    public void dropItem(@Nullable Entity brokenEntity) {
        if (!level.isClientSide()
                && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)
                && !(brokenEntity instanceof Player player && player.getAbilities().instabuild)) {
            spawnAtLocation(new ItemStack(ModItems.MURAL.get()), 0.0F);
        }
    }

    @Override
    public void playPlacementSound() {
        // no sound
    }

    @Override
    public ItemStack getPickedResult(HitResult target) {
        return new ItemStack(ModItems.MURAL.get());
    }


    public enum Type {
        BUNKER(128, 64),
        RIVER(64, 48),
        MAMENCHI(64, 64),
        LAB(16, 32),
        JR(16, 16),
        AMBER(16, 16),
        EGG(16, 32),
        CREATION_LAB(64, 32),
        FOSSILS(128, 64),
        HUNT_LEFT(64, 64),
        HUNT_MIDDLE(64, 64),
        HUNT_RIGHT(64, 64),
        MOSASAURUS_1(64, 32),
        PARK_ENTRANCE(64, 32),
        RIDING(64, 32),
        SKETCH(64, 32),
        TRICERATOPS_1(128, 32),
        VALLEY(64, 32),
        JOHN_HAMMOND(32, 32);

        public final int sizeX;
        public final int sizeY;
        public final net.minecraft.resources.ResourceLocation texture;

        Type(int sizeX, int sizeY) {
            this.sizeX = sizeX;
            this.sizeY = sizeY;
            this.texture = new net.minecraft.resources.ResourceLocation(
                    JurassicReborn.MODID,
                    "textures/painting/" + this.name().toLowerCase(Locale.ROOT) + ".png"
            );
        }

        public static Type byIndex(int index) {
            Type[] values = values();
            if (index < 0 || index >= values.length) {
                return values[0];
            }
            return values[index];
        }
    }
}
