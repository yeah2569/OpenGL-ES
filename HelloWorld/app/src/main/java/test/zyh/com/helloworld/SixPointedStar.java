package test.zyh.com.helloworld;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import static test.zyh.com.helloworld.Triangle.mMMatrix;

/**
 * Created by admin on 2018/1/24.
 */

public class SixPointedStar {
    float yAngle=0;
    float xAngle=0;
    final float UNIT_SIZE=1;
    int mProgram;
    int muMVPMatrixHandle;
    int maPositionHandle;
    int maColorHandle;
    String mVertexShader;
    String mFragmentShader;
    FloatBuffer mVertexBuffer;
    FloatBuffer mColorBuffer;
    static float[]mMMatrix = new float[16];
    int vCount = 0;

    public SixPointedStar(MySurfaceView mv,float r,float R,float z) {
        initVertexData(R, r, z);
        initShader(mv);
    }

    public void initVertexData(float R,float r,float z) {
        List<Float> flist = new ArrayList<Float>();
        float tempAngle = 360 / 6;
        for (float angle = 0;angle <360; angle+= tempAngle) {

            flist.add(0f);
            flist.add(0f);
            flist.add(z);
            flist.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle))));
            flist.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle))));
            flist.add(z);
            flist.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add(z);
            /*
            flist.add(0f);
            flist.add(0f);
            flist.add(z);
            flist.add((float) (r * UNIT_SIZE * Math.cos(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add((float) (r * UNIT_SIZE * Math.sin(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add(z);
            flist.add((float) (R * UNIT_SIZE * Math.cos(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add((float) (R * UNIT_SIZE * Math.sin(Math.toRadians(angle +
                    tempAngle / 2))));
            flist.add(z);
*/
        }
        vCount = flist.size() / 3;
        float[] vertexArray = new float[flist.size()];
        for (int i = 0; i < vCount; i++) {
            vertexArray[i * 3] = flist.get(i * 3);
            vertexArray[i * 3 + 1] = flist.get(i * 3 + 1);
            vertexArray[i * 3 + 2] = flist.get(i * 3 + 2);
        }
        ByteBuffer vbb = ByteBuffer.allocateDirect(vertexArray.length * 4);
        vbb.order(ByteOrder.nativeOrder());
        mVertexBuffer = vbb.asFloatBuffer();
        mVertexBuffer.put(vertexArray); //将顶点坐标数据放进缓冲
        mVertexBuffer.position(0);
        float[] colorArray = new float[vCount * 4]; //顶点着色数据的初始化
        for (int i = 0; i < vCount; i++) {
            if (i % 3 == 0) { //中心点为白色， RGBA 4 个通道[1,1,1,0]
             colorArray[i * 4] = 1;colorArray[i * 4 + 1] = 1;
             colorArray[i * 4 + 2] = 1;colorArray[i * 4 + 3] = 0;
             } else { //边上的点为淡蓝色， RGBA 4 个通道[0.45,0.75,0.75,0]
             colorArray[i * 4] = 0.45f;colorArray[i * 4 + 1] = 0.75f;
             colorArray[i * 4 + 2] = 0.75f;colorArray[i * 4 + 3] = 0;
             }
            }
        ByteBuffer cbb = ByteBuffer.allocateDirect(colorArray.length * 4);
        cbb.order(ByteOrder.nativeOrder()); //设置字节顺序为本地操作系统顺序
        mColorBuffer = cbb.asFloatBuffer();
        mColorBuffer.put(colorArray); //将顶点颜色数据放进缓冲
        mColorBuffer.position(0);
    }

    public void initShader(MySurfaceView mv) {
        mVertexShader = ShaderUtil.loadFromAssetsFile("vertex.sh", mv.getResources());
        mFragmentShader = ShaderUtil.loadFromAssetsFile("frag.sh", mv.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShader, mFragmentShader);
        maPositionHandle = GLES30.glGetAttribLocation(mProgram, "aPosition");
        maColorHandle = GLES30.glGetAttribLocation(mProgram, "aColor");
        muMVPMatrixHandle = GLES30.glGetUniformLocation(mProgram, "uMVPMatrix");
    }

    public void drawSelf() {
        GLES30.glUseProgram(mProgram);
        Matrix.setRotateM(mMMatrix,0,0,0,1,0); //初始化变换矩阵
        Matrix.translateM(mMMatrix,0,0,0,1); //设置沿 z 轴正向位移 1
        Matrix.rotateM(mMMatrix,0,yAngle,0,1,0); //设置绕 y 轴旋转 yAngle 度
        Matrix.rotateM(mMMatrix,0,xAngle,1,0,0); //设置绕 x 轴旋转 xAngle 度
        GLES30.glUniformMatrix4fv(muMVPMatrixHandle, 1, false, MatrixState.getFinalMatrix(mMMatrix), 0); //将最终变换矩阵传入渲染管线
        GLES30.glVertexAttribPointer( //将顶点位置数据送入渲染管线
              maPositionHandle, 3, GLES30.GL_FLOAT, false,3*4,mVertexBuffer);
         GLES30.glVertexAttribPointer( //将顶点颜色数据送入渲染管线
              maColorHandle,4,GLES30.GL_FLOAT,false,4*4,mColorBuffer);
         GLES30.glEnableVertexAttribArray(maPositionHandle); //启用顶点位置数据数组
         GLES30.glEnableVertexAttribArray(maColorHandle); //启用顶点颜色数据数组
         GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vCount);
    }
}
