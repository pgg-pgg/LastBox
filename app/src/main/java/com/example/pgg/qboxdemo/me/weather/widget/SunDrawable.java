package com.example.pgg.qboxdemo.me.weather.widget;

/**
 * Created by pgg on 2018/5/11.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.util.TypedValue;

/**
 * 设置下拉刷新时的太阳
 */
public class SunDrawable extends RefreshDrawable {

    RectF mBounds;
    float mWidth,mHeight,mCenterX,mCenterY,mPercent;
    final float mMaxAngle=180f;
    final float mRadius;
    final Paint mPaint=new Paint();
    int mOffset;
    boolean mRunning;
    float mDegrees;

    public SunDrawable(Context context, PullRefreshLayout layout) {
        super(context, layout);
        mPaint.setAntiAlias(true);//设置抗锯齿
        mPaint.setStrokeJoin(Paint.Join.ROUND);//设置线段连接处样式为圆弧
        mPaint.setStrokeCap(Paint.Cap.ROUND);//设置圆形线冒
        mPaint.setStrokeWidth(dp2px(2));//设置线宽度
        mPaint.setStyle(Paint.Style.STROKE);//设置为实线
        mPaint.setColor(0xffffffff);

        mRadius=dp2px(6);
    }


    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        mHeight=getRefreshLayout().getFinalOffset();
        mWidth=mHeight;
        mBounds=new RectF(bounds.width() / 2 - mWidth / 2, bounds.top - mHeight / 2, bounds.width() / 2 + mWidth / 2, bounds.top + mHeight / 2);
        mCenterX=mBounds.centerX();
        mCenterY=mBounds.centerY();
    }

    @Override
    public void setPercent(float percent) {
        mPercent=percent;
        invalidateSelf();
    }

    @Override
    public void setColorSchemeColors(int[] colorSchemeColors) {

    }

    @Override
    public void offsetTopAndBottom(int offset) {
        mOffset+=offset;
        invalidateSelf();
    }

    @Override
    public void start() {
        mRunning=true;
        mDegrees=0;
        invalidateSelf();
    }

    @Override
    public void stop() {
        mRunning=false;
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.save();
        canvas.translate(0,mOffset/2);

        if (isRunning()){
            canvas.rotate(mDegrees,mCenterX,mCenterY);
            mDegrees=mDegrees<360?mDegrees+8:0;
            invalidateSelf();
        }

        float percent=mPercent;
        RectF oval=new RectF(mCenterX-mRadius,mCenterY-mRadius,mCenterX+mRadius,mCenterY+mRadius);
        //画太阳的中心圈
        canvas.drawArc(oval,180,mMaxAngle*percent,false,mPaint);

        canvas.drawArc(oval,0,mMaxAngle*percent,false,mPaint);

        final int light_count=8;
        mPaint.setAlpha((int)(255f*percent));
        //画太阳的外圈
        for (int i=0;i<light_count;i++){
            double radians=Math.toRadians(i*(360/light_count));
            float x1=(float)(Math.cos(radians)*mRadius*1.6f);
            float y1=(float)(Math.sin(radians)*mRadius*1.6f);
            float x2=x1*(1f+0.4f*percent);
            float y2=y1*(1f+0.4f*percent);
            canvas.drawLine(mCenterX+x1,y1,mCenterX+x2,y2,mPaint);
        }
        mPaint.setAlpha(255);
        canvas.restore();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

}
