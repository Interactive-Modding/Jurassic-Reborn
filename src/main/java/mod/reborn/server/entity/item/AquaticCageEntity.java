package mod.reborn.server.entity.item;

import mod.reborn.server.entity.SwimmingDinosaurEntity;
import mod.reborn.server.entity.animal.EntityShark;
import mod.reborn.server.item.ItemHandler;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import java.util.Random;

public class AquaticCageEntity {

	public Random random;
	private boolean notcaptured;
	private ItemStack currentaquaticcage = new ItemStack(ItemHandler.AQUATIC_CAGE);

	public AquaticCageEntity() {
	}

	public AquaticCageEntity(ItemStack AqauticCage) {
		this.currentaquaticcage = AqauticCage;
		this.notcaptured = !this.currentaquaticcage.hasTagCompound();
	}

	public static void captureEntity(EntityLivingBase entity, ItemStack itemStackIn) {
		if (entity != null && (entity instanceof SwimmingDinosaurEntity|| entity instanceof EntitySquid ||entity instanceof EntityShark)) {
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