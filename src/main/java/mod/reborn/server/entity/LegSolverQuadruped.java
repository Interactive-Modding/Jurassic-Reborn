package mod.reborn.server.entity;

public final class LegSolverQuadruped extends LegSolver {
    public final Leg backLeft, backRight, frontLeft, frontRight;

    public LegSolverQuadruped(float forward, float side, float frontRange, float backRange) {
        this(0.0F, forward, side, frontRange, backRange);
    }

    public LegSolverQuadruped(float forwardCenter, float forward, float side, float frontRange, float backRange) {
        super(
            new Leg(forwardCenter - forward, side, backRange),
            new Leg(forwardCenter - forward, -side, backRange),
            new Leg(forwardCenter + forward, side, frontRange),
            new Leg(forwardCenter + forward, -side, frontRange)
        );
        this.backLeft = this.legs[0];
        this.backRight = this.legs[1];
        this.frontLeft = this.legs[2];
        this.frontRight = this.legs[3];
    }
}
