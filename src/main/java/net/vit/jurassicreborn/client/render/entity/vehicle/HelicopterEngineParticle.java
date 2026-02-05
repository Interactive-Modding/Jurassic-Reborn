package net.vit.jurassicreborn.client.render.entity.vehicle;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.Camera;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public class HelicopterEngineParticle extends TextureSheetParticle {
    private final SpriteSet spriteSet;
    private final float smokeParticleScale;

    public HelicopterEngineParticle(ClientLevel world, double x, double y, double z,
                                    double xSpeed, double ySpeed, double zSpeed,
                                    float scale, @Nullable SpriteSet spriteSet) {        super(world, x, y, z, xSpeed, ySpeed, zSpeed);
        this.spriteSet = spriteSet;

        // Motion
        this.xd *= 0.1D;
        this.yd *= 0.1D;
        this.zd *= 0.1D;
        this.xd += xSpeed;
        this.yd += ySpeed;
        this.zd += zSpeed;

        // Color/Alpha
        this.rCol = 0.66F;
        this.gCol = 0.95F;
        this.bCol = 1F;
        this.alpha = 0.1F;

        // Scale
        this.quadSize *= 0.75F;
        this.quadSize *= scale;
        this.smokeParticleScale = this.quadSize;

        // Lifespan
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
        } else if (this.spriteSet != null) {
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
    public void render(VertexConsumer vertexConsumer, Camera camera, float partialTicks) {
        if (this.sprite != null) {
            float f = ((float) this.age + partialTicks) / (float) this.lifetime * 32.0F;
            float scale = Math.min(Math.max(f, 0.0F), 1.0F); // Clamp between 0 and 1
            this.quadSize = this.smokeParticleScale * scale;
            super.render(vertexConsumer, camera, partialTicks);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
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
        public Particle createParticle(SimpleParticleType typeIn, ClientLevel worldIn, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new HelicopterEngineParticle(worldIn, x, y, z, xSpeed, ySpeed, zSpeed, 0.8F, spriteSet);
        }
    }
}
