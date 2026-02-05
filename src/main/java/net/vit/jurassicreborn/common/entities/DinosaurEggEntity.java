package net.vit.jurassicreborn.common.entities;

import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.items.ModItems;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;


public class DinosaurEggEntity extends Entity implements IEntityAdditionalSpawnData {
    private DinosaurEntity entity;

    private double motionX;
    private double motionY;
    private double motionZ;
    private UUID parent;
    private Dinosaur dinosaur;
    private int hatchTime;

    public DinosaurEggEntity(EntityType<? extends DinosaurEggEntity> type, Level world, DinosaurEntity entity, DinosaurEntity parent) {
        this(type, world);
        this.entity = entity;
        this.dinosaur = entity.getDinosaur();
        this.parent = parent.getUUID();
    }

    public DinosaurEggEntity(EntityType<? extends DinosaurEggEntity> type, Level world) {
        super(type, world);
        //        this.setSize(0.3F, 0.5F); todo: modernization
        this.hatchTime = this.random(5000, 6000);
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public void tick() {
        super.tick();
        if (dinosaur == null) {
            Optional<Entity> parentEntity = (this.level().isClientSide ? Optional.empty() : ((ServerLevel) level()).getEntity(this.parent) == null ? Optional.empty() : Optional.of(((ServerLevel) level()).getEntity(this.parent)));
            if (parentEntity.isPresent() && parentEntity.get() instanceof DinosaurEntity) {
                this.dinosaur = ((DinosaurEntity) parentEntity.get()).getDinosaur();
            }
        }

        if (!this.level().isClientSide) {
            if (this.entity == null) {
                this.kill();
            }

            this.hatchTime--;

            if (this.hatchTime <= 0) {
                this.hatch();
            }
        }

        Vec3 motion = this.getDeltaMovement();
        if (!this.isNoGravity()) {
            motion = motion.add(0.0, -0.04, 0.0);
        }
        this.move(MoverType.SELF, motion);
        float drag = this.onGround() ? 0.7F : 0.98F;
        this.setDeltaMovement(motion.x * drag, motion.y * 0.98F, motion.z * drag);
    }





    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (!this.level().isClientSide && this.isAlive()) {
            dropEggItem();
            this.kill();
        }
        return true;
    }


    @Override
    public InteractionResult interact(Player pPlayer, InteractionHand pHand) {


        if (this.entity != null && !this.level().isClientSide) {;
            dropEggItem();
            this.kill();
        }

        return super.interact(pPlayer, pHand);
    }

    public void hatch() {
        if(dinosaur != null && this.entity != null) {
            try {
                this.entity.setPos(this.getX(), this.getY(), this.getZ());
                this.level().addFreshEntity(this.entity);
                this.entity.playAmbientSound();
                this.kill();
                if(dinosaur == null) {
                    Optional<Entity> parentEntity =  (this.level().isClientSide ? Optional.empty() : ((ServerLevel) level()).getEntity(this.parent) == null ? Optional.empty() : Optional.of(((ServerLevel) level()).getEntity(this.parent)));
                    if(parentEntity.isPresent() && parentEntity.get() instanceof DinosaurEntity && this.dinosaur.shouldDefendOffspring() && ((DinosaurEntity) parentEntity.get()).family != null) {
                        ((DinosaurEntity) parentEntity.get()).family.addChild(this.entity.getUUID());
                    }
                }

//                for (Entity loadedEntity : this.level.loadedEntityList) {
//                    if (loadedEntity instanceof DinosaurEntity && loadedEntity.getUniqueID().equals(this.parent)) {
//                        DinosaurEntity parent = (DinosaurEntity) loadedEntity;
//                        if (parent.family != null && this.dinosaur.shouldDefendOffspring()) {
//                            parent.family.addChild(this.entity.getUniqueID());
//                        }
//                        break;
//                    }
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public int random(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compound) {
        this.hatchTime = compound.getInt("HatchTime");
        CompoundTag entityTag = compound.getCompound("Hatchling");
//        this.entity = (DinosaurEntity) EntityList.createEntityFromNBT(entityTag, this.level);
        if (!entityTag.isEmpty()) {
            Entity loaded = EntityType.loadEntityRecursive(entityTag, this.level(), e -> e);
            if (loaded instanceof DinosaurEntity dino) {
                this.entity = dino;
                this.dinosaur = dino.getDinosaur();
            }
        }
        this.parent = compound.getUUID("Parent");
        if (this.dinosaur == null && compound.contains("DinosaurID")) {
            this.dinosaur = DinosaurHandler.getById(compound.getInt("DinosaurID"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("HatchTime", this.hatchTime);
        if (this.entity != null) {
            compound.put("Hatchling", this.entity.serializeNBT());
        }
        compound.putUUID("Parent", this.parent);
        if (this.dinosaur != null) {
            compound.putInt("DinosaurID", DinosaurHandler.getId(this.dinosaur));
        }    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        int id = DinosaurHandler.getId(this.dinosaur != null ? this.dinosaur : Dinosaur.EMPTY);
        buffer.writeInt(id);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.dinosaur = DinosaurHandler.getById(additionalData.readInt());
    }

    @Nullable
    public Dinosaur getDinosaur() {
        return this.dinosaur;
    }
    @Override
    public boolean isPickable() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return true;
    }
    private void dropEggItem() {
        if (this.dinosaur == null || this.entity == null) {
            return;
        }
        var eggItem = ModItems.dinoEggs.get(this.dinosaur);
        if (eggItem == null) {
            return;
        }
        ItemStack stack = new ItemStack(eggItem.get());
        CompoundTag tag = stack.getOrCreateTag();
        // Preserve the dinosaur DNA information in the same format used by other
        // genetics-aware items so machines like the incubator can read it.
        DinoDNA dna = new DinoDNA(this.entity.getDinosaur(), this.entity.getDNAQuality(), this.entity.getGenetics());
        dna.writeToNBT(tag);
        stack.setTag(tag);
        this.spawnAtLocation(stack);
    }
}