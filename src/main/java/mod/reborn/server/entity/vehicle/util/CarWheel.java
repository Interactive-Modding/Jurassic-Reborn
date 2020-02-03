package mod.reborn.server.entity.vehicle.util;

import javax.vecmath.Vector2d;

import net.minecraft.util.math.Vec3d;

public class CarWheel {
    
    private final Vector2d relativeWheelPosition;        
    private Vec3d currentWheelPos = Vec3d.ZERO;
    private Vec3d prevCurrentWheelPos = Vec3d.ZERO;
    private final int ID;
    
    private CarWheel oppositeWheel;
    
    public CarWheel(int id, Vector2d relativeWheelPosition) {
	this.relativeWheelPosition = relativeWheelPosition;
	this.ID = id;
    }
    
    public Vector2d getRelativeWheelPosition() {
	return relativeWheelPosition;
    }
    
    public void setCurrentWheelPos(Vec3d currentWheelPos) {
	this.prevCurrentWheelPos = this.currentWheelPos;
        this.currentWheelPos = currentWheelPos;
    }
    
    public Vec3d getPrevCurrentWheelPos() {
	return prevCurrentWheelPos;
    }
    
    public Vec3d getCurrentWheelPos() {
        return currentWheelPos;
    }
    
    public int getID() {
	return ID;
    }
    
    public void setPair(CarWheel oppositeWheel) {
	this.oppositeWheel = oppositeWheel;
	oppositeWheel.oppositeWheel = this;
    }
    
    public CarWheel getOppositeWheel() {
	return oppositeWheel;
    }
}