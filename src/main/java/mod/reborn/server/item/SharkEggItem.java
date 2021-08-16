package mod.reborn.server.item;

import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.entity.animal.EntityShark;
import mod.reborn.server.tab.TabHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraft.block.BlockLiquid;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SharkEggItem extends DNAContainerItem {
    public SharkEggItem() {
        super();
        this.setMaxStackSize(1);
        this.setCreativeTab(CreativeTabs.MISC);
    }

    public String getItemStackDisplayName(ItemStack stack)
    {
        String s = ("" + I18n.translateToLocal("item.monsterPlacer.name")).trim();
        String s1 = EntityList.getTranslationName(new ResourceLocation(RebornMod.MODID, "entities.shark"));

        if (s1 != null)
        {
            s = s + " " + I18n.translateToLocal("entity." + s1 + ".name");
        }

        return s;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        ItemStack itemstack = player.getHeldItem(hand);

        if (world.isRemote)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            RayTraceResult raytraceresult = this.rayTrace(world, player, true);

            if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
            {
                BlockPos blockpos = raytraceresult.getBlockPos();

                if (!(world.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                }
                else if (world.isBlockModifiable(player, blockpos) && player.canPlayerEdit(blockpos, raytraceresult.sideHit, itemstack))
                {
                    EntityShark entity = spawnCreature(world, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

                    if (entity == null)
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
                    }
                    else
                    {
                        if (entity instanceof EntityLivingBase && itemstack.hasDisplayName())
                        {
                            entity.setCustomNameTag(itemstack.getDisplayName());
                        }

                        entity.isSpawnedByEgg = true;
                        if (!player.capabilities.isCreativeMode)
                        {
                            itemstack.shrink(1);
                        }
                        return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
                    }
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
            else
            {
                return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
            }
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if(worldIn.isRemote) return EnumActionResult.PASS;
        ItemStack itemstack = player.getHeldItem(hand);
        BlockPos blockpos = pos.offset(facing);
        double d0 = this.getYOffset(worldIn, blockpos);
        EntityShark entity = spawnCreature(worldIn, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + d0, (double)blockpos.getZ() + 0.5D);

        if (entity != null)
        {
            if (itemstack.hasDisplayName()) {
                entity.setCustomNameTag(itemstack.getDisplayName());
            }

            if (!player.capabilities.isCreativeMode)
            {
                itemstack.shrink(1);
            }
            worldIn.spawnEntity(entity);
        }

        return EnumActionResult.SUCCESS;
    }

    protected double getYOffset(World world, BlockPos pos)
    {
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(pos)).expand(0.0D, -1.0D, 0.0D);
        List<AxisAlignedBB> list = world.getCollisionBoxes((Entity)null, axisalignedbb);

        if (list.isEmpty())
        {
            return 0.0D;
        }
        else
        {
            double d0 = axisalignedbb.minY;

            for (AxisAlignedBB axisalignedbb1 : list)
            {
                d0 = Math.max(axisalignedbb1.maxY, d0);
            }

            return d0 - (double)pos.getY();
        }
    }

    @Nullable
    public static EntityShark spawnCreature(World worldIn, double x, double y, double z)
    {
        EntityShark shark = new EntityShark(worldIn);
        shark.setLocationAndAngles(x, y, z, MathHelper.wrapDegrees(worldIn.rand.nextFloat() * 360.0F), 0.0F);
        shark.rotationYawHead = shark.rotationYaw;
        shark.renderYawOffset = shark.rotationYaw;
        shark.isSpawnedByEgg = true;
        return shark;
    }
}
