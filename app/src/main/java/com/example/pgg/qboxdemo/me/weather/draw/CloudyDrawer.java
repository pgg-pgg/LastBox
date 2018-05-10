package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

public class CloudyDrawer extends BaseDrawer {

    final ArrayList<CircleHolder> holders = new ArrayList<>();
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public CloudyDrawer(Context context, boolean isNight) {
        super(context, isNight);
    }


    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if (holders.size()==0){
            holders.add(new CircleHolder(0.20f * width, -0.30f * width, 0.06f * width, 0.022f * width, 0.56f * width,
                    0.0015f, isNight ? 0x183c6b8c : 0x28ffffff));
            holders.add(new CircleHolder(0.58f * width, -0.35f * width, -0.15f * width, 0.032f * width, 0.6f * width,
                    0.00125f, isNight ? 0x223c6b8c : 0x33ffffff));
            holders.add(new CircleHolder(0.9f * width, -0.19f * width, 0.08f * width, -0.015f * width, 0.44f * width,
                    0.0025f, isNight ? 0x153c6b8c : 0x15ffffff));
        }
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        for (CircleHolder holder:holders){
            holder.updateAndDraw(canvas,paint,alpha);
        }
        return true;
    }

    @Override
    protected int[] getSkyBackgroundGradient() {
        return isNight?SkyBackground.CLOUDY_N:SkyBackground.CLOUDY_D;
    }

    public static class CircleHolder{
        private final float cx,cy,dx,dy,radius;
        private final int color;
        private boolean isGrowing=true;
        private float curpercent=0f;
        private final float percentSpeed;

        public CircleHolder(float cx,float cy,float dx,float dy,float radius,float percentSpeed,int color){
            this.cx=cx;
            this.cy=cy;
            this.dx=dx;
            this.dy=dy;
            this.radius=radius;
            this.percentSpeed=percentSpeed;
            this.color=color;
        }

        public void updateAndDraw(Canvas canvas, Paint paint,float alpha){
            float randomPercentSpeed=getRandom(percentSpeed*0.7f,percentSpeed*1.3f);
            if (isGrowing){
                curpercent+=randomPercentSpeed;
                if (curpercent>1f){
                    curpercent=1f;
                    isGrowing=false;
                }
            }else {
                curpercent-=randomPercentSpeed;
                if (curpercent<0f){
                    curpercent=0f;
                    isGrowing=true;
                }
            }
            float curCX=cx+dx*curpercent;
            float curCY=cy+dy*curpercent;;
            int curColor=convertAlphaColor(alpha*(Color.alpha(color)/255f),color);
            paint.setColor(curColor);
            canvas.drawCircle(curCX,curCY,radius,paint);
        }
    }
}
