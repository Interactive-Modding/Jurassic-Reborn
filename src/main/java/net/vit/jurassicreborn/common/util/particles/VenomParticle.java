package net.vit.jurassicreborn.common.util.particles;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

public class VenomParticle extends TextureSheetParticle {

    protected VenomParticle(ClientLevel level, double x, double y, double z, double vx, double vy, double vz) {
        super(level, x, y, z, vx, vy, vz);
        this.lifetime = 8 + this.random.nextInt(8); // similar to old random lifespan
        this.gravity = 0.0F;
        this.quadSize = this.quadSize * 0.75F;     // like old .particleScale *= 0.75
        // random dark-ish green (old code used random up to ~0.3)
        float c = (float)(this.random.nextDouble() * 0.3D);
        this.setColor(c * 0.6F, 0.6F + c * 0.3F, c * 0.6F);
        this.alpha = 1.0F;
        this.xd = vx;
        this.yd = vy;
        this.zd = vz;
    }

    @Override
    public void tick() {
        super.tick();
        // gentle fade like old visual
        this.alpha = 1.0F - (this.age / (float)this.lifetime);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // Factory/Provider (sprite from atlas)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        public Provider(SpriteSet sprites) { this.sprites = sprites; }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level,
                                       double x, double y, double z,
                                       double vx, double vy, double vz) {
            VenomParticle p = new VenomParticle(level, x, y, z, vx, vy, vz);
            p.pickSprite(this.sprites); // picks sprite index (replaces setParticleTextureIndex)
            return p;
        }
    }
}
