package net.vit.jurassicreborn.common.entities.EntityUtils.ai;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.Dinosaurs.Dinosaur;

import java.util.UUID;

public class Relationship {
    public static final int MAX_SCORE = 1000;

    private final UUID entity;
    private short score;

    public Relationship(UUID entity, short score) {
        this.entity = entity;
        this.score = score;
    }

    /**
     * @return true if this relationship should be removed (e.g., target missing)
     */
    public boolean update(DinosaurEntity owner) {
        DinosaurEntity other = this.get(owner);
        if (other == null) {
            // target entity no longer exists / not on server -> remove this relationship
            return true;
        }

        // "Busy" means we are navigating or already have a combat target.
        boolean busy = !owner.getNavigation().isDone() || owner.getTarget() != null;

        // Use magnitude to scale probabilities (0.0 .. 1.0)
        double intensity = Math.min(1.0, Math.abs(this.score) / (double) MAX_SCORE);

        Dinosaur.DinosaurType type = owner.getDinosaur().getDinosaurType();

        if (this.score < 0) {
            // Hostile relationship: possibly attack if not busy and not a scared species
            if (!busy && type != Dinosaur.DinosaurType.SCARED) {
                if (owner.getRandom().nextDouble() < 0.30 * intensity) {
                    owner.setTarget(other);
                }
            }
        } else if (this.score > 0) {
            // Friendly/ally relationship:
            // (1) If neutral/aggressive and the ally has a target, assist with some chance
            if ((type == Dinosaur.DinosaurType.AGGRESSIVE || type == Dinosaur.DinosaurType.NEUTRAL)
                    && other.getTarget() != null) {
                if (owner.getRandom().nextDouble() < 0.30 * intensity) {
                    owner.setTarget(other.getTarget());
                }
            }
            // (2) If not in a family and not busy, sometimes move toward the ally
            else if (owner.family == null && !busy) {
                if (owner.getRandom().nextDouble() < 0.60 * intensity) {
                    owner.getNavigation().moveTo(other, 0.8);
                }
            }
        }

        // Reward: if our current target died and the killer was this relationship's entity, increase score
        LivingEntity currentTarget = owner.getTarget();
        if (currentTarget != null) {
            if (currentTarget.isDeadOrDying() ||
                    (currentTarget instanceof DinosaurEntity de && de.isCarcass())) {
                LivingEntity killer = currentTarget.getKillCredit();
                if (killer != null && killer.getUUID().equals(this.entity)) {
                    this.score += 100;
                }
            }
        }

        // Clamp to [-MAX_SCORE, MAX_SCORE]
        if (this.score > MAX_SCORE)      this.score = MAX_SCORE;
        else if (this.score < -MAX_SCORE) this.score = (short) -MAX_SCORE;

        // Keep this relationship unless it points to the owner itself
        return other == owner;
    }

    public DinosaurEntity get(DinosaurEntity owner) {
        if (!owner.level().isClientSide && owner.level() instanceof ServerLevel sl) {
            var e = sl.getEntity(this.entity);
            if (e instanceof DinosaurEntity d) return d;
        }
        return null;
    }

    public void updateHerd(DinosaurEntity owner) {
        if (owner.family == null) {
            DinosaurEntity other = this.get(owner);
            if (other != null) {
                double distSq = other.distanceToSqr(owner);
                if (distSq < 32.0) {
                    this.score += 2;
                } else if (this.score > 0 && owner.getRandom().nextDouble() > 0.8) {
                    this.score--;
                }
                // Clamp after adjustments
                if (this.score > MAX_SCORE)      this.score = MAX_SCORE;
                else if (this.score < -MAX_SCORE) this.score = (short) -MAX_SCORE;
            }
        }
    }

    public void onAttacked(double damage) {
        // Damage reduces affinity; clamp after cast
        int newScore = this.score - (int)Math.round(damage);
        if (newScore >  MAX_SCORE) newScore =  MAX_SCORE;
        if (newScore < -MAX_SCORE) newScore = -MAX_SCORE;
        this.score = (short)newScore;
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
        return obj instanceof Relationship rel && rel.entity.equals(this.entity);
    }

    public void writeToNBT(CompoundTag tag) {
        tag.putUUID("UUID", this.entity);
        tag.putShort("Score", this.score);
    }

    public static Relationship readFromNBT(CompoundTag tag) {
        UUID uuid = tag.getUUID("UUID");
        short score = tag.getShort("Score");
        return new Relationship(uuid, score);
    }

    public UUID getUUID() {
        return this.entity;
    }

    public short getScore() {
        return this.score;
    }
}
