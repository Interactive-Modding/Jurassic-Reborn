package mod.reborn.server.block.entity;

import mod.reborn.server.block.BlockHandler;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CultivatorCapability implements IItemHandlerModifiable {
	
	private final CultivatorBlockEntity otherTile;
	private CultivatorBlockEntity mainTile;
	public boolean isUp = false;

	public CultivatorCapability(@Nullable CultivatorBlockEntity otherTile) {
		
		this.otherTile = otherTile;
		updateTile();

	}
	
	private void updateTile() {
		World world = otherTile.getWorld();
		BlockPos pos = otherTile.getPos();
		if (world == null || pos == null || !world.isBlockLoaded(pos))
			return;

		Block blockType = otherTile.getBlockType();
		BlockPos blockpos;
		
		if (blockType == BlockHandler.CULTIVATOR_BOTTOM) {
			this.mainTile = otherTile;
			return;
		} else if (blockType == BlockHandler.CULTIVATOR_TOP) {
			blockpos = pos.down();
			this.isUp = true;
		} else {
			this.mainTile = null;
			return;
		}

		Block block = world.getBlockState(blockpos).getBlock();
		TileEntity mainTile = world.getTileEntity(blockpos);
		if (mainTile instanceof CultivatorBlockEntity) {
			
			CultivatorBlockEntity mainTileTE = (CultivatorBlockEntity) mainTile;
			this.mainTile = mainTileTE;
			return;
		}
	}

	@Nullable
	private CultivatorBlockEntity getMainTile() {
		
		updateTile();
		CultivatorBlockEntity tileCultivator = this.mainTile;
		return tileCultivator != null && !tileCultivator.isInvalid() ? tileCultivator : null;
	}

	@Override
	public int getSlots() {
		CultivatorBlockEntity tile = getMainTile();
		return tile != null ? tile.getSlots().size() : 0;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		CultivatorBlockEntity tile = getMainTile();
		return tile != null ? tile.getStackInSlot(slot) : ItemStack.EMPTY;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		CultivatorBlockEntity tile = getMainTile();
		if (tile != null) {
			IItemHandler handler = tile.getCapabilityHandler();
			if (handler instanceof IItemHandlerModifiable) {
				((IItemHandlerModifiable) handler).setStackInSlot(slot, stack);
			}
		}

		tile = getMainTile();
		if (tile != null)
			tile.markDirty();
	}
	
	@Override
	@Nonnull
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

		CultivatorBlockEntity tile = getMainTile();
		if (tile == null)
			return stack;
		
		int starting = stack.getCount();
		ItemStack retValue = tile.getCapabilityHandler().insertItem(slot, stack, simulate);
		if (retValue.getCount() != starting && !simulate) {
			tile = getMainTile();
			if (tile != null)
				tile.markDirty();
		}

		return retValue;

	}

	@Override
	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		CultivatorBlockEntity tile = getMainTile();
		if (tile == null)
			return ItemStack.EMPTY;

		ItemStack retValue = tile.getCapabilityHandler().extractItem(slot, amount, simulate);
		if (!retValue.isEmpty() && !simulate) {
			tile = getMainTile();
			if (tile != null)
				tile.markDirty();
		}

		return retValue;
	}

	@Override
	public int getSlotLimit(int slot) {
		return 64;
	}

}