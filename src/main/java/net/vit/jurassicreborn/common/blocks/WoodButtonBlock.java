package net.vit.jurassicreborn.common.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WoodButtonBlock extends ButtonBlock {

    public WoodButtonBlock(BlockBehaviour.Properties properties) {
        // Parameters: (properties, pressDuration, arrowsCanPress, pressSound, releaseSound)
        super(properties, 30, true, SoundEvents.WOODEN_BUTTON_CLICK_ON, SoundEvents.WOODEN_BUTTON_CLICK_OFF);
    }
}
