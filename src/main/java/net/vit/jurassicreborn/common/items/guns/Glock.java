package net.vit.jurassicreborn.common.items.guns;

import net.vit.jurassicreborn.client.sounds.SoundHandler;

public class Glock extends Gun {
    public Glock() {
        super(
                1,                         // amountPerShot
                SoundHandler.FIRE,
                SoundHandler.EMPTY,
                SoundHandler.RELOAD,
                8,                         // clipSize
                40,                        // reloadCooldown (in ticks)
                5,                         // shotCooldown (in ticks)
                7.5F,                      // speed
                0.0F,                      // inaccuracy
                5.0F,                      // pitchOffset
                2                          // damage per bullet
        );
    }
}