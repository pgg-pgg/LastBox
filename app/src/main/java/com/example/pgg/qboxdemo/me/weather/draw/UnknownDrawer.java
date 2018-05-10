package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by pgg on 2018/5/10.
 */

public class UnknownDrawer extends BaseDrawer {

    public UnknownDrawer(Context context, boolean isNight) {
        super(context, isNight);
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        return true;
    }
}
