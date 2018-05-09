package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.LineBackgroundSpan;

/**
 * Created by pgg on 2018/5/9.
 */

public class EventSpan_Holiday implements LineBackgroundSpan {

    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int i5, int i6, int i7) {

        paint.setColor(Color.parseColor("#FFFF4081"));
        RectF rectF=new RectF(0,(-(right-bottom)/2),CircleBackGroundSpan.dip2px(18),(-(right-bottom)/2)+ CircleBackGroundSpan.dip2px(18));
        canvas.drawRoundRect(rectF,0,0,paint);
        paint.setTextSize(CircleBackGroundSpan.dip2px(14));
        paint.setColor(Color.WHITE);
        canvas.drawColor(Color.parseColor("#22FF4081"));
        canvas.drawText("休",CircleBackGroundSpan.dip2px(2),(-(right-bottom)/2)+ CircleBackGroundSpan.dip2px(14), paint);
    }
}
