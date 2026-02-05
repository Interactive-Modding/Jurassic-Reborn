package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.RebornConfig;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.minecraftforge.registries.ForgeRegistries;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility for capturing land creatures inside an ItemStack.
 */
public class CageEntity {

    private static final Set<String> ID_BLACKLIST = new HashSet<>();

    static {
        for (String id : RebornConfig.ENTITY_BLACKLIST.blacklist) {
            ID_BLACKLIST.add(id);
        }
    }

    /**
     * Captures the provided entity inside the given ItemStack if it is allowed.
     */
    public static void captureEntity(LivingEntity entity, ItemStack stack) {
        if (entity != null && !(entity instanceof Player || entity instanceof EnderDragon || entity instanceof WitherBoss || entity instanceof SwimmingDinosaurEntity || entity instanceof ElderGuardian)) {
            if (!stack.hasTag()) {
                ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType());
                if (id != null && !ID_BLACKLIST.contains(id.toString())) {
                    CompoundTag entityTag = new CompoundTag();
                    entity.saveWithoutId(entityTag);
                    entityTag.putString("id", id.toString());
                    stack.getOrCreateTag().put("EntityTag", entityTag);
                    stack.getTag().putString("name", entity.getDisplayName().getString());
                    entity.discard();
                }
            }
        }
    }
}
