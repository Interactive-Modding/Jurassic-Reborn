package net.vit.jurassicreborn.common.items.guns;

import net.vit.jurassicreborn.client.sounds.SoundHandler;

public class Remington extends Gun {
    public Remington() {
        super(
                1,
                SoundHandler.FIRE,
                SoundHandler.EMPTY,
                SoundHandler.RELOAD,
                8,                       // clipSize
                60,                      // reloadCooldown
                15,                      // shotCooldown
                7.5F,                    // speed
                0.1F,                    // inaccuracy
                0.0F,                    // pitchOffset
                15                       // damage
        );
    }
}