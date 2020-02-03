package mod.reborn.server.item.block;

import net.minecraft.block.BlockLeaves;
import net.minecraft.item.ItemLeaves;
import net.minecraft.item.ItemStack;

public class AncientItemLeaves extends ItemLeaves {

	public AncientItemLeaves(BlockLeaves block) {
		super(block);
		this.setHasSubtypes(false);
	}

	@Override
	public int getMetadata(int damage) {
		return 8; // Leaves which don't decay
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName();
	}

}
