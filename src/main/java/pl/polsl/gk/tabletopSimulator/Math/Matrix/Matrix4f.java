package pl.polsl.gk.tabletopSimulator.Math.Matrix;

/*
 * The MIT License (MIT)
 *
 * Copyright © 2015-2017, Heiko Brumme
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

import org.lwjgl.system.MemoryUtil;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector3f;
import pl.polsl.gk.tabletopSimulator.Math.Vector.Vector4f;

import java.nio.FloatBuffer;

/**
 * This class represents a 4x4-Matrix. GLSL equivalent to mat4.
 *
 * @author Heiko Brumme
 */
public class Matrix4f {
    public static final double PI = java.lang.Math.PI;
    static final float PI_f = (float) java.lang.Math.PI;
    static final float PI2_f = PI_f * 2.0f;
    static final float PIHalf_f = (float) ( PI * 0.5);

    private float m00, m01, m02, m03;
    private float m10, m11, m12, m13;
    private float m20, m21, m22, m23;
    private float m30, m31, m32, m33;

    /**
     * Creates a 4x4 identity matrix.
     */
    public Matrix4f() {
        setIdentity();
    }

    /**
     * Creates a 4x4 matrix with specified columns.
     *
     * @param col1 Vector with values of the first column
     * @param col2 Vector with values of the second column
     * @param col3 Vector with values of the third column
     * @param col4 Vector with values of the fourth column
     */
    public Matrix4f(Vector4f col1, Vector4f col2, Vector4f col3, Vector4f col4) {
        m00 = col1.x;
        m10 = col1.y;
        m20 = col1.z;
        m30 = col1.w;

        m01 = col2.x;
        m11 = col2.y;
        m21 = col2.z;
        m31 = col2.w;

        m02 = col3.x;
        m12 = col3.y;
        m22 = col3.z;
        m32 = col3.w;

        m03 = col4.x;
        m13 = col4.y;
        m23 = col4.z;
        m33 = col4.w;
    }

    public Matrix4f(Matrix4f matrix){
        m00 = matrix.m00;
        m10 = matrix.m10;
        m20 = matrix.m20;
        m30 = matrix.m30;

        m01 = matrix.m01;
        m11 = matrix.m11;
        m21 = matrix.m21;
        m31 = matrix.m31;

        m02 = matrix.m02;
        m12 = matrix.m12;
        m22 = matrix.m22;
        m32 = matrix.m32;

        m03 = matrix.m03;
        m13 = matrix.m13;
        m23 = matrix.m23;
        m33 = matrix.m33;
    }

    public Matrix4f getMatrix(Matrix4f matrix){
        return matrix;
    }

    /**
     * Sets this matrix to the identity matrix.
     */
    public final void setIdentity() {
        m00 = 1f;
        m11 = 1f;
        m22 = 1f;
        m33 = 1f;

        m01 = 0f;
        m02 = 0f;
        m03 = 0f;
        m10 = 0f;
        m12 = 0f;
        m13 = 0f;
        m20 = 0f;
        m21 = 0f;
        m23 = 0f;
        m30 = 0f;
        m31 = 0f;
        m32 = 0f;
    }

    /**
     * Adds this matrix to another matrix.
     *
     * @param other The other matrix
     *
     * @return Sum of this + other
     */
    public Matrix4f add(Matrix4f other) {
        Matrix4f result = new Matrix4f();

        result.m00 = this.m00 + other.m00;
        result.m10 = this.m10 + other.m10;
        result.m20 = this.m20 + other.m20;
        result.m30 = this.m30 + other.m30;

        result.m01 = this.m01 + other.m01;
        result.m11 = this.m11 + other.m11;
        result.m21 = this.m21 + other.m21;
        result.m31 = this.m31 + other.m31;

        result.m02 = this.m02 + other.m02;
        result.m12 = this.m12 + other.m12;
        result.m22 = this.m22 + other.m22;
        result.m32 = this.m32 + other.m32;

        result.m03 = this.m03 + other.m03;
        result.m13 = this.m13 + other.m13;
        result.m23 = this.m23 + other.m23;
        result.m33 = this.m33 + other.m33;

        return result;
    }

