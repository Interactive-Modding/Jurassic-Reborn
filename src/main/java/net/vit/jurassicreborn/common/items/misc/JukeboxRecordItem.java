package net.vit.jurassicreborn.common.items.misc;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.EitherHolder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.JukeboxPlayable;
import net.minecraft.world.item.JukeboxSong;

import java.util.function.Supplier;

public class JukeboxRecordItem extends Item {

    public JukeboxRecordItem(ResourceKey<JukeboxSong> songKey, Properties properties) {
        super(properties
                .stacksTo(1)
                .component(
                        DataComponents.JUKEBOX_PLAYABLE,
                        new JukeboxPlayable(
                                new EitherHolder<>(songKey),
                                true
                        )
                )
        );
    }
}