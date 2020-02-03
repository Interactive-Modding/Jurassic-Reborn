package mod.reborn.server.entity.item;

import mod.reborn.server.entity.DinosaurEntity;
import mod.reborn.server.item.Dart;
import mod.reborn.RebornMod;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TranquilizerDartEntity extends EntityThrowable implements IEntityAdditionalSpawnData {

    private ItemStack stack;
    
    public TranquilizerDartEntity(World worldIn) {
	super(worldIn);
    }
    
    public TranquilizerDartEntity(World worldIn, EntityLivingBase throwerIn, ItemStack stack) {
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
    
    @SideOnly(Side.CLIENT)
    private void spawnParticles() {
	Minecraft.getMinecraft().effectRenderer.spawnEffectParticle(
    	    EnumParticleTypes.CLOUD.getParticleID(), 
    	    this.posX + this.motionX / 4.0D, 
    	    this.posY + this.motionY / 4.0D, 
    	    this.posZ + this.motionZ / 4.0D, 
    	    -this.motionX / 20.0D, 
    	    -this.motionY / 20.0D + 0.2D, 
    	    -this.motionZ / 20.0D);
    }

    @Override
    protected void onImpact(RayTraceResult result) {
    	if(stack != null) {
			Item item = stack.getItem();
			if (result.entityHit instanceof DinosaurEntity) {
				if (item instanceof Dart) {
					((Dart) item).getConsumer().accept((DinosaurEntity) result.entityHit, stack);
				} else {
					RebornMod.getLogger().error("Expected Dart Item, got {} ", item.getRegistryName());
				}
			}
			if (!this.world.isRemote) {
				this.world.setEntityState(this, (byte) 3);
				this.setDead();
			}
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
