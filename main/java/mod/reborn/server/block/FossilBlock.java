package mod.reborn.server.block;

import java.util.Map;

import mod.reborn.server.api.SubBlocksBlock;
import mod.reborn.server.item.block.FossilItemBlock;
import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FossilBlock extends Block implements SubBlocksBlock {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 15);

    private int start;

    public FossilBlock(int start) {
        super(Material.ROCK);
        this.setHardness(2.0F);
        this.setResistance(8.0F);
        this.setSoundType(SoundType.STONE);
        this.setCreativeTab(TabHandler.FOSSILS);

        this.start = start;

        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, 0));
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(VARIANT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    protected ItemStack getSilkTouchDrop(IBlockState state) {
        return new ItemStack(this, 1, this.getMetaFromState(state));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return this.getMetaFromState(state);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        Map<Integer, Dinosaur> dinosaurs = EntityHandler.getDinosaurs();

        for (int i = 0; i < 16; i++) {
            Dinosaur dinosaur = dinosaurs.get(i + this.start);

            if (dinosaur != null && dinosaur.shouldRegister() && !dinosaur.isHybrid) {
                list.add(new ItemStack(this, 1, i));
            }
        }
    }

    public Dinosaur getDinosaur(int metadata) {
        return EntityHandler.getDinosaurById(this.start + metadata);
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
        return false;
    }

    @Override
    public ItemBlock getItemBlock() {
        return new FossilItemBlock(this);
    }

    @Override
    public boolean canHarvestBlock(IBlockAccess world, BlockPos pos, EntityPlayer player) {
        return false;
    }
}
