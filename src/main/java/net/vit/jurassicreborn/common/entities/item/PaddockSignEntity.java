package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.entities.ModEntities;
import net.vit.jurassicreborn.common.items.ModItems;
import org.jetbrains.annotations.Nullable;

public class PaddockSignEntity extends HangingEntity implements IEntityAdditionalSpawnData {
    private int dinosaur;
    public ResourceLocation getTextureLocation(PaddockSignEntity sign) {
        String name = DinosaurHandler.getName(sign.getDinosaur());
        // this will look for assets/jurassicreborn/textures/paddock/<name>_sign.png
        return new ResourceLocation(JurassicReborn.MODID,
                "textures/paddock/" + name + ".png");
    }
    // Normal (type + world) constructor
    public PaddockSignEntity(EntityType<? extends PaddockSignEntity> type, Level world) {
        super(type, world);
    }

    public PaddockSignEntity(PlayMessages.SpawnEntity msg, Level world) {
        this(ModEntities.PADDOCK_SIGN.get(), world);
        readSpawnData(msg.getAdditionalData());
    }
    public PaddockSignEntity(Level world, BlockPos clickedPos, Direction facing, int dinosaur) {
        super(ModEntities.PADDOCK_SIGN.get(), world, clickedPos.relative(facing));
        this.setDirection(facing);
        this.dinosaur = dinosaur;
    }


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
        // RESTORE FACING!
        if (tag.contains("Facing")) {
            this.setDirection(Direction.from2DDataValue(tag.getByte("Facing")));
        }
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }


    // -----------------------------------
    // -----------------------------------
    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeInt(this.dinosaur);
        // write the hanging x/y/z from super.pos
        buffer.writeLong(this.getPos().asLong());
        // write the facing direction
        buffer.writeByte(this.direction.get2DDataValue());
    }


    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.dinosaur = buffer.readInt();
        // read & set the hanging pos
        BlockPos p = BlockPos.of(buffer.readLong());
        this.pos = p;                   // super.pos
        this.setDirection(Direction.from2DDataValue(buffer.readUnsignedByte()));
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        // Forge will package up writeSpawnData() for us
        return NetworkHooks.getEntitySpawningPacket(this);
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
        if (!level.isClientSide()
                && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {

            if (brokenEntity instanceof Player p && p.getAbilities().instabuild) {
                return;
            }

            ItemStack stack = new ItemStack(ModItems.PADDOCK_SIGN.get());
            stack.getOrCreateTag().putInt("Dinosaur", this.dinosaur);
            spawnAtLocation(stack, 0f);
        }
    }

    public int getDinosaur() {
        return dinosaur;
    }

    public void setDinosaur(int dinosaur) {
        this.dinosaur = dinosaur;
    }
}
