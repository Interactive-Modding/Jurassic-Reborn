package mod.reborn.server.block.tree;

import com.google.common.collect.Lists;
import mod.reborn.server.api.GrindableItem;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.tuple.Pair;
import mod.reborn.server.plant.PlantHandler;

import java.util.List;
import java.util.Locale;
import java.util.Random;

public class AncientLogBlock extends BlockLog implements GrindableItem {
    private boolean petrified;
    private TreeType type;

    public AncientLogBlock(TreeType treeType, boolean petrified) {
        this.setDefaultState(this.getBlockState().getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
        this.setHardness(2.0F);
        this.setResistance(0.5F);
        this.setSoundType(SoundType.WOOD);
        this.setCreativeTab(TabHandler.PLANTS);
        this.petrified = petrified;
        this.type = treeType;

        String name = treeType.name().toLowerCase(Locale.ENGLISH) + "_log";

        if (petrified) {
            name += "_petrified";
            this.setHarvestLevel("pickaxe", 2);
            this.setHardness(4.0F);
            this.setResistance(4.0F);
        }

        this.setUnlocalizedName(name);
    }

    public TreeType getType() {
        return this.type;
    }

    public boolean isPetrified() {
        return this.petrified;
    }

    @Override
    public Material getMaterial(IBlockState state) {
        return this.petrified ? Material.ROCK : super.getMaterial(state);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(LOG_AXIS, EnumAxis.values()[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(LOG_AXIS).ordinal();
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LOG_AXIS);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, 0);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return 0;
    }

    @Override
    public boolean isGrindable(ItemStack stack) {
        return this.isPetrified();
    }

    @Override
    public ItemStack getGroundItem(ItemStack stack, Random random) {
        NBTTagCompound tag = stack.getTagCompound();

        int outputType = random.nextInt(6);

        if (outputType == 5) {
            ItemStack output = new ItemStack(ItemHandler.PLANT_SOFT_TISSUE, 1, PlantHandler.getPlantId(this.type.getPlant()));
            output.setTagCompound(tag);
            return output;
        } else if (outputType < 3) {
            return new ItemStack(Items.DYE, 1, 15);
        }

        return new ItemStack(Items.FLINT);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        float single = 100F/6F;
        NBTTagCompound tag = inputItem.getTagCompound();
        ItemStack output = new ItemStack(ItemHandler.PLANT_SOFT_TISSUE, 1, PlantHandler.getPlantId(this.type.getPlant()));
        output.setTagCompound(tag);
        return Lists.newArrayList(Pair.of(single, output), Pair.of(50f, new ItemStack(Items.DYE, 1, 15)), Pair.of(single*2f, new ItemStack(Items.FLINT)));
    }
}
