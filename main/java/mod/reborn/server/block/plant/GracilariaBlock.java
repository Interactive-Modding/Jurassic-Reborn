package mod.reborn.server.block.plant;

import mod.reborn.server.item.ItemHandler;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * Copyright 2016 Timeless Modding Team
 */
public class GracilariaBlock extends BlockBush {
    // This is needed because we user material of water so it doesn't have the block boundaries.
    public static final PropertyInteger LEVEL = PropertyInteger.create("level", 0, 15);
    /**
     * DESIGN:
     * <p>
     * This stuff spreads like mushrooms.  It grows on a variety of special surfaces.
     * It will spread to other surfaces within the radius until it reaches the specified
     * density.
     * <p>
     * It will spread quickly if within 5-11 range, slowly otherwise.
     */

    private static final int DENSITY_PER_AREA = 5;
    private static final int GOOD_LIGHT_SPREAD_CHANCE = 10;
    private static final int BAD_LIGHT_SPREAD_CHANCE = 1;
    private static final int SPREAD_RADIUS = 4;
    private static final AxisAlignedBB BOUNDS = new AxisAlignedBB(0.3F, 0.0F, 0.3F, 0.8F, 0.4F, 0.8F);

    public GracilariaBlock() {
        // Setting our material state to "water" is the trick to not having "walls" and air.
        // However, when we are water we alos need to have the LEVEL property.
        super(Material.WATER);
        this.setDefaultState(this.blockState.getBaseState().withProperty(LEVEL, 0));

        // Not tab because we are accessed in play via the item.
        this.setCreativeTab(null);
        this.setTickRandomly(true);
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return BOUNDS;
    }

    //  ____  _            _
    // | __ )| | ___   ___| | __
    // |  _ \| |/ _ \ / __| |/ /
    // | |_) | | (_) | (__|   <
    // |____/|_|\___/ \___|_|\_\

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return ItemHandler.GRACILARIA;
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        // This is necessary because we are "water"
        return state.getValue(LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        // This is necessary because we are "water"
        return new BlockStateContainer(this, LEVEL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        // This is so that it isn't always placed exactly at block center.
        return EnumOffsetType.XZ;
    }

    //  ____  _            _    ____            _
    // | __ )| | ___   ___| | _| __ ) _   _ ___| |__
    // |  _ \| |/ _ \ / __| |/ /  _ \| | | / __| '_ \
    // | |_) | | (_) | (__|   <| |_) | |_| \__ \ | | |
    // |____/|_|\___/ \___|_|\_\____/ \__,_|___/_| |_|

    private boolean canPlaceBlockOn(Block ground) {
        return ground == Blocks.SAND || ground == Blocks.CLAY || ground == Blocks.GRAVEL || ground == Blocks.DIRT;
    }

    @Override
    public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
        // Place on sand/clay, water here, water up
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block here = worldIn.getBlockState(pos).getBlock();
        Block up = worldIn.getBlockState(pos.up()).getBlock();

        return this.canPlaceBlockOn(down) && here == Blocks.WATER && up == Blocks.WATER;
    }

    @Override
    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state) {
        // Stay on sand/clay with water up
        Block down = worldIn.getBlockState(pos.down()).getBlock();
        Block up = worldIn.getBlockState(pos.up()).getBlock();

        return this.canPlaceBlockOn(down) && up == Blocks.WATER;
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (GameRuleHandler.PLANT_SPREADING.getBoolean(world)) {
            // Make sure we have enough light.
            int spreadChance = BAD_LIGHT_SPREAD_CHANCE;
            int light = world.getLight(pos);
            if (light >= 5 && light <= 11) {
                spreadChance = GOOD_LIGHT_SPREAD_CHANCE;
            }

            if (rand.nextInt(100) <= spreadChance) {
                // Density check
                int i = DENSITY_PER_AREA;

                // We only allow so many around us before we move one.
                for (BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-SPREAD_RADIUS, -3, -SPREAD_RADIUS), pos.add(SPREAD_RADIUS, 3, SPREAD_RADIUS))) {
                    // Count how many
                    if (world.getBlockState(blockpos).getBlock() == this) {
                        --i;
                        if (i <= 0) {
                            return;
                        }
                    }
                }

                // Choose a location then find the surface.
                BlockPos nextPos = null;
                int placementAttempts = 3;

                while (nextPos == null && placementAttempts > 0) {
                    // Chose a random location
                    int doubleRadius = SPREAD_RADIUS * 2;
                    BlockPos tmp = pos.add(rand.nextInt(doubleRadius) - SPREAD_RADIUS, -SPREAD_RADIUS,
                            rand.nextInt(doubleRadius) - SPREAD_RADIUS);
                    nextPos = this.findGround(world, tmp);
                    --placementAttempts;
                }

                if (nextPos != null) {
                    world.setBlockState(nextPos, this.getDefaultState());
                }
            }
        }
    }

    //             _            _
    //  _ __  _ __(_)_   ____ _| |_ ___
    // | '_ \| '__| \ \ / / _` | __/ _ \
    // | |_) | |  | |\ V / (_| | ||  __/
    // | .__/|_|  |_| \_/ \__,_|\__\___|
    // |_|

    private BlockPos findGround(World world, BlockPos start) {
        BlockPos pos = start;

        // Search a column
        Block down = world.getBlockState(pos.down()).getBlock();
        Block here = world.getBlockState(pos).getBlock();
        Block up = world.getBlockState(pos.up()).getBlock();

        for (int i = 0; i < 8; ++i) {
            if (this.canPlaceBlockOn(down) && here == Blocks.WATER && up == Blocks.WATER) {
                return pos;
            }

            down = here;
            here = up;
            pos = pos.up();
            up = world.getBlockState(pos.up()).getBlock();
        }

        return null;
    }
}
