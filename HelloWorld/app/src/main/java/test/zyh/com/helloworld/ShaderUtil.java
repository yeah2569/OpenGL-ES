package test.zyh.com.helloworld;

import android.content.res.Resources;
import android.opengl.GLES30;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by admin on 2017/9/5.
 */

public class ShaderUtil {

    public static int loadShader(int shaderType, String source){
        int shader = GLES30.glCreateShader(shaderType);
        if (shader != 0) {
            GLES30.glShaderSource(shader, source);
            GLES30.glCompileShader(shader);

            int []compiled = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compiled, 0);
            if (compiled[0] == 0) {
                Log.e("ES30_ERROR", "Cold not compile shader " + shaderType + ":");
                Log.e("ES30_ERROR", GLES30.glGetShaderInfoLog(shader));
                GLES30.glDeleteShader(shader);
            }
        }
        return shader;
    }

    public static int createProgram(String vertSource, String fragSource) {
        int vertShader = loadShader(GLES30.GL_VERTEX_SHADER, vertSource);
        if(vertShader == 0) { return 0;}
        int fragShader = loadShader(GLES30.GL_FRAGMENT_SHADER, fragSource);
        if (fragShader == 0) {
            return 0;
        }
        int program = GLES30.glCreateProgram();
        if (program != 0) {
            GLES30.glAttachShader(program, vertShader);
            checkGLError("glAttachShader");
            GLES30.glAttachShader(program, fragShader);
            checkGLError("glAttachShader");
            GLES30.glLinkProgram(program);
            int []linkStatus = new int[1];
            GLES30.glGetProgramiv(program, GLES30.GL_LINK_STATUS, linkStatus, 0);
            if (linkStatus[0] != GLES30.GL_TRUE){
                Log.e("ES30_ERROR", "Cold not link program: ");
                Log.e("ES30_ERROR", GLES30.glGetProgramInfoLog(program));
                GLES30.glDeleteProgram(program);
                program = 0;
            }
        }
        return  program;
    }

    public static void checkGLError(String op) {
        int error = 0;
        int is_error =  error == GLES30.glGetError() ? 1 : 0;
        while (is_error != GLES30.GL_NO_ERROR){
            Log.e("GLES30_ERROR", op + ": glError  " + error );
            throw new RuntimeException(op + ": glError " + error);

        }
    }

    public static String loadFromAssetsFile(String fname, Resources r) {
        String result = null;

        try {
            InputStream in = r.getAssets().open(fname);
            int ch = 0;
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            while((ch=in.read()) != -1) {
                baos.write(ch);
            }

            byte[] buff = baos.toByteArray();
            baos.close();
            result = new String(buff, "UTF-8");
            result = result.replaceAll("\\r\\n", "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
