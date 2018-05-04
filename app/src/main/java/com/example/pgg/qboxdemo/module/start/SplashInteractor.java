package com.example.pgg.qboxdemo.module.start;

/**
 * Created by pgg on 2018/5/2.
 */

public interface SplashInteractor {

    interface OnEnterIntoFinishListener{
        void isFirstOpen();

        void isNotFirstOpen();

        void showContentView();
    }

    void enterInto(boolean isFirstOpen,OnEnterIntoFinishListener listener);
}
