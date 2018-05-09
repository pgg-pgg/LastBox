package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.LineBackgroundSpan;

/**
 * Created by pgg on 2018/5/9.
 */

public class EventSpan_Workday implements LineBackgroundSpan {

    /**
     * 参数简介：坐标系是文本显示的横向区域
     * 即：文本横向是充满父区域，高度自适应
     * 所以：原点是：中间显示区域的左上角
     * @param canvas
     * @param paint
     * @param left
     * @param right
     * @param top
     * @param baseline
     * @param bottom
     * @param charSequence
     * @param i5
     * @param i6
     * @param i7
     */
    @Override
    public void drawBackground(Canvas canvas, Paint paint, int left, int right, int top, int baseline, int bottom, CharSequence charSequence, int i5, int i6, int i7) {
        paint.setColor(Color.parseColor("#FF212121"));
        RectF rectF = new RectF(0, (-(right - bottom) / 2), CircleBackGroundSpan.dip2px(18), (-(right - bottom) / 2 + CircleBackGroundSpan.dip2px(18)));
        canvas.drawRoundRect(rectF,0,0,paint);
        paint.setTextSize(CircleBackGroundSpan.dip2px(14));
        paint.setColor(Color.WHITE);
        canvas.drawColor(Color.parseColor("#22212121"));
        canvas.drawText("班",CircleBackGroundSpan.dip2px(2),(-(right-bottom)/2)+ CircleBackGroundSpan.dip2px(14), paint);
    }
}