    /**
     * Negates this matrix.
     *
     * @return Negated matrix
     */
    public Matrix4f negate() {
        return multiply(-1f);
    }

    /**
     * Subtracts this matrix from another matrix.
     *
     * @param other The other matrix
     *
     * @return Difference of this - other
     */
    public Matrix4f subtract(Matrix4f other) {
        return this.add(other.negate());
    }

    /**
     * Multiplies this matrix with a scalar.
     *
     * @param scalar The scalar
     *
     * @return Scalar product of this * scalar
     */
    public Matrix4f multiply(float scalar) {
        Matrix4f result = new Matrix4f();

        result.m00 = this.m00 * scalar;
        result.m10 = this.m10 * scalar;
        result.m20 = this.m20 * scalar;
        result.m30 = this.m30 * scalar;

        result.m01 = this.m01 * scalar;
        result.m11 = this.m11 * scalar;
        result.m21 = this.m21 * scalar;
        result.m31 = this.m31 * scalar;

        result.m02 = this.m02 * scalar;
        result.m12 = this.m12 * scalar;
        result.m22 = this.m22 * scalar;
        result.m32 = this.m32 * scalar;

        result.m03 = this.m03 * scalar;
        result.m13 = this.m13 * scalar;
        result.m23 = this.m23 * scalar;
        result.m33 = this.m33 * scalar;

        return result;
    }

    /**
     * Multiplies this matrix to a vector.
     *
     * @param vector The vector
     *
     * @return Vector product of this * other
     */
    public Vector4f multiply(Vector4f vector) {
        float x = this.m00 * vector.x + this.m01 * vector.y + this.m02 * vector.z + this.m03 * vector.w;
        float y = this.m10 * vector.x + this.m11 * vector.y + this.m12 * vector.z + this.m13 * vector.w;
        float z = this.m20 * vector.x + this.m21 * vector.y + this.m22 * vector.z + this.m23 * vector.w;
        float w = this.m30 * vector.x + this.m31 * vector.y + this.m32 * vector.z + this.m33 * vector.w;
        return new Vector4f(x, y, z, w);
    }

    /**
     * Multiplies this matrix to another matrix.
     *
     * @param other The other matrix
     *
     * @return Matrix product of this * other
     */
    public Matrix4f multiply(Matrix4f other, Matrix4f matrix) {
       // Matrix4f result = new Matrix4f();

        matrix.m00 = this.m00 * other.m00 + this.m01 * other.m10 + this.m02 * other.m20 + this.m03 * other.m30;
        matrix.m10 = this.m10 * other.m00 + this.m11 * other.m10 + this.m12 * other.m20 + this.m13 * other.m30;
        matrix.m20 = this.m20 * other.m00 + this.m21 * other.m10 + this.m22 * other.m20 + this.m23 * other.m30;
        matrix.m30 = this.m30 * other.m00 + this.m31 * other.m10 + this.m32 * other.m20 + this.m33 * other.m30;

        matrix.m01 = this.m00 * other.m01 + this.m01 * other.m11 + this.m02 * other.m21 + this.m03 * other.m31;
        matrix.m11 = this.m10 * other.m01 + this.m11 * other.m11 + this.m12 * other.m21 + this.m13 * other.m31;
        matrix.m21 = this.m20 * other.m01 + this.m21 * other.m11 + this.m22 * other.m21 + this.m23 * other.m31;
        matrix.m31 = this.m30 * other.m01 + this.m31 * other.m11 + this.m32 * other.m21 + this.m33 * other.m31;

        matrix.m02 = this.m00 * other.m02 + this.m01 * other.m12 + this.m02 * other.m22 + this.m03 * other.m32;
        matrix.m12 = this.m10 * other.m02 + this.m11 * other.m12 + this.m12 * other.m22 + this.m13 * other.m32;
        matrix.m22 = this.m20 * other.m02 + this.m21 * other.m12 + this.m22 * other.m22 + this.m23 * other.m32;
        matrix.m32 = this.m30 * other.m02 + this.m31 * other.m12 + this.m32 * other.m22 + this.m33 * other.m32;

        matrix.m03 = this.m00 * other.m03 + this.m01 * other.m13 + this.m02 * other.m23 + this.m03 * other.m33;
        matrix.m13 = this.m10 * other.m03 + this.m11 * other.m13 + this.m12 * other.m23 + this.m13 * other.m33;
        matrix.m23 = this.m20 * other.m03 + this.m21 * other.m13 + this.m22 * other.m23 + this.m23 * other.m33;
        matrix.m33 = this.m30 * other.m03 + this.m31 * other.m13 + this.m32 * other.m23 + this.m33 * other.m33;

        return matrix;
    }

