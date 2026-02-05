package net.vit.jurassicreborn.common.blocks;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModStoneButtonBlock extends ButtonBlock {

    public ModStoneButtonBlock(BlockBehaviour.Properties properties) {
        // Parameters: (properties, pressDuration, arrowsCanPress, pressSound, releaseSound)
        super(properties, 20, false, SoundEvents.STONE_BUTTON_CLICK_ON, SoundEvents.STONE_BUTTON_CLICK_OFF);
    }
}
