package com.example.pgg.qboxdemo.module.start;

import android.os.Handler;

/**
 * Created by pgg on 2018/5/2.
 */

public class SplashInteractorImpl implements SplashInteractor {

    @Override
    public void enterInto(boolean isFirstOpen, final OnEnterIntoFinishListener listener) {
        if (!isFirstOpen){
            listener.isFirstOpen();
        }else {
            listener.showContentView();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    listener.isNotFirstOpen();
                }
            },2000);
        }
    }
}
