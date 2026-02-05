package net.vit.jurassicreborn.common.entities;

import net.vit.jurassicreborn.common.entities.LegSolver;

public final class LegSolverQuadruped extends LegSolver {
    public final Leg backLeft, backRight, frontLeft, frontRight;

    public LegSolverQuadruped(float forward, float side, float frontRange, float backRange) {
        this(0.0F, forward, side, frontRange, backRange);
    }

    public LegSolverQuadruped(float forwardCenter, float forward, float side, float frontRange, float backRange) {
        super(
                new Leg(forwardCenter - forward,  side,  backRange), // back left
                new Leg(forwardCenter - forward, -side,  backRange), // back right
                new Leg(forwardCenter + forward,  side,  frontRange),  // front left
                new Leg(forwardCenter + forward, -side,  frontRange)   // front right
        );
        this.backLeft   = this.legs[0];
        this.backRight  = this.legs[1];
        this.frontLeft  = this.legs[2];
        this.frontRight = this.legs[3];
    }
}
