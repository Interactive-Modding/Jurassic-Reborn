package mod.reborn.server.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;

import java.util.Random;

public class DinosaurAttributes {
    private static final float MINIMUM_SCALE = 0.8F;
    private static final float MAXIMUM_SCALE = 1.2F;
    private static final float RARE_SCALE = 1.4F;

    private static final float MINIMUM_DAMAGE = 0.8F;
    private static final float MAXIMUM_DAMAGE = 1.2F;

    private static final float MINIMUM_HEALTH = 0.8F;
    private static final float MAXIMUM_HEALTH = 1.2F;

    private static final float MINIMUM_SPEED = 0.8F;
    private static final float MAXIMUM_SPEED = 1.2F;

    private float scaleModifier;
    private float damageModifier;
    private float healthModifier;
    private float speedModifier;

    private DinosaurAttributes(float scaleModifier, float damageModifier, float healthModifier, float speedModifier) {
        this.scaleModifier = scaleModifier;
        this.damageModifier = damageModifier;
        this.healthModifier = healthModifier;
        this.speedModifier = speedModifier;
    }

    public void writeToNBT(NBTTagCompound compound) {
        compound.setFloat("Scale", this.scaleModifier);
        compound.setFloat("Damage", this.damageModifier);
        compound.setFloat("Health", this.healthModifier);
        compound.setFloat("Speed", this.speedModifier);
    }

    public void write(ByteBuf buf) {
        buf.writeFloat(this.scaleModifier);
    }

    public static DinosaurAttributes combine(DinosaurEntity entity, DinosaurAttributes attributes1, DinosaurAttributes attributes2) {
        Random random = entity.getRNG();
        float scale = DinosaurAttributes.random(random, attributes1.scaleModifier, attributes2.scaleModifier);
        if (random.nextInt(3) == 0) {
            scale = DinosaurAttributes.random(random, MINIMUM_SCALE, MAXIMUM_SCALE);
        }
        float scaleStat = (2.0F + scale) / 3.0F;
        float damage = DinosaurAttributes.random(random, attributes1.damageModifier, attributes2.damageModifier);
        if (random.nextInt(3) == 0) {
            damage = DinosaurAttributes.random(random, MINIMUM_DAMAGE, MAXIMUM_DAMAGE) * scaleStat;
        }
        float health = DinosaurAttributes.random(random, attributes1.healthModifier, attributes2.healthModifier);
        if (random.nextInt(3) == 0) {
            health = DinosaurAttributes.random(random, MINIMUM_HEALTH, MAXIMUM_SPEED) * scaleStat;
        }
        float speed = DinosaurAttributes.random(random, attributes1.speedModifier, attributes2.speedModifier);
        if (random.nextInt(3) == 0) {
            speed = DinosaurAttributes.random(random, MINIMUM_SPEED, MAXIMUM_SPEED) / scaleStat;
        }
        return new DinosaurAttributes(scale, damage, health, speed);
    }

    public static DinosaurAttributes create(DinosaurEntity entity) {
        Random random = entity.getRNG();
        float scale = DinosaurAttributes.random(random, MINIMUM_SCALE, MAXIMUM_SCALE);
        float scaleStat = (2.0F + scale) / 3.0F;
        float damage = DinosaurAttributes.random(random, MINIMUM_DAMAGE, MAXIMUM_DAMAGE) * scaleStat;
        float health = DinosaurAttributes.random(random, MINIMUM_HEALTH, MAXIMUM_HEALTH) * scaleStat;
        float speed = DinosaurAttributes.random(random, MINIMUM_SPEED, MAXIMUM_SPEED) / scaleStat;
        return new DinosaurAttributes(scale, damage, health, speed);
    }

    public static DinosaurAttributes from(NBTTagCompound compound) {
        float scaleModifier = compound.getFloat("Scale");
        float damageModifier = compound.getFloat("Damage");
        float healthModifier = compound.getFloat("Health");
        float speedModifier = compound.getFloat("Speed");
        return new DinosaurAttributes(scaleModifier, damageModifier, healthModifier, speedModifier);
    }

    public static DinosaurAttributes from(ByteBuf buf) {
        float scale = buf.readFloat();
        return new DinosaurAttributes(scale, 1.0F, 1.0F, 1.0F);
    }

    private static float random(Random random, float v1, float v2) {
        float minimum = Math.min(v1, v2);
        float maximum = Math.max(v1, v2);
        float range = maximum - minimum;
        return (float) (random.nextDouble() * range + minimum);
    }

    private static float random(Random random, float v1, float v2, float v3) {
        float minimum = Math.min(v1, v2);
        float maximum = Math.max(v1, v2);
        float range = maximum - minimum;
        return (float) (random.nextDouble() * range + minimum);
    }

    public float getScaleModifier() {
        return this.scaleModifier;
    }

    public float getHealthModifier() {
        return this.healthModifier;
    }

    public float getDamageModifier() {
        return this.damageModifier;
    }

    public float getSpeedModifier() {
        return this.speedModifier;
    }

    public void setScaleModifier(float scaleModifier) {
        this.scaleModifier = scaleModifier;
    }
}
