package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.vit.jurassicreborn.common.entities.DinosaurEntity;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

import java.util.UUID;

public class TrackerDart extends Dart {

    public TrackerDart() {
        // Passes the lambda consumer and color to Dart
        super((entity, stack) -> {
            CompoundTag tag = ItemStackNbtUtil.getTag(stack);
            String uuid = tag != null && tag.contains("uuid") ? tag.getString("uuid") : "";
            init(entity, uuid);
        }, 0x111111);
    }

    public static void init(DinosaurEntity entity, String uuid) {
        try {
            UUID track = UUID.fromString(uuid);
            entity.addTracker(track);
        } catch (Exception ignored) {}
    }
}
