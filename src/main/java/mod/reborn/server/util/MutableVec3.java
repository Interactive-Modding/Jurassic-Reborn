package mod.reborn.server.util;

import net.minecraft.util.math.MathHelper;

/**
 * Mutable version of Minecraft's Vec3
 */
public class MutableVec3 {
    private static final String __OBFID = "CL_00000612";
    /**
     * X coordinate of Vec3D
     */
    public double xCoord;
    /**
     * Y coordinate of Vec3D
     */
    public double yCoord;
    /**
     * Z coordinate of Vec3D
     */
    public double zCoord;

    public MutableVec3(double x, double y, double z) {
        if (x == -0.0D) {
            x = 0.0D;
        }

        if (y == -0.0D) {
            y = 0.0D;
        }

        if (z == -0.0D) {
            z = 0.0D;
        }

        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
    }

    /**
     * Returns this vector for chaining with the result of the specified vector minus this.
     */
    public MutableVec3 subtractReverse(MutableVec3 vec) {
        return new MutableVec3(vec.xCoord - this.xCoord, vec.yCoord - this.yCoord, vec.zCoord - this.zCoord);
    }

    /**
     * Normalizes the vector to a length of 1 (except if it is the zero vector)
     */
    public MutableVec3 normalize() {
        double d0 = (double) MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
        if (d0 < 1.0E-4D) {
            this.set(0, 0, 0);
        } else {
            this.set(this.xCoord / d0, this.yCoord / d0, this.zCoord / d0);
        }
        return this;
    }

    public MutableVec3 set(MutableVec3 v) {
        return this.set(v.xCoord, v.yCoord, v.zCoord);
    }

    public MutableVec3 set(double x, double y, double z) {
        this.xCoord = x;
        this.yCoord = y;
        this.zCoord = z;
        return this;
    }

    public double dotProduct(MutableVec3 vec) {
        return this.xCoord * vec.xCoord + this.yCoord * vec.yCoord + this.zCoord * vec.zCoord;
    }

    /**
     * Returns a new vector with the result of this vector x the specified vector.
     */
    public MutableVec3 crossProduct(MutableVec3 vec) {
        return this.set(this.yCoord * vec.zCoord - this.zCoord * vec.yCoord, this.zCoord * vec.xCoord - this.xCoord * vec.zCoord, this.xCoord * vec.yCoord - this.yCoord * vec.xCoord);
    }

    public MutableVec3 subtract(MutableVec3 vec) {
        return this.subtract(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    public MutableVec3 subtract(double x, double y, double z) {
        return this.addVector(-x, -y, -z);
    }

    public MutableVec3 add(MutableVec3 vec) {
        return this.addVector(vec.xCoord, vec.yCoord, vec.zCoord);
    }

    /**
     * Adds the specified x,y,z vector components to this vector and returns the resulting vector. Does not change this vector.
     */
    public MutableVec3 addVector(double x, double y, double z) {
        return this.set(this.xCoord + x, this.yCoord + y, this.zCoord + z);
    }

    /**
     * Euclidean distance between this and the specified vector, returned as double.
     */
    public double distanceTo(MutableVec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return (double) MathHelper.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
    }

    /**
     * The square of the Euclidean distance between this and the specified vector.
     */
    public double squareDistanceTo(MutableVec3 vec) {
        double d0 = vec.xCoord - this.xCoord;
        double d1 = vec.yCoord - this.yCoord;
        double d2 = vec.zCoord - this.zCoord;
        return d0 * d0 + d1 * d1 + d2 * d2;
    }

    /**
     * Returns the length of the vector.
     */
    public double lengthVector() {
        return (double) MathHelper.sqrt(this.xCoord * this.xCoord + this.yCoord * this.yCoord + this.zCoord * this.zCoord);
    }

    /**
     * Returns a new vector with x value equal to the second parameter, along the line between this vector and the passed in vector, or null if not possible.
     */
    public MutableVec3 getIntermediateWithXValue(MutableVec3 vec, double x) {
        double d1 = vec.xCoord - this.xCoord;
        double d2 = vec.yCoord - this.yCoord;
        double d3 = vec.zCoord - this.zCoord;

        if (d1 * d1 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (x - this.xCoord) / d1;
            return d4 >= 0.0D && d4 <= 1.0D ? this.set(this.xCoord + d1 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
        }
    }

    /**
     * Returns a new vector with y value equal to the second parameter, along the line between this vector and the passed in vector, or null if not possible.
     */
    public MutableVec3 getIntermediateWithYValue(MutableVec3 vec, double y) {
        double d1 = vec.xCoord - this.xCoord;
        double d2 = vec.yCoord - this.yCoord;
        double d3 = vec.zCoord - this.zCoord;

        if (d2 * d2 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (y - this.yCoord) / d2;
            return d4 >= 0.0D && d4 <= 1.0D ? this.set(this.xCoord + d1 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
        }
    }

    /**
     * Returns a new vector with z value equal to the second parameter, along the line between this vector and the passed in vector, or null if not possible.
     */
    public MutableVec3 getIntermediateWithZValue(MutableVec3 vec, double z) {
        double d1 = vec.xCoord - this.xCoord;
        double d2 = vec.yCoord - this.yCoord;
        double d3 = vec.zCoord - this.zCoord;

        if (d3 * d3 < 1.0000000116860974E-7D) {
            return null;
        } else {
            double d4 = (z - this.zCoord) / d3;
            return d4 >= 0.0D && d4 <= 1.0D ? this.set(this.xCoord + d1 * d4, this.yCoord + d2 * d4, this.zCoord + d3 * d4) : null;
        }
    }

    @Override
    public String toString() {
        return "(" + this.xCoord + ", " + this.yCoord + ", " + this.zCoord + ")";
    }

    public MutableVec3 rotatePitch(float pitch) {
        float f1 = MathHelper.cos(pitch);
        float f2 = MathHelper.sin(pitch);
        double d0 = this.xCoord;
        double d1 = this.yCoord * (double) f1 + this.zCoord * (double) f2;
        double d2 = this.zCoord * (double) f1 - this.yCoord * (double) f2;
        return this.set(d0, d1, d2);
    }

    public MutableVec3 rotateYaw(float yaw) {
        float f1 = MathHelper.cos(yaw);
        float f2 = MathHelper.sin(yaw);
        double d0 = this.xCoord * (double) f1 + this.zCoord * (double) f2;
        double d1 = this.yCoord;
        double d2 = this.zCoord * (double) f1 - this.xCoord * (double) f2;
        return this.set(d0, d1, d2);
    }
}
