package mod.reborn.server.item;

import mod.reborn.server.tab.TabHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import mod.reborn.RebornMod;
import mod.reborn.server.dinosaur.Dinosaur;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.entity.EntityHandler;
import mod.reborn.server.util.LangUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class HatchedEggItem extends DNAContainerItem {
    public HatchedEggItem() {
        super();
        this.setHasSubtypes(true);
        this.setMaxStackSize(1);
        this.setCreativeTab(TabHandler.CREATIVE);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Dinosaur dinosaur = this.getDinosaur(stack);

        return LangUtils.translate(dinosaur.givesDirectBirth() ? "item.gestated.name" :"item.hatched_egg.name").replace("{dino}", LangUtils.getDinoName(dinosaur));
    }

    public Dinosaur getDinosaur(ItemStack stack) {
        return EntityHandler.getDinosaurById(stack.getMetadata());
    }

    public boolean getGender(EntityPlayer player, ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();

        boolean gender = player.world.rand.nextBoolean();

        if (nbt == null) {
            nbt = new NBTTagCompound();
        }

        if (nbt.hasKey("Gender")) {
            gender = nbt.getBoolean("Gender");
        } else {
            nbt.setBoolean("Gender", gender);
        }

        stack.setTagCompound(nbt);

        return gender;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subtypes) {
        List<Dinosaur> dinosaurs = new LinkedList<>(EntityHandler.getDinosaurs().values());
        Collections.sort(dinosaurs);
        if(this.isInCreativeTab(tab)) {
            for (Dinosaur dinosaur : dinosaurs) {
                if (dinosaur.shouldRegister()) {
                    subtypes.add(new ItemStack(this, 1, EntityHandler.getDinosaurId(dinosaur)));
                }
            }
        }
    }

    @Override
    public int getContainerId(ItemStack stack) {
        return EntityHandler.getDinosaurId(this.getDinosaur(stack));
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        pos = pos.offset(side);
        ItemStack stack = player.getHeldItem(hand);

        if (side == EnumFacing.EAST || side == EnumFacing.WEST) {
            hitX = 1.0F - hitX;
        } else if (side == EnumFacing.NORTH || side == EnumFacing.SOUTH) {
            hitZ = 1.0F - hitZ;
        }

        if (player.canPlayerEdit(pos, side, stack)) {
            if (!world.isRemote) {
                Dinosaur dinosaur = this.getDinosaur(stack);

                try {
                    DinosaurEntity entity = dinosaur.getDinosaurClass().getDeclaredConstructor(World.class).newInstance(world);

                    entity.setPosition(pos.getX() + hitX, pos.getY(), pos.getZ() + hitZ);
                    entity.setAge(0);
                    entity.setGenetics(this.getGeneticCode(player, stack));
                    entity.setDNAQuality(this.getDNAQuality(player, stack));
                    entity.setMale(this.getGender(player, stack));
                    if (!player.isSneaking()) {
                        entity.setOwner(player);
                    }

                    world.spawnEntity(entity);

                    if (!player.capabilities.isCreativeMode) {
                        stack.shrink(1);
                    }
                } catch (ReflectiveOperationException e) {
                    RebornMod.getLogger().warn("Failed to spawn dinosaur from hatched egg", e);
                }
            }

            return EnumActionResult.SUCCESS;
        }

        return EnumActionResult.PASS;
    }
}
