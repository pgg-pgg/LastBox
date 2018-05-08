package com.example.pgg.qboxdemo.global;

import android.app.Application;
import android.content.Context;
import android.util.DisplayMetrics;

import com.example.pgg.qboxdemo.module.main.MainActivity;
import com.mob.MobSDK;

/**
 * Created by pgg on 2018/5/2.
 * 基类的Application
 */

public class MyApplication extends Application {
    private static MainActivity sMainActivity = null;
    private static MyApplication mInstance;
    public static final boolean USE_SAMPLE_DATA = false;
    /**
     * 屏幕宽度
     */
    public static int screenWidth;
    /**
     * 屏幕高度
     */
    public static int screenHeight;
    /**
     * 屏幕密度
     */
    public static float screenDensity;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initScreenSize();
        MobSDK.init(this);
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }

    public static Context getInstance() {
        return mInstance;
    }

    /**
     * 初始化当前设备屏幕宽高
     */
    private void initScreenSize() {
        DisplayMetrics curMetrics = getApplicationContext().getResources().getDisplayMetrics();
        screenWidth = curMetrics.widthPixels;
        screenHeight = curMetrics.heightPixels;
        screenDensity = curMetrics.density;
    }
}
