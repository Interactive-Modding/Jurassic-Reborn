package net.vit.jurassicreborn.common.entities.vehicle;

import net.minecraft.world.phys.Vec3;

import net.minecraft.world.phys.Vec2;
/**
 * One physical wheel: stores its relative chassis position (in blocks),
 * the world-space position for the current / previous tick, and a pointer to
 * the wheel on the opposite side of the axle (convenient for tyre-track code).
 */
public final class CarWheel {

    private final int          id;
    private final Vec2     relPos;

    private Vec3 current = Vec3.ZERO;
    private Vec3 prev    = Vec3.ZERO;

    private CarWheel opposite;   // lazily set with setPair(...)

    public CarWheel(int id, Vec2 relativePos) {
        this.id     = id;
        this.relPos = relativePos;
    }

    /* --------------------------------------------------------------------- */
    /*  Tick update                                                          */
    /* --------------------------------------------------------------------- */


    public void setCurrentWheelPos(Vec3 worldPos) {
        this.prev    = this.current;
        this.current = worldPos;
    }

    /* --------------------------------------------------------------------- */
    /*  Accessors                                                            */
    /* --------------------------------------------------------------------- */

    public int       getID()                    { return id;      }
    public Vec2  getRelativeWheelPosition() { return relPos;  }
    public Vec3      getCurrentWheelPos()       { return current; }
    public Vec3      getPrevCurrentWheelPos()   { return prev;    }

    /* --------------------------------------------------------------------- */
    /*  Pairing helpers (left ↔ right)                                       */
    /* --------------------------------------------------------------------- */

    /** Links this wheel to its opposite axle-mate (and vice-versa). */
    public void setPair(CarWheel other) {
        this.opposite = other;
        other.opposite = this;
    }

    /** Returns the wheel on the opposite side of the axle (may be null). */
    public CarWheel getOppositeWheel() { return opposite; }
}
