package test.zyh.com.helloworld;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by admin on 2018/1/24.
 */

public class MySurfaceView extends GLSurfaceView {
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private SceneRenderer mRenderer;
    private float mPreviousY;
    private float mPreviousX;

    public MySurfaceView(Context context) {
        super(context);

        this.setEGLContextClientVersion(3);
        mRenderer = new MySurfaceView.SceneRenderer();
        this.setRenderer(mRenderer);
        this.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    public boolean onTouchEvent(MotionEvent e) {
        float y = e.getY();
        float x = e.getX();
        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dy = y - mPreviousY;
                float dx = x - mPreviousX;
                for ( SixPointedStar h: mRenderer.ha) {
                    h.yAngle += dx * TOUCH_SCALE_FACTOR;
                    h.xAngle += dy * TOUCH_SCALE_FACTOR;
                }
        }
        mPreviousX = x;
        mPreviousY = y;
        return  true;

    }

    private class SceneRenderer implements GLSurfaceView.Renderer{

        SixPointedStar ha[] = new SixPointedStar[1];

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            GLES30.glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
            for (int i = 0; i < ha.length; i++) {
                //ha[i] = new SixPointedStar(MySurfaceView.this, 0.2f, 0.5f, -0.3f * i);
                ha[i] = new SixPointedStar(MySurfaceView.this, 0.9f, 1.5f, -0.3f * i);
            }
            GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        }

        @Override
        public void onSurfaceChanged(GL10 gl,  int width, int height) {
            GLES30.glViewport(0, 0, width, height);
            float ratio = (float)width / height;
            MatrixState.setProjectOrtho(-ratio, ratio, -1, 1, 1, 10);
            MatrixState.setCamera(0, 0, 3, 0, 0, 0, 0, 1, 0);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            GLES30.glClear(GLES30.GL_DEPTH_BUFFER_BIT | GLES30.GL_COLOR_BUFFER_BIT);
            for ( SixPointedStar h : ha) {
                h.drawSelf();
            }
        }
    }

}
