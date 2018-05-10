package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.AnimationUtils;

import com.example.pgg.qboxdemo.me.weather.draw.BaseDrawer;
import com.orhanobut.logger.Logger;

/**
 * Created by pgg on 2018/5/10.
 */

public class DynamicWeather extends SurfaceView implements SurfaceHolder.Callback{

    private int mWidth, mHeight;
    private BaseDrawer preDrawer,curDrawer;
    private DrawThread mDrawThread;

    private float curDrawerAlpha = 0f;
    private BaseDrawer.Type curType = BaseDrawer.Type.DEFAULT;

    public DynamicWeather(Context context) {
        this(context,null);
    }

    public DynamicWeather(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DynamicWeather(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        curDrawerAlpha=0f;
        mDrawThread=new DrawThread();
        final SurfaceHolder holder=getHolder();
        holder.addCallback(this);
        holder.setFormat(PixelFormat.RGBA_8888);
        mDrawThread.start();
    }

    private void setDrawer(BaseDrawer baseDrawer){
        if (baseDrawer==null){
            return;
        }
        curDrawerAlpha=0f;
        if (this.curDrawer!=null){
            this.preDrawer=curDrawer;
        }
        this.curDrawer=baseDrawer;
    }

    public void setDrawerType(BaseDrawer.Type type) {
        if (type == null) {
            return;
        }
        if (type != curType) {
            curType = type;
            setDrawer(BaseDrawer.makeDrawerByType(getContext(), curType));
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth=w;
        mHeight=h;
    }

    public void onResume(){
        synchronized (mDrawThread){
            mDrawThread.isRunning=true;
            mDrawThread.notify();
        }
    }

    public void onPause(){
        synchronized (mDrawThread){
            mDrawThread.isRunning=false;
            mDrawThread.notify();
        }
    }

    public void onDestroy(){
        synchronized (mDrawThread){
            mDrawThread.mQuit=true;
            mDrawThread.notify();
        }
    }
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        synchronized (mDrawThread){
            mDrawThread.holder=surfaceHolder;
            mDrawThread.notify();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        synchronized (mDrawThread){
            mDrawThread.holder=surfaceHolder;
            mDrawThread.notify();
            while (mDrawThread.mActive){
                try {
                    mDrawThread.wait();
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        surfaceHolder.removeCallback(this);
    }

    private class DrawThread extends Thread{
        SurfaceHolder holder;
        boolean isRunning;
        boolean mActive;
        boolean mQuit;
        @Override
        public void run() {
            while (true){
                synchronized (this){
                    while (holder==null||!isRunning){
                        if (mActive){
                            mActive=false;
                            notify();
                        }
                        if (mQuit){
                            return;
                        }
                        try {
                            wait();
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }

                    if (!mActive){
                        mActive=true;
                        notify();
                    }
                    final long startTime= AnimationUtils.currentAnimationTimeMillis();
                    Canvas canvas=holder.lockCanvas();

                    if (canvas!=null){
                        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

                        drawSurface(canvas);

                        holder.unlockCanvasAndPost(canvas);
                    }else {
                        Logger.e("获取画布失败");
                    }
                    final long drawTime=AnimationUtils.currentAnimationTimeMillis()-startTime;
                    final long needSleepTime=16-drawTime;
                    if (needSleepTime>0){
                        try {
                            Thread.sleep(needSleepTime);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private boolean drawSurface(Canvas canvas) {
        final int w=mWidth;
        final int h=mHeight;
        if (w==0||h==0){
            return true;
        }
        boolean needDrawNextFrame=false;
        if (curDrawer!=null){
            curDrawer.setSize(w,h);
            needDrawNextFrame=curDrawer.draw(canvas,curDrawerAlpha);
        }
        if (preDrawer!=null&&curDrawerAlpha<1f){
            needDrawNextFrame=true;
            preDrawer.setSize(w,h);
            preDrawer.draw(canvas,1f-curDrawerAlpha);
        }
        if (curDrawerAlpha<1f){
            curDrawerAlpha+=0.04f;
            if (curDrawerAlpha>1){
                curDrawerAlpha=1f;
                preDrawer=null;
            }
        }

        return needDrawNextFrame;
    }


}
