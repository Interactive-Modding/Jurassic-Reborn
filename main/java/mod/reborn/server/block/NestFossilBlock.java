package mod.reborn.server.block;

import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.google.common.collect.Lists;
import mod.reborn.server.api.CleanableItem;
import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.item.block.NestFossilItemBlock;
import mod.reborn.server.tab.TabHandler;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NestFossilBlock extends Block implements SubBlocksBlock, CleanableItem {
    public static final PropertyEnum<Variant> VARIANT = PropertyEnum.create("variant", Variant.class);

    public boolean encased;

    public NestFossilBlock(boolean encased) {
        super(Material.ROCK);
        this.encased = encased;
        this.setHardness(1.5F);
        this.setCreativeTab(TabHandler.FOSSILS);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, Variant.VARIANT_1));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, Variant.values()[meta & 3]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal() & 3;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(Item.getItemFromBlock(this), 1, this.damageDropped(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT).ordinal() & 3;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (Variant type : Variant.values()) {
            list.add(new ItemStack(this, 1, type.ordinal()));
        }
    }

    @Override
    public ItemBlock getItemBlock() {
        return new NestFossilItemBlock(this, this.encased);
    }

    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return 1;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return this.encased;
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return true;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return true;
    }

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
        if (this.encased) {
            super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
        }
    }

    @Override
    public boolean isCleanable(ItemStack stack) {
        return this.encased;
    }

    @Override
    public ItemStack getCleanedItem(ItemStack stack, Random random) {
        if (random.nextBoolean()) { //50
            return new ItemStack(ItemHandler.FOSSILIZED_EGG, random.nextInt(7) + 1, stack.getItemDamage());
        }

        if (random.nextBoolean()) { //25
            return new ItemStack(Items.FLINT);
        } else {
            return new ItemStack(Items.DYE, 1, 15);
        }
    }

    @Override
    public List<ItemStack> getJEIRecipeTypes() {
        return getItemSubtypes(this);
    }

    @Override
    public List<Pair<Float, ItemStack>> getChancedOutputs(ItemStack inputItem) {
        List<Pair<Float, ItemStack>> list = Lists.newArrayList();
        float single = 100F/4F;
        for(int i = 0; i < 7; i++) {
            list.add(Pair.of(50F/7F, new ItemStack(ItemHandler.FOSSILIZED_EGG, i + 1, inputItem.getItemDamage())));
        }
        list.add(Pair.of(single, new ItemStack(Items.FLINT)));
        list.add(Pair.of(single, new ItemStack(Items.DYE, 1, 15)));
        return list;
    }

    public enum Variant implements IStringSerializable {
        VARIANT_1, VARIANT_2, VARIANT_3;

        @Override
        public String getName() {
            return this.name().toLowerCase(Locale.ENGLISH);
        }
    }
}
