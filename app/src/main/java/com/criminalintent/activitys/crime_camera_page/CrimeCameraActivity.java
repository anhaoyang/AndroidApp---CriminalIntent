package com.criminalintent.activitys.crime_camera_page;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Window;
import android.view.WindowManager;

import com.criminalintent.activitys.SingleFragmentActivity;

public class CrimeCameraActivity extends SingleFragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //隐藏标题栏：对AppCompatActivity无效 | 对Activity有效
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //隐藏标题栏：对AppCompatActivity有效
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);

        //隐藏标题栏：通用，但通常是在View创建好之后使用
        //getSupportActionBar().hide();

       // startOrientationChangeListener();
    }

    @Override
    protected Fragment createFragment() {
        return new CrimeCameraFragment();
    }
//
//    private OrientationEventListener mOrientationListener;
//    private String TAG = "MainActivity";
//    private int screenCurOrient = 2; //1表示正竖屏，2表示正横屏，3表示反竖屏，4表示反横屏
//
//    private final void startOrientationChangeListener() {
//        mOrientationListener = new OrientationEventListener(this) {
//            @Override
//            public void onOrientationChanged(int rotation) {
//                //判断四个方向
//                if (rotation == -1) {
//                    Log.d(TAG, "手机平放:" + rotation);
//                } else if (rotation < 10 || rotation > 350) {
//                    screenOrientChange(1);
//                } else if (rotation < 100 && rotation > 80) {
//                    screenOrientChange(4);
//                } else if (rotation < 190 && rotation > 170) {
//                    screenOrientChange(3);
//                } else if (rotation < 280 && rotation > 260) {
//                    screenOrientChange(2);
//                }
//                else
//                {
//                }
//            }
//        };
//        mOrientationListener.enable();
//    }
//
//    private void screenOrientChange(int Orient)
//    {
//        if(Orient != screenCurOrient)
//        {
//            screenCurOrient = Orient;
//            switch (screenCurOrient)
//            {
//                case 1:
//                    Log.d(TAG, "正竖屏:");
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    break;
//                case 2:
//                    Log.d(TAG, "正横屏:");
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    break;
//                case 3:
//                    Log.d(TAG, "反竖屏:");
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//                    break;
//                case 4:
//                    Log.d(TAG, "反横屏:");
//                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//                    break;
//            }
//        }
//    }
}
