package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import mod.reborn.server.dinosaur.Dinosaur;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Herd implements Iterable<DinosaurEntity> {
    public Set<DinosaurEntity> members = new HashSet<>();
    public DinosaurEntity leader;

    private Vec3d center;

    private float moveX;
    private float moveZ;

    public State state = State.IDLE;
    public int stateTicks;

    private Random random = new Random();

    public Set<EntityLivingBase> enemies = new HashSet<>();

    public boolean fleeing;

    private Dinosaur herdType;

    private int nextMemberCheck;
    private int failedPathTicks;

    public Herd(DinosaurEntity leader) {
        this.herdType = leader.getDinosaur();
        this.members.add(leader);
        this.leader = leader;
        this.resetStateTicks();
    }

    public void update() {
        if (this.leader == null || this.leader.isCarcass() || this.leader.isDead) {
            this.updateLeader();
        }

        if (this.stateTicks > 0 && this.failedPathTicks < this.members.size() * 2) {
            this.stateTicks--;
        } else {
            if (this.herdType.shouldRandomlyFlock()) {
                this.state = this.state == State.MOVING ? State.IDLE : State.MOVING;
            } else {
                this.state = State.IDLE;
            }

            this.resetStateTicks();
            this.enemies.clear();
            this.fleeing = false;
        }

        if (this.leader != null) {
            if (this.leader.shouldSleep()) {
                this.state = State.IDLE;
                this.resetStateTicks();
            }

            this.center = this.getCenterPosition();

            if (this.enemies.size() > 0) {
                if (this.fleeing) {
                    this.state = State.MOVING;

                    float angle = 0.0F;

                    for (EntityLivingBase attacker : this.enemies) {
                        angle += MathHelper.atan2(this.center.z - attacker.posZ, this.center.x - attacker.posX);
                    }

                    angle /= this.enemies.size();

                    this.moveX = -MathHelper.cos(angle);
                    this.moveZ = MathHelper.sin(angle);

                    this.normalizeMovement();
                } else {
                    this.state = State.IDLE;
                }
            } else {
                this.fleeing = false;
            }

            List<DinosaurEntity> remove = new LinkedList<>();

            for (DinosaurEntity entity : this) {
                if (entity.getDistanceSq(this.center.x, this.center.y, this.center.z) > 2048) {
                    remove.add(entity);
                }
            }

            for (DinosaurEntity entity : remove) {
                this.splitHerd(entity);

                if (entity == this.leader) {
                    this.updateLeader();
                }
            }

            if (this.leader == null) {
                return;
            }

            boolean attemptedPath = false;
            int failedPaths = 0;

            for (DinosaurEntity entity : this) {
                if (this.enemies.isEmpty() || this.fleeing) {
                    if (!(entity.getMetabolism().isHungry() || entity.getMetabolism().isThirsty()) && !entity.isMovementBlocked() && !entity.isInWater() && (this.fleeing || entity.getNavigator().noPath()) && (this.state == State.MOVING || this.random.nextInt(50) == 0)) {
                        float entityMoveX = this.moveX * 8.0F;
                        float entityMoveZ = this.moveZ * 8.0F;

                        float centerDistance = (float) Math.abs(entity.getDistance(this.center.x, entity.posY, this.center.z));

                        if (this.fleeing) {
                            centerDistance *= 4.0F;
                        }

                        if (centerDistance > 0) {
                            entityMoveX += (this.center.x - entity.posX) / centerDistance;
                            entityMoveZ += (this.center.z - entity.posZ) / centerDistance;
                        }

                        for (DinosaurEntity other : this) {
                            if (other != entity) {
                               /*TODO:make sure works */ float distance = (float) entity.getDistanceSq(other);

                                float separation = (entity.width * 1.5F) + 1.5F;

                                if (distance < separation) {
                                    float scale = distance / separation;
                                    entityMoveX += (entity.posX - other.posX) / scale;
                                    entityMoveZ += (entity.posZ - other.posZ) / scale;
                                }
                            }
                        }

                        double navigateX = entity.posX + entityMoveX;
                        double navigateZ = entity.posZ + entityMoveZ;

                        Dinosaur dinosaur = entity.getDinosaur();
                        double speed = this.state == State.IDLE ? 0.8 : dinosaur.getFlockSpeed();

                        if (this.fleeing) {
                            if (dinosaur.getAttackSpeed() > speed) {
                                speed = dinosaur.getAttackSpeed();
                            }
                        }

                        if (entity.disableHerdingTicks <= 0 && entity.getAttackTarget() == null && (this.members.size() > 1 || this.fleeing)) {
                            BlockPos navigatePos = entity.world.getHeight(new BlockPos(navigateX, 0, navigateZ)).up();
                            if (entity.getNavigator().getPath() != null && !entity.getNavigator().getPath().isFinished()) {
                                PathPoint finalPoint = entity.getNavigator().getPath().getFinalPathPoint();
                                if (navigatePos.getDistance(finalPoint.x, finalPoint.y, finalPoint.z) < 25) {
                                    continue;
                                }
                            }
                            attemptedPath = true;
                            if (entity.getDistanceSqToCenter(navigatePos) > 16 && !entity.isMovementBlocked()) {
                                boolean canMove = entity.getNavigator().tryMoveToXYZ(navigatePos.getX(), navigatePos.getY(), navigatePos.getZ(), speed);
                                if (!canMove) {
                                    failedPaths++;
                                }
                            }
                        }
                    }
                } else if (!this.fleeing && (entity.getAttackTarget() == null || this.random.nextInt(20) == 0) && this.enemies.size() > 0) {
                    if (entity.getAgePercentage() > 50) {
                        int index = this.random.nextInt(this.enemies.size());
                        Iterator<EntityLivingBase> enemyIterator = this.enemies.iterator();
                        for (int i = 0; i < index; i++) {
                            enemyIterator.next();
                        }
                        entity.setAttackTarget(enemyIterator.next());
                    }
                }
            }

            if (attemptedPath) {
                if (failedPaths > this.members.size() / 4) {
                    this.moveX = -this.moveX + (this.random.nextFloat() - 0.5F) * 0.1F;
                    this.moveZ = -this.moveZ + (this.random.nextFloat() - 0.5F) * 0.1F;
                    this.failedPathTicks++;
                    this.normalizeMovement();
                } else if (this.failedPathTicks > 0) {
                    this.failedPathTicks--;
                }
            }

            List<EntityLivingBase> invalidEnemies = new LinkedList<>();

            for (EntityLivingBase enemy : this.enemies) {
                if (enemy.isDead || (enemy instanceof DinosaurEntity && ((DinosaurEntity) enemy).isCarcass()) || (enemy instanceof EntityPlayer && ((EntityPlayer) enemy).capabilities.isCreativeMode) || enemy.getDistanceSq(this.center.x, this.center.y, this.center.z) > 1024 || this.members.contains(enemy)) {
                    invalidEnemies.add(enemy);
                }
            }

            this.enemies.removeAll(invalidEnemies);

            if (this.fleeing && this.enemies.size() == 0) {
                this.fleeing = false;
                this.state = State.IDLE;
            }

            if (this.state == State.IDLE) {
                this.moveX = 0.0F;
                this.moveZ = 0.0F;
            } else {
                this.moveX += (this.random.nextFloat() - 0.5F) * 0.1F;
                this.moveZ += (this.random.nextFloat() - 0.5F) * 0.1F;

                this.normalizeMovement();
            }

            this.refreshMembers();
        }
    }

    private void splitHerd(DinosaurEntity entity) {
        this.members.remove(entity);

        Herd newHerd = new Herd(entity);
        newHerd.fleeing = this.fleeing;
        newHerd.state = this.state;
        newHerd.enemies = new HashSet<>(this.enemies);
        entity.herd = newHerd;
    }

    private void resetStateTicks() {
        this.stateTicks = this.random.nextInt(this.state == State.MOVING ? 2000 : 4000) + 1000;
        this.failedPathTicks = 0;
    }

    public void refreshMembers() {
        List<DinosaurEntity> remove = new LinkedList<>();

        for (DinosaurEntity entity : this) {
            if (!entity.isAlive() || entity.getMetabolism().isStarving() || entity.getMetabolism().isDehydrated()) {
                remove.add(entity);
            }
        }

        this.members.removeAll(remove);

        if (this.leader.ticksExisted > this.nextMemberCheck) {
            this.nextMemberCheck = this.leader.ticksExisted + 20 + this.random.nextInt(20);

            AxisAlignedBB searchBounds = new AxisAlignedBB(this.center.x - 16, this.center.y - 5, this.center.z - 16, this.center.x + 16, this.center.y + 5, this.center.z + 16);

            List<Herd> otherHerds = new LinkedList<>();

            for (DinosaurEntity entity : this.leader.world.getEntitiesWithinAABB(this.leader.getClass(), searchBounds)) {
                if (!entity.isCarcass() && !entity.isDead && !(entity.getMetabolism().isStarving() || entity.getMetabolism().isDehydrated())) {
                    Herd otherHerd = entity.herd;
                    if (otherHerd == null || otherHerd.members.size() == 1) {
                        if (this.size() >= this.herdType.getMaxHerdSize()) {
                            if (this.leader != null && GameRuleHandler.KILL_HERD_OUTCAST.getBoolean(this.leader.world) && this.herdType.getDinosaurType() == Dinosaur.DinosaurType.AGGRESSIVE && !this.enemies.contains(entity)) {
                                this.enemies.add(entity);
                            }
                            return;
                        }
                        this.addMember(entity);
                    } else if (otherHerd != this && !otherHerds.contains(otherHerd)) {
                        otherHerds.add(otherHerd);
                    }
                }
            }

            for (Herd otherHerd : otherHerds) {
                int originalSize = this.size();

                if (otherHerd.size() <= originalSize && otherHerd.size() + originalSize < this.herdType.getMaxHerdSize()) {
                    for (DinosaurEntity member : otherHerd) {
                        this.members.add(member);
                        member.herd = this;
                    }

                    this.enemies.addAll(otherHerd.enemies);

                    this.fleeing |= otherHerd.fleeing;

                    otherHerd.disband();
                } else if (originalSize + 1 > this.herdType.getMaxHerdSize() && this.leader != null) {
                    if (GameRuleHandler.KILL_HERD_OUTCAST.getBoolean(this.leader.world) && this.herdType.getDinosaurType() == Dinosaur.DinosaurType.AGGRESSIVE) {
                        for (DinosaurEntity entity : otherHerd)
                            if (!this.enemies.contains(otherHerd)) {
                                this.enemies.add(entity);
                            } else {
                                return;
                            }
                    }
                }
            }
        }
    }

    public void updateLeader() {
        if (this.members.size() > 0) {
            this.leader = this.members.iterator().next();
        } else {
            this.leader = null;
        }
    }

    public Vec3d getCenterPosition() {
        if (this.members.size() == 1) {
            return this.leader.getPositionVector();
        }

        double x = 0.0;
        double z = 0.0;

        int count = 0;

        for (DinosaurEntity member : this.members) {
            if (!member.isCarcass() && !member.isInWater()) {
                x += member.posX;
                z += member.posZ;

                count++;
            }
        }

        if (count > 0) {
            x /= count;
            z /= count;
        } else {
            return this.leader.getPositionVector();
        }

        return new Vec3d(x, this.leader.world.getHeight(new BlockPos(x, 0, z)).getY(), z);
    }

    public void addMember(DinosaurEntity entity) {
        Herd oldHerd = entity.herd;

        if (oldHerd != null) {
            oldHerd.members.remove(entity);

            this.enemies.addAll(oldHerd.enemies);
            this.fleeing |= oldHerd.fleeing;

            if (oldHerd.leader == entity) {
                oldHerd.updateLeader();
            }
        }

        entity.herd = this;
        this.members.add(entity);
    }

    public void disband() {
        this.leader = null;
        this.members.clear();
    }

    public int size() {
        return this.members.size();
    }

    @Override
    public Iterator<DinosaurEntity> iterator() {
        return this.members.iterator();
    }

    public void normalizeMovement() {
        float length = (float) Math.sqrt(Math.pow(this.moveX, 2) + Math.pow(this.moveZ, 2));
        this.moveX = this.moveX / length;
        this.moveZ = this.moveZ / length;
    }

    public boolean shouldDefend(List<EntityLivingBase> entities) {
        return this.getScore(this) + (this.herdType.getAttackBias() * this.members.size()) > this.getScore(entities);
    }

    public double getScore(Iterable<? extends EntityLivingBase> entities) {
        double score = 0.0F;

        for (EntityLivingBase entity : entities) {
            if (entity != null && entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE) != null) {
                score += entity.getHealth() * entity.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getBaseValue();
            }
        }

        return score;
    }

    public boolean isBusy() {
        return this.fleeing || this.state == State.MOVING || !this.enemies.isEmpty();
    }

    public enum State {
        MOVING,
        IDLE
    }
}