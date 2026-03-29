package net.vit.jurassicreborn.common.items;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.JukeboxSong;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.vit.jurassicreborn.JurassicReborn;
import net.vit.jurassicreborn.client.sounds.SoundHandler;

public class ModJukeboxSongs {

    public static final DeferredRegister<JukeboxSong> JUKEBOX_SONGS =
            DeferredRegister.create(Registries.JUKEBOX_SONG, JurassicReborn.MODID);

    public static final ResourceKey<JukeboxSong> JURASSICREBORN_THEME =
            ResourceKey.create(Registries.JUKEBOX_SONG,
                    ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "jurassicreborn_theme"));

    public static final ResourceKey<JukeboxSong> TROODONS_AND_RAPTORS =
            ResourceKey.create(Registries.JUKEBOX_SONG,
                    ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "troodons_and_raptors"));

    public static final ResourceKey<JukeboxSong> DONT_MOVE_A_MUSCLE =
            ResourceKey.create(Registries.JUKEBOX_SONG,
                    ResourceLocation.fromNamespaceAndPath(JurassicReborn.MODID, "dont_move_a_muscle"));

    static {
        JUKEBOX_SONGS.register("jurassicreborn_theme", () ->
                new JukeboxSong(
                        Holder.direct(SoundHandler.JURASSICREBORN_THEME),
                        Component.translatable("jukebox_song.jurassicreborn.jurassicreborn_theme"),
                        4740 / 20F,
                        15
                )
        );

        JUKEBOX_SONGS.register("troodons_and_raptors", () ->
                new JukeboxSong(
                        Holder.direct(SoundHandler.JURASSICREBORN_THEME),
                        Component.translatable("jukebox_song.jurassicreborn.troodons_and_raptors"),
                        1760 / 20F,
                        15
                )
        );

        JUKEBOX_SONGS.register("dont_move_a_muscle", () ->
                new JukeboxSong(
                        Holder.direct(SoundHandler.DONT_MOVE_A_MUSCLE),
                        Component.translatable("jukebox_song.jurassicreborn.dont_move_a_muscle"),
                        2040 / 20F,
                        15
                )
        );
    }
}