    /**
     * Transposes this matrix.
     *
     * @return Transposed matrix
     */
    public Matrix4f transpose() {
        Matrix4f result = new Matrix4f();

        result.m00 = this.m00;
        result.m10 = this.m01;
        result.m20 = this.m02;
        result.m30 = this.m03;

        result.m01 = this.m10;
        result.m11 = this.m11;
        result.m21 = this.m12;
        result.m31 = this.m13;

        result.m02 = this.m20;
        result.m12 = this.m21;
        result.m22 = this.m22;
        result.m32 = this.m23;

        result.m03 = this.m30;
        result.m13 = this.m31;
        result.m23 = this.m32;
        result.m33 = this.m33;

        return result;
    }

    /**
     * Stores the matrix in a given Buffer.
     *
     * @param buffer The buffer to store the matrix data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00).put(m10).put(m20).put(m30);
        buffer.put(m01).put(m11).put(m21).put(m31);
        buffer.put(m02).put(m12).put(m22).put(m32);
        buffer.put(m03).put(m13).put(m23).put(m33);
        buffer.flip();
    }

    /**
     * Creates a orthographic projection matrix. Similar to
     * <code>glOrtho(left, right, bottom, top, near, far)</code>.
     *
     * @param left   Coordinate for the left vertical clipping pane
     * @param right  Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top    Coordinate for the bottom horizontal clipping pane
     * @param near   Coordinate for the near depth clipping pane
     * @param far    Coordinate for the far depth clipping pane
     *
     * @return Orthographic matrix
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f ortho = new Matrix4f();

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        ortho.m00 = 2f / (right - left);
        ortho.m11 = 2f / (top - bottom);
        ortho.m22 = -2f / (far - near);
        ortho.m03 = tx;
        ortho.m13 = ty;
        ortho.m23 = tz;

        return ortho;
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>glFrustum(left, right, bottom, top, near, far)</code>.
     *
     * @param left   Coordinate for the left vertical clipping pane
     * @param right  Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top    Coordinate for the bottom horizontal clipping pane
     * @param near   Coordinate for the near depth clipping pane, must be
     *               positive
     * @param far    Coordinate for the far depth clipping pane, must be
     *               positive
     *
     * @return Perspective matrix
     */
    public static Matrix4f frustum(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f frustum = new Matrix4f();

        float a = (right + left) / (right - left);
        float b = (top + bottom) / (top - bottom);
        float c = -(far + near) / (far - near);
        float d = -(2f * far * near) / (far - near);

        frustum.m00 = (2f * near) / (right - left);
        frustum.m11 = (2f * near) / (top - bottom);
        frustum.m02 = a;
        frustum.m12 = b;
        frustum.m22 = c;
        frustum.m32 = -1f;
        frustum.m23 = d;
        frustum.m33 = 0f;

        return frustum;
    }

