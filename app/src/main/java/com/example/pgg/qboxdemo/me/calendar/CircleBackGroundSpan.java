package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.example.pgg.qboxdemo.global.MyApplication;

/**
 * Created by pgg on 2018/5/9.
 */

public class CircleBackGroundSpan implements LineBackgroundSpan {

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int i5, int i6, int i7) {
        Paint paint1=new Paint();
        paint1.setColor(Color.parseColor("#FF4081"));
        canvas.drawCircle((right-left)/2, (bottom-top)/2+dip2px(4),dip2px(18),paint1);
    }

    public static int dip2px(float dpValue) {
        float scale = MyApplication.screenDensity;
        return (int)(dpValue * scale + 0.5F);
    }
}
