/*
 * Copyright 1996-2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 *
 */

package javax.vecmathimpl;

import java.io.Serializable;

public class Vector3d extends Tuple3d implements Serializable {
    static final long serialVersionUID = 3761969948420550442L;

    public Vector3d(double var1, double var3, double var5) {
        super(var1, var3, var5);
    }

    public Vector3d(double[] var1) {
        super(var1);
    }

    public Vector3d(Vector3d var1) {
        super(var1);
    }

    public Vector3d(Tuple3d var1) {
        super(var1);
    }

    public Vector3d() {
    }

    public final void cross(Vector3d var1, Vector3d var2) {
        double var3 = var1.y * var2.z - var1.z * var2.y;
        double var5 = var2.x * var1.z - var2.z * var1.x;
        this.z = var1.x * var2.y - var1.y * var2.x;
        this.x = var3;
        this.y = var5;
    }

    public final void normalize(Vector3d var1) {
        double var2 = 1.0D / Math.sqrt(var1.x * var1.x + var1.y * var1.y + var1.z * var1.z);
        this.x = var1.x * var2;
        this.y = var1.y * var2;
        this.z = var1.z * var2;
    }

    public final void normalize() {
        double var1 = 1.0D / Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
        this.x *= var1;
        this.y *= var1;
        this.z *= var1;
    }

    public final double dot(Vector3d var1) {
        return this.x * var1.x + this.y * var1.y + this.z * var1.z;
    }

    public final double lengthSquared() {
        return this.x * this.x + this.y * this.y + this.z * this.z;
    }

    public final double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public final double angle(Vector3d var1) {
        double var2 = this.dot(var1) / (this.length() * var1.length());
        if (var2 < -1.0D) {
            var2 = -1.0D;
        }

        if (var2 > 1.0D) {
            var2 = 1.0D;
        }

        return Math.acos(var2);
    }
}
