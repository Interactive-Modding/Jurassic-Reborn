package net.vit.jurassicreborn.common.util;

import net.minecraft.world.phys.Vec3;


public final class MathUtils {

    private MathUtils() {}

    /** cosine of the angle opposite side <i>ab</i> in a triangle. */
    public static double cosine(double ac, double bc, double ab) {
        // law-of-cosines – converted to degrees
        return Math.acos((ac * ac + bc * bc - ab * ab) / (2.0 * ac * bc))
                * (180.0 / Math.PI);
    }

    /** cosine angle from three world-space positions */
    public static double cosineFromPoints(Vec3 a, Vec3 b, Vec3 c) {
        double ac = a.subtract(c).horizontalDistance();   //  horizontalDistance == √(dx²+dz²)
        double bc = b.subtract(c).horizontalDistance();
        double ab = b.subtract(a).horizontalDistance();
        return cosine(ac, bc, ab);
    }
}
