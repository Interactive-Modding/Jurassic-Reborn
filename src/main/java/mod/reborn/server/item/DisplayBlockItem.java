package mod.reborn.server.item;

import mod.reborn.client.proxy.ClientProxy;
import mod.reborn.client.render.RenderingHandler;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mod.reborn.server.block.BlockHandler;
import mod.reborn.server.block.entity.DisplayBlockEntity;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.util.LangUtils;

import java.util.*;


public class DisplayBlockItem extends Item {
    public DisplayBlockItem() {
        super();
        this.setCreativeTab(TabHandler.DECORATIONS);
        this.setHasSubtypes(true);
    }

    @SideOnly(Side.CLIENT)
    public void initModels(Collection<Dinosaur> dinos, RenderingHandler renderer) {
        for (Dinosaur dino : dinos) {
            int dex = EntityHandler.getDinosaurId(dino);
            String dinoName = dino.getName().toLowerCase(Locale.ENGLISH).replaceAll(" ", "_");
            renderer.registerItemRenderer(this, getMetadata(dex, 0, false), "action_figure/action_figure_" + dinoName);
            renderer.registerItemRenderer(this, getMetadata(dex, 1, false), "action_figure/action_figure_" + dinoName);
            renderer.registerItemRenderer(this, getMetadata(dex, 2, false), "action_figure/action_figure_" + dinoName);
            renderer.registerItemRenderer(this, getMetadata(dex, 1, true), "skeleton/fossil/skeleton_fossil_" + dinoName);
            renderer.registerItemRenderer(this, getMetadata(dex, 2, true), "skeleton/fresh/skeleton_fresh_" + dinoName);
        }
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);
        ItemStack stack = player.getHeldItem(hand);
        if (!player.world.isRemote && player.canPlayerEdit(pos, side, stack)) {
            Block block = BlockHandler.DISPLAY_BLOCK;

            if (block.canPlaceBlockAt(world, pos)) {
                IBlockState state = block.getDefaultState();
                world.setBlockState(pos, block.getStateForPlacement(world, pos, side, hitX, hitY, hitZ, 0, player));
                block.onBlockPlacedBy(world, pos, state, player, stack);

                int mode = this.getVariant(stack);

                DisplayBlockEntity tile = (DisplayBlockEntity) world.getTileEntity(pos);

                if (tile != null) {
                    tile.setDinosaur(this.getDinosaurID(stack), mode > 0 ? mode == 1 : world.rand.nextBoolean(), this.isSkeleton(stack));
                    tile.setRot(180 - (int) player.getRotationYawHead());
                    world.notifyBlockUpdate(pos, state, state, 0);
                    tile.markDirty();
                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                }
            }
        }

        return EnumActionResult.SUCCESS;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String dinoName = LangUtils.getDinoName(this.getDinosaur(stack));
        if (!this.isSkeleton(stack)) {
            return LangUtils.translate("item.action_figure.name").replace("{dino}", dinoName);
        }
        return LangUtils.translate("item.skeleton." + (this.getVariant(stack) == 1 ? "fossil" : "fresh") + ".name").replace("{dino}", dinoName);
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(getDinosaurID(stack));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());

        Collections.sort(dinosaurs);
        if(this.isInCreativeTab(tab))
            for (Dinosaur dinosaur : dinosaurs) {
                if (dinosaur.shouldRegister()) {
                    subtypes.add(new ItemStack(this, 1, getMetadata(EntityHandler.getDinosaurId(dinosaur), 0, false)));
                    for (int variant = 1; variant < 3; variant++) {
                        subtypes.add(new ItemStack(this, 1, getMetadata(EntityHandler.getDinosaurId(dinosaur), variant, true)));
                    }
                }
            }
    }

    public static int getMetadata(int dinosaur, int variant, boolean isSkeleton) {
        return dinosaur << 4 | variant << 1 | (isSkeleton ? 1 : 0);
    }

    public int getDinosaurID(ItemStack stack) {
        return stack.getMetadata() >> 4 & 0xFFFF;
    }

    public int getVariant(ItemStack stack) {
        return stack.getMetadata() >> 1 & 7;
    }

    public boolean isSkeleton(ItemStack stack) {
        return (stack.getMetadata() & 1) == 1;
    }

    public int changeMode(ItemStack stack) {
        int dinosaur = this.getDinosaurID(stack);
        boolean skeleton = this.isSkeleton(stack);

        int mode = this.getVariant(stack) + 1;
        mode %= 3;

        stack.setItemDamage(getMetadata(dinosaur, mode, skeleton));

        return mode;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, World world, List<String> lore, ITooltipFlag tooltipFlag) {
        if (!this.isSkeleton(stack)) {
            lore.add(TextFormatting.BLUE + I18n.format("lore.change_gender.name"));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        if (!this.isSkeleton(stack)) {
            int mode = this.changeMode(stack);
            if (world.isRemote) {
                player.sendMessage(new TextComponentString(LangUtils.translate(LangUtils.GENDER_CHANGE.get("actionfigure")).replace("{mode}", LangUtils.getGenderMode(mode))));
            }
            return new ActionResult<>(EnumActionResult.SUCCESS, stack);
        }
        return new ActionResult<>(EnumActionResult.PASS, stack);
    }
}
