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
        return 0;
    }

    public static int createProgram(String vertSource, String fragSource) {
        return  0;
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
