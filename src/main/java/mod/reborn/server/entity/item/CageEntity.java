package mod.reborn.server.entity.item;

import mod.reborn.server.conf.RebornConfig;
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

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CageEntity {

	public Random random;
	private boolean notcaptured;
	private ItemStack currentcage = new ItemStack(ItemHandler.CAGE);

	// Load the blacklist from the configuration
	private static final Set<String> ID_BLACKLIST = new HashSet<>();

	static {
		for (String id : RebornConfig.ENTITY_BLACKLIST.blacklist) {
			ID_BLACKLIST.add(id);
		}
	}

	public CageEntity() {
	}

	public CageEntity(ItemStack Cage) {
		this.currentcage = Cage;
		this.notcaptured = !this.currentcage.hasTagCompound();
	}

	public static void captureEntity(EntityLivingBase entity, ItemStack itemStackIn) {
		if (entity != null && !(entity instanceof EntityPlayer || entity instanceof EntityDragon || entity instanceof EntityWither || entity instanceof SwimmingDinosaurEntity || entity instanceof EntityElderGuardian)) {
			String entityId = EntityList.getKey(entity.getClass()).toString();
			if (!itemStackIn.hasTagCompound() && !ID_BLACKLIST.contains(entityId)) {
				NBTTagCompound entityTag = new NBTTagCompound();
				entity.writeToNBT(entityTag);
				entityTag.setString("id", entityId);
				itemStackIn.setTagInfo("EntityTag", entityTag);
				itemStackIn.setTagInfo("name", new NBTTagString(entity.getName()));

				entity.setDead();
			}
		}
	}
}