package mod.reborn.client.particle;

import net.minecraft.client.particle.IParticleFactory;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WashingParticle extends Particle {

	public WashingParticle(World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int rotation) {
		super(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn);
		final int xSprinkle = ((zSpeedIn != 0) ? 1 : 0) * ((rotation == 0 || rotation == 3) ? 1 : -1);
		final int zSprinkle = ((xSpeedIn != 0) ? 1 : 0) * ((rotation == 0 || rotation == 3) ? 1 : -1);
		this.posX += (Math.random() * 0.2) * xSprinkle;
		this.posZ += (Math.random() * 0.2) * zSprinkle;
		this.motionX = xSpeedIn + (Math.random() * 0.06) * xSprinkle;
		this.motionY = ySpeedIn;
		this.motionZ = zSpeedIn + (Math.random() * 0.06) * zSprinkle;
		this.particleRed = 1.0F;
		this.particleGreen = 1.0F;
		this.particleBlue = 1.0F;
		this.setParticleTextureIndex(19 + this.rand.nextInt(4));
		this.setSize(0.01F, 0.01F);
		this.particleGravity = 0.04F;
		this.particleMaxAge = (int) (1.0D / (Math.random() * 0.8D + 0.2D));
		this.canCollide = false;
	}

	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.particleAge++ >= this.particleMaxAge) {
			this.setExpired();
		}

		this.motionY -= 0.04D * (double) this.particleGravity;
		this.move(this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9900000190734863D;
		this.motionY *= 0.9900000190734863D;
		this.motionZ *= 0.9900000190734863D;
	}

	@SideOnly(Side.CLIENT)
	public static class Factory implements IParticleFactory {
		public Particle createParticle(int particleID, World worldIn, double xCoordIn, double yCoordIn, double zCoordIn, double xSpeedIn, double ySpeedIn, double zSpeedIn, int... parameters) {
			return new WashingParticle(worldIn, xCoordIn, yCoordIn, zCoordIn, xSpeedIn, ySpeedIn, zSpeedIn, 0);
		}
	}

}
