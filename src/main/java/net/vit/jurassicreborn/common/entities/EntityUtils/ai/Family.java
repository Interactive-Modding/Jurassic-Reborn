package net.vit.jurassicreborn.common.entities.EntityUtils.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.entities.EntityUtils.GrowthStage;
import net.vit.jurassicreborn.common.util.BlockPosUtil;

import java.util.*;

public class Family {
    private UUID head;
    private final Set<UUID> parents  = new HashSet<>();
    private final Set<UUID> children = new HashSet<>();
    private BlockPos home;
    private int stayHome;

    public Family(UUID... parents) {
        if (parents != null && parents.length > 0) {
            this.head = parents[0];
            Collections.addAll(this.parents, parents);
        } else {
            this.head = null;
        }
    }

    public Family(Set<UUID> parents, Set<UUID> children, BlockPos home) {
        if (parents != null)  this.parents.addAll(parents);
        if (children != null) this.children.addAll(children);
        this.home = home;
        // Set a sensible default head if absent
        if (this.head == null && !this.parents.isEmpty()) {
            this.head = this.parents.iterator().next();
        }
    }

    public boolean update(DinosaurEntity entity) {
        if (entity == null) return false;

        if (this.home == null || entity.distanceToSqr(BlockPosUtil.blockPosToVec(this.home)) > 64 * 64) {
            this.home = BlockPosUtil.vecToBlockPos(entity.position());
        }

        Level world = entity.level;
        double centerX = 0.0;
        double centerZ = 0.0;

        Set<UUID> remove = new HashSet<>();
        Set<DinosaurEntity> members = new HashSet<>();

        // Parents
        for (UUID parent : this.parents) {
            DinosaurEntity parentEntity = this.get(world, parent);
            if (parentEntity == null || parentEntity.isDeadOrDying() || parentEntity.isCarcass()) {
                remove.add(parent);
            } else {
                centerX += parentEntity.getX();
                centerZ += parentEntity.getZ();
                members.add(parentEntity);
                parentEntity.family = this;
            }
        }

        // Children
        for (UUID child : this.children) {
            DinosaurEntity childEntity = this.get(world, child);
            if (childEntity == null || childEntity.isDeadOrDying() || childEntity.isCarcass() || childEntity.getAgePercentage() > 50) {
                remove.add(child);
            } else {
                members.add(childEntity);
                childEntity.family = this;
            }
        }

        this.parents.removeAll(remove);
        this.children.removeAll(remove);

        if (this.parents.isEmpty()) {
            return true; // dissolve family
        }

        // Ensure head is valid
        if ((remove.size() > 0 && !this.parents.contains(this.head)) || this.head == null) {
            this.head = this.parents.iterator().next();
        }

        centerX /= this.parents.size();
        centerZ /= this.parents.size();

        if (this.stayHome > 0) {
            this.stayHome--;
            centerX = this.home.getX();
            centerZ = this.home.getZ();
        } else {
            // bias center toward home a bit
            centerX = (this.home.getX() / 2.0) + (centerX / 2.0);
            centerZ = (this.home.getZ() / 2.0) + (centerZ / 2.0);
        }

        double centerDistance = entity.distanceToSqr(centerX, entity.getY(), centerZ);
        RandomSource random = entity.getRandom();

        // Occasionally encourage the group to drift toward the family center
        if (random.nextDouble() * centerDistance > 128) {
            for (DinosaurEntity member : members) {
                if (member.getAttackTarget() == null && member.getNavigation().isDone()) {
                    int travelX = (int) (centerX + random.nextInt(4) - 2);
                    int travelZ = (int) (centerZ + random.nextInt(4) - 2);
                    // 1.19.2: pick a surface Y using a heightmap, not Level#getHeight()
                    int travelY = world.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, travelX, travelZ);
                    member.getNavigation().moveTo(travelX + 0.5, travelY, travelZ + 0.5, 0.8);
                }
            }
        }

        // Breeding logic
        if (entity.getRandom().nextInt(50) == 0
                && (entity.getDinosaur().shouldBreedAroundOffspring() || this.children.isEmpty())) {

            DinosaurEntity father = null;
            DinosaurEntity mother = null;

            for (DinosaurEntity member : members) {
                if (this.parents.contains(member.getUUID())) {
                    boolean ready = !member.shouldSleep()
                            && member.getBreedCooldown() <= 0
                            && !member.isBreeding()
                            && member.getHealth() >= member.getMaxHealth()
                            && member.getGrowthStage() == GrowthStage.ADULT;

                    if (ready) {
                        if (member.isMale()) father = father == null ? member : father;
                        else                 mother = mother == null ? member : mother;
                    }
                }
            }

            if (father != null && mother != null) {
                if (father.distanceToSqr(mother) < 128) {
                    father.getNavigation().moveTo(mother, 1.0);
                    mother.getNavigation().moveTo(father, 1.0);
                    father.breed(mother);
                    mother.breed(father);
                }
            }
        }

        return false;
    }

    private DinosaurEntity get(Level world, UUID uuid) {
        if (world == null || world.isClientSide || uuid == null) return null;
        Entity e = ((ServerLevel) world).getEntity(uuid);
        return (e instanceof DinosaurEntity de) ? de : null;
    }

    public void addChild(UUID child) {
        if (child != null) this.children.add(child);
    }

    public UUID getHead() {
        return this.head;
    }

    public void writeToNBT(CompoundTag familyTag) {
        ListTag children = new ListTag();
        for (UUID child : this.children) {
            CompoundTag childTag = new CompoundTag();
            childTag.putUUID("UUID", child);
            children.add(childTag);
        }
        familyTag.put("Children", children);

        ListTag parents = new ListTag();
        for (UUID parent : this.parents) {
            CompoundTag parentTag = new CompoundTag();
            parentTag.putUUID("UUID", parent);
            parents.add(parentTag);
        }
        familyTag.put("Parents", parents);

        if (this.home != null) {
            familyTag.putLong("Home", this.home.asLong());
        }
        familyTag.putInt("StayHome", this.stayHome);
    }

    public static Family readFromNBT(CompoundTag familyTag) {
        Set<UUID> children = new HashSet<>();
        Set<UUID> parents  = new HashSet<>();

        ListTag parentsList  = familyTag.getList("Parents",  Tag.TAG_COMPOUND);
        ListTag childrenList = familyTag.getList("Children", Tag.TAG_COMPOUND);

        for (int i = 0; i < parentsList.size(); i++) {
            CompoundTag parentTag = parentsList.getCompound(i);
            parents.add(parentTag.getUUID("UUID"));
        }
        for (int i = 0; i < childrenList.size(); i++) {
            CompoundTag childTag = childrenList.getCompound(i);
            children.add(childTag.getUUID("UUID"));
        }

        BlockPos home = familyTag.contains("Home") ? BlockPos.of(familyTag.getLong("Home")) : null;

        Family family = new Family(parents, children, home);
        family.stayHome = familyTag.getInt("StayHome");
        return family;
    }

    public void setHome(BlockPos position, int stay) {
        this.home = position;
        this.stayHome = stay;
    }
}
