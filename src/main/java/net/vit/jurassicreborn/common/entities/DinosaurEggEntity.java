package net.vit.jurassicreborn.common.entities;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.vit.jurassicreborn.common.entities.Dinosaurs.DinosaurHandler;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.items.ModItems;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;


public class DinosaurEggEntity extends Entity {
    private static final EntityDataAccessor<Integer> WATCHER_DINOSAUR_ID =
            SynchedEntityData.defineId(DinosaurEggEntity.class, EntityDataSerializers.INT);
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
        setDinosaur(entity.getDinosaur());
        this.parent = parent.getUUID();
    }

    public DinosaurEggEntity(EntityType<? extends DinosaurEggEntity> type, Level world) {
        super(type, world);
        //        this.setSize(0.3F, 0.5F); todo: modernization
        this.hatchTime = this.random(5000, 6000);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        builder.define(WATCHER_DINOSAUR_ID, DinosaurHandler.getId(Dinosaur.EMPTY));
    }

    @Override
    public void tick() {
        super.tick();
        if (dinosaur == null) {
            Optional<Entity> parentEntity = (this.level().isClientSide ? Optional.empty() : ((ServerLevel) level()).getEntity(this.parent) == null ? Optional.empty() : Optional.of(((ServerLevel) level()).getEntity(this.parent)));
            if (parentEntity.isPresent() && parentEntity.get() instanceof DinosaurEntity) {
                setDinosaur(((DinosaurEntity) parentEntity.get()).getDinosaur());
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
            setDinosaur(dino.getDinosaur());
        }
        }
        this.parent = compound.getUUID("Parent");
        if (this.dinosaur == null && compound.contains("DinosaurID")) {
            setDinosaur(DinosaurHandler.getById(compound.getInt("DinosaurID")));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compound) {
        compound.putInt("HatchTime", this.hatchTime);
        if (this.entity != null) {
            CompoundTag hatchlingTag = new CompoundTag();
            var registry = this.level().registryAccess().registryOrThrow(net.minecraft.core.registries.Registries.ENTITY_TYPE);
            hatchlingTag.putString("id", registry.getKey(this.entity.getType()).toString());
            this.entity.saveWithoutId(hatchlingTag);
            hatchlingTag.putInt("DinosaurID", DinosaurHandler.getId(this.entity.getDinosaur()));
            hatchlingTag.putInt("DNAQuality", this.entity.getDNAQuality());
            hatchlingTag.putString("Genetics", this.entity.getGenetics());
            hatchlingTag.putBoolean("IsMale", this.entity.isMale());
            compound.put("Hatchling", hatchlingTag);
        }
        compound.putUUID("Parent", this.parent);
        if (this.dinosaur != null) {
            compound.putInt("DinosaurID", DinosaurHandler.getId(this.dinosaur));
        }
    }

    @Nullable
    public Dinosaur getDinosaur() {
        if (this.dinosaur == null) {
            this.dinosaur = DinosaurHandler.getById(this.entityData.get(WATCHER_DINOSAUR_ID));
        }
        return this.dinosaur;
    }

    private void setDinosaur(@Nullable Dinosaur dinosaur) {
        this.dinosaur = dinosaur;
        if (!this.level().isClientSide) {
            this.entityData.set(
                    WATCHER_DINOSAUR_ID,
                    DinosaurHandler.getId(dinosaur != null ? dinosaur : Dinosaur.EMPTY)
            );
        }
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
        CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
        // Preserve the dinosaur DNA information in the same format used by other
        // genetics-aware items so machines like the incubator can read it.
        DinoDNA dna = new DinoDNA(this.entity.getDinosaur(), this.entity.getDNAQuality(), this.entity.getGenetics());
        dna.writeToNBT(tag);
        ItemStackNbtUtil.setTag(stack, tag);
        this.spawnAtLocation(stack);
    }
}
