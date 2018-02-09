package test.zyh.com.helloworld;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by admin on 2018/1/24.
 */

public class Sample5_1_Activity extends AppCompatActivity {
    MyTDView mview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mview = new MyTDView(this);
        mview.requestFocus();
        mview.setFocusableInTouchMode(true);
        setContentView(mview);

        //setContentView(R.layout.main);

        /*
        // 以下是Android 4.4以后自动隐藏虚拟键全屏显示的组合代码
        View view = getWindow().getDecorView();
        view.setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
        );
        */
    }

    @Override
    protected void onPause() {
        super.onPause();
        mview.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mview.onResume();
    }
}
