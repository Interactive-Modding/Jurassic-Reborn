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

/**
 * A double precision floating point 4 by 4 matrix.
 * Primarily to support 3D rotations.
 */
public class Matrix4d implements java.io.Serializable, Cloneable {

    // Compatible with 1.1
    static final long serialVersionUID = 8223903484171633710L;

    /**
     * The first element of the first row.
     */
    public double m00;

    /**
     * The second element of the first row.
     */
    public double m01;

    /**
     * The third element of the first row.
     */
    public double m02;

    /**
     * The fourth element of the first row.
     */
    public double m03;

    /**
     * The first element of the second row.
     */
    public double m10;

    /**
     * The second element of the second row.
     */
    public double m11;

    /**
     * The third element of the second row.
     */
    public double m12;

    /**
     * The fourth element of the second row.
     */
    public double m13;

    /**
     * The first element of the third row.
     */
    public double m20;

    /**
     * The second element of the third row.
     */
    public double m21;

    /**
     * The third element of the third row.
     */
    public double m22;

    /**
     * The fourth element of the third row.
     */
    public double m23;

    /**
     * The first element of the fourth row.
     */
    public double m30;

    /**
     * The second element of the fourth row.
     */
    public double m31;

    /**
     * The third element of the fourth row.
     */
    public double m32;

    /**
     * The fourth element of the fourth row.
     */
    public double m33;
    /*
    double[] tmp = new double[16];
    double[] tmp_rot = new double[9];  // scratch matrix
    double[] tmp_scale = new double[3];  // scratch matrix
    */
    private static final double EPS = 1.0E-10;