    /**
     * Creates a perspective projection matrix. Similar to
     * <code>gluPerspective(fovy, aspec, zNear, zFar)</code>.
     *
     * @param fovy   Field of view angle in degrees
     * @param aspect The aspect ratio is the ratio of width to height
     * @param near   Distance from the viewer to the near clipping plane, must
     *               be positive
     * @param far    Distance from the viewer to the far clipping plane, must be
     *               positive
     *
     * @return Perspective matrix
     */
    public static Matrix4f perspective(float fovy, float aspect, float near, float far, Matrix4f matrix) {
       // Matrix4f perspective = new Matrix4f();

        float f = (float) (1f / Math.tan(Math.toRadians(fovy) / 2f));

        matrix.m00 = f / aspect;
        matrix.m11 = f;
        matrix.m22 = (far + near) / (near - far);
        matrix.m32 = -1f;
        matrix.m23 = (2f * far * near) / (near - far);
        matrix.m33 = 0f;

        return matrix;
    }

    /**
     * Creates a translation matrix. Similar to
     * <code>glTranslate(x, y, z)</code>.
     *
     * @param x x coordinate of translation vector
     * @param y y coordinate of translation vector
     * @param z z coordinate of translation vector
     *
     * @return Translation matrix
     */
    public static Matrix4f translate(float x, float y, float z, Matrix4f matrix) {
        //Matrix4f translation = new Matrix4f();

        matrix.m03 = x;
        matrix.m13 = y;
        matrix.m23 = z;

        return matrix;
    }

    /**
     * Creates a rotation matrix. Similar to
     * <code>glRotate(angle, x, y, z)</code>.
     *
     * @param angle Angle of rotation in degrees
     * @param x     x coordinate of the rotation vector
     * @param y     y coordinate of the rotation vector
     * @param z     z coordinate of the rotation vector
     *
     * @return Rotation matrix
     */
    public static Matrix4f rotate(float angle, float x, float y, float z, Matrix4f matrix) {
      //  Matrix4f rotation = new Matrix4f();

        float c = (float) Math.cos(Math.toRadians(angle));
        float s = (float) Math.sin(Math.toRadians(angle));
        Vector3f vec = new Vector3f(x, y, z);
        if (vec.length() != 1f) {
            vec = vec.normalize();
            x = vec.x;
            y = vec.y;
            z = vec.z;
        }

        matrix.m00 = x * x * (1f - c) + c;
        matrix.m10 = y * x * (1f - c) + z * s;
        matrix.m20 = x * z * (1f - c) - y * s;
        matrix.m01 = x * y * (1f - c) - z * s;
        matrix.m11 = y * y * (1f - c) + c;
        matrix.m21 = y * z * (1f - c) + x * s;
        matrix.m02 = x * z * (1f - c) + y * s;
        matrix.m12 = y * z * (1f - c) - x * s;
        matrix.m22 = z * z * (1f - c) + c;

        return matrix;
    }

    /**
     * Creates a scaling matrix. Similar to <code>gl
     * le(x, y, z)</code>.
     *
     * @param x Scale factor along the x coordinate
     * @param y Scale factor along the y coordinate
     * @param z Scale factor along the z coordinate
     *
     * @return Scaling matrix
     */
    public static Matrix4f scale(float x, float y, float z) {
        Matrix4f scaling = new Matrix4f();

        scaling.m00 = x;
        scaling.m11 = y;
        scaling.m22 = z;

        return scaling;
    }

    public static Matrix4f scaleMatrix(float scale,Matrix4f matrix) {

        matrix.m00 = scale;
        matrix.m11 = scale;
        matrix.m22 = scale;

        return matrix;
    }


