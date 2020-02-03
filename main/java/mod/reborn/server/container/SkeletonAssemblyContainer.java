package mod.reborn.server.container;

import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.item.DisplayBlockItem;
import mod.reborn.server.item.FossilItem;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.server.container.slot.FossilSlotCrafting;
import mod.reborn.server.container.slot.SkeletonCraftingSlot;

import javax.annotation.Nullable;

public class SkeletonAssemblyContainer extends Container {
    private static final int WIDTH = 5;
    private static final int HEIGHT = 5;

    public InventoryCrafting craftMatrix = new InventoryCrafting(this, WIDTH, HEIGHT);
    public IInventory craftResult = new InventoryCraftResult();
    private final World worldObj;
    private final BlockPos pos;

    public SkeletonAssemblyContainer(InventoryPlayer inventory, World world, BlockPos pos) {
        this.worldObj = world;
        this.pos = pos;
        this.addSlotToContainer(new SkeletonCraftingSlot(inventory.player, this.craftMatrix, this.craftResult, 0, 140, 52));

        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                this.addSlotToContainer(new FossilSlotCrafting(this.craftMatrix, x + y * WIDTH, 16 + x * 18, 16 + y * 18));
            }
        }

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(inventory, x + y * 9 + 9, 8 + x * 18, 119 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(inventory, x, 8 + x * 18, 177));
        }

        this.onCraftMatrixChanged(this.craftMatrix);
    }

    @Override
    public void onCraftMatrixChanged(IInventory inventory) {
        this.craftResult.setInventorySlotContents(0, this.getResult());
    }

    private ItemStack getResult() {
        AssemblyData data = this.getAssemblyData();
        if (data != null && data.dinosaur.getRecipe() != null) {
            String[][] recipe = data.dinosaur.getRecipe();
            int targetWidth = recipe[0].length;
            int targetHeight = recipe.length;

            Bounds bounds = this.calculateAssemblyBounds();
            if (bounds.getWidth() + 1 == targetWidth && bounds.getHeight() + 1 == targetHeight) {
                for (int y = 0; y < targetHeight; y++) {
                    for (int x = 0; x < targetWidth; x++) {
                        ItemStack stack = this.craftMatrix.getStackInSlot(x + bounds.minX + (y + bounds.minY) * WIDTH);
                        String targetBone = recipe[y][x];
                        if (!targetBone.equals(this.identify(stack))) {
                            this.craftResult.setInventorySlotContents(0, ItemStack.EMPTY);
                            return ItemStack.EMPTY;
                        }
                    }
                }
                return data.getResult();
            }
        }

        return ItemStack.EMPTY;
    }

    private String identify(ItemStack stack) {
        if (stack != ItemStack.EMPTY && stack.getItem() instanceof FossilItem) {
            return ((FossilItem) stack.getItem()).getBoneType();
        }
        return "";
    }

    private AssemblyData getAssemblyData() {
        AssemblyData data = null;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int id = x + y * WIDTH;
                ItemStack stack = this.craftMatrix.getStackInSlot(id);
                AssemblyData stackData = this.getAssemblyData(stack);
                if (stackData != null) {
                    if (data != null && !data.equals(stackData)) {
                        return null;
                    }
                    data = stackData;
                }
            }
        }
        return data;
    }

    private AssemblyData getAssemblyData(ItemStack stack) {
        if (stack != ItemStack.EMPTY && stack.getItem() instanceof FossilItem) {
            FossilItem item = (FossilItem) stack.getItem();
            return new AssemblyData(item.getDinosaur(stack), item.isFresh());
        }
        return null;
    }

    private Bounds calculateAssemblyBounds() {
        int minX = 5;
        int minY = 5;
        int maxX = 0;
        int maxY = 0;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int id = x + y * WIDTH;
                if (this.getAssemblyData(this.craftMatrix.getStackInSlot(id)) != null) {
                    if (x < minX) {
                        minX = x;
                    }
                    if (x > maxX) {
                        maxX = x;
                    }
                    if (y < minY) {
                        minY = y;
                    }
                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }
        return new Bounds(minX, minY, maxX, maxY);
    }

    @Override
    public void onContainerClosed(EntityPlayer player) {
        super.onContainerClosed(player);

        if (!this.worldObj.isRemote) {
            for (int i = 0; i < 25; ++i) {
                ItemStack stack = this.craftMatrix.removeStackFromSlot(i);

                if (stack != ItemStack.EMPTY) {
                    player.dropItem(stack, false);
                }
            }
        }
    }

    @Override
    public boolean canInteractWith(EntityPlayer playerIn) {
        return this.worldObj.getBlockState(this.pos).getBlock() == BlockHandler.SKELETON_ASSEMBLY && playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D,
                (double) this.pos.getZ() + 0.5D) <= 64.0D;
    }

    @Override
    @Nullable
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack()) {
            ItemStack itemstack1 = slot.getStack();
            stack = itemstack1.copy();

            if (index == 0) {
                if (!this.mergeItemStack(itemstack1, 26, 62, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, stack);
            } else if (index >= 26 && index < 53) {
                if (!this.mergeItemStack(itemstack1, 53, 62, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (index >= 53 && index < 62) {
                if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(itemstack1, 26, 53, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.getCount() == 0) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return stack;
    }

    @Override
    public boolean canMergeSlot(ItemStack stack, Slot slotIn) {
        return slotIn.inventory != this.craftResult && super.canMergeSlot(stack, slotIn);
    }

    private class Bounds {
        private final int minX;
        private final int minY;
        private final int maxX;
        private final int maxY;

        private Bounds(int minX, int minY, int maxX, int maxY) {
            this.minX = minX;
            this.minY = minY;
            this.maxX = maxX;
            this.maxY = maxY;
        }

        public int getWidth() {
            return this.maxX - this.minX;
        }

        public int getHeight() {
            return this.maxY - this.minY;
        }
    }

    private class AssemblyData {
        private final Dinosaur dinosaur;
        private final boolean fresh;

        private AssemblyData(Dinosaur dinosaur, boolean fresh) {
            this.dinosaur = dinosaur;
            this.fresh = fresh;
        }

        public ItemStack getResult() {
            int metadata = DisplayBlockItem.getMetadata(EntityHandler.getDinosaurId(this.dinosaur), this.fresh ? 2 : 1, true);
            return new ItemStack(ItemHandler.DISPLAY_BLOCK, 1, metadata);
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof AssemblyData) {
                AssemblyData data = (AssemblyData) obj;
                return data.dinosaur == this.dinosaur && data.fresh == this.fresh;
            }
            return false;
        }
    }
}