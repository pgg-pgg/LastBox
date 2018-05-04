package com.example.pgg.qboxdemo.global;

import android.app.Application;

import com.example.pgg.qboxdemo.module.main.MainActivity;
import com.mob.MobSDK;

/**
 * Created by pgg on 2018/5/2.
 * 基类的Application
 */

public class MyApplication extends Application {
    private static MainActivity sMainActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        MobSDK.init(this);
    }

    public static void setMainActivity(MainActivity activity) {
        sMainActivity = activity;
    }
}
