package mod.reborn.server.entity.ai.util;

import net.minecraft.util.math.Vec3d;

public class MathUtils {
    /**
     * Gets the cosine angle c (opposite to side ab)
     */
    public static double cosine(double ac, double bc, double ab) {
	return Math.acos((Math.pow(ac, 2d) + Math.pow(bc, 2d) - Math.pow(ab, 2d)) / (2 * ac * bc)) * (180D/Math.PI);
    }
	
    public static double cosineFromPoints(Vec3d a, Vec3d b, Vec3d c) {
	double lengthAC = Math.sqrt(Math.pow(a.x - c.x, 2) + Math.pow(a.z - c.z, 2));
	double lengthBC = Math.sqrt(Math.pow(b.x - c.x, 2) + Math.pow(b.z - c.z, 2));
	double lengthAB = Math.sqrt(Math.pow(b.x - a.x, 2) + Math.pow(b.z - a.z, 2));
	return cosine(lengthAC, lengthBC, lengthAB);
    }
}