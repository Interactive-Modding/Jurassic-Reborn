package net.vit.jurassicreborn.common.entities.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.JurassicConfig;
import net.vit.jurassicreborn.common.entities.SwimmingDinosaurEntity;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import java.util.HashSet;
import java.util.Set;

/**
 * Utility for capturing land creatures inside an ItemStack.
 */
public class CageEntity {

    private static final Set<String> ID_BLACKLIST = new HashSet<>();

    static {
        for (String id : JurassicConfig.ENTITY_BLACKLIST.blacklist) {
            ID_BLACKLIST.add(id);
        }
    }

    /**
     * Captures the provided entity inside the given ItemStack if it is allowed.
     */
    public static void captureEntity(LivingEntity entity, ItemStack stack) {
        if (entity != null && !(entity instanceof Player || entity instanceof Warden || entity instanceof EnderDragon || entity instanceof WitherBoss || entity instanceof SwimmingDinosaurEntity || entity instanceof ElderGuardian)) {
            if (!ItemStackNbtUtil.hasTag(stack)) {
                ResourceLocation id = BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType());
                if (id != null && !ID_BLACKLIST.contains(id.toString())) {
                    CompoundTag entityTag = new CompoundTag();
                    entity.saveWithoutId(entityTag);
                    entityTag.putString("id", id.toString());
                    CompoundTag tag = ItemStackNbtUtil.getOrCreateTag(stack);
                    tag.put("EntityTag", entityTag);
                    tag.putString("name", entity.getDisplayName().getString());
                    ItemStackNbtUtil.setTag(stack, tag);
                    entity.discard();
                }
            }
        }
    }
}
