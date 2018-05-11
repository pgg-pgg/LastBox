package com.example.pgg.qboxdemo.me.weather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.pgg.qboxdemo.me.weather.WeatherActivity;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.module.weather.api.entity.HourlyForecast;
import com.example.pgg.qboxdemo.module.weather.api.entity.Weather;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/11.
 */

public class HourlyForecastView extends View {
    private int width, height;
    private final float density;
    private ArrayList<HourlyForecast> forecastList;
    private Path tmpPath=new Path();
    private Path goneTmpPath=new Path();
    //理论上有8个数据（从1：00到22:00每隔3小时共8个数据）(添加第一列显示行的名称)，但是api只会返回现在的时间之后的
    private final int full_data_count=9;
    private final DashPathEffect dashPathEffect;//绘制虚线

    private final TextPaint paint=new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private Data[] datas;
    public class Data{
        public float offsetPercent;
        public int tmp;
        /** 2018-05-11 22:01*/
        public String date;
        private String wind_sc;
        //降水概率
        private String pop;
    }

    public HourlyForecastView(Context context) {
        this(context,null);
    }

    public HourlyForecastView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        density=context.getResources().getDisplayMetrics().density;
        dashPathEffect=new DashPathEffect(new float[]{density*3,density*3},1);
        if (isInEditMode()){
            return;
        }
        initView(context);
    }

    private void initView(Context context) {
        paint.setColor(Color.WHITE);
        paint.setStrokeWidth(1f*density);
        paint.setTextSize(12f*density);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTypeface(WeatherActivity.getTypeface(context));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isInEditMode()){
            return;
        }

        paint.setStyle(Paint.Style.FILL);
        // 一共需要 顶部文字2(+图占4行)+底部文字0 + 【间距1 + 日期1 + 间距0.5 +　晴1 + 间距0.5f + 微风1 +
        // 底部边距1f 】 = 12行
        final float textSize=this.height/12f;
        paint.setTextSize(textSize);
        final float textOffset=getTextPaintOffset(paint);
        final float dH=textSize*4f;
        final float dCenterY=textSize*4f;
        if (datas==null||datas.length<=1){
            canvas.drawLine(0,dCenterY,this.width,dCenterY,paint);
            return;
        }
        final float dW=this.width*1f/full_data_count;
        tmpPath.reset();
        goneTmpPath.reset();
        final  int length=datas.length;
        float[] x=new float[length];
        float[] y=new float[length];
        final float textPercent=1f;
        final float pathPercent=1f;

        final float smallerHeight=4*textSize;//图的高度
        final float smallerPercent=1-smallerHeight/2f/dH;
        paint.setAlpha((int)(255*textPercent));
        final int data_length_offset=Math.max(0,full_data_count-length);
        for (int i=0;i<length;i++){
            final Data d=datas[i];
            final int index=i+data_length_offset;
            x[i]=index*dW+dW/2f;
            y[i]=dCenterY-d.offsetPercent*dH*smallerPercent;
            canvas.drawText(d.tmp+"°",x[i],y[i]-textSize+textOffset,paint);

            if (i==0){
                final float i0_x=dW/2f;
                canvas.drawText("时间",i0_x,textSize*7.5f+textOffset,paint);
                canvas.drawText("降水率",i0_x,textSize*9f+textOffset,paint);
                canvas.drawText("风力",i0_x,textSize*10.5f+textOffset,paint);
            }

            canvas.drawText(d.date.substring(11),x[i],textSize*7.5f+textOffset,paint);
            canvas.drawText(d.pop+"%",x[i],textSize*9f+textOffset,paint);
            canvas.drawText(d.wind_sc,x[i],textSize*10.5f+textOffset,paint);
        }
        paint.setAlpha(255);
        paint.setStyle(Paint.Style.STROKE);
        final float data_x0=data_length_offset*dW;

        //绘制虚线
        goneTmpPath.moveTo(0,y[0]);
        goneTmpPath.lineTo(data_x0,y[0]);
        paint.setPathEffect(dashPathEffect);
        canvas.drawPath(goneTmpPath,paint);

        //绘制温度曲线
        for (int i=0;i<(length-1);i++){
            final float midX=(x[i]+x[i+1])/2f;
            final float midY=(y[i]+y[i+1])/2f;

            if (i==0){
                tmpPath.moveTo(data_x0,y[i]);
            }
            tmpPath.cubicTo(x[i]-1,y[i],x[i],y[i],midX,midY);

            if (i==(length-2)){
                tmpPath.cubicTo(x[i+1]-1,y[i+1],x[i+1],y[i+1],this.width,y[i+1]);
            }
        }

        final boolean needClip=pathPercent<1f;
        if (needClip){
            canvas.save();
            canvas.clipRect(0,0,this.width*pathPercent,this.height);
        }

        paint.setPathEffect(null);
        canvas.drawPath(tmpPath,paint);
        if (needClip){
            canvas.restore();
        }
    }

    public void setData(Weather weather){
        if (weather==null||!weather.isOK()){
            return;
        }
        if (this.forecastList==weather.get().hourlyForecast){
            invalidate();
            return;
        }
        try {
            final ArrayList<HourlyForecast> w_hourlyForecast=weather.get().hourlyForecast;
            if (w_hourlyForecast.size()==0){
                return;
            }
            if (!ApiManager.isToday(w_hourlyForecast.get(0).date)){
                return;
            }
            this.forecastList=w_hourlyForecast;
            if (forecastList==null&&forecastList.size()==0){
                return;
            }
            datas=new Data[forecastList.size()];
            int all_max=Integer.MIN_VALUE;
            int all_min=Integer.MAX_VALUE;
            for (int i=0;i<forecastList.size();i++){
                HourlyForecast forecast=forecastList.get(i);
                int tmp=Integer.valueOf(forecast.tmp);
                if (all_max<tmp){
                    all_max=tmp;
                }
                if (all_min>tmp){
                    all_min=tmp;
                }
                final Data data=new Data();
                data.tmp=tmp;
                data.date=forecast.date;
                data.wind_sc=forecast.wind.sc;
                data.pop=forecast.pop;
                datas[i]=data;
            }
            float all_distance=Math.abs(all_max-all_min);
            float average_distance=(all_max+all_min)/2f;

            for (Data d:datas){
                d.offsetPercent=(d.tmp-average_distance)/all_distance;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        invalidate();
    }


    public static float getTextPaintOffset(Paint paint) {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        return -(fontMetrics.bottom - fontMetrics.top) / 2f - fontMetrics.top;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
    }
}
