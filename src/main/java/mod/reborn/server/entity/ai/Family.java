package mod.reborn.server.entity.ai;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.GrowthStage;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

public class Family {
    private UUID head;
    private final Set<UUID> parents = new HashSet<>();
    private final Set<UUID> children = new HashSet<>();
    private BlockPos home;
    private int stayHome;

    public Family(UUID... parents) {
        this.head = parents[0];
        Collections.addAll(this.parents, parents);
    }

    public Family(Set<UUID> parents, Set<UUID> children, BlockPos home) {
        this.parents.addAll(parents);
        this.children.addAll(children);
        this.home = home;
    }

    public boolean update(DinosaurEntity entity) {
        if (this.home == null || entity.getDistanceSqToCenter(this.home) > 4096) {
            this.home = entity.getPosition();
        }
        World world = entity.world;
        double centerX = 0.0;
        double centerZ = 0.0;
        Set<UUID> remove = new HashSet<>();
        Set<DinosaurEntity> members = new HashSet<>();
        for (UUID parent : this.parents) {
            DinosaurEntity parentEntity = this.get(world, parent);
            if (parentEntity == null || parentEntity.isDead || parentEntity.isCarcass()) {
                remove.add(parent);
            } else {
                centerX += parentEntity.posX;
                centerZ += parentEntity.posZ;
                members.add(parentEntity);
                parentEntity.family = this;
            }
        }
        for (UUID child : this.children) {
            DinosaurEntity childEntity = this.get(world, child);
            if (childEntity == null || childEntity.isDead || childEntity.isCarcass() || childEntity.getAgePercentage() > 50) {
                remove.add(child);
            } else {
                members.add(childEntity);
                childEntity.family = this;
            }
        }
        this.parents.removeAll(remove);
        this.children.removeAll(remove);
        if (this.parents.isEmpty()) {
            return true;
        }
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
            centerX = (this.home.getX() / 2) + (centerX / 2);
            centerZ = (this.home.getZ() / 2) + (centerZ / 2);
        }
        double centerDistance = entity.getDistanceSq(centerX, entity.posY, centerZ);
        Random random = entity.getRNG();
        if (random.nextDouble() * centerDistance > 128) {
            for (DinosaurEntity member : members) {
                if (member.getAttackTarget() == null && member.getNavigator().noPath()) {
                    int travelX = (int) (centerX + random.nextInt(4) - 2);
                    int travelZ = (int) (centerZ + random.nextInt(4) - 2);
                    int travelY = world.getTopSolidOrLiquidBlock(new BlockPos(travelX, 0, travelZ)).getY();
                    member.getNavigator().tryMoveToXYZ(travelX, travelY, travelZ, 0.8);
                }
            }
        }
        if (entity.getRNG().nextInt(50) == 0 && (entity.getDinosaur().shouldBreedAroundOffspring() || this.children.isEmpty())) {
            DinosaurEntity father = null;
            DinosaurEntity mother = null;
            for (DinosaurEntity member : members) {
                if (this.parents.contains(member.getUniqueID())) {
                    if (!member.shouldSleep() && member.getBreedCooldown() <= 0 && !member.isBreeding() && member.getHealth() >= member.getMaxHealth() && member.getGrowthStage() == GrowthStage.ADULT) {
                        if (member.isMale()) {
                            father = member;
                        } else {
                            mother = member;
                        }
                    }
                }
            }
            if (father != null && mother != null) {
                if (father.getDistanceSq(mother) < 128) {
                    father.getNavigator().tryMoveToEntityLiving(mother, 1.0);
                    mother.getNavigator().tryMoveToEntityLiving(father, 1.0);
                    father.breed(mother);
                    mother.breed(father);
                }
            }
        }
        return false;
    }

    private DinosaurEntity get(World world, UUID uuid) {
        for (Entity entity : world.loadedEntityList) {
            if (entity instanceof DinosaurEntity && entity.getUniqueID().equals(uuid)) {
                return (DinosaurEntity) entity;
            }
        }
        return null;
    }

    public void addChild(UUID child) {
        this.children.add(child);
    }

    public UUID getHead() {
        return this.head;
    }

    public void writeToNBT(NBTTagCompound familyTag) {
        NBTTagList children = new NBTTagList();
        for (UUID child : this.children) {
            NBTTagCompound childTag = new NBTTagCompound();
            childTag.setUniqueId("UUID", child);
            children.appendTag(childTag);
        }
        familyTag.setTag("Children", children);
        NBTTagList parents = new NBTTagList();
        for (UUID parent : this.parents) {
            NBTTagCompound parentTag = new NBTTagCompound();
            parentTag.setUniqueId("UUID", parent);
            parents.appendTag(parentTag);
        }
        familyTag.setTag("Parents", parents);
        if (this.home != null) {
            familyTag.setLong("Home", this.home.toLong());
        }
        familyTag.setInteger("StayHome", this.stayHome);
    }

    public static Family readFromNBT(NBTTagCompound familyTag) {
        Set<UUID> children = new HashSet<>();
        Set<UUID> parents = new HashSet<>();
        NBTTagList parentsList = familyTag.getTagList("Parents", Constants.NBT.TAG_COMPOUND);
        NBTTagList childrenList = familyTag.getTagList("Children", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < parentsList.tagCount(); i++) {
            NBTTagCompound parentTag = parentsList.getCompoundTagAt(i);
            parents.add(parentTag.getUniqueId("UUID"));
        }
        for (int i = 0; i < childrenList.tagCount(); i++) {
            NBTTagCompound childTag = childrenList.getCompoundTagAt(i);
            children.add(childTag.getUniqueId("UUID"));
        }
        BlockPos home = null;
        if (familyTag.hasKey("Home")) {
            home = BlockPos.fromLong(familyTag.getLong("Home"));
        }
        Family family = new Family(parents, children, home);
        family.stayHome = familyTag.getInteger("StayHome");
        return family;
    }

    public void setHome(BlockPos position, int stay) {
        this.home = position;
        this.stayHome = stay;
    }
}
