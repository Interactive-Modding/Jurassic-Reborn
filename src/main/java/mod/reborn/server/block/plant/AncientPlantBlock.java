package mod.reborn.server.block.plant;

import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.GameRuleHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

public class AncientPlantBlock extends BlockBush {
    private static final int DENSITY_PER_AREA = 4;
    private static final int SPREAD_RADIUS = 6;

    public AncientPlantBlock(Material material) {
        super(material);
        this.setCreativeTab(TabHandler.PLANTS);
        this.setSoundType(SoundType.PLANT);
        this.setTickRandomly(true);
    }

    public AncientPlantBlock() {
        this(Material.PLANTS);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (GameRuleHandler.PLANT_SPREADING.getBoolean(world)) {
            int light = world.getLight(pos);

            if (light >= 5) {
                if (rand.nextInt((15 - light) / 2 + 10) == 0) {
                    int allowedInArea = DENSITY_PER_AREA;

                    BlockPos nextPos = null;
                    int placementAttempts = 3;

                    while (nextPos == null && placementAttempts > 0) {
                        int doubleRadius = SPREAD_RADIUS * 2;
                        BlockPos tmp = pos.add(rand.nextInt(doubleRadius) - SPREAD_RADIUS, -SPREAD_RADIUS, rand.nextInt(doubleRadius) - SPREAD_RADIUS);
                        nextPos = this.findGround(world, tmp);
                        placementAttempts--;
                    }

                    if (nextPos != null) {
                        for (BlockPos neighbourPos : BlockPos.getAllInBoxMutable(nextPos.add(-2, -3, -2), nextPos.add(2, 3, 2))) {
                            if (world.getBlockState(neighbourPos).getBlock() instanceof BlockBush) {
                                allowedInArea--;

                                if (allowedInArea <= 0) {
                                    return;
                                }
                            }
                        }

                        if (this.isNearWater(world, pos)) {
                            this.spread(world, nextPos);
                        }
                    }
                }
            }
        }
    }

    protected boolean isNearWater(World world, BlockPos nextPos) {
        for (BlockPos neighbourPos : BlockPos.getAllInBoxMutable(nextPos.add(-8, -3, -8), nextPos.add(8, 3, 8))) {
            Block neighbourState = world.getBlockState(neighbourPos).getBlock();

            if (neighbourState == Blocks.WATER || neighbourState == Blocks.FLOWING_WATER) {
                if (neighbourPos.getDistance(nextPos.getX(), nextPos.getY(), nextPos.getZ()) < 9) {
                    return true;
                }
            }
        }

        return false;
    }

    protected void spread(World world, BlockPos position) {
        world.setBlockState(position, this.getDefaultState());
    }

    protected BlockPos findGround(World world, BlockPos start) {
        BlockPos pos = start;

        IBlockState down = world.getBlockState(pos.down());
        IBlockState here = world.getBlockState(pos);
        IBlockState up = world.getBlockState(pos.up());

        for (int i = 0; i < 8; ++i) {
            if (this.canPlace(down, here, up)) {
                return pos.down();
            }

            down = here;
            here = up;
            pos = pos.up();
            up = world.getBlockState(pos);
        }

        return null;
    }

    protected boolean canPlace(IBlockState down, IBlockState here, IBlockState up) {
        return this.canSustainBush(down) && here.getBlock() == Blocks.AIR;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public Block.EnumOffsetType getOffsetType() {
        return EnumOffsetType.XZ;
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack heldItem = player.getHeldItem(hand);
        if (!world.isRemote && heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15) { // Check for bone meal (white dye)
            dropItem(world, pos); // Drop the item

            if (!player.capabilities.isCreativeMode) {
                heldItem.shrink(1); // Reduce the item count
            }
            world.playEvent(2005, pos, 0); // Trigger bone meal particle effect on the server

            return true;
        }

        if (world.isRemote && heldItem.getItem() == Items.DYE && heldItem.getMetadata() == 15) { // Client-side particle spawning
            spawnGrowthParticles(world, pos);
        }

        return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
    }


    @SideOnly(Side.CLIENT)
    private void spawnGrowthParticles(World world, BlockPos pos) {
        for (int i = 0; i < 10; i++) {
            double x = pos.getX() + 0.5 + (world.rand.nextDouble() - 0.5) * 0.6;
            double y = pos.getY() + 0.5;
            double z = pos.getZ() + 0.5 + (world.rand.nextDouble() - 0.5) * 0.6;

            world.spawnParticle(EnumParticleTypes.VILLAGER_HAPPY, x, y, z, 0.0D, 0.0D, 0.0D);
        }
    }



    private void dropItem(World world, BlockPos pos) {
        ItemStack plantItem = new ItemStack(this); // This will create an item based on the block itself, modify if you want a custom item

        EntityItem entityItem = new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), plantItem);
        world.spawnEntity(entityItem); // Spawn the item entity in the world
    }
}