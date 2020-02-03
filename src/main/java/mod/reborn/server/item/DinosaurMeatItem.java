package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.util.LangUtils;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class DinosaurMeatItem extends ItemFood {
    public DinosaurMeatItem() {
        super(3, 0.3F, true);
        this.setHasSubtypes(true);

        this.setCreativeTab(TabHandler.FOODS);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        return LangUtils.translate(this.getUnlocalizedName() + ".name").replace("{dino}", LangUtils.getDinoName(this.getDinosaur(stack)));
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote) {
            this.getDinosaur(stack).applyMeatEffect(player, false);
        }
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(stack.getItemDamage());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);
        if(this.isInCreativeTab(tab))
        for (Dinosaur dinosaur : dinosaurs) {
            if (dinosaur.shouldRegister()) {
                subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
            }
        }
    }

    //INFO: use DNAContainerItem.getDNAQuality()
//    public int getDNAQuality(EntityPlayer player, ItemStack stack) {
//        int quality = player.capabilities.isCreativeMode ? 100 : 0;
//
//        NBTTagCompound nbt = stack.getTagCompound();
//
//        if (nbt == null) {
//            nbt = new NBTTagCompound();
//        }
//
//        if (nbt.hasKey("DNAQuality")) {
//            quality = nbt.getInteger("DNAQuality");
//        } else {
//            nbt.setInteger("DNAQuality", quality);
//        }
//
//        stack.setTagCompound(nbt);
//
//        return quality;
//    }

    //INFO: use DNAContainerItem.getGeneticCode()
//    public String getGeneticCode(EntityPlayer player, ItemStack stack) {
//        NBTTagCompound nbt = stack.getTagCompound();
//
//        String genetics = GeneticsHelper.randomGenetics(player.world.rand);
//
//        if (nbt == null) {
//            nbt = new NBTTagCompound();
//        }
//
//        if (nbt.hasKey("Genetics")) {
//            genetics = nbt.getString("Genetics");
//        } else {
//            nbt.setString("Genetics", genetics);
//        }
//
//        stack.setTagCompound(nbt);
//
//        return genetics;
//    }

//    @Override
//    public void addInformation(ItemStack stack, World world, List<String> lore, ITooltipFlag tooltipFlag) {
//        int quality = this.getDNAQuality(player, stack);
//
//        TextFormatting formatting;
//
//        if (quality > 75) {
//            formatting = TextFormatting.GREEN;
//        } else if (quality > 50) {
//            formatting = TextFormatting.YELLOW;
//        } else if (quality > 25) {
//            formatting = TextFormatting.GOLD;
//        } else {
//            formatting = TextFormatting.RED;
//        }
//
//        lore.add(formatting + new LangHelper("lore.dna_quality.name").withProperty("quality", quality + "").build());
//        lore.add(TextFormatting.BLUE + new LangHelper("lore.genetic_code.name").withProperty("code", this.getGeneticCode(player, stack)).build());
//    }
}
