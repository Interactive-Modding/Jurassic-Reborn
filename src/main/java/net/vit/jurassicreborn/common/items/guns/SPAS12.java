package net.vit.jurassicreborn.common.items.guns;

import net.vit.jurassicreborn.client.sounds.SoundHandler;

public class SPAS12 extends Gun {
    public SPAS12() {
        super(
                1,                       // shots per trigger pull
                SoundHandler.FIRE,
                SoundHandler.EMPTY,
                SoundHandler.RELOAD,          // your mod’s RELOAD sound
                6,                      // clipSize
                60,                     // reloadCooldown
                20,                     // shotCooldown
                7.5F,                   // speed
                15.0F,                  // inaccuracy
                0.0F,                   // pitchOffset
                5                       // damage
        );
    }
}