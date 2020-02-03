package mod.reborn.server.block.entity;

import com.google.common.primitives.Ints;
import mod.reborn.RebornMod;
import mod.reborn.server.api.BreedableBug;
import mod.reborn.server.container.BugCrateContainer;
import mod.reborn.server.food.FoodHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;

public class BugCrateBlockEntity extends MachineBaseBlockEntity {
    private static final int[] INPUTS = new int[] { 0, 1, 2, 3, 4, 5 };
    private static final int[] OUTPUTS = new int[] { 6, 7, 8 };

    private NonNullList<ItemStack> slots = NonNullList.withSize(9, ItemStack.EMPTY);

    @Override
    protected int getProcess(int slot) {
        return 0;
    }

    @Override
    protected boolean canProcess(int process) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.getStackInSlot(i);
            BreedableBug bug = BreedableBug.getBug(stack);
            if (bug != null && !this.getBestFood(bug).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void processItem(int process) {
        for (int i = 0; i < 3; i++) {
            ItemStack stack = this.getStackInSlot(i);
            BreedableBug bug = BreedableBug.getBug(stack);
            if (bug != null) {
                ItemStack food = this.getBestFood(bug);
                if (!food.isEmpty()) {
                    ItemStack output = new ItemStack((Item) bug, bug.getBreedings(food));
                    for (int slot = 0; slot < this.slots.size(); slot++) {
                        if (this.slots.get(slot) == food) {
                            this.decreaseStackSize(slot);
                            break;
                        }
                    }
                    int outputSlot = this.getOutputSlot(output);
                    if (outputSlot != -1) {
                        this.mergeStack(outputSlot, output);
                    } else {
                        EntityItem item = new EntityItem(this.world, this.pos.getX() + 0.5, this.pos.getY() + 1.0, this.pos.getZ(), output);
                        this.world.spawnEntity(item);
                    }
                    return;
                }
            }
        }
    }

    private ItemStack getBestFood(BreedableBug bug) {
        ItemStack best = ItemStack.EMPTY;
        int highestBreedings = Integer.MIN_VALUE;
        for (int i = 3; i < 6; i++) {
            ItemStack food = this.getStackInSlot(i);
            if (!food.isEmpty()) {
                int breedings = bug.getBreedings(food);
                if (breedings > 0 && breedings > highestBreedings) {
                    highestBreedings = breedings;
                    best = food;
                }
            }
        }
        return best;
    }

    @Override
    protected int getMainOutput(int process) {
        return 0;
    }

    @Override
    protected int getStackProcessTime(ItemStack stack) {
        BreedableBug bug = BreedableBug.getBug(stack);
        if (bug != null) {
            ItemStack food = this.getBestFood(bug);
            if (food != null) {
                return Math.max(1, bug.getBreedings(food) / 3) * 400;
            }
        }
        return 0;
    }

    @Override
    protected int getProcessCount() {
        return 1;
    }

    @Override
    protected int[] getInputs() {
        return INPUTS;
    }

    @Override
    protected int[] getInputs(int process) {
        return INPUTS;
    }

    @Override
    protected int[] getOutputs() {
        return OUTPUTS;
    }

    @Override
    protected NonNullList<ItemStack> getSlots() {
        return this.slots;
    }

    @Override
    protected void setSlots(NonNullList<ItemStack> slots) {
        this.slots = slots;
    }

    @Override
    public Container createContainer(InventoryPlayer playerInventory, EntityPlayer player) {
        return new BugCrateContainer(playerInventory, this);
    }

    @Override
    public String getGuiID() {
        return RebornMod.MODID + ":";
    }

    @Override
    public String getName() {
        return this.hasCustomName() ? this.customName : "container.bug_crate";
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        NonNullList<ItemStack> slots = this.getSlots();
        boolean stacksEqual = !stack.isEmpty()&& stack.isItemEqual(slots.get(index)) && ItemStack.areItemStackTagsEqual(stack, slots.get(index));
        slots.set(index, stack);
        if (!stack.isEmpty() && stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }
        if (!stacksEqual) {
            int process = this.getProcess(index);
            if (process >= 0 && process < this.getProcessCount()) {
                int prevTotalProcessTime = this.totalProcessTime[process];
                for (int i = 0; i < 3; i++) {
                    ItemStack foodStack = this.getStackInSlot(i);
                    BreedableBug bug = BreedableBug.getBug(foodStack);
                    if (bug != null && this.getBestFood(bug) != null) {
                        this.totalProcessTime[process] = this.getStackProcessTime(foodStack);
                        break;
                    }
                }
                if (prevTotalProcessTime != this.totalProcessTime[process]) {
                    this.processTime[process] = 0;
                }
                this.markDirty();
            }
        }
    }
    
    @Override
	public boolean canExtractItem(int slotID, ItemStack itemstack, EnumFacing side) {
		return Ints.asList(OUTPUTS).contains(slotID);
	}

	@Override
	public boolean isItemValidForSlot(int slotID, ItemStack itemstack) {
		if (Ints.asList(INPUTS).contains(slotID)) {
			
			if((float)(slotID / 2F) <= 1 && itemstack.getItem() instanceof BreedableBug) {
				return true;
			}else if(!((float)(slotID / 2F) <= 1)){
				if(FoodHelper.isFood(itemstack.getItem()) && !(itemstack.getItem() instanceof BreedableBug)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean isEmpty() {
		return false;
	}
}
