package test.zyh.com.helloworld;

import android.opengl.Matrix;

/**
 * Created by admin on 2018/1/24.
 */

public class MatrixState {
    private static float[] mProjMatrix = new float[16];
    private static float[] mVMatrix = new float[16];
    private static float[] mMVPMatrix;

    public static void setCamera(
            float cx, float cy, float cz,
            float tx, float ty, float tz,
            float upx, float upy, float upz
    ) {
        Matrix.setLookAtM(mVMatrix, 0, cx, cy, cz, tx, ty, tz, upx, upy, upz);
    }

    public static void  setProjectOrtho(
            float left, float right,
            float bottom, float top,
            float near, float far
    ) {
        Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }

    public static float[] getFinalMatrix(float[] spec) {
        mMVPMatrix = new float[16];
        Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, spec, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);
        return mMVPMatrix;
    }
}
