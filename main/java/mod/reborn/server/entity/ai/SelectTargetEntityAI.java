package mod.reborn.server.entity.ai;

import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.entity.DinosaurEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class SelectTargetEntityAI extends EntityAIBase {
    private DinosaurEntity entity;
    private Set<Class<? extends EntityLivingBase>> targetClasses;
    protected final SelectTargetEntityAI.Sorter sorter;
    private EntityLivingBase targetEntity;

    public SelectTargetEntityAI(DinosaurEntity entity, Class<? extends EntityLivingBase>[] targetClasses) {
        this.entity = entity;
        this.targetClasses = new HashSet<>();
        this.sorter = new SelectTargetEntityAI.Sorter(entity);
        Collections.addAll(this.targetClasses, targetClasses);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.entity.resetAttackCooldown();
    }

    @Override
    public void startExecuting() {
        this.entity.setAttackTarget(this.targetEntity);

        Herd herd = this.entity.herd;

        if (herd != null && this.targetEntity != null) {
            List<EntityLivingBase> enemies = new LinkedList<>();

            if (this.targetEntity instanceof DinosaurEntity && ((DinosaurEntity) this.targetEntity).herd != null) {
                enemies.addAll(((DinosaurEntity) this.targetEntity).herd.members);
            } else {
                enemies.add(this.targetEntity);
            }

            for (EntityLivingBase enemy : enemies) {
                if (!herd.enemies.contains(enemy)) {
                    herd.enemies.add(enemy);
                }
            }
        }
    }
    
    @Override
    public boolean shouldExecute() {
        if (this.entity.getRNG().nextInt(10) != 0) {
            return false;
        }

        if (!this.entity.getMetabolism().isHungry() && RebornConfig.ENTITIES.huntWhenHungry) {
            return false;
        }

        Entity target = this.entity.getAttackTarget();
        if (target != null && !target.isDead && this.entity.getEntitySenses().canSee(target) ) {
            return false;
        }

        if (!(this.entity.herd != null && this.entity.herd.fleeing) && this.entity.getAgePercentage() > 50 && (this.entity.getOwner() == null || this.entity.getMetabolism().isStarving()) && !this.entity.isSleeping() && this.entity.getAttackCooldown() <= 0) {
            List<EntityLivingBase> entities = this.entity.world.getEntitiesWithinAABB(EntityLivingBase.class, this.entity.getEntityBoundingBox().grow(16.0D, 16.0D, 16.0D));

            if (entities.size() > 0) {
                this.targetEntity = null;
                double bestScore = Double.MAX_VALUE;
                
                Collections.sort(entities, this.sorter);

                for (EntityLivingBase entity : entities) {
                    if (!entity.equals(this.entity) && !entity.isInLava() && this.entity.getEntitySenses().canSee(entity) && this.canAttack(entity)) {
                        double score = entity.getHealth() <= 0.0F ? (this.entity.getDistanceSq(entity) / entity.getHealth()) : 0;

                        if (entity.isInWater()) {
                            score *= 3;
                        }

                        if (score < bestScore) {
                            bestScore = score;

                            if (entity.getRidingEntity() instanceof EntityLivingBase) {
                                this.targetEntity = (EntityLivingBase) entity.getRidingEntity();
                            } else {
                                this.targetEntity = entity;
                            }
                        }
                    }
                
                }

                return this.targetEntity != null;
            }
        }

        return false;
    }

    private boolean canAttack(EntityLivingBase entity) {
        if (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode) {
            return false;
        }
        if (!this.targetClasses.contains(entity.getClass())) {
            for (Class<? extends EntityLivingBase> clazz : this.targetClasses) {
                if (clazz.isAssignableFrom(entity.getClass())) {
                    return true;
                }
            }
            return false;
        }
        return true;
    }
    
    public static class Sorter implements Comparator<Entity>
    {
        private final Entity entity;

        public Sorter(Entity entityIn)
        {
            this.entity = entityIn;
        }

        public int compare(Entity entity1, Entity entity2)
        {
            double d0 = this.entity.getDistanceSq(entity1);
            double d1 = this.entity.getDistanceSq(entity2);

            if (d0 < d1)
            {
                return -1;
            }
            else
            {
                return d0 > d1 ? 1 : 0;
            }
        }
    }
}
