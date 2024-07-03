package mod.reborn.server.item;

import mod.reborn.server.entity.item.AquaticCageEntity;
import mod.reborn.server.tab.TabHandler;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;

public class Aquatic_Cage extends Item {

    public Aquatic_Cage() {
        this.setMaxStackSize(1);
        this.setCreativeTab(TabHandler.ITEMS);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand hand) {
        ItemStack itemStackIn = playerIn.getHeldItem(hand);

        if (!worldIn.isRemote) {
            // Check if the item stack contains a captured entity
            if (itemStackIn.hasTagCompound() && itemStackIn.getTagCompound().hasKey("EntityTag")) {
                // Release the captured entity
                NBTTagCompound entityTag = itemStackIn.getTagCompound().getCompoundTag("EntityTag");
                Entity releasedEntity = EntityList.createEntityFromNBT(entityTag, worldIn);
                if (releasedEntity != null) {
                    BlockPos playerPos = playerIn.getPosition();
                    releasedEntity.setLocationAndAngles(playerPos.getX(), playerPos.getY(), playerPos.getZ(), releasedEntity.rotationYaw, releasedEntity.rotationPitch);
                    worldIn.spawnEntity(releasedEntity);

                    // Clear the ItemStack NBT data
                    itemStackIn.setTagCompound(null);
                    return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
                }
            } else {
                // Perform ray trace to find the targeted entity
                Vec3d eyePos = playerIn.getPositionEyes(1.0F);
                Vec3d lookVec = playerIn.getLook(1.0F);
                Vec3d reachVec = eyePos.add(lookVec.scale(5.0D)); // Adjusted to properly add vectors
                RayTraceResult result = rayTraceEntities(worldIn, playerIn, eyePos, reachVec);

                if (result != null && result.typeOfHit == RayTraceResult.Type.ENTITY) {
                    Entity entity = result.entityHit;
                    if (entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer)) {
                        // Capture the entity
                        AquaticCageEntity.captureEntity((EntityLivingBase) entity, itemStackIn);
                        return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
                    }
                }
            }
        }

        return new ActionResult<>(EnumActionResult.PASS, itemStackIn);
    }

    private RayTraceResult rayTraceEntities(World world, EntityPlayer player, Vec3d start, Vec3d end) {
        List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, player.getEntityBoundingBox().grow(5.0D));
        for (Entity entity : entities) {
            if (entity.getEntityBoundingBox().grow(0.3F).calculateIntercept(start, end) != null) {
                return new RayTraceResult(entity);
            }
        }
        return null;
    }
    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
        if (stack.hasTagCompound()) {
            if (stack.getTagCompound().hasKey("name")) {
                tooltip.add(TextFormatting.AQUA + I18n.format("tooltip.cage.stored") + stack.getTagCompound().getString("name"));
            } else {
                tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cage.stored") + stack.getTagCompound().getString("id"));
            }
        } else {
            tooltip.add(TextFormatting.YELLOW + I18n.format("tooltip.cage.stored") + I18n.format("tooltip.cage.empty"));
        }
    }
}

