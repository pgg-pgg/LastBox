package com.example.pgg.qboxdemo.me.weather.draw;

import android.content.Context;
import android.graphics.Canvas;

/**
 * Created by pgg on 2018/5/10.
 */

public class DefaultDrawer extends BaseDrawer {

    public DefaultDrawer(Context context) {
        super(context, true);
    }

    @Override
    public boolean drawWeather(Canvas canvas, float alpha) {
        return false;
    }

    @Override
    protected int[] getSkyBackgroundGradient() {
        return SkyBackground.BLACK;
    }
}
