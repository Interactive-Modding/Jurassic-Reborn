package mod.reborn.server.entity.ai;

import mod.reborn.server.conf.RebornConfig;
import mod.reborn.server.entity.DinosaurEntity;

import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.math.Vec3d;

public class DinosaurWanderAvoidWater extends DinosaurWanderEntityAI {

    private int walkradius;
    public DinosaurWanderAvoidWater(DinosaurEntity creatureIn, double speedIn, int walkradius) {
	super(creatureIn, speedIn, 1, walkradius);
        this.walkradius = walkradius;
    }
    
    @Override
    protected boolean innerShouldStopExcecuting() { 
        return this.entity.canDinoSwim() || !this.entity.isInWater();
    }
    
    @Override
    protected boolean outterShouldExecute() {
        return this.entity.shouldEscapeWaterFast();
    }
    
    @Override
    public boolean shouldContinueExecuting() {
        return false;
    }
    
    @Override
    protected Vec3d getWanderPosition() {
	Vec3d vec3d = null;
	for(int i = 0; i < 100; i++) {
	    Vec3d vec = RandomPositionGenerator.getLandPos(this.entity, walkradius + 5, walkradius);
	    assert vec != null;
        if(vec3d == null || this.entity.getPositionVector().distanceTo(vec) < this.entity.getPositionVector().distanceTo(vec3d)) {
		vec3d = vec;
	    }
	}
        return vec3d == null ? super.getWanderPosition() : vec3d;
    }

}