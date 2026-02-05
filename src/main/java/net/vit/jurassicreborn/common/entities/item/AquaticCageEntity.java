package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.GlowSquid;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.entities.animal.SharkEntity;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Utility for capturing aquatic entities inside an ItemStack.
 */
public class AquaticCageEntity {

    /**
     * Captures the provided entity inside the given ItemStack if possible.
     * The entity's NBT is stored under the {@code EntityTag} key and the entity
     * is removed from the world.
     */
    public static void captureEntity(LivingEntity entity, ItemStack stack) {
        if (entity != null && (entity instanceof SwimmingDinosaurEntity || entity instanceof Squid ||entity instanceof TropicalFish||entity instanceof Pufferfish||entity instanceof Cod ||entity instanceof Salmon||entity instanceof Dolphin ||entity instanceof Turtle||entity instanceof Axolotl ||entity instanceof GlowSquid || entity instanceof SharkEntity)) {
            if (!stack.hasTag()) {
                CompoundTag entityTag = new CompoundTag();
                entity.saveWithoutId(entityTag);
                ResourceLocation id = ForgeRegistries.ENTITIES.getKey(entity.getType());
                if (id != null) {
                    entityTag.putString("id", id.toString());
                }
                stack.getOrCreateTag().put("EntityTag", entityTag);
                stack.getTag().putString("name", entity.getDisplayName().getString());
                entity.discard();
            }
        }
    }
}
