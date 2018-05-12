package com.example.pgg.qboxdemo.me.weather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;

import com.example.pgg.qboxdemo.me.weather.WeatherActivity;
import com.example.pgg.qboxdemo.module.weather.api.ApiManager;
import com.example.pgg.qboxdemo.module.weather.api.entity.DailyForecast;
import com.example.pgg.qboxdemo.module.weather.api.entity.Now;
import com.example.pgg.qboxdemo.module.weather.api.entity.Weather;
import com.example.pgg.qboxdemo.utils.UiUtil;

import java.util.Calendar;

/**
 * Created by pgg on 2018/5/12.
 */

/**
 * 太阳和风速，mmp，真滴烦，最主要是风车的绘制和小太阳的绘制
 */
public class AstroView extends View {

    private int width, height;
    private final float density;
    private final DashPathEffect dashPathEffect;
    private Path sunPath = new Path();
    private RectF sunRectF = new RectF();
    private Path fanPath = new Path();// 旋转的风扇的扇叶
    private Path fanPillarPath = new Path();// 旋转的风扇的柱子


    private float fanPillerHeight;
    private final TextPaint paint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private float paintTextOffset;
    final float offsetDegree = 15f;
    private float curRotate;// 旋转的风扇的角度
    private DailyForecast todayForecast;
    private Now now;
    private float sunArcHeight, sunArcRadius;

    private Rect visibleRect = new Rect();

