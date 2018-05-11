package com.example.pgg.qboxdemo.me.weather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.pgg.qboxdemo.me.weather.WeatherActivity;
import com.example.pgg.qboxdemo.module.weather.api.entity.Aqi;
import com.example.pgg.qboxdemo.module.weather.api.entity.City;

/**
 * Created by pgg on 2018/5/11.
 */

/**
 * 空气质量的弧形“表”10行 120dp
 */
public class AqiView extends View {

    private final float density;

    private TextPaint textPaint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private RectF rectF=new RectF();

    private City aqiCity;

    public AqiView(Context context) {
        this(context,null);
    }

    public AqiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        density=context.getResources().getDisplayMetrics().density;
        textPaint.setTextAlign(Paint.Align.CENTER);
        if (isInEditMode()){
            return;
        }
        textPaint.setTypeface(WeatherActivity.getTypeface(context));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final float w=getWidth();
        final float h=getHeight();
        if (w<=0||h<=0){
            return;
        }
        final float lineSize=h/10f;//大约12dp
        if (aqiCity==null){
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(lineSize*1.25f);
            textPaint.setColor(0xaaffffff);
            canvas.drawText("暂无数据",w/2f,h/2f,textPaint);
            return;
        }

        float currAqiPercent=-1f;
        try {
            currAqiPercent=Float.valueOf(aqiCity.aqi)/500f;//污染百分比
            currAqiPercent=Math.min(currAqiPercent,1f);
        }catch (Exception e){
            e.printStackTrace();
        }

        float aqiArcRadius=lineSize*4f;
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setStrokeWidth(lineSize*1);
        textPaint.setColor(0x55ffffff);
        rectF.set(-aqiArcRadius,-aqiArcRadius,aqiArcRadius,aqiArcRadius);
        final int saveCount=canvas.save();
        canvas.translate(w/2f,h/2f);
        //画深色圆弧
        final float startAngle=-210f;
        final float sweepAngle=240f;
        canvas.drawArc(rectF,startAngle+sweepAngle*currAqiPercent,sweepAngle*(1f-currAqiPercent),false,textPaint);
        if (currAqiPercent>=0f){
            //画外部圆环
            textPaint.setColor(0x99ffffff);
            canvas.drawArc(rectF,startAngle,sweepAngle*currAqiPercent,false,textPaint);
            //画内部圆
            textPaint.setColor(0xffffffff);
            textPaint.setStrokeWidth(lineSize/8f);
            canvas.drawCircle(0,0,lineSize/3f,textPaint);
            //画空气质量的数字
            textPaint.setStyle(Paint.Style.FILL);
            textPaint.setTextSize(lineSize*1.5f);
            textPaint.setColor(0xffffffff);
            try {
                canvas.drawText(aqiCity.aqi+"",0,lineSize*3,textPaint);
            }catch (Exception e){
            }
            //画空气质量的描述：良，优...
            textPaint.setTextSize(lineSize*1f);
            textPaint.setColor(0x88ffffff);
            try {
                canvas.drawText(aqiCity.qlty+"",0,lineSize*4.25f,textPaint);
            }catch (Exception e){
            }

            //画指针
            canvas.rotate(startAngle+sweepAngle*currAqiPercent-180f);
            textPaint.setStyle(Paint.Style.STROKE);
            textPaint.setColor(0xffffffff);
            float startX=lineSize/3f;
            canvas.drawLine(-startX,0,-lineSize*4.5f,0,textPaint);
        }
        canvas.restoreToCount(saveCount);
    }

    public void setData(Aqi aqi) {
        if (aqi != null && aqi.city != null) {
            this.aqiCity = aqi.city;
            invalidate();
        }
    }
}