    /**
     * Constructs and initializes a Matrix4d from the specified 16 values.
     *
     * @param m00 the [0][0] element
     * @param m01 the [0][1] element
     * @param m02 the [0][2] element
     * @param m03 the [0][3] element
     * @param m10 the [1][0] element
     * @param m11 the [1][1] element
     * @param m12 the [1][2] element
     * @param m13 the [1][3] element
     * @param m20 the [2][0] element
     * @param m21 the [2][1] element
     * @param m22 the [2][2] element
     * @param m23 the [2][3] element
     * @param m30 the [3][0] element
     * @param m31 the [3][1] element
     * @param m32 the [3][2] element
     * @param m33 the [3][3] element
     */
    public Matrix4d(double m00, double m01, double m02, double m03,
                    double m10, double m11, double m12, double m13,
                    double m20, double m21, double m22, double m23,
                    double m30, double m31, double m32, double m33) {
        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;

        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;

        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;

        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * Constructs and initializes a Matrix4d from the specified 16
     * element array.  this.m00 =v[0], this.m01=v[1], etc.
     *
     * @param v the array of length 16 containing in order
     */
    public Matrix4d(double[] v) {
        this.m00 = v[0];
        this.m01 = v[1];
        this.m02 = v[2];
        this.m03 = v[3];

        this.m10 = v[4];
        this.m11 = v[5];
        this.m12 = v[6];
        this.m13 = v[7];

        this.m20 = v[8];
        this.m21 = v[9];
        this.m22 = v[10];
        this.m23 = v[11];

        this.m30 = v[12];
        this.m31 = v[13];
        this.m32 = v[14];
        this.m33 = v[15];
    }

    /**
     * Constructs a new matrix with the same values as the
     * Matrix4d parameter.
     *
     * @param m1 the source matrix
     */
    public Matrix4d(Matrix4d m1) {
        this.m00 = m1.m00;
        this.m01 = m1.m01;
        this.m02 = m1.m02;
        this.m03 = m1.m03;

        this.m10 = m1.m10;
        this.m11 = m1.m11;
        this.m12 = m1.m12;
        this.m13 = m1.m13;

        this.m20 = m1.m20;
        this.m21 = m1.m21;
        this.m22 = m1.m22;
        this.m23 = m1.m23;

        this.m30 = m1.m30;
        this.m31 = m1.m31;
        this.m32 = m1.m32;
        this.m33 = m1.m33;
    }

    /**
     * Constructs and initializes a Matrix4d to all zeros.
     */
    public Matrix4d() {
        this.m00 = 0.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;

        this.m10 = 0.0;
        this.m11 = 0.0;
        this.m12 = 0.0;
        this.m13 = 0.0;

        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 0.0;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 0.0;
    }

    /**
     * Returns a string that contains the values of this Matrix4d.
     *
     * @return the String representation
     */
    @Override
    public String toString() {
        return
                this.m00 + ", " + this.m01 + ", " + this.m02 + ", " + this.m03 + "\n" +
                        this.m10 + ", " + this.m11 + ", " + this.m12 + ", " + this.m13 + "\n" +
                        this.m20 + ", " + this.m21 + ", " + this.m22 + ", " + this.m23 + "\n" +
                        this.m30 + ", " + this.m31 + ", " + this.m32 + ", " + this.m33 + "\n";
    }

    /**
     * Sets this Matrix4d to identity.
     */
    public final void setIdentity() {
        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;

        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;

        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    /**
     * Modifies the translational components of this matrix to the values
     * of the Vector3d argument; the other values of this matrix are not
     * modified.
     *
     * @param trans the translational component
     */
    public final void setTranslation(Vector3d trans) {
        this.m03 = trans.x;
        this.m13 = trans.y;
        this.m23 = trans.z;
    }

    /**
     * Sets the value of this matrix to a counter-clockwise rotation
     * about the x axis.
     *
     * @param angle the angle to rotate about the X axis in radians
     */
    public final void rotX(double angle) {
        double sinAngle, cosAngle;

        sinAngle = Math.sin(angle);
        cosAngle = Math.cos(angle);

        this.m00 = 1.0;
        this.m01 = 0.0;
        this.m02 = 0.0;
        this.m03 = 0.0;

        this.m10 = 0.0;
        this.m11 = cosAngle;
        this.m12 = -sinAngle;
        this.m13 = 0.0;

        this.m20 = 0.0;
        this.m21 = sinAngle;
        this.m22 = cosAngle;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    /**
     * Sets the value of this matrix to a counter-clockwise rotation
     * about the y axis.
     *
     * @param angle the angle to rotate about the Y axis in radians
     */
    public final void rotY(double angle) {
        double sinAngle, cosAngle;

        sinAngle = Math.sin(angle);
        cosAngle = Math.cos(angle);

        this.m00 = cosAngle;
        this.m01 = 0.0;
        this.m02 = sinAngle;
        this.m03 = 0.0;

        this.m10 = 0.0;
        this.m11 = 1.0;
        this.m12 = 0.0;
        this.m13 = 0.0;

        this.m20 = -sinAngle;
        this.m21 = 0.0;
        this.m22 = cosAngle;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    /**
     * Sets the value of this matrix to a counter-clockwise rotation
     * about the z axis.
     *
     * @param angle the angle to rotate about the Z axis in radians
     */
    public final void rotZ(double angle) {
        double sinAngle, cosAngle;

        sinAngle = Math.sin(angle);
        cosAngle = Math.cos(angle);

        this.m00 = cosAngle;
        this.m01 = -sinAngle;
        this.m02 = 0.0;
        this.m03 = 0.0;

        this.m10 = sinAngle;
        this.m11 = cosAngle;
        this.m12 = 0.0;
        this.m13 = 0.0;

        this.m20 = 0.0;
        this.m21 = 0.0;
        this.m22 = 1.0;
        this.m23 = 0.0;

        this.m30 = 0.0;
        this.m31 = 0.0;
        this.m32 = 0.0;
        this.m33 = 1.0;
    }

    /**
     * Sets the value of this matrix to the result of multiplying itself
     * with matrix m1.
     *
     * @param m1 the other matrix
     */
    public final void mul(Matrix4d m1) {
        double m00, m01, m02, m03,
                m10, m11, m12, m13,
                m20, m21, m22, m23,
                m30, m31, m32, m33;  // vars for temp result matrix

        m00 = this.m00 * m1.m00 + this.m01 * m1.m10 +
                this.m02 * m1.m20 + this.m03 * m1.m30;
        m01 = this.m00 * m1.m01 + this.m01 * m1.m11 +
                this.m02 * m1.m21 + this.m03 * m1.m31;
        m02 = this.m00 * m1.m02 + this.m01 * m1.m12 +
                this.m02 * m1.m22 + this.m03 * m1.m32;
        m03 = this.m00 * m1.m03 + this.m01 * m1.m13 +
                this.m02 * m1.m23 + this.m03 * m1.m33;

        m10 = this.m10 * m1.m00 + this.m11 * m1.m10 +
                this.m12 * m1.m20 + this.m13 * m1.m30;
        m11 = this.m10 * m1.m01 + this.m11 * m1.m11 +
                this.m12 * m1.m21 + this.m13 * m1.m31;
        m12 = this.m10 * m1.m02 + this.m11 * m1.m12 +
                this.m12 * m1.m22 + this.m13 * m1.m32;
        m13 = this.m10 * m1.m03 + this.m11 * m1.m13 +
                this.m12 * m1.m23 + this.m13 * m1.m33;

        m20 = this.m20 * m1.m00 + this.m21 * m1.m10 +
                this.m22 * m1.m20 + this.m23 * m1.m30;
        m21 = this.m20 * m1.m01 + this.m21 * m1.m11 +
                this.m22 * m1.m21 + this.m23 * m1.m31;
        m22 = this.m20 * m1.m02 + this.m21 * m1.m12 +
                this.m22 * m1.m22 + this.m23 * m1.m32;
        m23 = this.m20 * m1.m03 + this.m21 * m1.m13 +
                this.m22 * m1.m23 + this.m23 * m1.m33;

        m30 = this.m30 * m1.m00 + this.m31 * m1.m10 +
                this.m32 * m1.m20 + this.m33 * m1.m30;
        m31 = this.m30 * m1.m01 + this.m31 * m1.m11 +
                this.m32 * m1.m21 + this.m33 * m1.m31;
        m32 = this.m30 * m1.m02 + this.m31 * m1.m12 +
                this.m32 * m1.m22 + this.m33 * m1.m32;
        m33 = this.m30 * m1.m03 + this.m31 * m1.m13 +
                this.m32 * m1.m23 + this.m33 * m1.m33;

        this.m00 = m00;
        this.m01 = m01;
        this.m02 = m02;
        this.m03 = m03;
        this.m10 = m10;
        this.m11 = m11;
        this.m12 = m12;
        this.m13 = m13;
        this.m20 = m20;
        this.m21 = m21;
        this.m22 = m22;
        this.m23 = m23;
        this.m30 = m30;
        this.m31 = m31;
        this.m32 = m32;
        this.m33 = m33;
    }

    /**
     * Returns true if all of the data members of Matrix4d m1 are
     * equal to the corresponding data members in this Matrix4d.
     *
     * @param m1 the matrix with which the comparison is made
     * @return true or false
     */
    public boolean equals(Matrix4d m1) {
        try {
            return (this.m00 == m1.m00 && this.m01 == m1.m01 && this.m02 == m1.m02
                    && this.m03 == m1.m03 && this.m10 == m1.m10 && this.m11 == m1.m11
                    && this.m12 == m1.m12 && this.m13 == m1.m13 && this.m20 == m1.m20
                    && this.m21 == m1.m21 && this.m22 == m1.m22 && this.m23 == m1.m23
                    && this.m30 == m1.m30 && this.m31 == m1.m31 && this.m32 == m1.m32
                    && this.m33 == m1.m33);
        } catch (NullPointerException e2) {
            return false;
        }
    }

    /**
     * Returns true if the Object t1 is of type Matrix4d and all of the
     * data members of t1 are equal to the corresponding data members in
     * this Matrix4d.
     *
     * @param t1 the matrix with which the comparison is made
     * @return true or false
     */
    @Override
    public boolean equals(Object t1) {
        try {
            Matrix4d m2 = (Matrix4d) t1;
            return (this.m00 == m2.m00 && this.m01 == m2.m01 && this.m02 == m2.m02
                    && this.m03 == m2.m03 && this.m10 == m2.m10 && this.m11 == m2.m11
                    && this.m12 == m2.m12 && this.m13 == m2.m13 && this.m20 == m2.m20
                    && this.m21 == m2.m21 && this.m22 == m2.m22 && this.m23 == m2.m23
                    && this.m30 == m2.m30 && this.m31 == m2.m31 && this.m32 == m2.m32
                    && this.m33 == m2.m33);
        } catch (ClassCastException e1) {
            return false;
        } catch (NullPointerException e2) {
            return false;
        }
    }

    /**
     * Transforms the normal parameter by this Matrix4d and places the value
     * into normalOut.  The fourth element of the normal is assumed to be zero.
     *
     * @param normal the input normal to be transformed.
     * @param normalOut the transformed normal
     */
    public final void transform(Vector3d normal, Vector3d normalOut) {
        double x, y;
        x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
        y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
        normalOut.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
        normalOut.x = x;
        normalOut.y = y;
    }

    /**
     * Transforms the normal parameter by this transform and places the value
     * back into normal.  The fourth element of the normal is assumed to be zero.
     *
     * @param normal the input normal to be transformed.
     */
    public final void transform(Vector3d normal) {
        double x, y;

        x = this.m00 * normal.x + this.m01 * normal.y + this.m02 * normal.z;
        y = this.m10 * normal.x + this.m11 * normal.y + this.m12 * normal.z;
        normal.z = this.m20 * normal.x + this.m21 * normal.y + this.m22 * normal.z;
        normal.x = x;
        normal.y = y;
    }
}
