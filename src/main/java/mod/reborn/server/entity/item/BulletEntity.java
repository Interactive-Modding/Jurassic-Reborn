package mod.reborn.server.entity.item;

import io.netty.buffer.ByteBuf;
import mod.reborn.RebornMod;
import mod.reborn.server.damage.DamageSources;
import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.item.Bullet;
import mod.reborn.server.item.Dart;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BulletEntity extends EntityThrowable implements IEntityAdditionalSpawnData {

    private ItemStack stack;
    private int damage = 5;

    public BulletEntity(World worldIn) {
		super(worldIn);
		this.setSize(0.2F, 0.2F);
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

    public BulletEntity(World worldIn, EntityLivingBase throwerIn, ItemStack stack) {
		super(worldIn, throwerIn);
		this.stack = stack.copy();
	}

    @Override
    public void onUpdate() {
	if(world.isRemote) {
	    spawnParticles();
	}
	super.onUpdate();
    }

	@Override
	public void fall(float distance, float damageMultiplier) {
		super.fall(distance, damageMultiplier);
	}

	@SideOnly(Side.CLIENT)
	private void spawnParticles() {
		Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(EnumParticleTypes.CRIT.getParticleID(), this.posX, this.posY, this.posZ, 0, 0,0);
	}


	@Override
	public boolean isPushedByWater() {
		return false;
	}

	@Override
    protected void onImpact(RayTraceResult result) {
		if (!this.world.isRemote) {
			if (stack != null) {
				Item item = stack.getItem();
				if (result.entityHit instanceof EntityLiving || result.entityHit instanceof EntityPlayer && result.entityHit != thrower) {
					if (item instanceof Bullet) {
						result.entityHit.attackEntityFrom(DamageSources.BULLET, damage);
					} else {
						RebornMod.getLogger().error("Expected Bullet Item, got {} ", item.getRegistryName());
					}
				}
				this.world.setEntityState(this, (byte) 3);
				this.setDead();
			}
		}
	}

	public void shoot(Entity entityThrower, float rotationPitchIn, float rotationYawIn, float pitchOffset, float velocity, float inaccuracy)
	{
		float f = -MathHelper.sin(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		float f1 = -MathHelper.sin((rotationPitchIn + pitchOffset) * 0.017453292F);
		float f2 = MathHelper.cos(rotationYawIn * 0.017453292F) * MathHelper.cos(rotationPitchIn * 0.017453292F);
		this.shoot(f, f1, f2, velocity, inaccuracy);
		this.motionX += entityThrower.motionX;
		this.motionZ += entityThrower.motionZ;


		if (!entityThrower.onGround)
		{
			this.motionY += entityThrower.motionY;
		}
	}


	@Override
	public void onEntityUpdate() {
		if(this.ticksExisted > 50) {
			this.setDead();
		}
	}

	@Override
    public void writeSpawnData(ByteBuf buffer) {
	if(stack != null)
		ByteBufUtils.writeItemStack(buffer, stack);
    }

    @Override
    public void readSpawnData(ByteBuf additionalData) {
	if(stack != null)
		stack = ByteBufUtils.readItemStack(additionalData);
    }

}
