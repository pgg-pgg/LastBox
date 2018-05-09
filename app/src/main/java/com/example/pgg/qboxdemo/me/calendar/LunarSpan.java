package com.example.pgg.qboxdemo.me.calendar;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.style.LineBackgroundSpan;

import com.example.pgg.qboxdemo.utils.Lunar;

import java.util.Calendar;

/**
 * Created by pgg on 2018/5/9.
 */

public class LunarSpan implements LineBackgroundSpan {

    private String year;
    private String month;

    public LunarSpan(String year,String month){
        this.year=year;
        this.month=month;
    }
    @Override
    public void drawBackground(Canvas canvas, Paint paint, int i, int i1, int i2, int i3, int i4, CharSequence charSequence, int i5, int i6, int i7) {
        Calendar calendar= Calendar.getInstance();
        calendar.set(Integer.valueOf(year),Integer.valueOf(month)-1,Integer.valueOf(charSequence.toString()));

        Lunar lunar=new Lunar(calendar.getTime());
        String chinaDayString = lunar.getChinaDayString();
        Paint paint1 = new Paint();
        paint1.setTextSize(CircleBackGroundSpan.dip2px(10));
        paint1.setColor(Color.parseColor("#cccccc"));
        canvas.drawText(chinaDayString,(i1-i)/2-CircleBackGroundSpan.dip2px(10),(i4-i2)/2+CircleBackGroundSpan.dip2px(17),paint1);
    }
}
