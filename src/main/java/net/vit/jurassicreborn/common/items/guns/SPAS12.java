package net.vit.jurassicreborn.common.items.guns;

import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.items.TabHandler;

public class SPAS12 extends Gun {
    public SPAS12() {
        super(
                TabHandler.ITEMS,
                1,                       // shots per trigger pull
                SoundHandler.FIRE,
                SoundHandler.EMPTY,
                SoundHandler.RELOAD,
                6,                      // clipSize
                60,                     // reloadCooldown
                20,                     // shotCooldown
                0.9F,                   // speed
                15.0F,                  // inaccuracy
                0.0F,                   // pitchOffset
                5                       // damage
        );
    }
}