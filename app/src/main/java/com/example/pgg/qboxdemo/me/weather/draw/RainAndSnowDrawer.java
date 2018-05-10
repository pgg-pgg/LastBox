package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

/**
 * 雨夹雪
 */
public class RainAndSnowDrawer extends BaseDrawer {

    private GradientDrawable snowDrawable;
    private RainDrawer.RainDrawable rainDrawable;
    private ArrayList<SnowDrawer.SnowHolder> snowHolders=new ArrayList<>();
    private ArrayList<RainDrawer.RainHolder> rainHolders=new ArrayList<>();

    private static final int SNOW_COUNT=15;
    private static final int RAIN_COUNT=30;
    private static final float MIN_SIZE=6f;
    private static final float MAX_SIZE=14f;

    public RainAndSnowDrawer(Context context, boolean isNight) {
        super(context, isNight);
        snowDrawable=new GradientDrawable(GradientDrawable.Orientation.BL_TR,new int[] { 0x99ffffff, 0x00ffffff });
        snowDrawable.setShape(GradientDrawable.OVAL);
        snowDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        rainDrawable=new RainDrawer.RainDrawable();
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        for (SnowDrawer.SnowHolder holder:snowHolders){
            holder.updateRandom(snowDrawable,alpha);
            snowDrawable.draw(canvas);
        }
        for (RainDrawer.RainHolder holder:rainHolders){
            holder.updateRandom(rainDrawable,alpha);
            rainDrawable.draw(canvas);
        }
        return true;
    }

    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if (this.snowHolders.size()==0){
            final float minSize=dp2px(MIN_SIZE);
            final float maxSize=dp2px(MAX_SIZE);
            final float speed=dp2px(200);
            for (int i=0;i<SNOW_COUNT;i++){
                final float size=getRandom(minSize,maxSize);
                SnowDrawer.SnowHolder holder=new SnowDrawer.SnowHolder(getRandom(0,width),size,height,speed);
                snowHolders.add(holder);
            }
        }
        if (this.rainHolders.size()==0){
            final float rainWidth=dp2px(2);
            final float minRainHeight=dp2px(8);
            final float maxRainHeight=dp2px(14);
            final float speed=dp2px(360);
            for (int i=0;i<RAIN_COUNT;i++){
                float x=getRandom(0f,width);
                RainDrawer.RainHolder holder=new RainDrawer.RainHolder(x,rainWidth,minRainHeight,maxRainHeight,height,speed);
                rainHolders.add(holder);
            }
        }
    }

    @Override
    protected int[] getSkyBackgroundGradient() {
        return isNight?SkyBackground.RAIN_N:SkyBackground.RAIN_D;
    }
}
