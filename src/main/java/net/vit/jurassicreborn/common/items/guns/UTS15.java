package net.vit.jurassicreborn.common.items.guns;

import net.vit.jurassicreborn.client.sounds.SoundHandler;
import net.vit.jurassicreborn.common.items.guns.Gun;

public class UTS15 extends Gun {
    public UTS15() {
        super(
                1,                       // shots per trigger pull
                SoundHandler.FIRE,
                SoundHandler.EMPTY,
                SoundHandler.RELOAD,
                24,                     // clipSize
                80,                     // reloadCooldown
                5,                      // shotCooldown
                7.5F,                   // speed
                8.0F,                   // inaccuracy
                0.0F,                   // pitchOffset
                6                       // damage
        );
    }
}
