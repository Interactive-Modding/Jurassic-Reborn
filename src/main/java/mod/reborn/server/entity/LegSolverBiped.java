package mod.reborn.server.entity;

public final class LegSolverBiped extends LegSolver {
    public final Leg left, right;

    public LegSolverBiped(float forward, float side, float range) {
        super(new Leg(forward, side, range), new Leg(forward, -side, range));
        this.left = this.legs[0];
        this.right = this.legs[1];
    }
}
