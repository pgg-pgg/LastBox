package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

/**
 * Created by pgg on 2018/5/9.
 */

public class AnnulusSpan implements LineBackgroundSpan {
    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int i5, int i6, int i7) {
        paint.setAntiAlias(true);//消除锯齿
        paint.setStyle(Paint.Style.STROKE);//绘制空心圆，空心矩形
        int ringWidth=CircleBackGroundSpan.dip2px(1);//圆环宽度
        //绘制圆环
        paint.setColor(Color.parseColor("#303f9f"));
        paint.setStrokeWidth(ringWidth);
        canvas.drawCircle((right-left)/2,(bottom-top)/2, right/2-CircleBackGroundSpan.dip2px(1), paint);
    }
}
