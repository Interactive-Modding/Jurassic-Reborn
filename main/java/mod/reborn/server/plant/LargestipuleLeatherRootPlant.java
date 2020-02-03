package mod.reborn.server.plant;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;

/**
 * Created by Codyr on 26/10/2016.
 */
public class LargestipuleLeatherRootPlant extends Plant{
    @Override
    public String getName() { return "Largestipule Leather Root"; }

    @Override
    public Block getBlock() { return BlockHandler.LARGESTIPULE_LEATHER_ROOT; }

        @Override
        public int getHealAmount () {
            return 2000;
        }

    }
