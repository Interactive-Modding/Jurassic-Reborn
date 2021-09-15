package mod.reborn.server.items;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class DNAContainerItem extends Item {
    public DNAContainerItem(Properties properties) {
        super(properties);
    }

    public int getContainerId(ItemStack stack) {
        return 0;
    }

    public static int getDNAQuality(PlayerEntity player, ItemStack stack) {
        int quality = player.isCreative() ? 100 : 0;

        CompoundNBT nbt = stack.getTag();

        if (nbt == null) {
            nbt = new CompoundNBT();
        }

        if (nbt.contains("DNAQuality")) {
            quality = nbt.getInt("DNAQuality");
        } else {
            nbt.putInt("DNAQuality", quality);
        }
        stack.setTag(nbt);

        return quality;
    }

    /*public static String getGeneticCode(PlayerEntity player, ItemStack stack) {
        CompoundNBT nbt = stack.getTag();

        String genetics = GeneticsHelper.randomGenetics(player.world.rand);

        if (nbt == null) {
            nbt = new CompoundNBT();
        }

        if (nbt.contains("Genetics")) {
            genetics = nbt.getString("Genetics");
        } else {
            nbt.putString("Genetics", genetics);
        }

        stack.setTag(nbt);

        return genetics;
    }*/
}
