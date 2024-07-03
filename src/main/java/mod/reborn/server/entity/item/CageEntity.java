package mod.reborn.server.entity.item;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import java.util.Random;

public class CageEntity {

	public Random random;
	private boolean notcaptured;
	private ItemStack currentcage = new ItemStack(ItemHandler.CAGE);

	public CageEntity() {
	}

	public CageEntity(ItemStack Cage) {
		this.currentcage = Cage;
		this.notcaptured = !this.currentcage.hasTagCompound();
	}

	public static void captureEntity(EntityLivingBase entity, ItemStack itemStackIn) {
		if (entity != null && !(entity instanceof EntityPlayer || entity instanceof EntityDragon|| entity instanceof EntityWither|| entity instanceof SwimmingDinosaurEntity || entity instanceof EntityElderGuardian)) {
			if (!itemStackIn.hasTagCompound()) {
				NBTTagCompound entityTag = new NBTTagCompound();
				entity.writeToNBT(entityTag);
				entityTag.setString("id", EntityList.getKey(entity.getClass()).toString());
				itemStackIn.setTagInfo("EntityTag", entityTag);
				itemStackIn.setTagInfo("name", new NBTTagString(entity.getName()));

				entity.setDead();
			}
		}
	}
}