    private Matrix4f rotateX(float ang, Matrix4f dest){

    float sin = (float) Math.sin(ang), cos = cosFromSin(sin,ang);
    float lm10 = m10, lm11 = m11, lm12 = m12, lm13 = m13, lm20 = m20, lm21 = m21, lm22 = m22, lm23 = m23;

        dest.m20 = (lm10 * -sin + lm20 * cos);
        dest.m21 = (lm11 * -sin + lm21 * cos);
        dest.m22 = (lm12 * -sin + lm22 * cos);
        dest.m23 = (lm13 * -sin + lm23 * cos);
        dest.m10 = (lm10 * cos + lm20 * sin);
        dest.m11 = (lm11 * cos + lm21 * sin);
        dest.m12 = (lm12 * cos + lm22 * sin);
        dest.m13 = (lm13 * cos + lm23 * sin);
        dest.m00 = m00;
        dest.m01 = m01;
        dest.m02 = m02;
        dest.m03 = m03;
        dest.m30 = m30;
        dest.m31 = m31;
        dest.m32 = m32;
        dest.m33 = m33;

    return dest;

    }

    private Matrix4f rotateY(float ang, Matrix4f dest){

        float sin = (float) Math.sin(ang);
        float cos = cosFromSin(sin, ang);
        // add temporaries for dependent values
        float nm00 = m00 * cos + m20 * -sin;
        float nm01 = m01 * cos + m21 * -sin;
        float nm02 = m02 * cos + m22 * -sin;
        float nm03 = m03 * cos + m23 * -sin;
        // set non-dependent values directly

                dest.m20 = (m00 * sin + m20 * cos);
                dest.m21 = (m01 * sin + m21 * cos);
                dest.m22 = (m02 * sin + m22 * cos);
                dest.m23 = (m03 * sin + m23 * cos);
                // set other values
                dest.m00 = (nm00);
                dest.m01 = (nm01);
                dest.m02 = (nm02);
                dest.m03 = (nm03);
                dest.m10 = (m10);
                dest.m11 = (m11);
                dest.m12 = (m12);
                dest.m13 = (m13);
                dest.m30 = (m30);
                dest.m31 = (m31);
                dest.m32 = (m32);
                dest.m33 = (m33);

        return dest;
    }

    private Matrix4f rotateZ(float ang, Matrix4f dest){
        float sin = (float)Math.sin(ang);
        float cos = cosFromSin(sin,ang);

        return rotateTowardsXY(sin, cos, dest);

    }

    private Matrix4f rotateTowardsXY(float dirX, float dirY, Matrix4f dest){

        float nm00 = m00 * dirY + m10 * dirX;
        float nm01 = m01 * dirY + m11 * dirX;
        float nm02 = m02 * dirY + m12 * dirX;
        float nm03 = m03 * dirY + m13 * dirX;

        dest.m10 = (m00 * -dirX + m10 * dirY);
        dest.m11 = (m01 * -dirX + m11 * dirY);
        dest.m12 = (m02 * -dirX + m12 * dirY);
        dest.m13 = (m03 * -dirX + m13 * dirY);
        dest.m00 = (nm00);
        dest.m01 = (nm01);
        dest.m02 = (nm02);
        dest.m03 = (nm03);
        dest.m20 = (m20);
        dest.m21 = (m21);
        dest.m22 = (m22);
        dest.m23 = (m23);
        dest.m30 = (m30);
        dest.m31 = (m31);
        dest.m32 = (m32);
        dest.m33 = (m33);

        return dest;
    }

    private static float cosFromSin(float sin, float angle){
        // sin(x)^2 + cos(x)^2 = 1
        float cos = sqrt(1.0f - sin * sin);
        float a = angle + PIHalf_f;
        float b = a - (int)(a / PI2_f) * PI2_f;
        if (b < 0.0)
            b = PI2_f + b;
        if (b >= PI_f)
            return -cos;
        return cos;
    }

    public static float sqrt(float r){
        return (float) java.lang.Math.sqrt(r);
    }

    public Matrix4f rotateX(float ang){
       return rotateX(ang, this);

    }
    public Matrix4f rotateY(float ang){
        return rotateY(ang, this);

    }
    public Matrix4f rotateZ(float ang){
        return rotateX(ang, this);
    }

}
