package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import mod.reborn.server.dinosaur.Dinosaur;

import java.util.UUID;

public class Relationship {
    public static final int MAX_SCORE = 1000;

    private final UUID entity;
    private short score;

    public Relationship(UUID entity, short score) {
        this.entity = entity;
        this.score = score;
    }

    public boolean update(DinosaurEntity owner) {
        DinosaurEntity entity = this.get(owner);
        if (entity == null) {
            return true;
        }
        boolean isPreoccupied = owner.getNavigator().noPath() && owner.getAttackTarget() == null;
        double scaleScore = this.score / (double) MAX_SCORE;
        Dinosaur.DinosaurType dinosaurType = owner.getDinosaur().getDinosaurType();
        if (this.score < 0) {
            if (!isPreoccupied && dinosaurType != Dinosaur.DinosaurType.SCARED && owner.getRNG().nextDouble() * scaleScore > 0.3) {
                owner.setAttackTarget(entity);
            }
        } else if (this.score > 0) {
            if ((dinosaurType == Dinosaur.DinosaurType.AGGRESSIVE || dinosaurType == Dinosaur.DinosaurType.NEUTRAL) && entity.getAttackTarget() != null && owner.getRNG().nextDouble() * scaleScore > 0.3) {
                owner.setAttackTarget(entity.getAttackTarget());
            } else if (owner.family == null && !isPreoccupied && owner.getRNG().nextDouble() * scaleScore > 0.6) {
                owner.getNavigator().tryMoveToEntityLiving(entity, 0.8);
            }
        }
        EntityLivingBase lastAttacker = owner.getAttackTarget();
        if (lastAttacker != null && (lastAttacker.isDead || (lastAttacker instanceof DinosaurEntity && ((DinosaurEntity) lastAttacker).isCarcass()))) {
            EntityLivingBase lastAttackerKiller = lastAttacker.getRevengeTarget();
            if (lastAttackerKiller != null && lastAttackerKiller.getUniqueID().equals(this.entity)) {
                this.score += 100;
            }
        }
        if (this.score > MAX_SCORE) {
            this.score = MAX_SCORE;
        } else if (this.score < -MAX_SCORE) {
            this.score = -MAX_SCORE;
        }
        return entity == owner;
    }

    public DinosaurEntity get(DinosaurEntity owner) {
        for (Entity entity : owner.world.loadedEntityList) {
            if (entity instanceof DinosaurEntity && entity.getUniqueID().equals(this.entity)) {
                return (DinosaurEntity) entity;
            }
        }
        return null;
    }

    public void updateHerd(DinosaurEntity owner) {
        if (owner.family == null) {
            DinosaurEntity entity = this.get(owner);
            if (entity != null) {
                double distance = entity.getDistanceSq(owner);
                if (distance < 32) {
                    this.score += 2;
                } else if (this.score > 0 && owner.getRNG().nextDouble() > 0.8) {
                    this.score--;
                }
            }
        }
    }

    public void onAttacked(double damage) {
        this.score -= damage;
    }

    public void setFamily() {
        this.score = MAX_SCORE;
    }

    @Override
    public int hashCode() {
        return this.entity.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Relationship && ((Relationship) obj).entity.equals(this.entity);
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setUniqueId("UUID", this.entity);
        compound.setShort("Score", this.score);
    }

    public static Relationship readFromNBT(NBTTagCompound compound) {
        UUID uuid = compound.getUniqueId("UUID");
        short score = compound.getShort("Score");
        return new Relationship(uuid, score);
    }

    public UUID getUUID() {
        return this.entity;
    }

    public short getScore() {
        return this.score;
    }
}
