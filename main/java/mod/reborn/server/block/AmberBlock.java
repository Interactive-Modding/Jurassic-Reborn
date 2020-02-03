package mod.reborn.server.block;

import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AmberBlock extends Block {
    public AmberBlock() {
        super(Material.ROCK);
        this.setHardness(3.0F);
        this.setResistance(5.0F);
        this.setCreativeTab(TabHandler.BLOCKS);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> ret = new ArrayList<>();

        Random rand = world instanceof World ? ((World) world).rand : RANDOM;

        int count = rand.nextInt(fortune + 1) - 1;

        if (count < 0) {
            count = 0;
        }

        for (int i = 0; i < count + 1; i++) {
            Item item = ItemHandler.AMBER;

            if (item != null) {
                ret.add(new ItemStack(item, 1, rand.nextBoolean() ? 1 : 0));
            }
        }

        return ret;
    }

    @Override
    public boolean canDropFromExplosion(Explosion explosion) {
        return false;
    }
}
