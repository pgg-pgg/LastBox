package com.example.pgg.qboxdemo.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by pgg on 2018/5/4.
 */

public class AppUtils {

    public static int getVersionCode(Context context){
        if (context!=null){
            try {
                return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionCode;
            }catch (PackageManager.NameNotFoundException ignore){

            }
        }
        return 0;
    }

    public static String getVersionName(Context context){
        if (context!=null){
            try {
                return context.getPackageManager().getPackageInfo(context.getPackageName(),0).versionName;
            }catch (PackageManager.NameNotFoundException ignore){

            }
        }
        return "";
    }
}
