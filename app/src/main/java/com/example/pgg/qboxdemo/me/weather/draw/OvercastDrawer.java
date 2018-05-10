package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by pgg on 2018/5/10.
 */

public class OvercastDrawer extends BaseDrawer {

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    final ArrayList<CloudyDrawer.CircleHolder> holders = new ArrayList<>();
    public OvercastDrawer(Context context, boolean isNight) {
        super(context, isNight);
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        for (CloudyDrawer.CircleHolder holder:this.holders){
            holder.updateAndDraw(canvas,paint,alpha);
        }
        return true;
    }

    @Override
    protected void setSize(int width, int height) {
        super.setSize(width, height);
        if(holders.size() == 0) {
            holders.add(new CloudyDrawer.CircleHolder( 0.20f * width , -0.30f * width , 0.06f * width, 0.022f *width, 0.56f * width, 0.0015f  ,isNight ? 0x44374d5c :0x4495a2ab));
            holders.add(new CloudyDrawer.CircleHolder( 0.59f * width , -0.35f * width , -0.18f * width, 0.032f *width, 0.6f * width, 0.00125f  ,isNight ? 0x55374d5c :0x335a6c78));
            holders.add(new CloudyDrawer.CircleHolder( 0.9f * width , -0.18f * width , 0.08f * width, -0.015f *width, 0.42f * width, 0.0025f ,isNight ? 0x5a374d5c :0x556f8a8d));
        }
    }

    @Override
    protected int[] getSkyBackgroundGradient() {
        return isNight?SkyBackground.OVERCAST_N:SkyBackground.OVERCAST_D;
    }
}
