package net.vit.jurassicreborn.common.entities.vehicle;

import net.minecraft.world.phys.Vec3;

import java.util.List;

public class WheelParticleData {
    private int age;
    private final Vec3 pos, opposite;
    private final int maxAge = 200;
    private final long worldTime;
    private boolean render = true;

    public WheelParticleData(Vec3 pos, Vec3 opposite, long worldTime) {
        this.pos = pos;
        this.opposite = opposite;
        this.worldTime = worldTime;
    }

    public WheelParticleData setShouldRender(boolean r) { this.render = r; return this; }
    public boolean shouldRender()              { return render; }
    public Vec3  getPosition()                 { return pos;    }
    public Vec3  getOppositePosition()         { return opposite; }
    public long  getWorldTime()                { return worldTime; }

    public void tick(List<WheelParticleData> dead) {
        if (++age >= maxAge) dead.add(this);
    }
    public void onUpdate(List<WheelParticleData> markedRemoved) {
        if (this.age++ >= this.maxAge) {
            markedRemoved.add(this);
        }
    }
    public float getAlpha(float partial) {
        if (age > 199) return 0f;
        float f  = (float) Math.pow(((double) age + partial) / maxAge, 2);
        float f1 = 2f - f * 2f;
        return Math.min(f1, 1f) * 0.3f;
    }
}
