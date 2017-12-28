package test.zyh.com.helloworld;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by admin on 2017/9/14.
 */

public class MyTDView extends GLSurfaceView {

    final float ANGLE_SPAN = 0.375f;
    RotateThread rthread;
    SceneRenderer mRender;


    public MyTDView(Context context) {
        super(context);
        this.setEGLContextClientVersion(3);
        mRender = new SceneRenderer();
        this.setRenderer(mRender);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    private class SceneRenderer implements GLSurfaceView.Renderer{

        Triangle tle;

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES30.glClearColor(0, 0, 0, 1.0f);
            tle = new Triangle(MyTDView.this);
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
            rthread = new RotateThread();
            rthread.start();
        }

        @Override
        public void onSurfaceChanged(GL10 gl,  int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float)width / height;
            Matrix.frustumM(Triangle.mProjMatrix, 0, -ratio, ratio, -1, 1, 1, 10);
            Matrix.setLookAtM(Triangle.mVMatrix, 0, 0, 0, 3, 0, 0, 0, 0, 1.0f, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            tle.drawSelf();

        }
    }

    public class RotateThread extends Thread {
        public boolean flag = true;

        @Override
        public void run() {
            while (flag) {
                mRender.tle.xAngle = mRender.tle.xAngle + ANGLE_SPAN;
                try {
                    Thread.sleep(20);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
