package net.vit.jurassicreborn.common.entities.vehicle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class WashingParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected WashingParticle(ClientLevel level,
                              double x, double y, double z,
                              double xSpeed, double ySpeed, double zSpeed,
                              SpriteSet sprites, int rotation) {

        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;

        /* Random lateral sprinkle identical to the old implementation */
        int xSprinkle = (zSpeed != 0 ? 1 : 0) * ((rotation == 0 || rotation == 3) ? 1 : -1);
        int zSprinkle = (xSpeed != 0 ? 1 : 0) * ((rotation == 0 || rotation == 3) ? 1 : -1);

        this.x += (level.random.nextDouble() * 0.2) * xSprinkle;
        this.z += (level.random.nextDouble() * 0.2) * zSprinkle;

        this.xd = xSpeed + (level.random.nextDouble() * 0.06) * xSprinkle;
        this.yd = ySpeed;
        this.zd = zSpeed + (level.random.nextDouble() * 0.06) * zSprinkle;

        this.quadSize = 0.01F;
        this.gravity = 0.04F;
        this.lifetime = (int) (1.0D / (level.random.nextDouble() * 0.8D + 0.2D)); // ~2–5 ticks

        if (sprites != null) {
            this.pickSprite(sprites); // start with a random droplet sprite
        }
    }

    /* --------------------------------------------------------------------- */
    /*  Per-tick logic                                                        */
    /* --------------------------------------------------------------------- */

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        this.yd -= 0.04D * this.gravity;
        this.move(this.xd, this.yd, this.zd);

        this.xd *= 0.9900000190734863D;
        this.yd *= 0.9900000190734863D;
        this.zd *= 0.9900000190734863D;

        if (this.sprites != null) {
            this.pickSprite(this.sprites); // animate between the four droplet frames
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        // Use the lit sheet so the droplet reflects block-light like vanilla water particles
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }

    /* --------------------------------------------------------------------- */
    /* --------------------------------------------------------------------- */

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type,
                                       ClientLevel level,
                                       double x, double y, double z,
                                       double xSpeed, double ySpeed, double zSpeed) {
            // Old code always passed rotation 0; keep that behaviour here.
            return new WashingParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprites, 0);
        }
    }
}
