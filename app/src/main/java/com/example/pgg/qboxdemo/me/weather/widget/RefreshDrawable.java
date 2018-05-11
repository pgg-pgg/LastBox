package com.example.pgg.qboxdemo.me.weather.widget;

/**
 * Created by pgg on 2018/5/11.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.PixelFormat;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * 刷新时的Drawable
 */
public abstract class RefreshDrawable extends Drawable implements Drawable.Callback,Animatable{

    private PullRefreshLayout mRefreshLayout;

    public RefreshDrawable(Context context,PullRefreshLayout layout){
        mRefreshLayout=layout;
    }

    public Context getContext(){
        return mRefreshLayout!=null?mRefreshLayout.getContext():null;
    }

    public PullRefreshLayout getRefreshLayout(){
        return mRefreshLayout;
    }

    /**
     * 设置百分比
     * @param percent
     */
    public abstract void setPercent(float percent);

    /**
     * 设置颜色集合
     * @param colorSchemeColors
     */
    public abstract void setColorSchemeColors(int[] colorSchemeColors);

    /**
     * 顶部和底部的距离
     * @param offset
     */
    public abstract void offsetTopAndBottom(int offset);

    @Override
    public void invalidateDrawable(@NonNull Drawable drawable) {
        final Callback callback=getCallback();
        if (callback!=null){
            callback.invalidateDrawable(this);
        }
    }

    @Override
    public void scheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable, long l) {
        final Callback callback=getCallback();
        if (callback!=null){
            callback.scheduleDrawable(this,runnable,l);
        }
    }

    @Override
    public void unscheduleDrawable(@NonNull Drawable drawable, @NonNull Runnable runnable) {
        final Callback callback=getCallback();
        if (callback!=null){
            callback.unscheduleDrawable(this,runnable);
        }
    }

    @Override
    public void setAlpha(int i) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
