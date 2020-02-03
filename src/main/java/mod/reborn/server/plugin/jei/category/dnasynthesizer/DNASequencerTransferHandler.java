package mod.reborn.server.plugin.jei.category.dnasynthesizer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mod.reborn.RebornMod;
import mod.reborn.server.block.entity.DNASequencerBlockEntity;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.container.DNASequencerContainer;
import mod.reborn.server.message.DNASequenceTransferClicked;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

public class DNASequencerTransferHandler implements IRecipeTransferHandler<DNASequencerContainer> {

    private final IRecipeTransferHandlerHelper helper;

    public DNASequencerTransferHandler(IRecipeTransferHandlerHelper helper) {
        this.helper = helper;
    }

    @Override
    public Class<DNASequencerContainer> getContainerClass() {
        return DNASequencerContainer.class;
    }

    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(DNASequencerContainer container, IRecipeLayout recipeLayout, EntityPlayer player, boolean maxTransfer, boolean doTransfer) {

        DNASequencerBlockEntity blockEntity = (DNASequencerBlockEntity) container.getInventoryTile();
        int nextProcessID = 0;
        boolean canBeProcessed = false;
        for(int i = 0; i < 3; i++) {
            if(blockEntity.isProcessing(nextProcessID)) {
                nextProcessID++;
            } else {
                canBeProcessed = true;
                break;
            }
        }
        if(!canBeProcessed) {
            return helper.createUserErrorWithTooltip("Machine is full"); //TODO: localize
        }
        int slotOffset = nextProcessID * 2;

        Map<Integer, List<ItemStack>> requiredInputs = Maps.newHashMap();
        List<Pair<Integer, Integer>> linkedInputMap = Lists.newArrayList();
        List<Integer> leftInputs = Lists.newArrayList();

        recipeLayout.getItemStacks().getGuiIngredients().forEach((id, ingredient) -> {
            if(ingredient.isInput()) {
                requiredInputs.put(id + slotOffset, ingredient.getAllIngredients());
                leftInputs.add(id);
            }
        });
        List<ItemStack> foundStacks = Lists.newArrayList();
        for(int i = 9; i < 45; i++) {
            Slot slot = container.getSlot(i);
            ItemStack stack = slot.getStack();
            for(Map.Entry<Integer, List<ItemStack>> entry : requiredInputs.entrySet()) {
                loop:
                for(ItemStack requiredStack : entry.getValue()) {
                    if(requiredStack.isItemEqual(stack) && container.getSlot(entry.getKey()).isItemValid(stack)) {
                        for(ItemStack foundStack : foundStacks) {
                            if(foundStack.isItemEqual(stack)) {
                                continue loop;
                            }
                        }
                        foundStacks.add(stack);
                        linkedInputMap.add(Pair.of(i, entry.getKey()));
                        leftInputs.remove(Integer.valueOf(entry.getKey() - slotOffset));
                    }
                }
            }
        }
        if(!leftInputs.isEmpty()) {
            return helper.createUserErrorForSlots(I18n.format("jei.tooltip.error.recipe.transfer.missing"), leftInputs);
        }

        if(doTransfer) {
            RebornMod.NETWORK_WRAPPER.sendToServer(new DNASequenceTransferClicked(linkedInputMap));
        }

        return null;
    }

    public static void setItemServer(EntityPlayer player, List<Pair<Integer, Integer>> list) {
        Container container = player.openContainer;
        list.forEach(pair -> {
            Slot fromSlot = container.getSlot(pair.getLeft());
            Slot toSlot = container.getSlot(pair.getRight());

            ItemStack prevStack = toSlot.getStack();

            toSlot.putStack(fromSlot.getStack().splitStack(1));
            if(!prevStack.isEmpty()) {
                for(int i = 9; i < 45; i++) {
                    Slot slot = container.getSlot(i);
                    if(!slot.getHasStack()) {
                        slot.putStack(prevStack);
                    }
                }
            }
        });
    }
}
