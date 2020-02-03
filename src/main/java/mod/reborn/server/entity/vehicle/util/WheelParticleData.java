package mod.reborn.server.entity.vehicle.util;

import java.util.List;

import net.minecraft.util.math.Vec3d;

public class WheelParticleData {
    private int age;
    
    private final Vec3d position;
    private final Vec3d oppositePosition;
    private final int maxAge = 200;
    private final long worldTime;
    private boolean shouldRender = true;
    
    public WheelParticleData(Vec3d position, Vec3d oppositePosition, long worldTime) {
        this.position = position;
        this.oppositePosition = oppositePosition;
        this.worldTime = worldTime;
    }
    
    public WheelParticleData setShouldRender(boolean shouldRender) {
        this.shouldRender = shouldRender;
        return this;
    }
    
    public boolean shouldRender() {
	return shouldRender;
    }
    
    public Vec3d getOppositePosition() {
	return oppositePosition;
    }
    
    public long getWorldTime() {
	return worldTime;
    }
    
    public void onUpdate(List<WheelParticleData> markedRemoved) {
        if (this.age++ >= this.maxAge) {
            markedRemoved.add(this);
        }
    }
    
    public Vec3d getPosition() {
        return position;
    }
    
    public float getAlpha(float partialTicks) {
        if (age > 199) {
            return 0f;
        }
        float f = (float) Math.pow(((double) this.age + partialTicks) / (double) this.maxAge, 2);
        float f1 = 2.0F - f * 2.0F;

        if (f1 > 1.0F) {
            f1 = 1.0F;
        }

        f1 = f1 * 0.3F;
        return f1;
    }
}