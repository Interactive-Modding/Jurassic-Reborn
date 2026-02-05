package net.vit.jurassicreborn.common.util.math;

/** Minimal mutable vector used for legacy vehicle code. */
public class MutableVec3 {
    public double xCoord;
    public double yCoord;
    public double zCoord;

    public MutableVec3(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    public void set(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }
}