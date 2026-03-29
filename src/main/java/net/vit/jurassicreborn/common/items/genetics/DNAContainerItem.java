package net.vit.jurassicreborn.common.items.genetics;

import net.minecraft.core.component.DataComponents;
import net.vit.jurassicreborn.common.genetics.DinoDNA;
import net.vit.jurassicreborn.common.genetics.GeneticsHelper;
import net.vit.jurassicreborn.common.genetics.PlantDNA;
import net.vit.jurassicreborn.common.genetics.StorageTypeRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import net.vit.jurassicreborn.common.util.ItemStackNbtUtil;

public class DNAContainerItem extends Item {

    protected boolean appendTooltip = true;

    public DNAContainerItem(Properties pProperties) {
        super(pProperties);
    }

    public int getContainerId(ItemStack stack) {//todo:what
        return 0;
    }


    public static int getDNAQuality(boolean creative, ItemStack stack) {
        int quality = creative ? 100 : 0;

        CompoundTag nbt = ItemStackNbtUtil.getOrCreateTag(stack);


        if (nbt.contains("DNA")) {
            if(nbt.getCompound("DNA").getString("StorageId").equals("DinoDNA")){
                DinoDNA dna = DinoDNA.fromStack(stack);
                if(dna == null){
                    return 0;
                }
                quality = dna.getDNAQuality();
            }else if(nbt.getCompound("DNA").getString("StorageId").equals("PlantDNA")){
                PlantDNA dna = PlantDNA.fromStack(stack);
                if (dna == null) {
                    return 0;
                }
                quality = dna.getDNAQuality();

            }
        }else{
            //this item is hopelessly broken. Escape before we mess anything up.
            return quality;
        }


        ItemStackNbtUtil.setTag(stack, nbt);//why- OH right

        return quality;
    }

    public static String getGeneticCode(RandomSource player, ItemStack stack) {
        CompoundTag nbt = ItemStackNbtUtil.getOrCreateTag(stack);

        String genetics = GeneticsHelper.randomGenetics(player);


        if (nbt.contains("DNA")) {
            if(nbt.getCompound("DNA").getString("StorageId").equals("DinoDNA")){
                DinoDNA dna = DinoDNA.readFromNBT(nbt);
                if(dna == null){
                    return genetics;
                }
                genetics = dna.getGenetics();
            }
        }else{
            //this item is hopelessly broken. Escape before we mess anything up.
            return genetics;
        }

        ItemStackNbtUtil.setTag(stack, nbt);

        return genetics;
    }

    public static boolean hasGeneticCode(ItemStack stack){
        return ItemStackNbtUtil.getOrCreateTag(stack).contains("Genetics");
    }

    @Override
    public void appendHoverText(
            ItemStack stack,
            Item.TooltipContext context,
            List<Component> lore,
            TooltipFlag flag
    ) {
        if (!appendTooltip) return;

        var customData = stack.get(DataComponents.CUSTOM_DATA);
        if (customData == null) {
            lore.add(Component.translatable("cage.empty")
                    .withStyle(ChatFormatting.DARK_RED));
            return;
        }

        CompoundTag tag = customData.copyTag();

        if (!tag.getBoolean("isCreative")) {
            if (tag.contains("DNA")) {
                StorageTypeRegistry
                        .getStorageType(tag.getCompound("DNA").getString("StorageId"))
                        .load(tag)
                        .addInformation(stack, lore);
            } else {
                lore.add(Component.translatable("cage.empty")
                        .withStyle(ChatFormatting.DARK_RED));
            }
            return;
        }

        boolean fromCreativeMenu = tag.getBoolean("isCreative");
        int quality = getDNAQuality(fromCreativeMenu, stack);

        RandomSource rand = context.level() != null
                ? context.level().getRandom()
                : RandomSource.create();

        ChatFormatting colour =
                quality > 75 ? ChatFormatting.GREEN :
                        quality > 50 ? ChatFormatting.YELLOW :
                                quality > 25 ? ChatFormatting.GOLD :
                                        ChatFormatting.RED;

        lore.add(
                Component.translatable("lore.dna_quality", quality, "%")
                        .withStyle(colour)
        );

        if (hasGeneticCode(stack)) {
            lore.add(
                    Component.translatable(
                            "lore.genetic_code",
                            getGeneticCode(rand, stack)
                    ).withStyle(ChatFormatting.BLUE)
            );
        }
    }

}