    public AstroView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        density = context.getResources().getDisplayMetrics().density;
        dashPathEffect = new DashPathEffect(new float[]{density * 3, density * 3}, 1);
        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(density);
        paint.setTextAlign(Paint.Align.CENTER);
        if (isInEditMode()) {
            return;
        }
        paint.setTypeface(WeatherActivity.getTypeface(context));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (this.todayForecast == null || this.now == null) {
            return;
        }
        paint.setColor(Color.WHITE);
        float textSize = paint.getTextSize();
        try {
            //按照在OnSizeChange方法中定好的外部虚线环的路径开始绘制
            paint.setStrokeWidth(density);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(0x55ffffff);
            paint.setPathEffect(dashPathEffect);
            canvas.drawPath(sunPath, paint);
            paint.setPathEffect(null);
            paint.setColor(Color.WHITE);
            int saveCount = canvas.save();
            //将画布移动到风车中心
            canvas.translate(width / 2f - fanPillerHeight * 1f, textSize + sunArcHeight - fanPillerHeight);
            paint.setStyle(Paint.Style.FILL);//填充绘制
            paint.setTextAlign(Paint.Align.LEFT);
            //绘制风速相关的文字
            final float fanHeight = textSize * 2f;
            canvas.drawText("风速", fanHeight + textSize, -textSize, paint);
            canvas.drawText(now.wind.spd + "km/h " + now.wind.dir, fanHeight + textSize, 0, paint);
            //绘制风车的支架
            paint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(fanPillarPath, paint);
            //根据风速控制风车的旋转速度
            canvas.rotate(curRotate * 360f);
            float speed = 0f;
            try {
                speed = Float.valueOf(now.wind.spd);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //速度最小为0.75
            speed = Math.max(speed, 0.75f);
            curRotate += 0.001f * speed;
            if (curRotate > 1f) {
                curRotate = 0f;
            }
            paint.setStyle(Paint.Style.FILL);
            //绘制风扇叶，三个，通过不断旋转画布实现
            canvas.drawPath(fanPath, paint);
            canvas.rotate(120f);
            canvas.drawPath(fanPath, paint);
            canvas.rotate(120f);
            canvas.drawPath(fanPath, paint);
            canvas.restoreToCount(saveCount);//保证不断旋转

            //绘制底部水平线
            paint.setStyle(Paint.Style.STROKE);
            final float lineLeft = width / 2f - sunArcRadius;
            canvas.drawLine(lineLeft, sunArcHeight + textSize, width - lineLeft, sunArcHeight + textSize, paint);

            //绘制气压和日出日落文字
            paint.setStyle(Paint.Style.FILL);
            paint.setTextAlign(Paint.Align.RIGHT);
            final float pressureTextRight = width / 2f + sunArcRadius - textSize * 2.5f;
            canvas.drawText("气压 " + now.pres + "hpa", pressureTextRight, sunArcHeight + paintTextOffset, paint);
            final float textLeft = width / 2f - sunArcRadius;// sunArcSize;
            paint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("日出 " + todayForecast.astro.sr, textLeft, textSize * 10.5f + paintTextOffset, paint);
            canvas.drawText(todayForecast.astro.ss + " 日落", width - textLeft, textSize * 10.5f + paintTextOffset, paint);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

        //绘制虚线上的太阳，表示太阳下山的位置
        try {
            //判断是否在白天，如果太阳落山了，那么就不需要绘制
            String[] sr = todayForecast.astro.sr.split(":");// 日出
            int srTime = Integer.valueOf(sr[0]) * 60 * 60 + Integer.valueOf(sr[1]) * 60 + 0;// 精确到秒
            String[] ss = todayForecast.astro.ss.split(":");// 日落
            int ssTime = Integer.valueOf(ss[0]) * 60 * 60 + Integer.valueOf(ss[1]) * 60 + 0;
            Calendar c = Calendar.getInstance();
            int curTime = c.get(Calendar.HOUR_OF_DAY) * 60 * 60 + c.get(Calendar.MINUTE) * 60 + c.get(Calendar.MINUTE);
            if (curTime >= srTime && curTime <= ssTime) {// 说明是在白天，需要画太阳
                canvas.save();
                canvas.translate(width / 2f, sunArcRadius + textSize);// 先平移到外环圆弧的圆心
                float percent = (curTime - srTime) / ((float) (ssTime - srTime));//太阳摆放的位置百分比
                float degree = 15f + 150f * percent;//根据百分比计算出在虚线弧上的角度
                final float sunRadius = density * 4f;
                canvas.rotate(degree);// 旋转到太阳的角度

                paint.setStyle(Paint.Style.FILL);
                paint.setStrokeWidth(density * 1.333f);// 宽度是2对应半径是6
                canvas.translate(-sunArcRadius, 0);// 平移到太阳应该在的位置
                canvas.rotate(-degree);// 转正方向。。。
                canvas.drawCircle(0, 0, sunRadius, paint);//绘制太阳的圆盘
                paint.setStyle(Paint.Style.STROKE);
                //绘制太阳的外八条短线
                final int light_count = 8;
                for (int i = 0; i < light_count; i++) {// 画刻度
                    double radians = Math.toRadians(i * (360 / light_count));
                    float x1 = (float) (Math.cos(radians) * sunRadius * 1.6f);
                    float y1 = (float) (Math.sin(radians) * sunRadius * 1.6f);
                    float x2 = x1 * (1f + 0.4f * 1f);
                    float y2 = y1 * (1f + 0.4f * 1f);
                    canvas.drawLine(0 + x1, y1, 0 + x2, y2, paint);
                }
                canvas.restore();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        getGlobalVisibleRect(visibleRect);//获取视图在屏幕坐标中的可视区域，并映射到visibleRect矩形中
        //如果是空那么就需要重绘
        if (!visibleRect.isEmpty()){
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void setData(Weather weather) {
        try {
            if (ApiManager.acceptWeather(weather)) {
                this.now = weather.get().now;
                final DailyForecast forecast = weather.getTodayDailyForecast();
                if (forecast != null) {
                    todayForecast = forecast;
                }
                if (this.now != null || this.todayForecast != null) {
                    invalidate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.width = w;
        this.height = h;
        // 间距1 图8.5行 间距0.5 字1行 间距1 = 12;
        try {
            final float textSize = height / 12f;
            paint.setTextSize(textSize);
            paintTextOffset = UiUtil.getTextPaintOffset(paint);

            //添加最外部虚线的圆弧的路径，此时canvas的在0,0
            sunPath.reset();
            sunArcHeight = textSize * 8.5f;
            sunArcRadius = (float) (sunArcHeight / (1f - Math.sin(Math.toRadians(offsetDegree))));
            final float sunArcLeft = width / 2f - sunArcRadius;
            sunRectF.left = sunArcLeft;
            sunRectF.top = textSize;
            sunRectF.right = width - sunArcLeft;
            sunRectF.bottom = sunArcRadius * 2f + textSize;
            sunPath.addArc(sunRectF, -165, +150);// 圆形的最右端点为0，顺时针sweepAngle

            //开始定好风车路劲，分两步，首先定好风扇叶的路径，此时canvas已经移动到风车的中心点
            //也就是说fanPath和fanPillarPath的中心点在扇叶圆形的中间
            fanPath.reset();
            final float fanSize = textSize * 0.2f;// 风扇底部半圆的半径
            final float fanHeight = textSize * 2f;
            final float fanCenterOffsetY = fanSize * 1.6f;
            //扇叶的底部的圆弧
            fanPath.addArc(new RectF(-fanSize, -fanSize - fanCenterOffsetY, fanSize, fanSize - fanCenterOffsetY), 0, 180);
            //扇叶的边缘
            fanPath.quadTo(-fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, 0, -fanHeight - fanCenterOffsetY);
            fanPath.quadTo(fanSize * 1f, -fanHeight * 0.5f - fanCenterOffsetY, fanSize, -fanCenterOffsetY);
            fanPath.close();
            //第二步，定好风车的底部支架
            fanPillarPath.reset();
            final float fanPillarSize = textSize * 0.25f;// 柱子的宽度
            fanPillarPath.moveTo(0, 0);
            fanPillerHeight = textSize * 4f;// 柱子的宽度
            fanPillarPath.lineTo(fanPillarSize, fanPillerHeight);
            fanPillarPath.lineTo(-fanPillarSize, fanPillerHeight);
            fanPillarPath.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
