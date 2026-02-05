package net.vit.jurassicreborn.client.render.entity.vehicle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.Camera;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

public class HelicopterGroundParticle extends TextureSheetParticle {
    @Nullable
    private final SpriteSet spriteSet;
    private final float groundParticleScale;

    public HelicopterGroundParticle(ClientLevel world, double x, double y, double z,
                                    double velX, double velY, double velZ,
                                    float scale,@Nullable SpriteSet spriteSet) {
        super(world, x, y, z, velX, velY, velZ);
        this.spriteSet = spriteSet;

        // Motion
        this.xd *= 0.1D;
        this.yd *= 0.1D;
        this.zd *= 0.1D;
        this.xd += velX;
        this.yd += velY;
        this.zd += velZ;

        // Color
        this.rCol = 1.0f;
        this.gCol = 1.0f;
        this.bCol = 1.0f;

        // Scale
        this.quadSize *= 0.75F;
        this.quadSize *= scale;
        this.groundParticleScale = this.quadSize;

        // Lifetime
        this.lifetime = (int) (8.0D / (this.random.nextDouble() * 0.8D + 0.2D));
        this.lifetime = (int) ((float) this.lifetime * scale);

        if (spriteSet != null) {
            this.setSpriteFromAge(spriteSet);
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.age++ >= this.lifetime) {
            this.remove();
            return;
        }

        if (this.spriteSet != null) {
            this.setSpriteFromAge(this.spriteSet);
        }
        this.yd += 0.004D;
        this.move(this.xd, this.yd, this.zd);

        if (this.y == this.yo) {
            this.xd *= 1.1D;
            this.zd *= 1.1D;
        }

        this.xd *= 0.96D;
        this.yd *= 0.96D;
        this.zd *= 0.96D;

        if (this.onGround) {
            this.xd *= 0.7D;
            this.zd *= 0.7D;
        }
    }

    @Override
    public void render(VertexConsumer consumer, Camera camera, float partialTicks) {
        if (this.sprite != null) {
            float f = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
            float scale = Math.min(Math.max(f, 0.0F), 1.0F);
            this.quadSize = this.groundParticleScale * scale;
            super.render(consumer, camera, partialTicks);
        }
    }

    @Override
    public net.minecraft.client.particle.ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    // Factory for registration
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet spriteSet) {
            this.spriteSet = spriteSet;
        }

        @Nullable
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel world, double x, double y, double z, double velX, double velY, double velZ) {
            return new HelicopterGroundParticle(world, x, y, z, velX, velY, velZ, 1.0F, spriteSet);
        }
    }
}
